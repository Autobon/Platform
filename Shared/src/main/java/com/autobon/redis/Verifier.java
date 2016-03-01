package com.autobon.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by wh on 2016/1/14.
 */
//@Component
public class Verifier {
    @Autowired
    SessionRedis sessionRedis;
    /**
     * 产生一个6位的纯数字的随机校验码
     * @param target 校验码目标
     * @param expiredSeconds 过期时间(秒)
     * @return 成功返回纯数字的6位校验码
     */
    public String generateCode(String target, int expiredSeconds){
        StringBuffer buffer = new StringBuffer("0123456789");
        StringBuffer saltStr = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();
        for(int i = 0;i < 6;i++){
            saltStr.append(buffer.charAt(random.nextInt(range)));
        }
        sessionRedis.saveSessionOfVal(target,saltStr.toString(),expiredSeconds);
        return saltStr.toString();
    }

    /**
     * 校验
     * @param target 校验目标
     * @param code 校验码
     * @return  返回码   -1：验证失败  0：验证码已过期  1：验证成功
     */
    public int verifyCode(String target, String code){
        int result = 0;
        String trueCode = sessionRedis.getSessionOfVal(target);
        if (trueCode!=null)
        {
            if(trueCode.equals(code)){
                result = 1;
            }
            else {
                result = -1;
            }
        }
        return result;
    }
}
