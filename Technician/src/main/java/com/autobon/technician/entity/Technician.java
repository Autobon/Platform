package com.autobon.technician.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

/**
 * Created by dave on 16/2/5.
 */
@Entity
@Table
public class Technician implements UserDetails {
    public enum Status {
        NOTVERIFIED(0), VERIFIED(1), REJECTED(2), BANNED(3);
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

    @Column private String phone; // 手机号

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

    @Column private Date verifyAt; // 认证通过日期

    @Column private Date lastLoginAt; // 最后登录时间

    @Column private String lastLoginIp; // 最后登录IP

    @Column private Date createAt; // 注册时间

    @Column private int star; // 技师星级

    @Column private float voteRate; // 技师好评率

    @Column private String skill; // 技师技能

    @JsonIgnore
    @Column(name = "status")
    private int statusCode; // 帐户状态码,请使用getStatus()来获取状态枚举类型值

    private static String Token = "Autobon~!@#2016=";

    public Technician() {
        this.setStatus(Status.NOTVERIFIED);
        this.createAt = new Date();
    }

    public static String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(password.getBytes());
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

    // 根据用户ID生成token
    public static String makeToken(int id) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Token.getBytes(), "AES"));
            return "technician:" + Base64.getEncoder().encodeToString(cipher.doFinal(new Integer(id).toString().getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    // 从token返回用户Id
    public static int decodeToken(String token) {
        String[] arr = token.split(":");
        if (arr.length < 2 || !arr[0].equals("technician")) return 0;
        else token = arr[1];
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Token.getBytes(), "AES"));
            return Integer.parseInt(new String(cipher.doFinal(Base64.getDecoder().decode(token))));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean isAvailable() {
        return this.getStatus() == Status.VERIFIED;
    }

    public boolean isActivated() {
        return this.getStatus() != Status.NOTVERIFIED;
    }

    public boolean isBanned() {
        return this.getStatus() == Status.BANNED;
    }

    public void setBanned() {
        this.setStatus(Status.BANNED);
    }

    public void setActived() {
        this.setStatus(Status.VERIFIED);
    }

    public void setDeactived() {
        this.setStatus(Status.NOTVERIFIED);
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

    public String getPassword() {
        return password;
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

    public Date getVerifyAt() {
        return verifyAt;
    }

    public void setVerifyAt(Date verifyAt) {
        this.verifyAt = verifyAt;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public float getVoteRate() {
        return voteRate;
    }

    public void setVoteRate(float voteRate) {
        this.voteRate = voteRate;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    protected int getStatusCode() {
        return statusCode;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> ret = new ArrayList<>();
        ret.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "TECHNICIAN";
            }
        });
        if (getStatus() == Status.VERIFIED) {
            ret.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "VERIFIED_TECHNICIAN";
                }
            });
        }
        return ret;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return phone;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return getStatus() != Status.BANNED;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return getStatus() == Status.VERIFIED;
    }
}
