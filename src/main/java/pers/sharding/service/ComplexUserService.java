package pers.sharding.service;

import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.sharding.dao.domain.ComplexUser;
import pers.sharding.dao.mapper.ComplexUserMapper;

@Service
public class ComplexUserService {
    @Autowired
    private ComplexUserMapper complexUserMapper;
    public ComplexUser getByIdAndOrderId(Integer id, Integer orderId){
        HintManager.clear();
        HintManager.getInstance().setDatabaseShardingValue("ds0");
        return complexUserMapper.getByIdAndOrderId(id, orderId);
    }


}
