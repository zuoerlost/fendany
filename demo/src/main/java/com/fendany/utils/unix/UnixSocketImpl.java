package com.fendany.utils.unix;

import com.sun.jna.Library;
import java.nio.ByteBuffer;

/**
 * JNA UNIX SOCK
 */
public interface UnixSocketImpl extends Library {

    int socket(int domain, int type, int protocol);

    int connect(int sockfd, UnixSocketAddress socketAddress, int addrlen);

    int read(int fd, ByteBuffer buffer, int count);

    int write(int fd, ByteBuffer buffer, int count);

    int close(int fd);

    String strerror(int errno);
}