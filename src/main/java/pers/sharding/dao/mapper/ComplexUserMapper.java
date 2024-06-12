package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.ComplexUser;

public interface ComplexUserMapper {


    ComplexUser getByIdAndOrderId(@Param("id") Integer id, @Param("order_id") Integer orderId);


}
