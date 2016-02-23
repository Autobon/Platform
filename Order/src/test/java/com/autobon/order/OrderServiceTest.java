package com.autobon.order;

import com.autobon.order.entity.Order;
import com.autobon.order.repository.OrderRepository;
import com.autobon.order.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

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
    public void findAllOrdersTest(){
        OrderService os = new OrderService();
        os.setOrderRepository(orderRepository);
        Pageable p = new PageRequest(0,10);
        Page<Order> orders = os.findAllOrders();
        Assert.assertTrue(orders != null);
    }

}
