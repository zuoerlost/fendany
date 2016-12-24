package com.fendany.demo;

import com.fendany.utils.http.HttpFactory;
import com.fendany.utils.security.Des3Pro;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by moilions on 2016/11/9.
 * <p>
 * pid=7vl5qiJf0kt&iid=6eauuUYX7jA&hid=j5PR7kfSs4n
 */
public class Test3Des {

    private String url = "http://211.167.232.2:9898/ins/user/getUserFile";

    private String key = "@6eauuUYX7jA@LEYUE100@00";

    @Test
    public void testc210() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        call(getMsg());
    }

    private String call(String msg) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpFactory httpFactory = new HttpFactory();
        httpFactory.init();
        CloseableHttpClient closeableHttpClient = httpFactory.get();
        HttpPost httpPost = new HttpPost(url);
        byte[] key_byte = key.getBytes();
        try {
            byte[] msg_byte = Des3Pro.encrypt(key_byte, msg.getBytes());
            String req = Base64.encodeBase64String(msg_byte);
            System.out.println("【加密前】" + msg);
            System.out.println("【加密后】" + req);

//            req = sur1;

            StringEntity input = new StringEntity(req);
            httpPost.setEntity(input);
            HttpResponse response = closeableHttpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("【 解码前 】" + result);
            byte[] res_byte = Base64.decodeBase64(result);
            byte[] result_byte = Des3Pro.decrypt(key_byte, res_byte);
            String result_str = new String(result_byte);
            System.out.println("【 解码后 】" + result_str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMsg(){
        return "{\n" +
                "            \"package\": {\n" +
                "                \"head\": {\n" +
                "                    \"busseID\": \"C210\",\n" +
                "                    \"sendTradeNum\": \"20150701083030-10011001-0001\",\n" +
                "                    \"senderCode\": \"100000001\",\n" +
                "                    \"senderName\": \"平安保险公司\",\n" +
                "                    \"receiverCode\": \"400001431\",\n" +
                "                    \"receiverName\": \"郑州中心医院\",\n" +
                "                    \"intermediaryCode\": \"003\",\n" +
                "                    \"intermediaryName\": \"乐约健康\",\n" +
                "                    \"hosorgNum\": \"001\",\n" +
                "                    \"hosorgName\": \"操作员姓名\",\n" +
                "                    \"systemType\": \"1\",\n" +
                "                    \"busenissType\": \"2\",\n" +
                "                    \"standardVersionCode\": \"version:1.0.0\",\n" +
                "                    \"clientmacAddress \": \"30BB7E0A5E2D \",\n" +
                "                    \" recordCount \": \"1\"\n" +
                "                },\n" +
                "                \"body\": [" +
                "                       {   \"inHospitalNum\":\"500756956\"," +
                "                           \"pageNum\":\"1\" " +
                "                           ,\"name\":\"李时忠\"" +
//                "                           ,\"treatBeginDate\":\"20160914\"," +
//                "                           \"treatEndDate\":\"20160916\"" +
                "                       }\n" +
                "                ],\n" +
                "                \"additionInfo\": {\n" +
                "                    \"errorCode\": \"0\",\n" +
                "                    \"errorMsg\": \"\",\n" +
                "                    \"receiverTradeNum\": \"20150701083030-10012231-0001\",\n" +
                "                    \"correlationId\": \"\",\n" +
                "                    \"asyncAsk\": \"0\",\n" +
                "                    \"callback\": \"\",\n" +
                "                    \"curDllAddr\": \"\"\n" +
                "                }\n" +
                "            }\n" +
                "        }";
    }

}
