package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单状态记录
 * Created by tian on 18/6/7.
 */
@Entity
@Table(name = "t_order_status_record")
public class OrderStatusRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private int orderId;    // 订单ID

    @Column private int status;      // 状态：0未接单 10已接单 20已签到 30施工前照片已上传 40施工中 50施工完成

    @Column private Date recordTime;   // 记录时间

    public OrderStatusRecord() {}

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
}
