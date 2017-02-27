package com.fendany.demo;

import org.junit.Test;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by moilions on 2016/11/9.
 */
public class TestleyueWeijiwei {


    private static final String WJW_URL = "http://218.60.147.172:8001/ins/user/getUserFile";

    public static String c220 = "http://218.60.147.172:8001/ins/User/confirm";

    @Test
    public void testc210() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Testleyue.call(getC210(),WJW_URL);
    }

    @Test
    public void testc220() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Testleyue.call(getC220("2_21806406_1"),c220);
    }

    private String getC210(){
        return "{\n" +
                "            \"package\": {\n" +
                "                \"head\": {\n" +
                "                    \"busseID\": \"C210\",\n" +
                "                    \"sendTradeNum\": \"20150701083030-10011001-0001\",\n" +
                "                    \"senderCode\": \"100000001\",\n" +
                "                    \"senderName\": \"平安保险公司\",\n" +
                "                    \"receiverCode\": \"400012448\",\n" +
                "                    \"receiverName\": \"大连医科大学附属第二医院\",\n" +
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
                "                       {   \"inHospitalNum\":\"457210\"," +
                "                           \"pageNum\":\"1\" " +
                "                           ,\"name\":\"赵玉杰\"" +
                "                           ,\"treatBeginDate\":\"20161102\"," +
                "                           \"treatEndDate\":\"20161104\"" +
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

    private String getC220(String medicalNum){
        return "{\n" +
                "            \"package\": {\n" +
                "                \"head\": {\n" +
                "                    \"busseID\": \"C220\",\n" +
                "                    \"sendTradeNum\": \"20150701083030-10011001-0001\",\n" +
                "                    \"senderCode\": \"100000001\",\n" +
                "                    \"senderName\": \"平安保险公司\",\n" +
                "                    \"receiverCode\": \"400012448\",\n" +
                "                    \"receiverName\": \"大连医科大学附属第二医院\",\n" +
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
                "                       {   \"medicalNum\":\""+medicalNum+"\" \n," +
                "                           \"affirmFlg\":\"1\" \n" +
                "                           ,\"imageFlg\":\"0\" \n" +
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
