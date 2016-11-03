package com.fendany.utils.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lost on 2016/11/2.
 */
@Component
public class HttpFire implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpFire.class);

    @Value("${http.test.url}")
    private String url;

    @Value("${http.test.logPath}")
    private String logPath;

    @Value("${http.test.replay}")
    private long replay;

    private PoolingHttpClientConnectionManager cm;

    public void init() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory cf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry socketRegistry = RegistryBuilder.create()
                .register("https", cf)
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                .build();
        cm = new PoolingHttpClientConnectionManager(socketRegistry);
        cm.setMaxTotal(1);
        cm.setDefaultSocketConfig(socketConfig);
    }

    public CloseableHttpClient get() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    public void invoke() {

        if (url == null) {
            LOGGER.info(" URL no found use default ");
            url = "https://mhis-yedi-stg1.pingan.com.cn:59443/yedi-platform/";
        }

        LOGGER.info(" HTTP CREATE! ");
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Connection", "Keep-Alive");
        httppost.addHeader("Content-Type", "application/json");
        StringEntity input = null;
        try {
            input = new StringEntity(getString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httppost.setEntity(input);
//        CloseableHttpClient defaultHttpClient = get();
        CloseableHttpClient defaultHttpClient = HttpClients.createDefault();
        ResultBean resultBean = new ResultBean();
        //添加参数
        long time_c = System.currentTimeMillis();
        resultBean.setBegin(time_c);
        try {
            LOGGER.info(" HTTP CONNECTION! at time [{}]", time_c);
            HttpResponse response = defaultHttpClient.execute(httppost);
            //发送Post,并返回一个HttpResponse对象
            if (response.getStatusLine().getStatusCode() == 200) {//如果状态码为200,就是正常返回
                long time_r = System.currentTimeMillis();
                resultBean.setEnd(time_r);
                LOGGER.info(" HTTP RESPONSE! at time [{}]", time_r);
                LOGGER.info(" HTTP use [{}] ms", time_r - time_c);
            } else {
                long time_e = System.currentTimeMillis();
                resultBean.setEnd(time_e);
                LOGGER.info(" HTTP CONNECTION ERROR! at time [{}]", time_e);
                LOGGER.info(" HTTP use [{}] ms", time_e - time_c);
            }
            resultBean.setSuccess(true);
            String result = EntityUtils.toString(response.getEntity());
            LOGGER.info(" HTTP RESULT at [{}] ", result);
        } catch (Exception e) {
            long time_e = System.currentTimeMillis();
            resultBean.setEnd(time_e);
            resultBean.setErrorMessage(e.getMessage());
            LOGGER.error(" HTTP CONNECTION ERROR at [{}]", e.getMessage());
            LOGGER.info(" HTTP CONNECTION ERROR! at time [{}]", time_e);
            LOGGER.info(" HTTP use [{}] ms", time_e - time_c);
        } finally {

            try {
                defaultHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(logPath);
            if (!file.exists()) {
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
                bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.write("{" + date + "} " + resultBean.toString() + "\r\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                LOGGER.error(" Write log file error at [{}]", e.getMessage());
            } finally {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getString (){
        String s = "{                                                                       "+
                "                                                                        "+
                "    \"package\": {                                                      "+
                "                                                                        "+
                "        \"body\": [                                                     "+
                "                                                                        "+
                "              {                                                         "+
                "                                                                        "+
                "         \"settleSerialNum\":\"20150831091400004016\",                  "+
                "                                                                        "+
                "         \"revokeDate\":\"20150831171401\",                             "+
                "                                                                        "+
                "         \"updateBy\":\"吴医师\",                                       "+
                "                                                                        "+
                "         \"isRetainedFlg\":\"1\"                                        "+
                "                                                                        "+
                "        }                                                               "+
                "                                                                        "+
                "        ],                                                              "+
                "                                                                        "+
                "        \"additionInfo\": {                                             "+
                "                                                                        "+
                "            \"standardVersionCode\": \"\",                              "+
                "                                                                        "+
                "            \"correlationId\": \"\",                                    "+
                "                                                                        "+
                "            \"asyncAsk\": \"0\",                                        "+
                "                                                                        "+
                "            \"errorCode\": \"\",                                        "+
                "                                                                        "+
                "            \"callback\": \"\",                                         "+
                "                                                                        "+
                "            \"errorMsg\": \"\",                                         "+
                "                                                                        "+
                "            \"curDllAddr\": \"10.180.193.46:4000\",                     "+
                "                                                                        "+
                "            \"receiverTradeNum\": \"\"                                  "+
                "                                                                        "+
                "        },                                                              "+
                "                                                                        "+
                "        \"head\": {                                                     "+
                "                                                                        "+
                "                              \"recordCount\": \"10\",                  "+
                "                                                                        "+
                "            \"sendTradeNum\": \"20789845889560-08954531-0155\",         "+
                "                                                                        "+
                "            \"systemType\": \"3\",                                      "+
                "                                                                        "+
                "            \"senderCode\": \"400000181\",                              "+
                "                                                                        "+
                "            \"busenissType\": \"2\",                                    "+
                "                                                                        "+
                "            \"clientmacAddress\": \"AE:AC:BD:1A:C3\",                   "+
                "                                                                        "+
                "            \"standardVersionCode\": \"\",                              "+
                "                                                                        "+
                "            \"senderName\": \"武汉市中心医院\",                         "+
                "                                                                        "+
                "            \"intermediaryName\": \"市医保\",                           "+
                "                                                                        "+
                "            \"hosorgName\": \"测试\",                                   "+
                "                                                                        "+
                "            \"intermediaryCode\": \"2\",                                "+
                "                                                                        "+
                "            \"receiverName\": \"平安养老保险股份有限公司\",             "+
                "                                                                        "+
                "            \"receiverCode\": \"200000042\",                            "+
                "                                                                        "+
                "            \"busseID\": \"3300\",                                      "+
                "                                                                        "+
                "            \"hosorgNum\": \"00001\"                                    "+
                "                                                                        "+
                "        }                                                               "+
                "                                                                        "+
                "    }                                                                   "+
                "                                                                        "+
                "}                                                                       ";
        return s;


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

        init();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                invoke();
            }
        }, 1000, replay);
    }
}
