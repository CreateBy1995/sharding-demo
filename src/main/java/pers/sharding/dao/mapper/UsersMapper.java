package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.City;
import pers.sharding.dao.domain.Users;

import java.util.List;

public interface UsersMapper {

    List<Users> listByIds(@Param("ids") List<Long> ids);

    List<Users> listByIdAndName(@Param("ids") List<Long> ids, @Param("names") List<String> names);

    List<Users> listByIdRange(@Param("min") Integer min, @Param("max") Integer max);

    List<Users> pageByIds(@Param("ids") List<Long> ids, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Users getByIdAndName(@Param("id") Long id, @Param("name") String name);

    Users getById(@Param("id") Long id);

    int create(Users user);

    int batchCreate(@Param("userList") List<Users> userList);

}
