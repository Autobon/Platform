package com.autobon.order;

import com.autobon.order.entity.OrderShow;
import com.autobon.order.repository.OrderRepository;
import com.autobon.order.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by liz on 2016/2/23.
 */
public class OrderServiceTest {
    private OrderRepository orderRepository = null;

    @Before
    public void setup(){
        this.orderRepository = Mockito.mock(OrderRepository.class);
    }

    @Test
    public void addOrderTest(){
        OrderService os = new OrderService();
        os.setOrderRepository(orderRepository);
        OrderShow orderShow = new OrderShow();
        orderShow.setOrderNum("20160224094113456");
        boolean flag = os.addOrder(orderShow);
        Assert.assertTrue(flag);
    }

}
