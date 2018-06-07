package com.autobon.platform.schedule;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.repository.CoopAccountRepository;
import com.autobon.order.entity.Order;
import com.autobon.order.repository.OrderRepository;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wh on 2018/6/7.
 */
public class OrderTask {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CoopAccountRepository coopAccountRepository;
    @Autowired
    TechnicianRepository technicianRepository;



    public void pushNewCreate36(){


        List<Order> orderList = orderRepository.findNewCreate36();

        for(Order order :orderList){
            CoopAccount coopAccount = coopAccountRepository.findOne(order.getCreatorId());
            if(coopAccount!= null && coopAccount.getPushId() != null){

                //todo 推送提示
            }

            if(order.getMainTechId() != 0){
               Technician technician = technicianRepository.findOne(order.getMainTechId());
                if(technician != null && technician.getPushId() != null){


                    // todo 推送提示
                }
            }
        }

    }

    public void pushNewCreate12(){


        List<Order> orderList = orderRepository.findNewCreate12();

        for(Order order :orderList){
            CoopAccount coopAccount = coopAccountRepository.findOne(order.getCreatorId());
            if(coopAccount!= null && coopAccount.getPushId() != null){

                //todo 推送提示
            }

            if(order.getMainTechId() != 0){
                Technician technician = technicianRepository.findOne(order.getMainTechId());
                if(technician != null && technician.getPushId() != null){


                    // todo 推送提示
                }
            }
        }

    }


}
