package pers.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.sharding.ro.UsersQueryRO;
import pers.sharding.service.UsersService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.UsersVO;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/listByIds")
    public List<UsersVO> listByIds(@RequestBody UsersQueryRO ro) {
        return ReflectionUtil.convertList(usersService.listByIds(ro.getIds()), UsersVO.class);
    }

    @PostMapping("/pageByIds")
    public List<UsersVO> pageByIds(@RequestBody UsersQueryRO ro) {
        return ReflectionUtil.convertList(usersService.pageByIds(ro.getIds(), ro.getOffset(), ro.getLimit()), UsersVO.class);
    }

    @GetMapping("/getByIdAndName")
    public UsersVO getByIdAndName(Long id, String name) {
        return ReflectionUtil.convert(usersService.getByIdAndName(id, name), UsersVO.class);
    }

    @GetMapping("/get/{id}")
    public UsersVO getById(@PathVariable("id") Long id) {
        return ReflectionUtil.convert(usersService.getById(id), UsersVO.class);
    }
}
