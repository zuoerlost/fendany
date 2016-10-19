package com.fendany.utils.cmd;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by zhangbaisong083 on 16-10-19.
 * 1.判断当前系统是否为linux
 * 2.当前需求只执行linux命令
 * 3.控制台信息输出
 * 4.错误控制台信息输出
 */
@Component
public class RuntimeCmdImpl implements RuntimeCmd {

    private static final String LINUX = "Linux";

    private static final String OS_NAME = "os.name";

    @Override
    public RuntimeCmdBean execute(String cmd) {
        RuntimeCmdBean runtimeCmdBean = new RuntimeCmdBean();
        String os_name = System.getProperty(OS_NAME);
        if (StringUtils.equals(LINUX, os_name)) {
            try {
                String[] cmd_array = cmd.split(" ");
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(cmd_array);
                InputStream inputStream = process.getInputStream();
                runtimeCmdBean.setSout(invoke(inputStream));
                InputStream errorStream = process.getErrorStream();
                String errorInfo = invoke(errorStream);
                if (StringUtils.isNotEmpty(errorInfo)) {
                    runtimeCmdBean.setEout(errorInfo);
                    runtimeCmdBean.setSuccess(false);
                } else {
                    runtimeCmdBean.setSuccess(true);
                }
            } catch (IOException e) {
                runtimeCmdBean.setSuccess(false);
                runtimeCmdBean.setEout(e.getMessage());
            }
        } else {
            runtimeCmdBean.setEout("current os is not linux");
            runtimeCmdBean.setSuccess(false);
        }
        return runtimeCmdBean;
    }

    @Override
    public RuntimeCmdBean execute(String[] cmd) {
        RuntimeCmdBean runtimeCmdBean = new RuntimeCmdBean();
        String os_name = System.getProperty(OS_NAME);
        if (StringUtils.equals(LINUX, os_name)) {
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(cmd);
                InputStream inputStream = process.getInputStream();
                runtimeCmdBean.setSout(invoke(inputStream));
                InputStream errorStream = process.getErrorStream();
                String errorInfo = invoke(errorStream);
                if (StringUtils.isNotEmpty(errorInfo)) {
                    runtimeCmdBean.setEout(errorInfo);
                    runtimeCmdBean.setSuccess(false);
                } else {
                    runtimeCmdBean.setSuccess(true);
                }
            } catch (IOException e) {
                runtimeCmdBean.setSuccess(false);
                runtimeCmdBean.setEout(e.getMessage());
            }
        } else {
            runtimeCmdBean.setEout("current os is not linux");
            runtimeCmdBean.setSuccess(false);
        }
        return runtimeCmdBean;
    }

    private String invoke(InputStream inputStream) throws IOException {
        BufferedInputStream in = new BufferedInputStream(inputStream);
        BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
        StringBuffer stringBuffer = new StringBuffer();
        String lineStr;
        while ((lineStr = inBr.readLine()) != null) {
            stringBuffer.append(lineStr);
        }
        inBr.close();
        in.close();
        return stringBuffer.toString();
    }

}
