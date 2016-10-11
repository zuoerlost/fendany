package com.fendany.demo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * 2016年10月08日10:47:08
 * 尝试远程链接方案
 *
 */
public class SSHConnectDemo {

    private static Log log = LogFactory.getLog(SSHConnectDemo.class);

    private String ip;

    private String username;

    private String password;

    private int port = 22;

    private Session session = null;

    private Channel channel = null;

    private InputStream inStream = null;

    private OutputStream osStream = null;

    //用于存放命令
    private BlockingQueue<String> queueCommand = new ArrayBlockingQueue<String>(10);

    //是否已经读取完
    private boolean isFinishRead = false;

    /**
     * @param ip       ip地址
     * @param username ssh的用户名
     * @param password 密码
     */
    public SSHConnectDemo(String ip, String username, String password) {
        this.ip = ip.trim();
        this.username = username.trim();
        this.password = password.trim();
    }

    /**
     * ssh交互的连接
     */
    public void connect() throws Exception {
        try {
            session = (new JSch()).getSession(this.username, this.ip, this.port);
            session.setPassword(this.password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(20000);
            channel = session.openChannel("shell");
            inStream = channel.getInputStream();
            osStream = channel.getOutputStream();
            channel.connect(10000);
            //输入命令的线程
            Write w = new Write(osStream);
            //读取命令返回数据的线程
            Read r = new Read(inStream);
            new Thread(w).start();
            new Thread(r).start();
            //等待读取的连接信息
            while (!isFinishRead) {
                Thread.sleep(10);
            }
            isFinishRead = false;
        } catch (JSchException e) {
            e.printStackTrace();
            log.error("Connect to " + ip + ":" + port + " failed,please check your username and password!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("connect to the system have an error:" + e.getMessage());
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
                        System.out.print(message);
                    }
                }

            } catch (TimeoutException e) {
                log.error("read the response message timeout");
            } catch (Exception e) {
                log.error("read the response message have an exception:" + e.getMessage());
            } finally {
                isFinishRead = true;
                executor.shutdownNow();
                log.info("finish to read the data!");
            }
        }
    }

    private class Write implements Runnable {
        private OutputStream out;

        Write(OutputStream out) {
            this.out = out;
        }

        public void run() {
            try {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();
                    if (StringUtils.isNotEmpty(line)) {
                        if (line.equals("exit")) {
                            break;
                        } else {
                            out.write((line + "\n").getBytes());
                            out.flush();
                        }
                    }
                }
            } catch (Exception e) {
                log.error("execute the command have an error:" + e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
            }
        }
    }
}