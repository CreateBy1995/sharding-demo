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

    // 如果某个表没有配置任何规则（分片、主从等），则查询只会走固定某个数据源。
    // 至于要走哪个数据源，则是按照数据源配置的顺序去选择。
    // 例如表user无任何规则，且数据源配置了ds0,ds1，则优先使用ds0，如果ds0不存在表user，则依次后推。
    // SingleTableRouteEngine#route

    // 广播路由引擎会将写请求打到配置的所有节点
    // ShardingDatabaseBroadcastRoutingEngine#route
    // 而如果是读请求与则随机请求某个节点即可
    // ShardingUnicastRoutingEngine#route

}
