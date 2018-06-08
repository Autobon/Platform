package com.autobon.merchandiser.entity;

import com.autobon.shared.Crypto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


/**
 * Created by wh on 2018/5/30.
 */
@Entity
@Table(name="t_merchandiser")
public class Merchandiser  implements UserDetails {


    private static Logger log = LoggerFactory.getLogger(Merchandiser.class);
    public enum Status {
        VERIFIED(0), BANNED(1);
        private int statusCode;

        Status(int statusCode) {
            this.statusCode = statusCode;
        }

        public static Status getStatus(int statusCode) {
            for (Status s : Status.values()) {
                if (s.getStatusCode() == statusCode) return s;
            }
            return null;
        }
        public int getStatusCode() {
            return this.statusCode;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String phone; // 手机号

    @JsonIgnore
    @Column private String password; // 密码

    @Column private String name; // 姓名

    @Column private String gender; // 性别

    @Column private String avatar; // 头像

    @Column private String idNo; // 身份证编号

    @Column private String idPhoto; // 身份证正面照片

    @Column private String bank; // 银行卡归属银行

    @Column private String bankAddress; // 开户行地址

    @Column private String bankCardNo; // 银行卡号码

    @Column private Date lastLoginAt; // 最后登录时间

    @Column private String lastLoginIp; // 最后登录IP

    @Column private Date createAt; // 注册时间


    @Column private String pushId; // 个推客户端ID, 由手机端更新


    @JsonIgnore
    @Column(name = "status")
    private int statusCode; // 帐户状态码,请使用getStatus()来获取状态枚举类型值

    private static String Token = "Autobon~!@#2016=";

    public Merchandiser() {
        this.setStatus(Status.VERIFIED);
        this.createAt = new Date();
    }

    public static String encryptPassword(String password) {
        return Crypto.encryptBySha1(password);
    }

    // 根据用户ID生成token
    public static String makeToken(int id) {
        return "merchandiser:" + Crypto.encryptAesBase64(String.valueOf(id), Token);
    }

    // 从token返回用户Id
    public static int decodeToken(String token) {
        String[] arr = token.split(":");
        if (arr.length < 2 || !arr[0].equals("merchandiser")) return 0;
        else token = arr[1];
        try {
            return Integer.parseInt(Crypto.decryptAesBase64(token, Token));
        } catch (Exception ex) {
            log.info("无效token: " + token);
        }
        return 0;
    }

    public Status getStatus() {
        return Status.getStatus(this.getStatusCode());
    }

    public void setStatus(Status s) {
        this.setStatusCode(s.getStatusCode());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> ret = new ArrayList<>();
        ret.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "MERCHANDISER";
            }
        });
        if (getStatus() == Status.VERIFIED) {
            ret.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "VERIFIED_MERCHANDISER";
                }
            });
        }
        return ret;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>
     * .
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return null;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }



    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }



    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }
}
