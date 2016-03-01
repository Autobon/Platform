package com.autobon.staff.service;

import com.autobon.crypto.MD5;
import com.autobon.redis.SessionRedis;
import com.autobon.staff.entity.Staff;
import com.autobon.staff.entity.StaffShow;
import com.autobon.staff.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * Created by liz on 2016/2/18.
 */

@Service
public class StaffService {
//    private SessionRedis sessionRedis = null;
//    @Autowired
//    public  void setSessionRedis(SessionRedis sessionRedis){this.sessionRedis = sessionRedis;}
//
//    private StaffRepository staffRepository = null;
//    @Autowired
//    public void setStaffAccountRepository(StaffRepository staffRepository){this.staffRepository = staffRepository;}
//
//    /**
//     * 验证账号密码
//     * @param username 用户名
//     * @param password 密码
//     * @return  状态码 0：验证成功   1，用户名不存在  2，密码错误
//     */
//    public int login(String username, String password) {
//        Staff staff = staffRepository.findByUsername(username);
//        if(staff == null){
//            return 1;
//        }
//
//        MD5 md5 = new MD5();
//        String md5Password = md5.getMD5ofStr(password + staff.getSalt());
//        if(staff.getPassword().equals(md5Password)){
//            return 0;
//        }
//
//        return 2;
//    }
//
//    /**
//     * 销毁token
//     *
//     * @param token 登陆令牌
//     * @return 状态码  0 成功  1 失败
//     */
//    public int logout(String token) {
//        boolean res = sessionRedis.delSessionAllOfList(token);
//        if(res){
//            return 0;
//        }
//        return 1;
//    }
//
//    /**
//     * 通过userName 生成一个唯一的token
//     *
//     * @param userName 手机号
//     * @return 加密后的唯一码
//     */
//    public String generateToken(String userName) {
//
//        MD5 md5 = new MD5();
//        //手机号码+时间戳+随机数 生成一个唯一的token
//        String token = md5.getMD5ofStr(userName + System.currentTimeMillis() + this.getRandom(4));
//
//        return token;
//    }
//
//
//
//    /**
//     * 保存token 及登陆信息
//     *
//     * @param token         登陆令牌
//     * @param staffShow     员工信息
//     * @param expireSeconds 保存时间
//     * @return 状态码 0 成功  1 失败
//     */
//    public int saveToken(String token, StaffShow staffShow, long expireSeconds) {
//        sessionRedis.saveSessionOfList(token, staffShow, expireSeconds);
//        return 0;
//    }
//
//    /**
//     * 通过token 解析 staffInfo
//     *
//     * @param token 登陆令牌
//     * @return staffInfo对象
//     */
//    public StaffShow loadToken(String token) {
//        StaffShow staffInfo = (StaffShow)sessionRedis.getSessionOfList(token);
//        return staffInfo;
//    }
//
//    public StaffShow convertByUsername(String Username) {
//        Staff staff = staffRepository.findByUsername(Username);
//
//        if(staff != null){
//            StaffShow staffShow = new StaffShow(staff.getId(), Username,new Date());
//            return staffShow;
//        }
//
//        return null;
//    }
//
//    /**
//     * 生成随机数
//     * @param length 随机数长度
//     * @return 随机数
//     */
//    public String getRandom(int length){
//
//        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        StringBuffer saltStr = new StringBuffer();
//        Random random = new Random();
//        int range = buffer.length();
//
//        for(int i = 0;i < length;i++){
//            saltStr.append(buffer.charAt(random.nextInt(range)));
//        }
//
//        return saltStr.toString();
//    }
}
