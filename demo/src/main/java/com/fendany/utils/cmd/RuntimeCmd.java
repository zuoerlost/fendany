package com.fendany.utils.cmd;

/**
 * Created by zuoer on 16-10-19.
 */
public interface RuntimeCmd {

    RuntimeCmdBean execute(String cmd);

    RuntimeCmdBean execute(String[] cmd);
}
