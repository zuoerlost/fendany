package com.fendany.demo;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

/**
 * Created by zuoer on 16-9-18.
 *
 */
public class Test4Demo {

    public static void test00() throws UnsupportedEncodingException {

        HttpPost httppost = new HttpPost("http://localhost:2375/containers/0419099d0b7f/attach?logs=0&stream=1&stdin=1&stdout=1&stderr=1");
        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httppost)) {

            System.out.println("executing request " + httppost.getURI());
            HttpEntity entity = response.getEntity();

            final InputStream in = entity.getContent();
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line1 = null;
                    while ((line1 = reader.readLine()) != null) {
                        System.out.println(line1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

//            System.out.println("--------------0000---------------");
//            while (true) {
//                Scanner scanner = new Scanner(System.in);
//                String line = scanner.nextLine();
//                System.out.println(line);
//                if (StringUtils.isNotEmpty(line)) {
//                    if (line.equals("exit")) {
//                        break;
//                    } else {
//
//                        byte[] msg = (line + "\n").getBytes();
//                        int length = msg.length;
//
//                        byte[] header = new byte[8];
//                        /**
//                         * 0: stdin (will be writen on stdout)
//                         * 1: stdout
//                         * 2: stderr
//                         */
//                        header[0] = 0;
//                        header[1] = 0;
//                        header[2] = 0;
//                        header[3] = 0;
//                        header[4] = (byte) ((length >>> 24) & 0xff);
//                        header[5] = (byte) ((length >>> 16) & 0xff);
//                        header[6] = (byte) ((length >>> 8) & 0xff);
//                        header[7] = (byte) (length & 0xff);
//
//                        OutputStream out = new ByteArrayOutputStream();
//                        out.write(header);
//                        out.write(line.getBytes());
//                        entity.writeTo(out);
////                        InputStream in = entity.getContent();
////                        byte[] result = new byte[8];
////                        in.read(result);
//                    }
//                }
//                System.out.println("----------------SCANER-------------");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        test00();
    }

}
