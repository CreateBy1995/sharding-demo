package pers.sharding;

/**
 * @Author: dongcx
 * @CreateTime: 2024-05-28
 * @Description:
 */
public class Test {
    // ShardingComplexRoutingEngine
    // 当连表查询中使用的是绑定表的时候，路由计算仅会涉及到主表，所以绑定表之间的分区键需要完全相同，且可用的数据节点配置也需要一致
    // BindingTableRule#getBindingActualTable 计算绑定表的真实表名称


    // 对每个逻辑表应用标准路由，收集各自的 RouteContext。如果只有一个 RouteContext，直接将其结果添加到最终的 RouteContext 中；
    // 如果有多个 RouteContext，则使用笛卡尔积路由生成新的 RouteContext。最终返回合并后的 RouteContext，包含所有逻辑表的路由结果
//    public RouteContext route(final ShardingRule shardingRule) {
//        for (String each : logicTables) {
//            Optional<TableRule> tableRule = shardingRule.findTableRule(each);
//            if (tableRule.isPresent()) {
//                // 使用标准路由引擎路由
//                if (!bindingTableNames.contains(each)) {
//                    routeContexts.add(new ShardingStandardRoutingEngine(tableRule.get().getLogicTable(), shardingConditions, props).route(shardingRule));
//                }
//                shardingRule.findBindingTableRule(each).ifPresent(bindingTableRule -> bindingTableNames.addAll(bindingTableRule.getTableRules().keySet()));
//            }
//        }
//        // 省略部分分支...
//        // 对多个路由结果进行笛卡尔积
//        RouteContext routeContext = new ShardingCartesianRoutingEngine(routeContexts).route(shardingRule);
//        result.getOriginalDataNodes().addAll(routeContext.getOriginalDataNodes());
//        result.getRouteUnits().addAll(routeContext.getRouteUnits());
//        return result;
//    }
}
