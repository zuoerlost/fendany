package com.fendany.demo;

import com.fendany.utils.unix.UnixSocker;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by zuoer on 16-9-26.
 *
 * socat -t100 -v UNIX-LISTEN:/tmp/docker.sock,mode=777,reuseaddr,fork UNIX-CONNECT:/var/run/docker.sock
 *
 */
public class TestUnixSocket2 {

    @Test
    public void test00() throws InterruptedException, ExecutionException, IOException {
        String ps = "GET /v1.23/containers/json HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String ps_a = "GET /v1.23/containers/json?all=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String data_stats = "GET /v1.23/containers/68e0e0821b24/stats?stream=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String stop = "POST /v1.23/containers/68e0e0821b24/stop?t=10 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "Content-Length: 0\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n";

        String start = "POST /v1.23/containers/68e0e0821b24/start HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "Content-Length: 0\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n";

        String restart = "POST /v1.23/contains/68e0e0821b24/restart?t=10 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "Content-Length: 0\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n";

        String logs = "GET /v1.23/containers/68e0e0821b24/logs?stderr=1&stdout=1&tail=all HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String rm = "DELETE /v1.23/containers/68e0e0821b24 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String rmi_1 = "DELETE /v1.23/images/hiapdb:base HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String rmi_2 = "DELETE /v1.23/images/8b903c9a83de HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        System.out.println(UnixSocker.INSTANCE.invoke(ps));
    }

}
