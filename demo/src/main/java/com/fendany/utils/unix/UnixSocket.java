package com.fendany.utils.unix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 2016年09月22日17:09:05
 * @author zhangbaisong083
 *
 */
public class UnixSocket extends Socket {

    public static final String LIBC = "c";

    private static final short AF_UNIX = 1;

    private static final int SOCK_STREAM = 1;

    private static final int PROTOCOL = 0;

    private UnixSocketImpl library = null;

    private int sockfd = -1;

    private InputStream is = null;

    private OutputStream os = null;

    public UnixSocket(String path) throws SocketException {
        if (Platform.isWindows() || Platform.isWindowsCE()) {
            throw new SocketException("loadLibrary(): Unix sockets are not supported on Windows platforms");
        }

        library = (UnixSocketImpl) Native.loadLibrary(LIBC, UnixSocketImpl.class);
        this.sockfd = socket(AF_UNIX, SOCK_STREAM, PROTOCOL);

        try {
            UnixSocketAddress socketAddress = new UnixSocketAddress();
            socketAddress.setFamily(AF_UNIX);
            socketAddress.setPath(path);
            int i = this.connect(socketAddress, socketAddress.size());

            if (i != 0) {
                new SocketException("【UnixSocket】: could not connect to socket");
            }

        } catch (LastErrorException lee) {
            throwSocketException("【UnixSocket】: could not connect to socket", lee);
        }

        this.is = new BufferedInputStream(new UnixSocketInputStream(this));
        this.os = new BufferedOutputStream(new UnixSocketOutputStream(this));
    }


    private void throwIOException(String prefixMessage, LastErrorException lee) throws IOException {
        String strerror = library.strerror(lee.getErrorCode());
        throw new IOException(prefixMessage + ": " + strerror);
    }

    private void throwSocketException(String prefixMessage, LastErrorException lee) throws SocketException {
        String strerror = library.strerror(lee.getErrorCode());
        throw new SocketException(prefixMessage + ": " + strerror);
    }

    /**
     * 调用C库 打开sock文件链接
     */
    private int socket(int domain, int type, int protocol) throws SocketException {
        try {
            return library.socket(domain, type, protocol);
        } catch (LastErrorException lee) {
            throwSocketException("【UnixSocket】: could not open socket", lee);
            return -1;
        }
    }

    /**
     * 调用C库 连接SOCK文件
     */
    private int connect(UnixSocketAddress socketAddress, int addrlen) throws SocketException {
        try {
            int result = this.library.connect(this.sockfd, socketAddress, addrlen);
            return result;
        } catch (LastErrorException lee) {
            throwSocketException("【UnixSocket】: could not connect to socket", lee);
            return -1;
        }
    }

    /**
     * used by UnixInputStream
     */
    public int read(byte[] buf, int count) throws IOException {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(buf);
            int length = this.library.read(this.sockfd, buffer, count);
            return length;
        } catch (LastErrorException lee) {
            throwIOException("【UnixSocket】【read】: could not read from socket", lee);
            return -1;
        }
    }

    /**
     * Used by UnixOutputStream
     */
    public int write(byte[] buf, int count) throws IOException {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(buf);
            int length = this.library.write(this.sockfd, buffer, count);
            return length;
        } catch (LastErrorException lee) {
            throwIOException("【UnixSocket】【write】: could not write to socket", lee);
            return -1;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return is;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return os;
    }

    @Override
    public void shutdownInput() throws IOException {
        is = null;
    }

    @Override
    public void shutdownOutput() throws IOException {
        os = null;
    }

    @Override
    public synchronized void close() throws IOException {
        try {
            shutdownInput();
            shutdownOutput();
            this.library.close(this.sockfd);
        } catch (LastErrorException lee) {
            throwIOException("【UnixSocket】【close】: could not close socket", lee);
        }
    }
}