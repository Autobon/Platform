package com.autobon.cooperators.entity;

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
 * Created by lu on 2016/3/7.
 */

@Entity
@Table(name = "t_cooperators")
public class Cooperator  implements UserDetails {

    private static Logger log = LoggerFactory.getLogger(Cooperator.class);
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
    @Column private int id;

    @Column private String phone; // 账号

    @Column private String shortname; //企业简称

    @JsonIgnore
    @Column private String password; //密码

    @Column private String fullname; //企业名称

    @Column private String businessLicense; //营业执照号

    @Column private String corporationName; //法人姓名

    @Column private String corporationIdNo; //法人身份证号

    @Column private String bussinessLicensePic; //营业执照副本照片

    @Column(name = "corporation_id_pic_a")
    private String corporationIdPicA; //法人身份证正面照

    @Column(name = "corporation_id_pic_b")
    private String corporationIdPicB; //法人身份证背面照

    @Column private String longitude; //商户位置经度

    @Column private String latitude; //商户位置纬度

    @Column private String invoiceHeader; //发票抬头

    @Column private String taxIdNo; //纳税识别号

    @Column private String postcode; //邮政编码

    @Column private String province; //省

    @Column private String city; //市

    @Column private String district; //区

    @Column private String address; //详细地址

    @Column private String contact; //联系人姓名

    @Column private String contactPhone; //联系人电话

   // @Column private int statusCode; //状态 0-未审核 1-审核成功 2-审核失败 3-账号禁用

    @Column private Date lastLoginTime; //上次登录时间

    @Column private String lastLoginIp; //上次登录IP

    @Column private Date createTime; //注册时间

    @JsonIgnore
    @Column(name = "status")
    private int statusCode; // 帐户状态码,请使用getStatus()来获取状态枚举类型值

    private static String Token = "Autobon~!@#2016=";

    public  Cooperator(){
        this.createTime=new Date();
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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Status getStatus() {
        return Status.getStatus(this.getStatusCode());
    }

    public void setStatus(Status s) {
        this.setStatusCode(s.getStatusCode());
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

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCorporationIdNo() {
        return corporationIdNo;
    }

    public void setCorporationIdNo(String corporationIdNo) {
        this.corporationIdNo = corporationIdNo;
    }

    public String getBussinessLicensePic() {
        return bussinessLicensePic;
    }

    public void setBussinessLicensePic(String bussinessLicensePic) {
        this.bussinessLicensePic = bussinessLicensePic;
    }

    public String getCorporationIdPicA() {
        return corporationIdPicA;
    }

    public void setCorporationIdPicA(String corporationIdPicA) {
        this.corporationIdPicA = corporationIdPicA;
    }

    public String getCorporationIdPicB() {
        return corporationIdPicB;
    }

    public void setCorporationIdPicB(String corporationIdPicB) {
        this.corporationIdPicB = corporationIdPicB;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(String invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public String getTaxIdNo() {
        return taxIdNo;
    }

    public void setTaxIdNo(String taxIdNo) {
        this.taxIdNo = taxIdNo;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static String encryptPassword(String password) {
        return Crypto.encryptBySha1(password);
    }

    // 根据用户ID生成token
    public static String makeToken(int id) {
        return "cooperator:" + Crypto.encryptAesBase64(String.valueOf(id), Token);
    }
}
