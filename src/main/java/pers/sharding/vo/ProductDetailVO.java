package pers.sharding.vo;

import lombok.Data;

/**
 * @Author: dongcx
 * @CreateTime: 2023-09-26
 * @Description:
 */
@Data
public class ProductDetailVO {
    private Long id;
    private String productName;
    private Long itemId;
    private Long cityId;
    private String cityName;

}
