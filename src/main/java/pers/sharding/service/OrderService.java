package pers.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.domain.Order;
import pers.sharding.dao.mapper.OrderMapper;
import pers.sharding.ro.OrderCreateRO;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    public Order getOrder(Long id){
        return orderMapper.getById(id);
    }

    public int create(OrderCreateRO ro){
        Order order = new Order();
        order.setUserId(ro.getUserId());
        return orderMapper.create(order);
    }
}
