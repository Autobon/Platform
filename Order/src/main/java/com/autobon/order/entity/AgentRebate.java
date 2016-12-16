package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by wh on 2016/12/16.
 */

@Entity
@Table(name = "t_agent_rebate")
public class AgentRebate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private int primaryAgent;   //һ������
    @Column
    private int secondAgent;    //��������

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrimaryAgent() {
        return primaryAgent;
    }

    public void setPrimaryAgent(int primaryAgent) {
        this.primaryAgent = primaryAgent;
    }

    public int getSecondAgent() {
        return secondAgent;
    }

    public void setSecondAgent(int secondAgent) {
        this.secondAgent = secondAgent;
    }
}
