package pers.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.sharding.ro.OrderCreateRO;
import pers.sharding.service.OrderService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.OrderVO;

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
