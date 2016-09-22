package com.fendany.demo;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

//import java.util.concurrent.CopyOnWriteArraySet;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
// 类似Servlet的注解mapping。无需在web.xml中配置。
//@ServerEndpoint("/websocket")
public class MyWebSocket {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private static ConcurrentMap<String, HttpEntity> entityMap = new ConcurrentHashMap();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(final Session session) {
        new Thread(() -> {
            String sessionId = session.getId();
            addOnlineCount();
            System.out.println(" 当前回话ID -->" + sessionId);
            System.out.println(" 有新连接加入！当前在线人数为" + getOnlineCount());
            HttpPost httppost = new HttpPost("http://localhost:2375/containers/0419099d0b7f/attach?logs=0&stream=1&stdout=1");
            try (CloseableHttpClient httpclient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpclient.execute(httppost)) {
                System.out.println("executing request " + httppost.getURI());
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    entityMap.put(sessionId, entity);
                    sendMsgFromServer(session, entity);
                    System.out.println(" entityMap --> " + entityMap.toString());
                } else {
                    System.out.println(" error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        session.close();
//        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("收到了:" + message);
        String id = session.getId();
        HttpEntity entity = entityMap.get(id);
        OutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(message.getBytes("utf-8"));
        entity.writeTo(outputStream);
        System.out.println("已经写完了:" + message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 向客户端发送服务器信息
     */
    public void sendMsgFromServer(Session session, HttpEntity entity) throws IOException {
        InputStream in = entity.getContent();
//        BufferedReader bufferedReader = new BufferedReader(new ReaderUTF8(in));
//        StringBuffer stringBuffer = new StringBuffer();
//        String line = bufferedReader.readLine();
//        while (line != null) {
//            System.out.println(" line " + line);
//            stringBuffer.append(line);
//        }
//        sendMessage(stringBuffer.toString(),session);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     */
    public void sendMessage(String message, Session session) throws IOException {
//        session.getBasicRemote().sendText(message);
        session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount.decrementAndGet();
    }
}