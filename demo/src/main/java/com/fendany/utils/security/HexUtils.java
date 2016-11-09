package com.fendany.utils.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by QIANDUO875 on 2016-06-17.
 */
public class HexUtils {


    public static String bytes2Hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    public static byte[] hex2Bytes(String hex) {
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException e) {
            return null;
        }
    }

    public static byte[] hex(String key) {
        String f = DigestUtils.md5Hex(key);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i = 0; i < 24; i++) {
            enk[i] = bkeys[i];
        }
        return enk;
    }

}
