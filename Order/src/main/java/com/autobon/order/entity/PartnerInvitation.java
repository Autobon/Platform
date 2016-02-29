package com.autobon.order.entity;

import com.autobon.technician.entity.Technician;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dave on 16/2/29.
 */
@Entity
@Table(name = "t_partner_invitation")
public class PartnerInvitation {
    public enum Status {
        NOT_ACCEPTED(0), ACCEPTED(1), REJECTED(2), EXPIRED(3);
        private int statusCode;

        Status(int statusCode) {this.statusCode = statusCode;}

        public static Status getStatus(int statusCode) {
            for (Status s : Status.values()) {
                if (s.getStatusCode() == statusCode) return s;
            }
            return null;
        }

        public int getStatusCode() {return this.statusCode;}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "main_tech_id")
    private Technician mainTech;

    @ManyToOne
    @JoinColumn(name = "invited_tech_id")
    private Technician invitedTech;

    @Column private Date createAt;

    @JsonIgnore
    @Column(name = "status")
    private int statusCode;

    public PartnerInvitation() {
        this.setStats(Status.NOT_ACCEPTED);
        this.createAt = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Technician getInvitedTech() {
        return invitedTech;
    }

    public void setInvitedTech(Technician invitedTech) {
        this.invitedTech = invitedTech;
    }

    public Technician getMainTech() {
        return mainTech;
    }

    public void setMainTech(Technician mainTech) {
        this.mainTech = mainTech;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createdAt) {
        this.createAt = createdAt;
    }

    public Status getStatus() {
        return Status.getStatus(this.statusCode);
    }

    public void setStats(Status s) {
        this.statusCode = s.getStatusCode();
    }

    protected int getStatusCode() {
        return statusCode;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
