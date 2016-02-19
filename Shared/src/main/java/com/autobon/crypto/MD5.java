package com.autobon.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import javax.xml.bind.DatatypeConverter;

public class MD5 {
    public MD5() {
    }

    public String getMD5ofStr(String inbuf) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(inbuf.getBytes());
            byte[] buf = md5.digest();
            return DatatypeConverter.printHexBinary(buf);
        }
        catch(Exception ex){
            return null;
        }
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16).toUpperCase();
    }


    public static String getFileSHA1(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("SHA1");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16).toUpperCase();
    }


    public static String getFileCRC32(File file) {
        FileInputStream fileinputstream;
        try {
            fileinputstream = new FileInputStream(file);
            CRC32 crc32 = new CRC32();
            for (CheckedInputStream checkedinputstream = new CheckedInputStream(
                    fileinputstream, crc32); checkedinputstream.read() != -1;) {
            }
            return Long.toHexString(crc32.getValue());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        // <filepath,md5>
        Map<String, String> map = new HashMap<String, String>();
        String md5;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5 = getFileMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }


    public static Map<String, String> getDirSHA1(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        // <filepath,SHA1>
        Map<String, String> map = new HashMap<String, String>();
        String sha1;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirSHA1(f, listChild));
            } else {
                sha1 = getFileSHA1(f);
                if (sha1 != null) {
                    map.put(f.getPath(), sha1);
                }
            }
        }
        return map;
    }


    public static Map<String, String> getDirCRC32(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        // <filepath,CRC32>
        Map<String, String> map = new HashMap<String, String>();
        String crc32;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirCRC32(f, listChild));
            } else {
                crc32 = getFileCRC32(f);
                if (crc32 != null) {
                    map.put(f.getPath(), crc32);
                }
            }
        }
        return map;
    }
}
