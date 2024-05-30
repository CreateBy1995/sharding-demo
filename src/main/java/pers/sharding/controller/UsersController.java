package pers.sharding.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.sharding.dao.domain.Users;
import pers.sharding.ro.OrderCreateRO;
import pers.sharding.ro.UsersQueryRO;
import pers.sharding.service.OrderService;
import pers.sharding.service.UsersService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.OrderVO;
import pers.sharding.vo.UsersVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/listByIds")
    public List<UsersVO> listByIds (@RequestBody UsersQueryRO ro) {
        return usersService.listByIds(ro.getIds()).stream().map(item -> ReflectionUtil.convert(item, UsersVO.class)).collect(Collectors.toList());
    }
}
