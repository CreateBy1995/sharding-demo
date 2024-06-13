package pers.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.sharding.ro.OrderCreateRO;
import pers.sharding.ro.OrdersQueryRO;
import pers.sharding.ro.UsersQueryRO;
import pers.sharding.service.OrderService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.OrderVO;
import pers.sharding.vo.UsersVO;

import java.util.List;

/**
 * 无配置规则表测试接口
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get/{id}")
    public OrderVO getOrder(@PathVariable Long id) {
        return ReflectionUtil.convert(orderService.getOrder(id), OrderVO.class);
    }

    @PostMapping("/create")
    public Integer createOrder(@RequestBody OrderCreateRO ro) {
        return orderService.create(ro);
    }
}
