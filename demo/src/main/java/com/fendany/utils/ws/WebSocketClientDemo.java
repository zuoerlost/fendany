package com.fendany.utils.ws;

import com.neovisionaries.ws.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
        ws.addListener(new WebSocketListener() {

            @Override
            public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

            }

            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                System.out.println("______________-----onConnected----_____________");
            }

            @Override
            public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
                System.out.println("______________-----onConnectError----_____________");
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

            }

            @Override
            public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {

            }

            @Override
            public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

            }

            @Override
            public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

            }

            @Override
            public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {

            }

            @Override
            public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {

            }

            @Override
            public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {

            }

            @Override
            public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {

            }
        });
    }
}
