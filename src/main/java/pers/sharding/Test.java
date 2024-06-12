//package pers.sharding;
//
//import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
//import org.apache.shardingsphere.infra.datanode.DataNode;
//import org.apache.shardingsphere.infra.route.context.RouteContext;
//import org.apache.shardingsphere.sharding.route.engine.condition.ShardingCondition;
//import org.apache.shardingsphere.sharding.route.engine.condition.value.ListShardingConditionValue;
//import org.apache.shardingsphere.sharding.route.engine.condition.value.RangeShardingConditionValue;
//import org.apache.shardingsphere.sharding.route.engine.condition.value.ShardingConditionValue;
//import org.apache.shardingsphere.sharding.route.strategy.ShardingStrategy;
//import org.apache.shardingsphere.sharding.rule.ShardingRule;
//import org.apache.shardingsphere.sharding.rule.TableRule;
//
//import java.util.*;
//
///**
// * @Author: dongcx
// * @CreateTime: 2024-05-28
// * @Description:
// */
//public class Test {
//    // ShardingStandardRoutingEngine的route方法
//    public RouteContext route(final ShardingRule shardingRule) {
//        RouteContext result = new RouteContext();
//        // 结合具体使用的分片算法解析出数据节点（数据分片的最小单元，由数据源名称和真实表组成。 例：ds0.tb0）
//        Collection<DataNode> dataNodes = getDataNodes(shardingRule, shardingRule.getTableRule(logicTableName));
//        // dataNodes结构转换成路由单元  代码略
//        return result;
//    }
//
//
//    private Collection<DataNode> getDataNodes(final ShardingRule shardingRule, final TableRule tableRule) {
//        // 获取分库、分库算法
//        ShardingStrategy databaseShardingStrategy = createShardingStrategy(...);
//        ShardingStrategy tableShardingStrategy = createShardingStrategy(...);
//        // 根据配置的分片算法去路由 hint / standard / complex
//        if (isRoutingByHint(shardingRule, tableRule)) {
//            return routeByHint(tableRule, databaseShardingStrategy, tableShardingStrategy);
//        }
//        if (isRoutingByShardingConditions(shardingRule, tableRule)) {
//            return routeByShardingConditions(shardingRule, tableRule, databaseShardingStrategy, tableShardingStrategy);
//        }
//        return routeByMixedConditions(shardingRule, tableRule, databaseShardingStrategy, tableShardingStrategy);
//    }
//
//
//    private Collection<DataNode> routeByShardingConditionsWithCondition(final ShardingRule shardingRule, final TableRule tableRule,
//                                                                        final ShardingStrategy databaseShardingStrategy, final ShardingStrategy tableShardingStrategy) {
//        Collection<DataNode> result = new LinkedList<>();
//        // 遍历解析出来的分片条件 也就是上述语句中的 user.id in (2,4,6,8)
//        // 将这个条件代入到配置的分库分表策略中计算，也就是route0方法，得出需要路由的节点
//        for (ShardingCondition each : shardingConditions.getConditions()) {
//            Collection<DataNode> dataNodes = route0(tableRule,
//                    databaseShardingStrategy, getShardingValuesFromShardingConditions(shardingRule, databaseShardingStrategy.getShardingColumns(), each),
//                    tableShardingStrategy, getShardingValuesFromShardingConditions(shardingRule, tableShardingStrategy.getShardingColumns(), each));
//            result.addAll(dataNodes);
//            originalDataNodes.add(dataNodes);
//        }
//        return result;
//    }
//
//    private Collection<DataNode> route0(final TableRule tableRule,
//                                        final ShardingStrategy databaseShardingStrategy, final List<ShardingConditionValue> databaseShardingValues,
//                                        final ShardingStrategy tableShardingStrategy, final List<ShardingConditionValue> tableShardingValues) {
//        // 先对数据源路由 路由规则根据分片策略(standard、hint、complex、inline)以及分片算法而定
//        Collection<String> routedDataSources = routeDataSources(tableRule, databaseShardingStrategy, databaseShardingValues);
//        // 再遍历每个数据源 对表进行路由
//        // routeTables跟routeDataSources类似，此处仅解析routeDataSources
//        Collection<DataNode> result = new LinkedList<>();
//        for (String each : routedDataSources) {
//            result.addAll(routeTables(tableRule, each, tableShardingStrategy, tableShardingValues));
//        }
//        return result;
//    }
//
//
//    // routeTables
//    private Collection<String> routeDataSources(final TableRule tableRule, final ShardingStrategy databaseShardingStrategy, final List<ShardingConditionValue> databaseShardingValues) {
//        if (databaseShardingValues.isEmpty()) {
//            return tableRule.getActualDatasourceNames();
//        }
//        // 根据选定的分片策略执行doSharding
//        // standard => StandardShardingStrategy
//        // hint => HintShardingStrategy
//        // complex => ComplexShardingStrategy
//        // inline => NoneShardingStrategy
//        // 如果此处是StandardShardingStrategy的话会根据条件 user.id in (2,4,6,8) 以及 算法ds${id % 2} 得出需要路由的数据源为 ds0,ds1
//        Collection<String> result = new LinkedHashSet<>(databaseShardingStrategy.doSharding(tableRule.getActualDatasourceNames(), databaseShardingValues, properties));
//        Preconditions.checkState(!result.isEmpty(), "no database route info");
//        Preconditions.checkState(tableRule.getActualDatasourceNames().containsAll(result),
//                "Some routed data sources do not belong to configured data sources. routed data sources: `%s`, configured data sources: `%s`", result, tableRule.getActualDatasourceNames());
//        return result;
//    }
//
//    private Collection<DataNode> routeTables(final TableRule tableRule, final String routedDataSource,
//                                             final ShardingStrategy tableShardingStrategy, final List<ShardingConditionValue> tableShardingValues) {
//        Collection<String> availableTargetTables = tableRule.getActualTableNames(routedDataSource);
//        // 如果不含分片条件 则返回所有的可用表 否则执行对应的分片策略
//        Collection<String> routedTables = new LinkedHashSet<>(tableShardingValues.isEmpty()
//                ? availableTargetTables : tableShardingStrategy.doSharding(availableTargetTables, tableShardingValues, properties));
//        // 转换成数据节点 代码略..
//        return result;
//    }
//    public Collection<String> doSharding(final Collection<String> availableTargetNames, final Collection<ShardingConditionValue> shardingConditionValues, final ConfigurationProperties props) {
//        // 执行时也分为两种情况。当sql为精准查询时（例如 =/in这些条件），会将分片条件带入行表达式计算出对应的数据节点。
//        // 而如果sql为范围查询比如使用的是 >、<、BETWEEN AND等这些条件。那么会直接进行全路由，，默认情况下行表达式算法不开启范围查询，可以通过allow-range-query-with-inline-sharding设定。
//    }
//}
