package pers.sharding.ro;

import lombok.Data;

import java.util.List;

@Data
public class UsersQueryRO {
    private List<Long> ids;
    private List<String> names;
    private Integer offset;
    private Integer limit;

    private Long min;
    private Long max;
}
