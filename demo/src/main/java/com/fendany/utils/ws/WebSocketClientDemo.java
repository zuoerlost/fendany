package com.fendany.utils.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by moilions on 2017/2/23.
 */
//@Component
public class WebSocketClientDemo implements InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketClientDemo.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info(" 【WS】【Client】：ready to connection server ");
        WebSocket ws = new WebSocketFactory().createSocket("ws://localhost:9999/ws/abc/123");
        ws.connect();
    }
}
