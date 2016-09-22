package com.fendany.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

//@ServerEndpoint("/sshws")
public class SSHWebSocket extends SSHConnection {

    private static final Log LOG = LogFactory.getLog(SSHWebSocket.class);

    private static final ConcurrentHashMap<String, Session> SESSION_CACHE = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) throws Exception {
        init("zuoer", "112");
        String id = getId();
        LOG.info(" 打开当前Session - " + id);
        SESSION_CACHE.put(id, session);
        connect();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        String id = close();
        SESSION_CACHE.remove(id);
        session.close();
        LOG.info(" 【WS】： close -- at " + id);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        writeCommand(message);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     */
    public void sendMessage(String message, Session session) throws IOException {
//        session.getBasicRemote().sendText(message);
        RemoteEndpoint.Basic basic = session.getBasicRemote();
        basic.sendText(message);
        basic.flushBatch();
    }

    /**
     * 不得不说 这个方法 有毒，调用就会有问题
     */
    public void sendAsyncMessage(String message, Session session) {
        session.getAsyncRemote().sendText(message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error(" 【WS】：" + error.getMessage(), error);
    }

    @Override
    protected void readMessage(String message, String id) {
        try {
            Session session = SESSION_CACHE.get(id);
            if (session != null) {
                sendMessage(message, session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
