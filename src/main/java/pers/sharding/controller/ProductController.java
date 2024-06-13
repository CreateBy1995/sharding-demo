package pers.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.sharding.ro.ProductQueryRO;
import pers.sharding.service.ProductItemService;
import pers.sharding.service.ProductService;
import pers.sharding.util.ReflectionUtil;
import pers.sharding.vo.ProductDetailVO;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductItemService productItemService;

    @PostMapping("/listByIds")
    public List<ProductDetailVO> listByIds(@RequestBody ProductQueryRO ro) {
        return ReflectionUtil.convertList(productService.listByIds(ro.getIds()), ProductDetailVO.class);
    }

    @PostMapping("/listByIds1")
    public List<ProductDetailVO> listByIds1(@RequestBody ProductQueryRO ro) {
        return ReflectionUtil.convertList(productItemService.listByIds(ro.getIds()), ProductDetailVO.class);
    }

}
