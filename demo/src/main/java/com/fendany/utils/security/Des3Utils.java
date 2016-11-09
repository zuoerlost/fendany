package com.fendany.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Des3Utils {

    private static final String Algorithm = "DES/ECB/NoPadding";

    private static final String DES3KEY = "8bd92b8143c143f4ac572e1387f0c406";

    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < b.length; i++) {
            stmp = (Integer.toHexString(b[i] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (i < b.length - 1) {
                hs = hs + ":";
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 加密3DES
     *
     * @return
     */
    public static String getKeyBy3Des() {
        String key = KeyGenUtils.gen3DESKeyStr();
        byte[] decodeBase64 = Base64.decodeBase64(key);
        byte[] enk = HexUtils.hex(DES3KEY);
        byte[] encoded = Des3Utils.encryptMode(enk, decodeBase64);
        return Base64.encodeBase64String(encoded);
    }

    /**
     * 解密3DES
     *
     * @param key
     * @return
     */
    public static byte[] getKeyFrom3Des(String key) {
        byte[] enk = HexUtils.hex(DES3KEY);
        byte[] decode = Base64.decodeBase64(key);
        byte[] bytes = Des3Utils.decryptMode(enk, decode);
        if (null == bytes) {
            return null;
        }
        return bytes;
    }

}
