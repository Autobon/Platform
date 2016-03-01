package com.autobon.shared;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by dave on 16/3/1.
 */
public class Crypto {

    public static String encryptBySha1(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(text.getBytes());
            byte[] message = digest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : message) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() < 2) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    //AES加密,并返回BASE64编码
    //key不是16字节的倍数时,用字符#补齐成16字节的倍数
    public static String encryptAesBase64(String text, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(alignBytes(key, 16), "AES"));
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //解密上述加密的结果
    public static String decryptAesBase64(String text, String key) {

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(alignBytes(key, 16), "AES"));
            return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //将text字节长度补齐成base的倍数,返回字节数组
    private static byte[] alignBytes(String text, int base) {
        byte[] bytes = text.getBytes();
        if (bytes.length % base != 0) {
            StringBuilder sb = new StringBuilder(text);
            for (int i = 0; i < base-bytes.length%base; i++) {
                sb.append('#');
            }
            bytes = sb.toString().getBytes();
        }
        return bytes;
    }
}
