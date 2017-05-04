package com.autobon.technician.entity;

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
 * Created by dave on 16/2/5.
 */
@Entity
@Table(name = "t_technician")
public class Technician implements UserDetails {
    private static Logger log = LoggerFactory.getLogger(Technician.class);
    public enum Status {
        NEWLY_CREATED(0), IN_VERIFICATION(1), VERIFIED(2), REJECTED(3), BANNED(4);
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

    @Column private Date requestVerifyAt; // 申请认证日期

    @Column private String verifyMsg; // 认证审核消息

    @Column private Date lastLoginAt; // 最后登录时间

    @Column private String lastLoginIp; // 最后登录IP

    @Column private Date createAt; // 注册时间

    @Column private String skill; // 技师技能,多个技能ID用逗号拼接而成的字符串

    @Column private String pushId; // 个推客户端ID, 由手机端更新

    @Column private String reference; //推荐人号码（注册用户）

    @Column private int filmLevel;

    @Column private int filmWorkingSeniority;

    @Column private int carCoverLevel;

    @Column private int carCoverWorkingSeniority;

    @Column private int colorModifyLevel;

    @Column private int colorModifyWorkingSeniority;

    @Column private int beautyLevel;

    @Column private int beautyWorkingSeniority;

    @Column private String resume;

    @Column private int workStatus;


    @JsonIgnore
    @Column(name = "status")
    private int statusCode; // 帐户状态码,请使用getStatus()来获取状态枚举类型值

    private static String Token = "Autobon~!@#2016=";

    public Technician() {
        this.setStatus(Status.NEWLY_CREATED);
        this.createAt = new Date();
    }

    public static String encryptPassword(String password) {
        return Crypto.encryptBySha1(password);
    }

    // 根据用户ID生成token
    public static String makeToken(int id) {
        return "technician:" + Crypto.encryptAesBase64(String.valueOf(id), Token);
    }

    // 从token返回用户Id
    public static int decodeToken(String token) {
        String[] arr = token.split(":");
        if (arr.length < 2 || !arr[0].equals("technician")) return 0;
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

    public Date getRequestVerifyAt() {
        return requestVerifyAt;
    }

    public void setRequestVerifyAt(Date requestVerifyAt) {
        this.requestVerifyAt = requestVerifyAt;
    }

    public String getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(String verifyMsg) {
        this.verifyMsg = verifyMsg;
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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    protected int getStatusCode() {
        return statusCode;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getFilmLevel() {
        return filmLevel;
    }

    public void setFilmLevel(int filmLevel) {
        this.filmLevel = filmLevel;
    }

    public int getCarCoverLevel() {
        return carCoverLevel;
    }

    public void setCarCoverLevel(int carCoverLevel) {
        this.carCoverLevel = carCoverLevel;
    }

    public int getColorModifyLevel() {
        return colorModifyLevel;
    }

    public void setColorModifyLevel(int colorModifyLevel) {
        this.colorModifyLevel = colorModifyLevel;
    }

    public int getBeautyLevel() {
        return beautyLevel;
    }

    public void setBeautyLevel(int beautyLevel) {
        this.beautyLevel = beautyLevel;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
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
        return getStatus() != Status.BANNED;
    }


    public int getFilmWorkingSeniority() {
        return filmWorkingSeniority;
    }

    public void setFilmWorkingSeniority(int filmWorkingSeniority) {
        this.filmWorkingSeniority = filmWorkingSeniority;
    }

    public int getCarCoverWorkingSeniority() {
        return carCoverWorkingSeniority;
    }

    public void setCarCoverWorkingSeniority(int carCoverWorkingSeniority) {
        this.carCoverWorkingSeniority = carCoverWorkingSeniority;
    }

    public int getColorModifyWorkingSeniority() {
        return colorModifyWorkingSeniority;
    }

    public void setColorModifyWorkingSeniority(int colorModifyWorkingSeniority) {
        this.colorModifyWorkingSeniority = colorModifyWorkingSeniority;
    }

    public int getBeautyWorkingSeniority() {
        return beautyWorkingSeniority;
    }

    public void setBeautyWorkingSeniority(int beautyWorkingSeniority) {
        this.beautyWorkingSeniority = beautyWorkingSeniority;
    }
}
