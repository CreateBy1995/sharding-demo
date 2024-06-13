package pers.sharding.ro;

import lombok.Data;

import java.util.List;

@Data
public class ProductQueryRO {
    private List<Long> ids;
}
