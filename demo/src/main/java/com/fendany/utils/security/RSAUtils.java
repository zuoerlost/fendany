package com.fendany.utils.security;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by QIANDUO875 on 2016-07-26.
 */
public class RSAUtils {

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final int KEY_SIZE = 512;
    private static final int MAX_DECRYPT_BLOCK = KEY_SIZE / 8;
    private static final int MAX_ENCRYPT_BLOCK = MAX_DECRYPT_BLOCK - 11;

    public static RSAKeyPair genKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAKeyPair RSAKeyPair = new RSAKeyPair((RSAPublicKey)keyPair.getPublic(), (RSAPrivateKey)keyPair.getPrivate());
        return  RSAKeyPair;
    }

    public static String sign(RSAPrivateKey key, String content) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(key);
        signature.update(content.getBytes("UTF-8"));
        return HexUtils.bytes2Hex(signature.sign());
    }

    public static boolean verify(RSAPublicKey key, String content, String sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(key);
        signature.update(content.getBytes("UTF-8"));
        return signature.verify(HexUtils.hex2Bytes(sign));
    }

    public static String decryptFromHex(RSAKey key, String hexString) throws Exception {
        return new String(decrypt(key, HexUtils.hex2Bytes(hexString)), "UTF-8");
    }

//    public static byte[] decrypt(RSAKey key, byte[] bytes) throws Exception {
//        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, (Key) key);
//        byte[][] arrays = splitArray(bytes);
//        ByteArrayOutputStream is = new ByteArrayOutputStream();
//        for(byte[] arr : arrays){
//            is.write(cipher.doFinal(arr));
//        }
//        return is.toByteArray();
//    }

    public static byte[] decrypt(RSAKey key, byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, (Key) key);
        int inputLen = bytes.length;
        int offSet = 0;
        byte[] cache;
        ByteArrayOutputStream is = new ByteArrayOutputStream();
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            is.write(cache);
            offSet += MAX_DECRYPT_BLOCK;
        }
        return is.toByteArray();
    }

    private static byte[][] splitArray(byte[] data){
        int x = data.length / MAX_DECRYPT_BLOCK;
        int y = data.length % MAX_DECRYPT_BLOCK;
        int z = 0;
        if(y!=0){
            z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
            arr = new byte[MAX_DECRYPT_BLOCK];
            if(i==x+z-1 && y!=0){
                System.arraycopy(data, i*MAX_DECRYPT_BLOCK, arr, 0, y);
            }else{
                System.arraycopy(data, i*MAX_DECRYPT_BLOCK, arr, 0, MAX_DECRYPT_BLOCK);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static String encryptToHex(RSAKey key, String data) throws Exception {
        return HexUtils.bytes2Hex(encrypt(key, data));
    }

//    public static byte[] encrypt(RSAKey key, String data) throws Exception {
//        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
//        cipher.init(Cipher.ENCRYPT_MODE, (Key) key);
//
//        String[] datas = splitString(data);
//
//        ByteArrayOutputStream is = new ByteArrayOutputStream();
//        for (String str : datas) {
//            byte[] tmp = cipher.doFinal(str.getBytes("UTF-8"));
//            is.write(tmp);
//        }
//        return is.toByteArray();
//    }

    public static byte[] encrypt(RSAKey key, String data) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, (Key) key);
        byte[] bytes = data.getBytes("UTF-8");
        int inputLen = bytes.length;
        ByteArrayOutputStream is = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            is.write(cache);
            offSet += MAX_ENCRYPT_BLOCK;
        }
        return is.toByteArray();
    }

    public static RSAPublicKey toPublicKey(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey toPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec  keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }


    private static String[] splitString(String string) {
        int x = string.length() / MAX_ENCRYPT_BLOCK;
        int y = string.length() % MAX_ENCRYPT_BLOCK;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i=0; i<x+z; i++) {
            if (i==x+z-1 && y!=0) {
                str = string.substring(i*MAX_ENCRYPT_BLOCK, i*MAX_ENCRYPT_BLOCK+y);
            }else{
                str = string.substring(i*MAX_ENCRYPT_BLOCK, i*MAX_ENCRYPT_BLOCK+MAX_ENCRYPT_BLOCK);
            }
            strings[i] = str;
        }
        return strings;
    }

    public static final class RSAKeyPair {

        private RSAPrivateKey privateKey;
        private RSAPublicKey publicKey;

        public RSAKeyPair(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public RSAPrivateKey getPrivateKey() {
            return privateKey;
        }

        public RSAPublicKey getPublicKey() {
            return publicKey;
        }
    }

}
