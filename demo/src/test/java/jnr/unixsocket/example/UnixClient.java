/*
 * This file is part of the JNR project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jnr.unixsocket.example;

import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.util.Scanner;

public class UnixClient {

    public static void test00() throws IOException {
        java.io.File path = new java.io.File("/var/run/docker.sock");

        UnixSocketAddress address = new UnixSocketAddress(path);
        UnixSocketChannel channel = UnixSocketChannel.open(address);
        System.out.println("connected to " + channel.getRemoteSocketAddress());

        new Thread(() -> {
            while (true) {
                PrintWriter w = new PrintWriter(Channels.newOutputStream(channel));
                Scanner scanner = new Scanner(System.in);
                System.out.println("------请输入-----");
                String line = scanner.nextLine();
                System.out.println("msg : " + line);
                if (line.equals("exit")) {
                    break;
                } else {
                    w.print(line);
                    w.flush();
                    w.close();
                    System.out.println("------Done-----");
                }
            }
            System.out.println(" 退出输入模式 ");
        }).start();

        System.out.println("------监听回执信息------ ");
        new Thread(() -> {
            try {
                InputStreamReader r = new InputStreamReader(Channels.newInputStream(channel));
                CharBuffer result = CharBuffer.allocate(1024);
                r.read(result);
                result.flip();
                System.out.println("read from server: " + result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void test01() throws IOException {
        File path = new File("/tmp/docker.sock");
        String data = "GET /v1.23/containers/json HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        UnixSocketAddress address = new UnixSocketAddress(path);
        UnixSocketChannel channel = UnixSocketChannel.open(address);
        System.out.println("connected to " + channel.getRemoteSocketAddress());

        InputStream inputStream = Channels.newInputStream(channel);
        OutputStream outputStream = Channels.newOutputStream(channel);

//        ReadableByteChannel readableByteChannel =  Channels.newChannel(inputStream);

        PrintWriter w = new PrintWriter(outputStream);
        w.print(data);
        w.flush();
        w.close();
        outputStream.close();

        InputStreamReader r = new InputStreamReader(inputStream);
        CharBuffer result = CharBuffer.allocate(1024);
        r.read(result);
        result.flip();
        System.out.println("read from server: " + result.toString());

//        byte[] buffer = new byte[1];
//        int i = inputStream.read(buffer);
//        while (i > 0) {
//            i = inputStream.read(buffer);
//        }


//        BufferedReader bufferedReader = new BufferedReader(new ReaderUTF8(inputStream));
//        String line = null;
//        while((line = bufferedReader.readLine()) !=null){
//            System.out.println(line);
//        }

//        ExecutorService executor = null;
//        //读取数据的阻塞时的超时时间
//        int readTimeout = 100000000;
//        try {
//            final byte[] buffer = new byte[1024];
//            executor = Executors.newFixedThreadPool(1);
//            //这个线程主要是用于处理读取数据read阻塞超时的处理
//            Callable<Integer> readTask = () -> inputStream.read(buffer);
//            int readByte = 1;
//            while (readByte >= 0) {
//                Future<Integer> future = executor.submit(readTask);
//                readByte = future.get(readTimeout, TimeUnit.MILLISECONDS);
//                if (readByte >= 0) {
//                    String message = new String(buffer, 0, readByte);
//                    System.out.println(message);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            executor.shutdownNow();
//        }

    }

    public static void main(String[] args) throws IOException {
        test01();
    }
}
