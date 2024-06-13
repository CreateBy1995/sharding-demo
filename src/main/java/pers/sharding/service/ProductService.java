package pers.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.aggr.ProductDetail;
import pers.sharding.dao.mapper.ProductMapper;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    public List<ProductDetail> listByIds(List<Long> ids){
        return productMapper.listByIds(ids);
    }

}
