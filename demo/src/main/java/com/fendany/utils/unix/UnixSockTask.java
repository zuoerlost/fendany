package com.fendany.utils.unix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ZHANGBAISONG083 on 16-9-27.
 * 单条返回数据 UnixSockTask
 */
public abstract class UnixSockTask implements Callable<String> {

    protected UnixSocket unixSocket;

    public UnixSockTask(UnixSocket unixSocket) {
        this.unixSocket = unixSocket;
    }

    public void send(String data) throws IOException {
        PrintWriter w = new PrintWriter(unixSocket.getOutputStream());
        w.print(data);
        w.flush();
        w.close();
    }

}