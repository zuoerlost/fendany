package com.fendany.utils.ssh;

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
 * 1.目前实现了本地SSH调用，还没有实现远程SSH调用
 */
public abstract class SSHConnection implements SSHReadMessage {

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

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void init(String ip, String username, String password, int port) {
        this.id = UUID.randomUUID().toString();
        this.ip = ip.trim();
        this.port = port;
        this.username = username.trim();
        this.password = password.trim();
    }

    public void init(String ip, String username, String password) {
        init(ip, username, password, 22);
    }

    public void init(String username, String password, int port) {
        init("127.0.0.1", username, password, port);
    }

    public void init(String username, String password) {
        init("127.0.0.1", username, password, 22);
    }

    public void connect() throws Exception {
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(this.username, this.ip, this.port);
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

    public String close() throws IOException {
        executorService.shutdownNow();
        osStream.close();
        inStream.close();
        channel.disconnect();
        session.disconnect();
        return id;
    }

    public void writeCommand(String command) {
        try {
            osStream.write((command).getBytes());
            osStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getId() {
        return id;
    }

    private class Read implements Runnable {

        public InputStream is;

        public Read(InputStream is) {
            this.is = is;
        }

        public void run() {
            log.info("start to read");
            final byte[] buffer = new byte[1024];
            ExecutorService executor = null;
            //读取数据的阻塞时的超时时间
            try {
                executor = Executors.newFixedThreadPool(1);
                //这个线程主要是用于处理读取数据read阻塞超时的处理
                Callable<Integer> readTask = () -> is.read(buffer);
                int readByte = 1;
                while (readByte >= 0) {
                    Future<Integer> future = executor.submit(readTask);
                    readByte = future.get();
                    if (readByte >= 0) {
                        String message = new String(buffer, 0, readByte);
                        readMessage(message, id);
                    }
                }
            } catch (Exception e) {
                log.error("read the response message have an exception:" + e.getMessage());
            } finally {
                try {
                    log.info("finish to read the data!");
                    executor.shutdownNow();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error(" close error : " + e.getMessage(), e);
                }
            }
        }
    }


}
