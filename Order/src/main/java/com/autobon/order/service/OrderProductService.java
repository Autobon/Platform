package com.autobon.order.service;

import com.autobon.order.entity.OrderProduct;
import com.autobon.order.entity.Product;
import com.autobon.order.repository.OrderProductRepository;
import com.autobon.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by wh on 2016/11/18.
 */
@Service
public class OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;


    @PersistenceContext
    protected EntityManager em;


    @Transactional
    public void batchInsert(List<OrderProduct> list) {
        for(int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if(i % 30== 0) {
                em.flush();
                em.clear();
            }
        }
    }


    @Transactional
    public void save(List<OrderProduct> list){
        for(int i = 0; i < list.size(); i++) {
            orderProductRepository.save(list.get(i));
        }
    }


    public List<OrderProduct> get(int orderId){

        return orderProductRepository.findByOrderId(orderId);

    }

    public int deleteByOrderId(int orderId){
        orderProductRepository.deleteByOrderId(orderId);
        return 0;
    }

}
