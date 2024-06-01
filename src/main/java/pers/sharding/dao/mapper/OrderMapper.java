package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.Order;

import java.util.List;

public interface OrderMapper {

    Order getById(@Param("id") Long id);

    List<Order> listByIds(@Param("ids") List<Long> ids);

    int create(Order order);
}
