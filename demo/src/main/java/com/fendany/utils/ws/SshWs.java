package com.fendany.utils.ws;

import com.fendany.utils.ssh.SSHConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{name}/{pass}")
@Component
public class SshWs {

    private static final Log LOG = LogFactory.getLog(SshWs.class);

    private static final ConcurrentHashMap<String, SSHConnection> SSH_CACHE = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(final Session session, @PathParam("name") String name, @PathParam("pass") String pass) {
        String id = session.getId();
        LOG.info(" 【ws】: NEW session at " + id);
        SSHConnection sshConnection = new SSHConnection() {
            @Override
            public void readMessage(String message, String id) {
                try {
                    sendMessage(message, session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            sshConnection.init(name, pass);
            sshConnection.connect();
            SSH_CACHE.put(id, sshConnection);
            LOG.info(" 【ws】: NEW sshConnection at " + sshConnection.getId());
        } catch (Exception e) {
            LOG.info(" 【ws】: error at " + e.getMessage(), e);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String id = session.getId();
        SSHConnection sshConnection = SSH_CACHE.remove(id);
        sshConnection.close();
        // 这个东西不能关，一关就会报错
//        session.close();
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String id = session.getId();
        SSHConnection sshConnection = SSH_CACHE.get(id);
        sshConnection.writeCommand(message);
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

    /**
     *
     */
    public void sendAsyncMessage(String message, Session session) throws IOException {
        session.getAsyncRemote().sendText(message);
    }

}