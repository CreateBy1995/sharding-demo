package pers.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.aggr.ProductDetail;
import pers.sharding.dao.mapper.ProductItemMapper;

import java.util.List;

@Service
public class ProductItemService {
    @Autowired
    private ProductItemMapper productItemMapper;
    public List<ProductDetail> listByIds(List<Long> productIds){
        return productItemMapper.listByIds(productIds);
    }

}
