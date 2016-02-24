package com.autobon.getui.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by dave on 16/2/23.
 */
public abstract class GZip {
    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";

    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compress(bais, baos);
        byte[] output = baos.toByteArray();
        baos.flush();
        baos.close();
        bais.close();
        return output;
    }

    public static void compress(File file) throws Exception {
        compress(file, true);
    }

    public static void compress(File file, boolean delete) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + ".gz");
        compress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if(delete) {
            file.delete();
        }

    }

    public static void compress(InputStream is, OutputStream os) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        byte[] data = new byte[1024];

        int count;
        while((count = is.read(data, 0, 1024)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();
        gos.flush();
        gos.close();
    }

    public static void compress(String path) throws Exception {
        compress(path, true);
    }

    public static void compress(String path, boolean delete) throws IOException {
        File file = new File(path);
        compress(file, delete);
    }

    public static byte[] decompress(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decompress(bais, baos);
        data = baos.toByteArray();
        baos.flush();
        baos.close();
        bais.close();
        return data;
    }

    public static void decompress(File file) throws Exception {
        decompress(file, true);
    }

    public static void decompress(File file, boolean delete) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(".gz", ""));
        decompress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if(delete) {
            file.delete();
        }

    }

    public static void decompress(InputStream is, OutputStream os) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(is);
        byte[] data = new byte[1024];

        int count;
        while((count = gis.read(data, 0, 1024)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    public static void decompress(String path) throws IOException {
        decompress(path, true);
    }

    public static void decompress(String path, boolean delete) throws IOException {
        File file = new File(path);
        decompress(file, delete);
    }
}
