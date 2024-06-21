package pers.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.aggr.UsersGroup;
import pers.sharding.dao.domain.Users;
import pers.sharding.dao.mapper.UsersMapper;
import pers.sharding.ro.UsersCreateRO;
import pers.sharding.util.ReflectionUtil;

import java.util.List;
import java.util.Objects;

@Service
public class UsersService {
    @Autowired
    private UsersMapper usersMapper;

    public List<Users> listByIds(List<Long> ids) {
        return usersMapper.listByIds(ids);
    }


    public List<Users> orderByIds(List<Long> ids) {
        return usersMapper.orderByIds(ids);
    }



    public List<UsersGroup> groupByIds(List<Long> ids) {
        return usersMapper.groupByIds(ids);
    }

    public List<Users> listByIdAndName(List<Long> ids, List<String> names) {
        return usersMapper.listByIdAndName(ids, names);
    }


    public List<Users> listByIdRange(Long min, Long max, Integer offset, Integer limit) {
        return usersMapper.listByIdRange(min, max, offset, limit);
    }

    public Integer create(UsersCreateRO ro) {
        Users users = ReflectionUtil.convert(ro, Users.class);
        if (Objects.nonNull(users.getId())) {
            return usersMapper.createWithId(users);
        } else {
            return usersMapper.create(users);
        }

    }

    public Integer batchCreate(List<UsersCreateRO> roList) {
        List<Users> userList = ReflectionUtil.convertList(roList, Users.class);
        boolean result = userList.stream().allMatch(item -> Objects.nonNull(item.getId()));
        if (result) {
            return usersMapper.batchCreateWithId(userList);
        } else {
            return usersMapper.batchCreate(userList);
        }
    }


    public List<Users> pageByIds(List<Long> ids, Integer offset, Integer limit) {
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

    public Users getByIdAndName(Long id, String name) {
        return usersMapper.getByIdAndName(id, name);
    }

    public Users getById(Long id) {
        return usersMapper.getById(id);
    }

}
