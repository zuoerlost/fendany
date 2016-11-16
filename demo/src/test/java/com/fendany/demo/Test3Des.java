package com.fendany.demo;

import com.alibaba.fastjson.JSON;
import com.fendany.utils.http.HttpFactory;
import com.fendany.utils.security.Des3Pro;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
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

    private String msg = "{\"package\":{\"additionInfo\":" +
            "{\"asyncAsk\":\"0\",\"callback\":\"\",\"correlationId\":\"\",\"curDllAddr\":\"10.180.193.46:4000\",\"errorCode\":\"\",\"errorMsg\":\"\",\"receiverTradeNum\":\"20161116110029-11101-1159\"}," +
            "\"body\":[{\"credentialNum\":\"\",\"credentialType\":\"\",\"inHospitalNum\":\"500461899\",\"name\":\"古菊莲\",\"pageNum\":\"1\",\"treatBeginDate\":\"20161001\",\"treatEndDate\":\"20161108\"}]," +
            "\"head\":{\"busenissType\":\"2\",\"busseID\":\"C210\",\"clientmacAddress\":\"AE:AC:BD:1A:C3\",\"hosorgName\":\"测试粒\",\"hosorgNum\":\"00001\",\"intermediaryCode\":\"\",\"intermediaryName\":\"\",\"receiverCode\":\"400001431\",\"receiverName\":\"郑州市中心医院\",\"recordCount\":\"10\",\"sendTradeNum\":\"20160621889560-11111111-123\",\"senderCode\":\"200000148\",\"senderName\":\"中国平安财产保险股份有限公司\",\"standardVersionCode\":\"1.0.0\",\"systemType\":\"1\"}}}";

    private String msg1 = "{\"package\":{\"additionInfo\":{\"asyncAsk\":\"0\",\"callback\":\"\",\"correlationId\":\"\",\"curDllAddr\":\"10.180.193.46:4000\",\"errorCode\":\"\",\"errorMsg\":\"\",\"receiverTradeNum\":\"20161116153028-11101-1236\"},\"body\":[{\"credentialNum\":\"\",\"credentialType\":\"\",\"inHospitalNum\":\"592700223\",\"name\":\"韦子萱\",\"pageNum\":\"1\",\"treatBeginDate\":\"20161104\",\"treatEndDate\":\"20161108\"}],\"head\":{\"busenissType\":\"2\",\"busseID\":\"C210\",\"clientmacAddress\":\"AE:AC:BD:1A:C3\",\"hosorgName\":\"测试粒\",\"hosorgNum\":\"00001\",\"intermediaryCode\":\"\",\"intermediaryName\":\"\",\"receiverCode\":\"400001431\",\"receiverName\":\"郑州市中心医院\",\"recordCount\":\"10\",\"sendTradeNum\":\"20160621889560-11111111-123\",\"senderCode\":\"200000148\",\"senderName\":\"中国平安财产保险股份有限公司\",\"standardVersionCode\":\"1.0.0\",\"systemType\":\"1\"}}}";

    private String key = "@6eauuUYX7jA@LEYUE100@00";

    private String res = "to+GuYqf+HwOOs3RHYgrkxVV86RVHFrJQgejFGTTPOfvEWn52mLR/faf1GZiq6VDPCMRyGXFrol+IppxImFMvwILcz/qLazDjltDISEtyXwe+q6YEhXxlpg/l/MnEaVdZ320cEtn+SJWGvv8It54YupXBxCEbgntQFO3HhS8UDwyvefZoNOu+g==";

    private String surce = "ihlED7zNznd5MS6vlWi6HzGsmqsbKmn8MlN7yKMeAgBCsrb3CGFrdvRxPU5sqBT9DJHddC0vkr8XDUX7UQq1IyI7hxtBhSy5g9djcjLLZgDHdPwCLt2K1INxKDz3ABXs9PUmnYuqISxjXF3wctmPBBjBdaIneFEqx+sY0i+93wjvT3G1KVoBBzd4n3HL51Oj/ghyuTBkRXT/tCJ46Qsy+PN+ph0c78NrxxTudQER1XS5BCNp4Afk8OhKfiX1wtUhlYJhhdEMACHvJdtTS44AGR3DqGeISYtQa21ALb/PHtuCtnMf2q2Fq721n/Llik3/6tTZ8Ihjw9FBc1CKTrn0ZBUm0CPp6iZVY6Z63WnHHYX3YVhSci6xNlPvfTynJLG2bLjSZ3pBkRi59c91Ly8xr5eq1hRiqRDxXv6WcV0+DBfwwJ6HFKRIuwiM7FLXuwfiZ320cEtn+SJFlYEAd1VnKwlGC8iCemf1HgIvfQMeXz4fw/Tku1v07XyjQWfEcE7jX3JiJZlVSvYTG7Rrj3xCNE9nayFLVFkQOf5/k8Jy+n9xUw1um09WcEJ9wJiKlBtWEZaFDcbXBLofqqd6tDC8GMM2yD6DoOOU3XaaFxTED2Yp7NrDrLEW4HnuZIlZHUCMrjsqmrkXMbHZsfWR6i2nupszlZ7WHAKO8kMUeXrFIlOwrn/1n3iF1i+ZoIYWxhSf7t4b00o+Y2zFdCV2xuvLsf2cizVhAFjMlGWkHC3b2o1fg9B1u0ImjCGxeWD9AZoTgJJjKfHvZ7rKVdou/vNdsZyA2zIMyjVHyZ4laUpbi+odSjW4OcrMEQtiqLabCjGKS940eMoEjMpxpdljoM8bF2Xt0zr5DNUW4/7sDIL2v5bu3hvTSj5jbElK3gi8LW4G/iiTUSBH8p5DSUXd4SyR/mybAjJ7FHeUGntqfAs5fxL6uJ5Jc2qrF/nMie/KUm67o+UTifGF2QKI8zNaKPgtDyo7P0Syf/MjlN5u8ihiywxSo7rAyB4RsJ4GKyibgBq0tHfILpWE2I7IUYlQfGnBEw==\n";

    private String sur1 = "ihlED7zNznd5MS6vlWi6HzGsmqsbKmn8MlN7yKMeAgBCsrb3CGFrdvRxPU5sqBT9DJHddC0vkr8XDUX7UQq1IyI7hxtBhSy5g9djcjLLZgDHdPwCLt2K1INxKDz3ABXs9PUmnYuqISxjXF3wctmPBBjBdaIneFEqx+sY0i+93wjvT3G1KVoBBzd4n3HL51Oj/ghyuTBkRXT/tCJ46Qsy+PN+ph0c78NrxxTudQER1XS5BCNp4Afk8OhKfiX1wtUhlYJhhdEMACHvJdtTS44AGR3DqGeISYtQa21ALb/PHtuCtnMf2q2Fq721n/Llik3/6tTZ8Ihjw9FBc1CKTrn0ZBUm0CPp6iZVY6Z63WnHHYX3YVhSci6xNlPvfTynJLG2bLjSZ3pBkRi59c91Ly8xr5eq1hRiqRDxXv6WcV0+DBfwwJ6HFKRIuwiM7FLXuwfiZ320cEtn+SJFlYEAd1VnKwlGC8iCemf1HgIvfQMeXz4fw/Tku1v07XyjQWfEcE7jX3JiJZlVSvYTG7Rrj3xCNE9nayFLVFkQOf5/k8Jy+n9xUw1um09WcEJ9wJiKlBtWEZaFDcbXBLofqqd6tDC8GMM2yD6DoOOU3XaaFxTED2Yp7NrDrLEW4HnuZIlZHUCMrjsqmrkXMbHZsfWR6i2nupszlZ7WHAKO8kMUeXrFIlOwrn/1n3iF1i+ZoIYWxhSf7t4b00o+Y2zFdCV2xuvLsf2cizVhAFjMlGWkHC3b2o1fg9B1u0ImjCGxeWD9AZoTgJJjKfHvZ7rKVdou/vNdsZyA2zIMyjVHyZ4laUpbi+odSjW4OcrMEQtiqLabCjGKS940eMoEjMpxpdljoM8bF2Xt0zr5DNUW4/7sDIL2v5bu3hvTSj5jbElK3gi8LW4G/iiTUSBH8p5DSUXd4SyR/mybAjJ7FHeUGntqfAs5fxL6uJ5Jc2qrF/nMie/KUm67o+UTifGF2QKI8zNaKPgtDyo7P0Syf/MjlN5u8ihiywxSo7rAyB4RsJ4GKyibgBq0tHfILpWE2I7IUYlQfGnBEw==\n";

    @Test
    public void test00() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
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
        }

    }

    @Test
    public void test01() {
//        byte[] key_byte = Base64.decodeBase64(key);
        byte[] key_byte = key.getBytes();
        System.out.println("【KEY_LEN】" + key_byte.length);
        System.out.println("【 解码前 】" + surce);
        byte[] res_byte = Base64.decodeBase64(surce);
        byte[] result = Des3Pro.decrypt(key_byte, res_byte);
        String result_str = new String(result);
        System.out.println("【 解码后 】" + result_str);
        System.out.println("【 name  】" + JSON.parseObject(result_str).get("name"));
    }

    private static class Result {

        private String code;

        private String message;

        private String data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "code='" + code + '\'' +
                    ", message='" + message + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

    private String getMsg(){
        return "{\n" +
                "            \"package\": {\n" +
                "                \"head\": {\n" +
                "                    \"busseID\": \"1800\",\n" +
                "                    \"sendTradeNum\": \"20150701083030-10011001-0001\",\n" +
                "                    \"senderCode\": \"001\",\n" +
                "                    \"senderName\": \"平安保险公司\",\n" +
                "                    \"receiverCode\": \"200000042\",\n" +
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
                "                \"body\": [{\"credentialType\":\"1\",\"credentialNum\":\"341221198903048135\",\"name\":\"\\u5218\\u5e86\\u8f89\",\"beginDate\":\"20161110\",\"endDate\":\"20161210\"}\n" +
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
