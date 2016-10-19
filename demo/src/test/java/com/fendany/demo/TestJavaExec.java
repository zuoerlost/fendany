package com.fendany.demo;

import com.alibaba.fastjson.JSON;
import com.fendany.utils.cmd.RuntimeCmd;
import com.fendany.utils.cmd.RuntimeCmdImpl;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zuoer on 16-10-17.
 * 1.判断当前系统是否是linux
 * 2.输出错误日志以便查看
 *
 *
 */
public class TestJavaExec {

    @Test
    public void test00() {

        ExecutorService service = Executors.newFixedThreadPool(2);

        //linux
//      String cmd = "./fork_wait";
        String cmd = "ls";
//      String[] cmd=new String[3];
//      cmd[0]="/bin/sh";
//      cmd[1]="-c";
//      cmd[2]="ls -l ./";
        Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
        try {

            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            InputStream error = p.getErrorStream();
            OutputStream out = p.getOutputStream();
            InputStream inputStream = p.getInputStream();
            service.execute(new Task(error));
            service.execute(new Task(inputStream));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test01(){
        Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
        try {
//            String[] cmd=new String[3];
//            cmd[0]="ps";
//            cmd[1] = "-ef";
//            cmd[2] = "\\|";
//            cmd[2] = "grep";
//            cmd[2] = "docker";
            String cmd = "ps -ef ";
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            InputStream error = p.getErrorStream();
            OutputStream out = p.getOutputStream();
            InputStream inputStream = p.getInputStream();
            new Task(inputStream).run();
//            p.wait()
            new Task(error).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02(){
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperties().toString());
    }

    @Test
    public void test03(){
        RuntimeCmd runtimeCmd = new RuntimeCmdImpl();
//        System.out.println(JSON.toJSONString(runtimeCmd.execute("docker -H unix:///tmp/docker.sock exec -i shanghai sh -c 'ls -l'")));

        String[] cmd = new String[9];
        cmd[0] = "docker";
        cmd[1] = "-H";
        cmd[2] = "unix:///tmp/docker.sock";
        cmd[3] = "exec";
        cmd[4] = "-i";
        cmd[5] = "shanghai";
        cmd[6] = "sh";
        cmd[7] = "-c";
        cmd[8] = "ls -l && ps -ef";
        System.out.println(JSON.toJSONString(runtimeCmd.execute(cmd)));

    }

    private static class Task implements Runnable{

        private InputStream inputStream;

        public Task(InputStream in){
            inputStream = in;
        }

        @Override
        public void run() {
            BufferedInputStream in = new BufferedInputStream(inputStream);
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            try {
                while ((lineStr = inBr.readLine()) != null)
                    //获得命令执行后在控制台的输出信息
                    System.out.println(lineStr);// 打印输出信息
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inBr.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
