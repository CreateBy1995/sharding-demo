package pers.sharding.vo;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import pers.sharding.dao.domain.Order;
import pers.sharding.util.ReflectionUtil;

/**
 * @Author: dongcx
 * @CreateTime: 2023-09-26
 * @Description:
 */
@Data
public class OrderVO {
    private Long id;
    private Long userId;

}
