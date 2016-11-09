package com.fendany.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MDUtils.class);
    private static ThreadLocal<MessageDigest> MD5 = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                LOG.error(e.getMessage(), e);
                return null;
            }
        }
    };
    private static ThreadLocal<MessageDigest> SHA256 = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                LOG.error(e.getMessage(), e);
                return null;
            }
        }
    };

    public static String sha256 (byte[] bytes) {
        SHA256.get().update(bytes);
        return Base64.encodeBase64String(SHA256.get().digest()).replaceAll("=", "");
    }

    public static String sha256Hex (byte[] bytes) {
        SHA256.get().update(bytes);
        return HexUtils.bytes2Hex(SHA256.get().digest());
    }

    /**
     *
     * MD5摘要,线程安全 <br>
     * 默认使用UTF-8, getBytes
     *
     * @param input
     * @return
     */
    public static String md5(String input) {
        try {
            return md5(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * MD5摘要, 线程安全
     *
     * @param input
     * @return
     */
    public static String md5(byte[] input) {
        MD5.get().update(input);
        return Base64.encodeBase64String(MD5.get().digest()).replaceAll("=", "");
    }

}
