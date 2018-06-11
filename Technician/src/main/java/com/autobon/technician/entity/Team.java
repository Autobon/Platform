package com.autobon.technician.entity;

import com.autobon.technician.vo.TeamShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by tian on 18/6/5.
 */
@Entity
@Table(name = "t_team")
public class Team {
    private static Logger log = LoggerFactory.getLogger(Team.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String name; // 团队名称

    @Column private int managerId; // 负责人ID

    @Column private String managerName; // 负责人名称

    @Column private String managerPhone; // 负责人名称

    public Team(TeamShow teamShow) {
        this.name = teamShow.getName();
        this.managerId = teamShow.getManagerId();
        this.managerName = teamShow.getManagerName();
        this.managerPhone = teamShow.getManagerPhone();
    }

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

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }
}
