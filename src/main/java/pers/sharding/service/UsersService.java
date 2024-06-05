package pers.sharding.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.domain.Order;
import pers.sharding.dao.domain.Users;
import pers.sharding.dao.mapper.OrderMapper;
import pers.sharding.dao.mapper.UsersMapper;
import pers.sharding.ro.OrderCreateRO;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersMapper usersMapper;
    public List<Users> listByIds(List<Long> ids){
        return usersMapper.listByIds(ids);
    }

    public Users getByIdAndName(Long id, String name){
        return usersMapper.getByIdAndName(id, name);
    }

    public Users getById(Long id){
        return usersMapper.getById(id);
    }

}
