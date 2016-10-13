package com.fendany.doc;

/**
 * Created by zuoer on 16-10-13.
 */
public interface DocCommandParams {

    interface PARSE {

        /**
         * 固定长度 报文
         */
        String LENGTH = "Content-Length";

        /**
         * 不定长度报文，或超级长度报文
         */
        String TRANSFER_ENCODING = "Transfer-Encoding: chunked\r\n";

    }

    interface COMMAND {

        String GET_INFO = "GET /info";

        String GET_CONTAINERS_JSON = "GET /containers/json";

        String GET_CONTAINERS = "GET /containers/";

        String GET_IMAGES = "GET /images/json";

        String POST_CONTAINERS = "POST /containers/";

        String HTTP = " HTTP/1.1\n";

        String HOST = "Host: \r\n";

        String USER_AGENT = "User-Agent: Docker-Client (linux)\n";

        String CONTENT_LENGTH = "Content-Length: 0\r\n";

        String CONTENT_TYPE = "Content-Type: text/plain\r\n";

    }


}
