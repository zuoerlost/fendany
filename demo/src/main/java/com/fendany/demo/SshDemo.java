package com.fendany.demo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * Created by zuoer on 16-9-19.
 */
public class SshDemo {


    /**
     * 利用JSch包实现远程主机SHELL命令执行
     *
     * @param ip         主机IP
     * @param user       主机登陆用户名
     * @param psw        主机登陆密码
     * @param port       主机ssh2登陆端口，如果取默认值，传-1
     * @param privateKey 密钥文件路径
     * @param passphrase 密钥的密码
     */
    public static void sshShell(String ip, String user, String psw
            , int port, String privateKey, String passphrase) throws Exception {

        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();

        //设置密钥和密码
        if (privateKey != null && !"".equals(privateKey)) {
            if (passphrase != null && "".equals(passphrase)) {
                //设置带口令的密钥
                jsch.addIdentity(privateKey, passphrase);
            } else {
                //设置不带口令的密钥
                jsch.addIdentity(privateKey);
            }
        }

        if (port <= 0) {
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        } else {
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip, port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(300000);

        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("shell");
            channel.connect(1000);

            //获取输入流和输出流
            InputStream instream = channel.getInputStream();
            OutputStream outstream = channel.getOutputStream();

            //发送需要执行的SHELL命令，需要用\n结尾，表示回车
            String shellCommand = "ls \n";
            outstream.write(shellCommand.getBytes());
            outstream.flush();


            //获取命令执行的结果
            if (instream.available() > 0) {
                byte[] data = new byte[instream.available()];
                int nLen = instream.read(data);

                if (nLen < 0) {
                    throw new Exception("network error.");
                }

                //转换输出结果并打印出来
                String temp = new String(data, 0, nLen, "iso8859-1");
                System.out.println(temp);
            }
            outstream.close();
            instream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

    /**
     * 利用JSch包实现远程主机SHELL命令执行
     *
     * @param user 主机登陆用户名
     * @param psw  主机登陆密码
     */
    public static void sshShell(String user, String psw) throws Exception {

        ChannelShell channel = null;
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, "127.0.0.1");

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        try {
            //创建sftp通信通道
            System.out.println("______________________________");
            channel = (ChannelShell) session.openChannel("shell");
            //获取输入流和输出流
            InputStream instream = channel.getInputStream();
//            InputStream extStream = channel.getExtInputStream();
            OutputStream outstream = channel.getOutputStream();
            channel.connect();
            new Thread(new Write(outstream)).start();
            new Thread(new Read(instream)).start();

//            extStream.close();
//            outstream.close();
//            instream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

    private static class Read implements Runnable {

        public InputStream is;

        public Read(InputStream is) {
            this.is = is;
        }

        public void run() {
            System.out.println("start to read");
            try {
                while (true) {
                    if (is.available() > 0) {
                        byte[] data = new byte[is.available()];
                        int nLen = is.read(data);

                        if (nLen < 0) {
                            throw new Exception("network error.");
                        }
                        //转换输出结果并打印出来
                        String temp = new String(data, 0, nLen, "iso8859-1");
                        System.out.println(temp);
                    } else {
                        Thread.sleep(100);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static class Write implements Runnable {

        private OutputStream out;

        Write(OutputStream out) {
            this.out = out;
        }

        public void run() {
            try {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    String line = scanner.nextLine();
                    System.out.println("line : " + line);
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
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void main(String[] args) throws Exception {
//        sshShell("zuoer", "112");
//    }

}
