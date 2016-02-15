package com.autobon.mobile.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by dave on 16/2/12.
 */
public class PartnerProfile {

    @Id private int id;

    @Column private String name;

    @Column private String short_name;

    @Column private String bizLicenseNo;

    @Column private String bizLicensePhoto;

    @Column private String legalPersonName;

    @Column private String legalPersonId;

    @Column private String legalPersonIdPhoto;

    @Column private String taxId;

    @Column private String invoiceHeader;

    @Column private String invoiceAddress;

    @Column private String invoiceZipCode;

    @Column private String contactName;

    @Column private String contactPhone;

    @Column private Date verifyAt;


    public PartnerProfile() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getBizLicenseNo() {
        return bizLicenseNo;
    }

    public void setBizLicenseNo(String bizLicenseNo) {
        this.bizLicenseNo = bizLicenseNo;
    }

    public String getBizLicensePhoto() {
        return bizLicensePhoto;
    }

    public void setBizLicensePhoto(String bizLicensePhoto) {
        this.bizLicensePhoto = bizLicensePhoto;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLegalPersonId() {
        return legalPersonId;
    }

    public void setLegalPersonId(String legalPersonId) {
        this.legalPersonId = legalPersonId;
    }

    public String getLegalPersonIdPhoto() {
        return legalPersonIdPhoto;
    }

    public void setLegalPersonIdPhoto(String legalPersonIdPhoto) {
        this.legalPersonIdPhoto = legalPersonIdPhoto;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(String invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoiceZipCode() {
        return invoiceZipCode;
    }

    public void setInvoiceZipCode(String invoiceZipCode) {
        this.invoiceZipCode = invoiceZipCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Date getVerifyAt() {
        return verifyAt;
    }

    public void setVerifyAt(Date verifyAt) {
        this.verifyAt = verifyAt;
    }
}
