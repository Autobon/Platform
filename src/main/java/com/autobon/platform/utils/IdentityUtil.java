package com.autobon.platform.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Component
public class IdentityUtil {

//    位权值数组

    private static byte[] Wi = new byte[17];

//    身份证前部分字符数

    private static final byte fPart = 6;

//    身份证算法求模关键值

    private static final byte fMod = 11;

//    旧身份证长度

    private static final byte oldIDLen = 15;

//    新身份证长度

    private static final byte newIDLen = 18;

//    新身份证年份标志

    private static final String yearFlag = "19";

//    校验码串 

    private static final String CheckCode = "10X98765432";

//    最小的行政区划码

    private static final int minCode = 150000;

//    最大的行政区划码

    private static final int maxCode = 700000;

//    旧身份证号码

//    private String oldIDCard="";

//    新身份证号码

//    private String newIDCard="";

//    地区及编码


    //private String Area[][2] =

    private static void setWiBuffer() {

        for (int i = 0; i < Wi.length; i++) {

            int k = (int) Math.pow(2, (Wi.length - i));

            Wi[i] = (byte) (k % fMod);

        }

    }


    //获取新身份证的最后一位:检验位

    private static String getCheckFlag(String idCard) {

        int sum = 0;

        //进行加权求和 

        for (int i = 0; i < 17; i++) {

            sum += Integer.parseInt(idCard.substring(i, i + 1)) * Wi[i];

        }

        //取模运算，得到模值

        byte iCode = (byte) (sum % fMod);

        return CheckCode.substring(iCode, iCode + 1);

    }


    //判断串长度的合法性

    private static boolean checkLength(final String idCard, boolean newIDFlag) {

        boolean right = (idCard.length() == oldIDLen) || (idCard.length() == newIDLen);

        newIDFlag = false;

        if (right) {

            newIDFlag = (idCard.length() == newIDLen);

        }

        return right;

    }


    //获取时间串

    private static String getIDDate(final String idCard, boolean newIDFlag) {

        String dateStr = "";

        if (newIDFlag)

            dateStr = idCard.substring(fPart, fPart + 8);

        else

            dateStr = yearFlag + idCard.substring(fPart, fPart + 6);

        return dateStr;

    }


    //判断时间合法性

    private static boolean checkDate(final String dateSource) {

        String dateStr = dateSource.substring(0, 4) + "-" + dateSource.substring(4, 6) + "-" + dateSource.substring(6, 8);

        System.out.println(dateStr);

        DateFormat df = DateFormat.getDateInstance();

        df.setLenient(false);

        try {

            Date date = df.parse(dateStr);

            return (date != null);

        } catch (ParseException e) {

            // TODO Auto-generated catch block

            return false;

        }

    }


    //旧身份证转换成新身份证号码

    public static String getNewIDCard(final String oldIDCard) {

        //初始化方法

        IdentityUtil.setWiBuffer();

        if (!checkIDCard(oldIDCard)) {

            return oldIDCard;

        }

        String newIDCard = oldIDCard.substring(0, fPart);

        newIDCard += yearFlag;

        newIDCard += oldIDCard.substring(fPart, oldIDCard.length());

        String ch = getCheckFlag(newIDCard);

        newIDCard += ch;

        return newIDCard;

    }


    //新身份证转换成旧身份证号码

    public static String getOldIDCard(final String newIDCard) {

        //初始化方法

        IdentityUtil.setWiBuffer();

        if (!checkIDCard(newIDCard)) {

            return newIDCard;

        }

        String oldIDCard = newIDCard.substring(0, fPart) +

                newIDCard.substring(fPart + yearFlag.length(), newIDCard.length() - 1);

        return oldIDCard;

    }


    //判断身份证号码的合法性

    public static boolean checkIDCard(final String idCard) {

        //初始化方法

        IdentityUtil.setWiBuffer();

        boolean isNew = true;

        //String message = "";

        if (!checkLength(idCard, isNew)) {

            //message = "ID长度异常";

            return false;

        }

        String idDate = getIDDate(idCard, isNew);

        if (!checkDate(idDate)) {

            //message = "ID时间异常";

            return false;

        }

        if (isNew) {

            String checkFlag = getCheckFlag(idCard);

            String theFlag = idCard.substring(idCard.length() - 1, idCard.length());

            if (!checkFlag.equals(theFlag)) {

                //message = "新身份证校验位异常";

                return false;

            }

        }

        return true;

    }


    public static void main(String[] args) {
        String idNo = "422130198809180235";
        String idNo2 = "429004192208280087";
        Boolean b1 = checkIDCard(idNo);
        Boolean b2 = checkIDCard(idNo2);
        System.out.println(" >>>>" + b1 + "<<<<<" + b2);
    }


    public IdentityUtil() {

        setWiBuffer();

    }


}