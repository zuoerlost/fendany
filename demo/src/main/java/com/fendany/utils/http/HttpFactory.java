package com.fendany.utils.http;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpFactory {

    private PoolingHttpClientConnectionManager cm;

    public void init() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory cf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry socketRegistry = RegistryBuilder.create()
                .register("https", cf)
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                .build();
        cm = new PoolingHttpClientConnectionManager(socketRegistry);
        cm.setMaxTotal(1);
        cm.setDefaultSocketConfig(socketConfig);
    }


    public CloseableHttpClient get() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    public void close(){
        HttpClients.custom().setConnectionManager(cm).disableAuthCaching();
    }

}
