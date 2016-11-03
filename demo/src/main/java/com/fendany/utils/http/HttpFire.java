package com.fendany.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lost on 2016/11/2.
 */
@Component
public class HttpFire implements InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpFire.class);

    @Value("${http.test.url}")
    private String url ;

    @Value("${http.test.logPath}")
    private String logPath;

    @Value("${http.test.replay}")
    private long replay;

    public void invoke() {

        if( url == null ){
            LOGGER.info(" URL no found use default ");
            url = "https://mhis-yedi-stg1.pingan.com.cn:59443/yedi-platform/";
        }

        LOGGER.info(" HTTP CREATE! ");
        HttpPost httppost=new HttpPost(url);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResultBean resultBean = new ResultBean();
        //添加参数
        long time_c = System.currentTimeMillis();
        resultBean.setBegin(time_c);
        try {
            LOGGER.info(" HTTP CONNECTION! at time [{}]",time_c );
            HttpResponse response=defaultHttpClient.execute(httppost);
            //发送Post,并返回一个HttpResponse对象
            if(response.getStatusLine().getStatusCode()==200){//如果状态码为200,就是正常返回
                long time_r = System.currentTimeMillis();
                resultBean.setEnd(time_r);
                LOGGER.info(" HTTP RESPONSE! at time [{}]",time_r );
                LOGGER.info(" HTTP use [{}] ms",time_r - time_c);
            } else {
                long time_e = System.currentTimeMillis();
                resultBean.setEnd(time_e);
                LOGGER.info(" HTTP CONNECTION ERROR! at time [{}]",time_e );
                LOGGER.info(" HTTP use [{}] ms",time_e - time_c);
            }
            resultBean.setSuccess(true);
            String result= EntityUtils.toString(response.getEntity());
            LOGGER.info(" HTTP RESULT at [{}] ",result);
        } catch (Exception e) {
            long time_e = System.currentTimeMillis();
            resultBean.setEnd(time_e);
            resultBean.setErrorMessage(e.getMessage());
            LOGGER.error(" HTTP CONNECTION ERROR at [{}]",e.getMessage());
            LOGGER.info(" HTTP CONNECTION ERROR! at time [{}]",time_e );
            LOGGER.info(" HTTP use [{}] ms",time_e - time_c);
        } finally {
            defaultHttpClient.close();
            File file = new File(logPath);
            if( !file.exists() ){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            BufferedWriter bufferedWriter = null;
            try {
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                String date = dataFormat.format(time_c);
                bufferedWriter = new BufferedWriter(new FileWriter(file,true));
                bufferedWriter.write("{"+date+"} " + resultBean.toString() + "\r\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                LOGGER.error(" Write log file error at [{}]",e.getMessage());
            } finally {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                invoke();
            }
        },1000,replay);
    }
}
