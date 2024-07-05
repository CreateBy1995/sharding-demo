package pers.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pers.sharding.dao.domain.Order;
import pers.sharding.dao.mapper.OrderMapper;
import pers.sharding.ro.OrderCreateRO;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService self;

    public Order getOrder(Long id) {
        return orderMapper.getById(id);
    }

    @Transactional
    public int create(OrderCreateRO ro){

        Order order = new Order();
        order.setUserId(ro.getUserId());
        orderMapper.create(order);
        return self.create2(ro);
    }

    @Transactional(propagation = Propagation.NESTED)
    public int create2(OrderCreateRO ro){
        Order order = new Order();
        order.setUserId(ro.getUserId());
        int result = orderMapper.create(order);
        if (ro.getUserId() == 2L){
            throw new RuntimeException("user id error");
        }
        return result;
    }
}
