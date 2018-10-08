package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by ty on 2017/9/5.
 */
@Entity
@Table(name="t_work_detail_view")
public class WorkDetailView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "order_num")
    private String orderNum;

    @Column(name = "real_order_num")
    private String realOrderNum;

    @Column(name = "add_time")
    private Date addTime;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @Column(name = "license")
    private String license;

    @Column(name = "vin")
    private String vin;

    @Column
    private int source;

    @Column(name = "tech_id")
    private int techId;

    @Column(name = "tech_name")
    private String techName;

    @Column(name = "project1")
    private Integer project1;
    @Column(name = "project_name1")
    private String projectName1;
    @Column(name = "position1")
    private String position1;

    @Column(name = "project2")
    private Integer project2;
    @Column(name = "project_name2")
    private String projectName2;
    @Column(name = "position2")
    private String position2;

    @Column(name = "project3")
    private Integer project3;
    @Column(name = "project_name3")
    private String projectName3;
    @Column(name = "position3")
    private String position3;

    @Column(name = "project4")
    private Integer project4;
    @Column(name = "project_name4")
    private String projectName4;
    @Column(name = "position4")
    private String position4;

    @Column(name = "project5")
    private Integer project5;
    @Column(name = "project_name5")
    private String projectName5;
    @Column(name = "position5")
    private String position5;

    @Column(name = "project6")
    private Integer project6;
    @Column(name = "project_name6")
    private String projectName6;
    @Column(name = "position6")
    private String position6;


    @Column(name = "payment")
    private float payment;

    @Column(name = "pay_status")
    private int payStatus; //支付状态: 0-未出帐, 1-已出账进入月度账单, 2-已转账支付

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "total_cost")
    private float totalCost;

    public WorkDetailView(){}

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public Integer getProject1() {
        return project1;
    }

    public void setProject1(Integer project1) {
        this.project1 = project1;
    }

    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public Integer getProject2() {
        return project2;
    }

    public void setProject2(Integer project2) {
        this.project2 = project2;
    }

    public String getPosition2() {
        return position2;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public Integer getProject3() {
        return project3;
    }

    public void setProject3(Integer project3) {
        this.project3 = project3;
    }

    public String getPosition3() {
        return position3;
    }

    public void setPosition3(String position3) {
        this.position3 = position3;
    }

    public Integer getProject4() {
        return project4;
    }

    public void setProject4(Integer project4) {
        this.project4 = project4;
    }

    public String getPosition4() {
        return position4;
    }

    public void setPosition4(String position4) {
        this.position4 = position4;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getProject5() {
        return project5;
    }

    public void setProject5(Integer project5) {
        this.project5 = project5;
    }

    public String getPosition5() {
        return position5;
    }

    public void setPosition5(String position5) {
        this.position5 = position5;
    }

    public Integer getProject6() {
        return project6;
    }

    public void setProject6(Integer project6) {
        this.project6 = project6;
    }

    public String getPosition6() {
        return position6;
    }

    public void setPosition6(String position6) {
        this.position6 = position6;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getProjectName1() {
        return projectName1;
    }

    public void setProjectName1(String projectName1) {
        this.projectName1 = projectName1;
    }

    public String getProjectName2() {
        return projectName2;
    }

    public void setProjectName2(String projectName2) {
        this.projectName2 = projectName2;
    }

    public String getProjectName3() {
        return projectName3;
    }

    public void setProjectName3(String projectName3) {
        this.projectName3 = projectName3;
    }

    public String getProjectName4() {
        return projectName4;
    }

    public void setProjectName4(String projectName4) {
        this.projectName4 = projectName4;
    }

    public String getProjectName5() {
        return projectName5;
    }

    public void setProjectName5(String projectName5) {
        this.projectName5 = projectName5;
    }

    public String getProjectName6() {
        return projectName6;
    }

    public void setProjectName6(String projectName6) {
        this.projectName6 = projectName6;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public String getRealOrderNum() {
        return realOrderNum;
    }

    public void setRealOrderNum(String realOrderNum) {
        this.realOrderNum = realOrderNum;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
