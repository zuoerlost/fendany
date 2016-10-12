package com.fendany.doc;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuoer on 16-10-12.
 */
public class DocCommandHelper {

    private static final String LOG_HEAD = "【parse】【ERROR】: ";

    /**
     * 固定长度 报文
     */
    private static final String CONTENT_LENGTH = "Content-Length";

    /**
     * 布丁长度报文，或超级长度报文
     */
    private static final String TRANSFER_ENCODING = "Transfer-Encoding: chunked\r\n";

    private static final List<String> HTTP_STATUS = new ArrayList<String>();

    public static final String COMMAND = "Host: \r\nUser-Agent: Docker-Client/1.11.2 (linux)\r\n";

    static {
        HTTP_STATUS.add("200");
        HTTP_STATUS.add("404");
    }

    /**
     * 1.取第一行 HTTP 协议通信状态
     * 2.取当前报文类型标识
     * 3.取当前报文体
     */
    public static DocCommandResult parse(String result) throws Exception {

        DocCommandResult docCommandResult = new DocCommandResult();
        int fl_num = result.indexOf("\r\n");
        String fl = result.substring(0, fl_num);
        String[] fl_array = fl.split(" ");
        if (fl_array.length < 3) {
            throw new Exception(LOG_HEAD + "取HTTP状态异常 at " + fl);
        }
        docCommandResult.setHttpCode(fl_array[1]);
        docCommandResult.setHttpMessage(fl_array[2]);
        boolean flag = checkHttpStatus(fl_array[1]);
        docCommandResult.setFix(flag);
        if (flag) {
            // 目前探测到只有200的情况下有报文体，其他情况直接返回当前对象
            String body = result.substring(result.indexOf("\r\n\r\n") + 4);
            if (result.indexOf(CONTENT_LENGTH) > 0) {
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

    public static boolean checkHttpStatus(String httpStatus) {
        if (StringUtils.isNotEmpty(httpStatus) && HTTP_STATUS.contains(httpStatus)) {
            return true;
        }
        return false;
    }

}
