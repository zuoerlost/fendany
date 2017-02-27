package com.fendany.utils.security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by moilions on 2016/12/19.
 *
 */
public class DESedeUtil {

    private static final String DESede = "DES";

    private Key key;

    public DESedeUtil(String str) {
        this.setKey(str);
    }

    public DESedeUtil() {
        this.setKey("123abc.*");
    }

    public void setKey(String strKey) {
        try {
            SecretKeyFactory e = SecretKeyFactory.getInstance("DES");
            this.key = e.generateSecret(new DESKeySpec(strKey.getBytes("UTF8")));
        } catch (Exception var3) {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + var3);
        }
    }

    public String jiami(String strMing) {
        Object byteMi = null;
        Object byteMing = null;
        String strMi = "";
        BASE64Encoder base64en = new BASE64Encoder();

        try {
            byte[] byteMing1 = strMing.getBytes("UTF8");
            Object e = null;

            Cipher cipher;
            byte[] e1;
            try {
                cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                cipher.init(1, this.key, SecureRandom.getInstance("SHA1PRNG"));
                e1 = cipher.doFinal(byteMing1);
            } catch (Exception var19) {
                throw new RuntimeException("Error initializing SqlMap class. Cause: " + var19);
            } finally {
                cipher = null;
            }

            strMi = base64en.encode(e1);
        } catch (Exception var21) {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + var21);
        } finally {
            base64en = null;
            byteMi = null;
        }

        return strMi;
    }

    public String jiemi(String strMi) {
        BASE64Decoder base64De = new BASE64Decoder();
        Object byteMing = null;
        Object byteMi = null;
        String strMing = "";

        try {
            byte[] byteMi1 = base64De.decodeBuffer(strMi);
            Object byteFina = null;

            Cipher e;
            byte[] byteFina1;
            try {
                e = Cipher.getInstance("DES/ECB/PKCS5Padding");
                e.init(2, this.key, SecureRandom.getInstance("SHA1PRNG"));
                byteFina1 = e.doFinal(byteMi1);
            } catch (Exception var19) {
                throw new RuntimeException("Error initializing SqlMap class. Cause: " + var19);
            } finally {
                e = null;
            }

            strMing = new String(byteFina1, "UTF8");
        } catch (Exception var21) {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + var21);
        } finally {
            base64De = null;
            byteMi = null;
        }

        return strMing;
    }

    private byte[] getEncCode(byte[] byteS) {
        Object byteFina = null;

        Cipher cipher;
        byte[] byteFina1;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(1, this.key, SecureRandom.getInstance("SHA1PRNG"));
            byteFina1 = cipher.doFinal(byteS);
        } catch (Exception var8) {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + var8);
        } finally {
            cipher = null;
        }

        return byteFina1;
    }

    private byte[] getDesCode(byte[] byteD) {
        Object byteFina = null;

        Cipher cipher;
        byte[] byteFina1;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(2, this.key, SecureRandom.getInstance("SHA1PRNG"));
            byteFina1 = cipher.doFinal(byteD);
        } catch (Exception var8) {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + var8);
        } finally {
            cipher = null;
        }

        return byteFina1;
    }

//    public static void main(String[] args) {
//        DESedeUtil des = new DESedeUtil("12313235");
//        String str1 = "abc平安科技";
//        String str2 = des.jiami(str1);
//        DESedeUtil des1 = new DESedeUtil();
//        String deStr = des1.jiemi("yg2AB6LKFD/l5Z6VqrqcpA==");
//        System.out.println("密文:" + str2);
//        System.out.println("明文:" + deStr);
//    }


}
