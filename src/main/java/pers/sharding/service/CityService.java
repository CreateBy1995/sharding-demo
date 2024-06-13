package pers.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.domain.City;
import pers.sharding.dao.mapper.CityMapper;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityMapper mapper;
    public List<City> listByIds(List<Long> ids){
        return mapper.listByIds(ids);
    }


}
