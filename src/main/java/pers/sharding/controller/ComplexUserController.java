package pers.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.sharding.service.ComplexUserService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.ComplexUserVO;

@RestController
@RequestMapping(value = "/complex")
public class ComplexUserController {
    @Autowired
    private ComplexUserService complexUserService;

    @GetMapping("/getByIdAndOrderId")
    public ComplexUserVO getByIdAndOrderId(Integer id, Integer orderId) {
        return ReflectionUtil.convert(complexUserService.getByIdAndOrderId(id, orderId), ComplexUserVO.class);
    }
}
