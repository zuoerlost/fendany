package com.fendany.utils.unix;

import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.concurrent.*;

/**
 * Created by zuoer on 16-9-23.
 * <p>
 * UnixSocker
 * 1.线程池属性
 * 2.
 */
public enum UnixSocker {

    INSTANCE;

    public ExecutorService executorService = null;

    public BlockingDeque<UnixSockTask> tasks = null;

    public int loop = 5;

    UnixSocker() {
        System.out.println("~~~1~~~");
        executorService = Executors.newFixedThreadPool(5);
        tasks = new LinkedBlockingDeque<>(loop);
        try {
            // 初始化所有待机UnixSocket
            for (int i = 0; i < loop; i++) {
                tasks.put(new UnixSockTask(new UnixSocket("/tmp/docker.sock")));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("~~~2~~~");
    }

    public String invoke(String message) throws InterruptedException, IOException, ExecutionException {
        System.out.println("~~~3~~~");
        UnixSockTask unixSockTask = tasks.take();
        unixSockTask.write(message);
        Future<String> response = executorService.submit(unixSockTask);
        String result = response.get();
        return result;
    }

    /**
     *
     */
    private class UnixSockTask implements Callable<String> {

        private UnixSocket unixSocket;

        private InputStream in;

        private OutputStream out;

        private boolean isDone;

        public UnixSockTask(UnixSocket unixSocket) {
            try {
                this.unixSocket = unixSocket;
                in = unixSocket.getInputStream();
                out = unixSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        @Override
//        public String call() throws Exception {
//            StringBuffer stringBuffer = new StringBuffer();
//            byte[] buffer = new byte[1024];
//            int readByte = 1;
//            while (readByte >= 0) {
//                readByte = in.read();
//                if (readByte >= 0) {
//                    String message = new String(buffer, 0, readByte);
//                    stringBuffer.append(message);
//                    String result = stringBuffer.toString();
//                    if (result.contains("Content-Length: ")) {
//                        int a = result.indexOf("Content-Length: ") + 16;
//                        if (result.contains("\r\n\r\n")) {
//                            int b = result.indexOf("\r\n\r\n");
//                            String length_ = result.substring(a, b);
//                            System.out.println("length_ : " + length_);
//                            int length = Integer.parseInt(length_);
//                            System.out.println("length : " + length);
//                            int now = result.getBytes().length;
//                            System.out.println("now : " + now);
//                            if (b + length == now) {
//                                break;
//                            }
//                        }
//                    } else if (result.contains("Transfer-Encoding: chunked\r")) {
//                        if (result.endsWith("\r\n0\r\n\r")) {
//                            break;
//                        }
//                    }
//                }
//            }
//            return null;
//        }

        public void write(String data) throws IOException {
            PrintWriter w = new PrintWriter(out);
            w.print(data);
            w.flush();
            w.close();
        }

        public UnixSocket getUnixSocket() {
            return unixSocket;
        }

        public boolean isDone() {
            return isDone;
        }

        @Override
        public String call() throws Exception {
            StringBuffer stringBuffer = new StringBuffer();
            final byte[] buffer = new byte[1024];
            ExecutorService executor = null;
            try {
                executor = Executors.newFixedThreadPool(1);
                //这个线程主要是用于处理读取数据read阻塞超时的处理
                Callable<Integer> readTask = () -> in.read(buffer);
                int readByte = 1;
                while (readByte >= 0) {
                    Future<Integer> future = executor.submit(readTask);
                    //抛弃阻塞时间
                    readByte = future.get();
                    if (readByte >= 0) {
                        String message = new String(buffer, 0, readByte);
                        stringBuffer.append(message);
                        String result = stringBuffer.toString();
                        if (result.contains("Content-Length: ")) {
                            int a = result.indexOf("Content-Length: ") + 16;
                            if (result.contains("\r\n\r\n")) {
                                int b = result.indexOf("\r\n\r\n");
                                String length_ = result.substring(a, b);
                                System.out.println("length_ : " + length_);
                                int length = Integer.parseInt(length_);
                                System.out.println("length : " + length);
                                int now = result.getBytes().length;
                                System.out.println("now : " + now);
                                if (b + length == now) {
                                    break;
                                }
                            }
                        } else if (result.contains("Transfer-Encoding: chunked\r")) {
                            if (result.endsWith("\r\n0\r\n\r")) {
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

}
