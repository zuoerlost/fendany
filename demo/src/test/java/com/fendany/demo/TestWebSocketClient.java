package com.fendany.demo;

import com.neovisionaries.ws.client.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by moilions on 2017/2/24.
 *
 * 1.测试目的，无固定IP会怎么样
 * 2.测试100s发一次请求会怎么样，查看会不会断
 *
 */
public class TestWebSocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestWebSocketClient.class);

    @Test
    public void test00() {
        try {
            WebSocket ws = new WebSocketFactory().createSocket("ws://120.52.138.140/fendany/ws/demo");
            ws.connect();
            ws.addListener(new WebSocketAdapter() {

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    LOGGER.info(" 【onConnected】 ");
                }


                @Override
                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                    LOGGER.info(" 【onConnectError】 ： {}", exception.getLocalizedMessage(), exception);
//                    websocket.recreate().connect();
                }


                @Override
                public void onDisconnected(WebSocket websocket,
                                           WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                           boolean closedByServer) throws Exception {
                    LOGGER.info(" 【onDisconnected】 ");
//                    websocket.recreate().connect();
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
//                    System.out.println(" rev from server ：" + text);
                    LOGGER.info(" 【onTextMessage】rev from server ：" + text);
//                    websocket.sendText(text);
                }

            });

            // 等待全量信息停转
            for (; ; ) {

                if(ws.isOpen()){
                    ws.sendText("sj" + System.currentTimeMillis());
                } else {
                    LOGGER.info(" ws is closed ready to recon");
                    ws.recreate().connect();
                }
                Thread.sleep(100 * 1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
