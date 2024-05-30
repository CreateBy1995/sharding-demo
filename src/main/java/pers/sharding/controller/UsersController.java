package pers.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.sharding.dao.domain.Users;
import pers.sharding.ro.OrderCreateRO;
import pers.sharding.ro.UsersQueryRO;
import pers.sharding.service.OrderService;
import pers.sharding.service.UsersService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.OrderVO;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/listByIds")
    public List<Users> listByIds (@RequestBody UsersQueryRO ro) {
        return usersService.listByIds(ro.getIds());
    }
}
