package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.Users;

import java.util.List;

public interface UsersMapper {

    List<Users> listByIds(@Param("ids") List<Long> ids);

    List<Users> listByIdAndName(@Param("ids") List<Long> ids, @Param("names") List<String> names);

    List<Users> pageByIds(@Param("ids") List<Long> ids, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Users getByIdAndName(@Param("id") Long id, @Param("name") String name);

    Users getById(@Param("id") Long id);

}
