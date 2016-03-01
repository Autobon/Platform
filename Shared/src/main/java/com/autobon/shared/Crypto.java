package com.autobon.shared;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
