package com.fendany.demo;

import com.neovisionaries.ws.client.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by moilions on 2017/2/24.
 */
public class TestWebSocketClient {

    @Test
    public void test00() {
        try {
            final WebSocket ws = new WebSocketFactory().createSocket("ws://localhost:9999/fendany/ws/demo");
            ws.connect();
            ws.sendPing();
            ws.sendText(" hello word! ");
            ws.addListener(new WebSocketAdapter() {

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    System.out.println(" rev from server ：" + text);
                    ws.sendText(text);
                }

            });

            // 等待全量信息停转
            for( ;; ){
                Thread.sleep(5000);
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
