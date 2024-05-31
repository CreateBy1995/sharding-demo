package pers.sharding.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroup;
import org.apache.shardingsphere.infra.executor.sql.prepare.ExecutionPrepareDecorator;
import org.apache.shardingsphere.infra.route.context.RouteContext;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.sharding.rule.ShardingRule;

import java.util.Collection;

/**
 * @Author: dongcx
 * @CreateTime: 2024-05-31
 * @Description:
 */
@Slf4j
public class CustomExecutionPrepareDecorator implements ExecutionPrepareDecorator {
    @Override
    public Collection<ExecutionGroup> decorate(RouteContext routeContext, ShardingSphereRule rule, Collection collection) {
        log.info("CustomExecutionPrepareDecorator decorate");
        return collection;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Class getTypeClass() {
        return ShardingRule.class;
    }
}
