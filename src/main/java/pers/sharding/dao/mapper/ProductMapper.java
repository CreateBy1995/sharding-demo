package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.aggr.ProductDetail;

import java.util.List;

public interface ProductMapper {

    List<ProductDetail> listByIds(@Param("ids") List<Long> ids);
}
