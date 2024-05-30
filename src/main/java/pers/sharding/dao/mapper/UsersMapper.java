package pers.sharding.dao.mapper;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.Order;
import pers.sharding.dao.domain.Users;

import java.util.List;

public interface UsersMapper {

    List<Users> listByIds(@Param("ids") List<Long> ids);

    Users getById(@Param("id") Long id);

}
