package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.City;

import java.util.List;

public interface CityMapper {

    List<City> listByIds(@Param("ids") List<Long> ids);

}
