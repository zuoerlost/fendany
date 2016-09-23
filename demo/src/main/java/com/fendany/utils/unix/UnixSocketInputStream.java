package com.fendany.utils.unix;

import java.io.IOException;
import java.io.InputStream;

/**
 * 封装 unix可使用的InputStream
 */
public class UnixSocketInputStream extends InputStream {

    private UnixSocket unixSocket = null;

    public UnixSocketInputStream(UnixSocket unixSocket) {
        this.unixSocket = unixSocket;
    }

    @Override
    public long skip(long n) throws IOException {
        return -1;
    }

    @Override
    public synchronized void mark(int readlimit) {
        // 目前没有用
    }

    @Override
    public synchronized void reset() throws IOException {
        // 目前没有用
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int available() throws IOException {
        return -1;
    }

    @Override
    public int read() throws IOException {
        byte[] data = new byte[1];
        int read = read(data);
        if (read != 1) {
            throw new IOException("【UnixSocketInputStream】【read】: could not read one byte");
        }
        return (int) data[0];
    }

    @Override
    public int read(byte[] data) throws IOException {
        return read(data, 0, data.length);
    }

    @Override
    public int read(byte[] data, int offset, int length) throws IOException {
        int readLength = 0;
        if (offset == 0) {
            readLength = this.unixSocket.read(data, length);
        } else {
            throw new IOException("【UnixSocketInputStream】【read】: offset not supported");
        }
        return readLength;
    }

    @Override
    public void close() throws IOException {
        // 目前没有用
    }
}
