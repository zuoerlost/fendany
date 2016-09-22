package com.fendany.demo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Created by zuoer on 16-9-20.
 *
 */
public abstract class SSHConnection {

    private static Log log = LogFactory.getLog(SSHConnection.class);

    private String id;

    private String ip;

    private String username;

    private String password;

    private int port = 22;

    private Session session = null;

    private Channel channel = null;

    private InputStream inStream = null;

    private OutputStream osStream = null;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);


    protected void init(String ip, String username, String password, int port) {
        this.id = UUID.randomUUID().toString();
        this.ip = ip.trim();
        this.port = port;
        this.username = username.trim();
        this.password = password.trim();
    }

    protected void init(String ip, String username, String password) {
        init(ip, username, password, 22);
    }

    protected void init(String username, String password, int port) {
        init("127.0.0.1", username, password, port);
    }

    protected void init(String username, String password) {
        init("127.0.0.1", username, password, 22);
    }

    protected void connect() throws Exception {
        try {
            session = (new JSch()).getSession(this.username, this.ip, this.port);
            session.setPassword(this.password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(20000);
            channel = session.openChannel("shell");
            inStream = channel.getInputStream();
            osStream = channel.getOutputStream();
            channel.connect(10000);
            Read r = new Read(inStream);
            executorService.execute(r);
        } catch (JSchException e) {
            e.printStackTrace();
            log.error("Connect to " + ip + ":" + port + " failed,please check your username and password!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("connect to the system have an error:" + e.getMessage());
        }
    }

    protected String close() throws IOException {
        executorService.shutdownNow();
        osStream.close();
        inStream.close();
        channel.disconnect();
        session.disconnect();
        return id;
    }

    protected abstract void readMessage(String message,String id);

    protected void writeCommand(String command){
        try {
            osStream.write((command).getBytes());
            osStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Read implements Runnable {

        InputStream is;

        public Read(InputStream is) {
            this.is = is;
        }

        public void run() {
            log.info("start to read");
            final byte[] buffer = new byte[1024];
            ExecutorService executor = null;
            //读取数据的阻塞时的超时时间
            int readTimeout = 1000000000;
            try {
                executor = Executors.newFixedThreadPool(1);
                //这个线程主要是用于处理读取数据read阻塞超时的处理
                Callable<Integer> readTask = () -> is.read(buffer);
                int readByte = 1;
                while (readByte >= 0) {
                    Future<Integer> future = executor.submit(readTask);
                    readByte = future.get(readTimeout, TimeUnit.MILLISECONDS);
                    if (readByte >= 0) {
                        String message = new String(buffer, 0, readByte);
                        readMessage(message,id);
                    }
                }

            } catch (TimeoutException e) {
                log.error("read the response message timeout");
            } catch (Exception e) {
                log.error("read the response message have an exception:" + e.getMessage());
            } finally {
                executor.shutdownNow();
                log.info("finish to read the data!");
            }
        }
    }

    protected String getId() {
        return id;
    }
}
