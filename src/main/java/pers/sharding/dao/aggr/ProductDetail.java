package pers.sharding.dao.aggr;

import lombok.Data;

/**
 * @Author: dongcx
 * @CreateTime: 2024-06-13
 * @Description:
 */
@Data
public class ProductDetail {
    private Long id;
    private String productName;
    private Long itemId;
    private Long cityId;
    private String cityName;
}
