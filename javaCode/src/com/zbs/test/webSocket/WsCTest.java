package com.zbs.test.webSocket;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;

/**
 * Created by moilions on 2017/3/2.
 */
public class WsCTest {

    public void test00() throws IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Session session = container.connectToServer(null,null);

    }

}
