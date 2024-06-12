package pers.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.domain.Users;
import pers.sharding.dao.mapper.UsersMapper;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersMapper usersMapper;
    public List<Users> listByIds(List<Long> ids){
        return usersMapper.listByIds(ids);
    }

    public List<Users> listByIdAndName(List<Long> ids, List<String> names){
        return usersMapper.listByIdAndName(ids, names);
    }


    public List<Users> listByIdRange(Integer min, Integer max){
        return usersMapper.listByIdRange(min, max);
    }





    public List<Users> pageByIds(List<Long> ids, Integer offset, Integer limit){
        // 测试获取连接死锁问题
//        new Thread(()->{
//            usersMapper.pageByIds(ids, offset, limit);
//        }).start();
//        try {
//            TimeUnit.MILLISECONDS.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return usersMapper.pageByIds(ids, offset, limit);
    }
    public Users getByIdAndName(Long id, String name){
        return usersMapper.getByIdAndName(id, name);
    }

    public Users getById(Long id){
        return usersMapper.getById(id);
    }

}
