package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.Order;

public interface OrderMapper {

    Order getById(@Param("id") Long id);

    int create(Order order);
}
