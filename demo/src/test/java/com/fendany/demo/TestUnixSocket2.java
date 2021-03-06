package com.fendany.demo;

import com.fendany.doc.DocCommandService;
import com.fendany.doc.DocCommandServiceImpl;
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

        String info = "GET /info HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/ (linux)\r\n" +
                "\r\n";

        String inspect = "GET /containers/shanghai/json HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/ (linux)\r\n" +
                "\r\n";

        String kill = "POST /containers/shanghai/kill?signal=KILL HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/ (linux)\r\n" +
                "Content-Length: 0\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n";

        String ps = "GET /containers/json HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String ps_a = "GET /containers/json?all=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client (linux)\r\n" +
                "\r\n";

        String ps_l = "GET /containers/json?limit=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String data_stats = "GET /v1.23/containers/68e0e0821b24/stats?stream=1 HTTP/1.1\r\n" +
                "Host: \r\n" +
                "User-Agent: Docker-Client/1.11.2 (linux)\r\n" +
                "\r\n";

        String stop = "POST /v1.23/containers/shanghai/stop?t=10 HTTP/1.1\r\n" +
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

        System.out.println(UnixSocker.INSTANCE.invoke(inspect));
    }

    @Test
    public void test01() throws Exception {
        DocCommandService docCommandService = new DocCommandServiceImpl();
//        System.out.println(docCommandService.getDockerInfo());
//        System.out.println(docCommandService.getRunningContainers());
//        System.out.println(docCommandService.getAllContainers());
//        System.out.println(docCommandService.getLastContainer());
//        System.out.println(docCommandService.getContainerInfoByNameOrId("shanghai111"));
//        System.out.println(docCommandService.startContainerByNameOrId("shanghai"));
//        System.out.println(docCommandService.restartContainerByNameOrId("shanghai"));
//        System.out.println(docCommandService.stopContainerByNameOrId("shanghai"));
        System.out.println(docCommandService.killContainerByNameOrId("shanghai"));
    }

}
