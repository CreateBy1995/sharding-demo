package pers.sharding.dao.mapper;

import org.apache.ibatis.annotations.Param;
import pers.sharding.dao.domain.City;
import pers.sharding.dao.domain.Order;

import java.util.List;

public interface CityMapper {

    List<City> listByIds(@Param("ids") List<Long> ids);

    int create(City city);
}
