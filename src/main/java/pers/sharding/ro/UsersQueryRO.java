package pers.sharding.ro;

import lombok.Data;

import java.util.List;

@Data
public class UsersQueryRO {
    private List<Long> ids;
}
