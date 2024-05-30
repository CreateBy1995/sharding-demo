package pers.sharding.vo;

import lombok.Data;
import pers.sharding.dao.domain.Order;
import pers.sharding.dao.domain.Users;
import pers.sharding.util.ReflectionUtil;

/**
 * @Author: dongcx
 * @CreateTime: 2023-09-26
 * @Description:
 */
@Data
public class UsersVO {
    private Long id;
    private Long userId;

    public static UsersVO convert(Users source) {
        return ReflectionUtil.convert(source, UsersVO.class);
    }
}
