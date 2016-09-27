package com.fendany.utils.unix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ZHANGBAISONG083 on 16-9-27.
 * 单条返回数据 UnixSockTask
 */
public class UnixSockTask implements Callable<String> {

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final String TRANSFER_ENCODING = "Transfer-Encoding: chunked\r\n";

    private UnixSocket unixSocket;

    public UnixSockTask(UnixSocket unixSocket) {
        this.unixSocket = unixSocket;
    }

    public void send(String data) throws IOException {
        PrintWriter w = new PrintWriter(unixSocket.getOutputStream());
        w.print(data);
        w.flush();
        w.close();
    }

    @Override
    public String call() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        final byte[] buffer = new byte[1024];
        ExecutorService executor = null;
        try {
            executor = Executors.newFixedThreadPool(1);
            Callable<Integer> readTask = () -> unixSocket.getInputStream().read(buffer);
            int readByte = 1;
            while (readByte >= 0) {
                Future<Integer> future = executor.submit(readTask);
                //抛弃阻塞时间
                readByte = future.get();
                if (readByte >= 0) {
                    String message = new String(buffer, 0, readByte);
                    stringBuffer.append(message);
                    String result = stringBuffer.toString();
                    if (result.indexOf("\r\n") > 0 && (result.contains("HTTP/1.1 200 OK\r\n") || result.contains("HTTP/1.1 4"))) {
                        // 成功有相应信息模式
                        if (result.contains(CONTENT_LENGTH)) {
                            // 长度可预见模式
                            int a = result.indexOf(CONTENT_LENGTH) + 16;
                            if (result.contains("\r\n\r\n")) {
                                // body位置
                                int c = result.indexOf("\r\n\r\n") + 4;
                                // 头部输出完成
                                int b = result.substring(a).indexOf("\r\n");
                                String length_ = result.substring(a, a + b);
                                int length = Integer.parseInt(length_);
                                int now = result.length();
                                // \r\n 算两个字符 =..=
                                // 判断当前是否已经读取全部长度
                                if (c + length == now) {
                                    System.out.println("Read Over Length : " + now);
                                    break;
                                }
                            }
                        } else if (result.contains(TRANSFER_ENCODING)) {
                            // 长度不可预见模式
                            if (result.endsWith("\r\n0\r\n\r\n")) {
                                break;
                            }
                        }
                    } else {
                        // 除成功有相应信息外其他信息
                        if (result.endsWith("\r\n\r\n")) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }
        return stringBuffer.toString();
    }
}