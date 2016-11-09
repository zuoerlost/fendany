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

    private String url = "http://211.167.232.2:9898/ins/user/authorize";

    private String msg = "{\"credentialType\":\"1\",\"credentialNum\":\"341221198903048135\",\"name\":\"\\u5218\\u5e86\\u8f89\",\"beginDate\":\"20161109\",\"endDate\":\"20161109\"}";

    private String key = "@6eauuUYX7jA@LEYUE100@00";

    private String res = "to+GuYqf+HwOOs3RHYgrkxVV86RVHFrJQgejFGTTPOfvEWn52mLR/faf1GZiq6VDPCMRyGXFrol+IppxImFMvwILcz/qLazDjltDISEtyXwe+q6YEhXxlpg/l/MnEaVdZ320cEtn+SJWGvv8It54YupXBxCEbgntQFO3HhS8UDwyvefZoNOu+g==";

    @Test
    public void test00() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpFactory httpFactory = new HttpFactory();
        httpFactory.init();
        CloseableHttpClient closeableHttpClient = httpFactory.get();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("pid", "7vl5qiJf0kt");
        httpPost.addHeader("iid", "6eauuUYX7jA");
        httpPost.addHeader("hid", "j5PR7kfSs4n");
        StringEntity input = null;
//        byte[] key_byte = Base64.decodeBase64(key);
        byte[] key_byte = key.getBytes();
        try {
            byte[] msg_byte = Des3Pro.encrypt(key_byte, msg.getBytes());
            String req = Base64.encodeBase64String(msg_byte);
            System.out.println("【加密前】" + msg);
            System.out.println("【加密后】" + req);
            input = new StringEntity(req);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(input);
        HttpResponse response = closeableHttpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        Result resultBean = JSON.parseObject(result , Result.class);
        System.out.println("【resultBean 】" + resultBean.toString());
        System.out.println("【 解码前 】" + resultBean.getData());
        byte[] res_byte = Base64.decodeBase64(resultBean.getData());
        byte[] result_byte = Des3Pro.decrypt(key_byte, res_byte);
        String result_str = new String(result_byte);
        System.out.println("【 解码后 】" + result_str);
    }

    @Test
    public void test01() {
//        byte[] key_byte = Base64.decodeBase64(key);
        byte[] key_byte = key.getBytes();
        System.out.println("【KEY_LEN】" + key_byte.length);
        System.out.println("【 解码前 】" + res);
        byte[] res_byte = Base64.decodeBase64(res);
        byte[] result = Des3Pro.decrypt(key_byte, res_byte);
        String result_str = new String(result);
        System.out.println("【 解码后 】" + result_str);
        System.out.println("【 name  】" + JSON.parseObject(result_str).get("name"));
    }

    private static class Result{

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

}
