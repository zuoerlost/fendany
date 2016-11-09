package com.fendany.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyGenUtils {

    private static KeyGenerator desKeyGen = null;
    private static Logger LOG = LoggerFactory.getLogger(KeyGenUtils.class);

    static {
        try {
            SecureRandom rm = SecureRandom.getInstance("SHA1PRNG");
            rm.setSeed(System.currentTimeMillis());
            desKeyGen = KeyGenerator.getInstance("DESede");
            desKeyGen.init(168, rm);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static byte[] gen3DESKey() {
        return desKeyGen.generateKey().getEncoded();
    }

    public static String gen3DESKeyStr() {
        return Base64.encodeBase64String(gen3DESKey());
    }

}
