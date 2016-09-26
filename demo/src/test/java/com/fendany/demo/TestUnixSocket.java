package com.fendany.demo;

import com.fendany.utils.unix.UnixSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.*;

/**
 * Created by zuoer on 16-9-21.
 * 测试使用UNIX
 */
public class TestUnixSocket {

    public static void main(String[] args) throws IOException {
        String filePath = "/tmp/docker.sock";
        UnixSocket unixSocket = new UnixSocket(filePath);
        String data = "GET /v1.23/containers/json?all=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String data_stats = "GET /v1.23/containers/68e0e0821b24/stats?stream=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        InputStream inputStream = unixSocket.getInputStream();
        OutputStream outputStream = unixSocket.getOutputStream();

        PrintWriter w = new PrintWriter(outputStream);
        w.print(data);
        w.flush();
        w.close();
        outputStream.close();

        Thread thread = new Thread(new TestUnixSocket.Read(inputStream));
        thread.start();

    }

    private static class Read implements Runnable {

        public InputStream is;

        public Read(InputStream is) {
            this.is = is;
        }

        public void run() {
            final byte[] buffer = new byte[1024];
            ExecutorService executor = null;
            try {
                executor = Executors.newFixedThreadPool(1);
                //这个线程主要是用于处理读取数据read阻塞超时的处理
                Callable<Integer> readTask = () -> is.read(buffer);
                int readByte = 1;
                while (readByte >= 0) {
                    Future<Integer> future = executor.submit(readTask);
                    //抛弃阻塞时间
                    readByte = future.get();
                    if (readByte >= 0) {
                        String message = new String(buffer, 0, readByte);
                        System.out.print(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executor.shutdownNow();
            }
        }
    }

}
