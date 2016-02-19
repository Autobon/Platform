package com.hpe.crypto;

import com.autobon.crypto.MD5;
import org.junit.*;

public class MD5Test {

    @Test
    public void checkMD5(){
        MD5 target = new MD5();
        String md5 = target.getMD5ofStr("admin123");
        Assert.assertEquals("0192023A7BBD73250516F069DF18B500", md5);
    }
}
