//package pers.sharding;
//
//import org.apache.shardingsphere.infra.binder.LogicSQL;
//import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroup;
//import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroupContext;
//import org.apache.shardingsphere.infra.executor.sql.context.ExecutionContext;
//import org.apache.shardingsphere.infra.executor.sql.context.ExecutionContextBuilder;
//import org.apache.shardingsphere.infra.executor.sql.context.ExecutionUnit;
//import org.apache.shardingsphere.infra.executor.sql.context.SQLUnit;
//import org.apache.shardingsphere.infra.executor.sql.execute.engine.ConnectionMode;
//import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutionUnit;
//import org.apache.shardingsphere.infra.executor.sql.prepare.driver.DriverExecutionPrepareEngine;
//import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
//import org.apache.shardingsphere.infra.rewrite.engine.result.SQLRewriteResult;
//import org.apache.shardingsphere.infra.route.context.RouteContext;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author: dongcx
// * @CreateTime: 2024-05-28
// * @Description:
// */
//public class Test {
//
//    // 创建执行上下文
//    // ExecutionContext  主要存储了执行单元ExecutionUnit 其实主要就是对上述得到的SQLRewriteResult结构进行一个简单的转换
//    // 可以简单理解为  dataSourceName -> 改写后的sql
//    // ds0 -> select * from tb0 where id in (2,4,6,8)
//    // ds0 -> select * from tb2 where id in (2,4,6,8)
//    private ExecutionContext createExecutionContext(final LogicSQL logicSQL, final ShardingSphereMetaData metaData, final RouteContext routeContext, final SQLRewriteResult rewriteResult) {
//        return new ExecutionContext(logicSQL, ExecutionContextBuilder.build(metaData, rewriteResult, logicSQL.getSqlStatementContext()), routeContext);
//    }
//
//
//    // 通过执行单元和配置信息，对执行单元进行分组
//    private ExecutionGroupContext<JDBCExecutionUnit> createExecutionGroupContext() throws SQLException {
//        DriverExecutionPrepareEngine<JDBCExecutionUnit, Connection> prepareEngine = createDriverExecutionPrepareEngine();
//        return prepareEngine.prepare(executionContext.getRouteContext(), executionContext.getExecutionUnits());
//    }
//
//
//    // AbstractExecutionPrepareEngine#prepare
//    public final ExecutionGroupContext<T> prepare(final RouteContext routeContext, final Collection<ExecutionUnit> executionUnits) throws SQLException {
//        Collection<ExecutionGroup<T>> result = new LinkedList<>();
//        for (Map.Entry<String, List<SQLUnit>> entry : aggregateSQLUnitGroups(executionUnits).entrySet()) {
//            // 按照数据源聚合 得出如下结构 ds0 -> [sql1,sql2] ,  ds1 -> [sql3,sql4]
//            String dataSourceName = entry.getKey();
//            List<SQLUnit> sqlUnits = entry.getValue();
//            // 对sql进行分组, 也就是
//            List<List<SQLUnit>> sqlUnitGroups = group(sqlUnits);
//            // 判断当前模型  如果当前要执行的sql数 小于max-connections-size-per-query(每个查询的最大连接数) , 那么就是连接限制模式 否则为内存限制模式
//            // 此处连接模型的实现与官网给出的文档有不一致，不确定是个人理解错误还是官网文档没及时更新？
//            ConnectionMode connectionMode = maxConnectionsSizePerQuery < sqlUnits.size() ? ConnectionMode.CONNECTION_STRICTLY : ConnectionMode.MEMORY_STRICTLY;
//            // 为sql分配数据库连接 结构大致为
//            // [JdbcExecutionUnit(conn1, sql1), JdbcExecutionUnit(conn1, sql2)] 也有可能是conn1和conn2，取决于用户的配置max-connections-size-per-query
//            // 假设ds0有10条sql, 且max-connections-size-per-query 配置为3 那么 10条sql会被分组为 4、4、2 分别交给3个连接去处理 使用连接限制模式
//            // 假设ds0有10条sql, 且max-connections-size-per-query 配置为1 那么 10条sql会被分组为 10 分别交给1个连接去处理 使用连接限制模式
//            // 假设ds0有5条sql, 且max-connections-size-per-query 配置为10 那么 10条sql会被分组为 1、1、1、1、1 分别交给5个连接去处理  使用内存限制模式
//            result.addAll(group(dataSourceName, sqlUnitGroups, connectionMode));
//        }
//        // 装饰器模式，通过SPI的形式去实现 实现ExecutionPrepareDecorator接口
//        return decorate(routeContext, result);
//    }
//
//}
