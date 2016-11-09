package com.fendany.utils.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Des3Pro {

    private static final String ECB =  "DES/ECB";
    private static final String PKCS5 =  "DES/ECB/PKCS5Padding";
    private static final String DESede =  "DESede";
    private static final String Algorithm = "DES/ECB/NoPadding";

    public static byte[] encrypt(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, DESede);
            //加密
            Cipher c1 = Cipher.getInstance(DESede);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, DESede);
            //解密
            Cipher c1 = Cipher.getInstance(DESede);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
