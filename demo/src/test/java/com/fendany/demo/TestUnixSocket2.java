package com.fendany.demo;

import com.fendany.utils.unix.UnixSocker;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by zuoer on 16-9-26.
 *
 * socat -t100 -v UNIX-LISTEN:/tmp/docker.sock,mode=777,reuseaddr,fork UNIX-CONNECT:/var/run/docker.sock
 *
 */
public class TestUnixSocket2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        test00();
    }

    public static void test00() throws InterruptedException, ExecutionException, IOException {
        String data = "GET /v1.23/containers/json?all=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";
        System.out.println(UnixSocker.INSTANCE.invoke(data));
    }

}
