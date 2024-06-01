package pers.sharding.ro;

import lombok.Data;

import java.util.List;

@Data
public class OrdersQueryRO {
    private List<Long> ids;
}
