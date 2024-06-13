package pers.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.sharding.ro.CityQueryRO;
import pers.sharding.service.CityService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.CityVO;

import java.util.List;

@RestController
@RequestMapping(value = "/city")
public class CityController {
    @Autowired
    private CityService cityService;


    @PostMapping("/listByIds")
    public List<CityVO> listByIds(@RequestBody CityQueryRO ro) {
        return ReflectionUtil.convertList(cityService.listByIds(ro.getIds()), CityVO.class);
    }


}
