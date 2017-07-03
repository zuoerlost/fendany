package com.fendany.utils.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;

//@ServerEndpoint("/ws/{name}/{pass}")
@ServerEndpoint("/ws/{senderCode}")
@Component
public class WebSocketServerDemo {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerDemo.class);

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(final Session session, @PathParam("senderCode") String senderCode) throws IOException {
        String id = session.getId();
        LOG.info("【ws】【DEMO】: NEW session at {},{},{}", senderCode);
        String uuid = UUID.randomUUID().toString().replace("-","");
        sendMessage(uuid + "Hello world", session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        LOG.info("【ws】【DEMO】: NEW session ready to close! ");
        // 这个东西不能关，一关就会报错
//        session.close();
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        if(StringUtils.isEmpty(message)){
            LOG.info(" 【onMessage】 ");
            return;
        }
        LOG.info("rev message : {}", message.toString());
        sendMessage(message.toString(), session);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     */
    public void sendMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }

}