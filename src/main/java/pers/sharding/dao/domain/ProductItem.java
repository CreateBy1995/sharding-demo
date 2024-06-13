package pers.sharding.dao.domain;

import lombok.Data;

/**
 * @Author: dongcx
 * @CreateTime: 2023-09-26
 * @Description:
 */
@Data
public class ProductItem {
    private Long id;
    private Long productId;
    private Long cityId;
}
