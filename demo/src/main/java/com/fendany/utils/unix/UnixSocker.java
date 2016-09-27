package com.fendany.utils.unix;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.*;

/**
 * Created by zuoer on 16-9-23.
 * <p>
 * UnixSocker
 * 1.线程池属性
 * 2.
 */
public enum UnixSocker {

    INSTANCE;

    private ExecutorService executorService = null;

    private BlockingDeque<UnixSockTask> tasks = null;

    private int loop = 10;

    private String path = "/tmp/docker.sock";

    UnixSocker() {
        try {
            executorService = Executors.newFixedThreadPool(loop);
            tasks = new LinkedBlockingDeque<>(loop);
            for (int i = 0; i < loop; i++) {
                tasks.put(new UnixSockTask(new UnixSocket(path)));
            }
        }catch (InterruptedException | SocketException e) {
            e.printStackTrace();
        }
    }

    public String invoke(String message)  {
        try {
            UnixSockTask unixSockTask = tasks.take();
            unixSockTask.send(message);
            Future<String> response = executorService.submit(unixSockTask);
            String result = response.get();
            tasks.put(unixSockTask);
            executorService.shutdown();
            return result;
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
