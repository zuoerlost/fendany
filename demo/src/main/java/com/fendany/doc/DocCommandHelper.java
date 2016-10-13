package com.fendany.doc;

import com.fendany.utils.unix.UnixSocker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fendany.doc.DocCommandParams.COMMAND.*;
import static com.fendany.doc.DocCommandParams.PARSE.LENGTH;
import static com.fendany.doc.DocCommandParams.PARSE.TRANSFER_ENCODING;

/**
 * Created by zuoer on 16-10-12.
 */
public class DocCommandHelper {

    private static final String LOG_HEAD = "【parse】【ERROR】: ";

    private static final List<String> HAVE_BODY = new ArrayList<String>();

    private static final List<String> HTTP_STATUS = new ArrayList<String>();

    static {
        // 目前探测到 只有200和400 有报文体
        HAVE_BODY.add("200");// 处理成功
        HAVE_BODY.add("400");// 错的请求信息
        HAVE_BODY.add("404");// 页面未找到
        HAVE_BODY.add("500");// 处理异常

        // 操作状态收集
        HTTP_STATUS.add("200");// 处理成功
        HTTP_STATUS.add("204");// 处理成功，没有文本信息
        HTTP_STATUS.add("304");// 操作没有些该内容

    }

    /**
     * 1.取第一行 HTTP 协议通信状态
     * 2.取当前报文类型标识
     * 3.取当前报文体
     */
    public static DocCommandResult parse(String result) throws Exception {

        DocCommandResult docCommandResult = new DocCommandResult();
        String[] fl_array = parseArray(result);
        docCommandResult.setHttpCode(fl_array[1]);
        docCommandResult.setHttpMessage(fl_array[2]);
        boolean flag = hasBody(fl_array[1]);
        docCommandResult.setFix(flag);
        if (flag) {
            // 目前探测到只有200的情况下有报文体，其他情况直接返回当前对象
            String body = result.substring(result.indexOf("\r\n\r\n") + 4);
            if (result.indexOf(LENGTH) > 0) {
                docCommandResult.setTypeFixLength();
                docCommandResult.setOut(body.trim());
            } else if (result.indexOf(TRANSFER_ENCODING) > 0) {
                int begin = body.indexOf("\r\n");
                int end = body.indexOf("\r\n0\r\n\r\n");
                docCommandResult.setTypeEndFile();
                docCommandResult.setOut(body.substring(begin, end).trim());
            } else {
                throw new Exception(LOG_HEAD + "当前报文中未出现报文长度类型 ");
            }
        }
        return docCommandResult;
    }

    public static String[] parseArray(String result) throws Exception {
        int fl_num = result.indexOf("\r\n");
        if (fl_num < 0) {
            throw new Exception(LOG_HEAD + "取HTTP状态异常 at " + result);
        }
        String[] httpStatus = new String[3];
        String fl = result.substring(0, fl_num).trim();
        int f_s = fl.indexOf(" ");
        String temp = fl.substring(f_s + 1);
        httpStatus[0] = fl.substring(0, f_s);
        httpStatus[1] = temp.substring(0, temp.indexOf(" "));
        httpStatus[2] = temp.substring(temp.indexOf("") + 1);
        return httpStatus;
    }

    public static boolean hasBody(String httpStatus) {
        if (StringUtils.isNotEmpty(httpStatus) && HAVE_BODY.contains(httpStatus))
            return true;
        return false;
    }

    public static boolean checkHttpStatus(String httpStatus) {
        if (StringUtils.isNotEmpty(httpStatus) && HTTP_STATUS.contains(httpStatus))
            return true;
        return false;
    }

    public static String buildGetInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(GET_INFO);
        appendCommon(stringBuffer);
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public static String buildGetContainers(String command) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(GET_CONTAINERS_JSON);
        stringBuffer.append(command);
        appendCommon(stringBuffer);
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public static String buildGetContainers(String nameOrId, String command) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(GET_CONTAINERS);
        stringBuffer.append(nameOrId);
        stringBuffer.append(command);
        appendCommon(stringBuffer);
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public static String buildPostContainers(String nameOrId, String command) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(POST_CONTAINERS);
        stringBuffer.append(nameOrId);
        stringBuffer.append(command);
        appendCommon(stringBuffer);
        stringBuffer.append(CONTENT_LENGTH);
        stringBuffer.append(CONTENT_TYPE);
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public static String buildGetImages(String command) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(GET_IMAGES);
        stringBuffer.append(command);
        appendCommon(stringBuffer);
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    /**
     * 添加默认信息
     */
    private static StringBuffer appendCommon(StringBuffer stringBuffer) {
        stringBuffer.append(HTTP);
        stringBuffer.append(HOST);
        stringBuffer.append(USER_AGENT);
        return stringBuffer;
    }

    public static DocCommandResult invoke(String request) throws Exception {
        String result = UnixSocker.INSTANCE.invoke(request);
        return DocCommandHelper.parse(result);
    }

}
