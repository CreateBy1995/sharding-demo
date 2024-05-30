package pers.sharding;

import org.apache.shardingsphere.infra.binder.LogicSQL;
import org.apache.shardingsphere.infra.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroup;
import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroupContext;
import org.apache.shardingsphere.infra.executor.sql.context.ExecutionContext;
import org.apache.shardingsphere.infra.executor.sql.context.ExecutionUnit;
import org.apache.shardingsphere.infra.executor.sql.context.SQLUnit;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.ConnectionMode;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutionUnit;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.raw.callback.RawSQLExecutorCallback;
import org.apache.shardingsphere.infra.executor.sql.execute.result.ExecuteResult;
import org.apache.shardingsphere.infra.executor.sql.execute.result.query.QueryResult;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.rewrite.context.SQLRewriteContext;
import org.apache.shardingsphere.infra.rewrite.engine.GenericSQLRewriteEngine;
import org.apache.shardingsphere.infra.rewrite.engine.RouteSQLRewriteEngine;
import org.apache.shardingsphere.infra.rewrite.engine.result.SQLRewriteResult;
import org.apache.shardingsphere.infra.rewrite.engine.result.SQLRewriteUnit;
import org.apache.shardingsphere.infra.route.context.RouteContext;
import org.apache.shardingsphere.infra.route.context.RouteUnit;
import org.apache.shardingsphere.infra.rule.identifier.type.RawExecutionRule;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: dongcx
 * @CreateTime: 2024-05-28
 * @Description:
 */
public class Test {
    public ExecutionContext generateExecutionContext(final LogicSQL logicSQL, final ShardingSphereMetaData metaData, final ConfigurationProperties props) {
        RouteContext routeContext = route(logicSQL, metaData, props);
        // SQL改写
        // SQLRewriteResult 有两个实现类，有路由改写引擎返回的数据为 RouteSQLRewriteResult
        // RouteSQLRewriteResult 的结构为 RouteUnit -> SQLRewriteUnit , 相同于存储了 节点和要执行的真实SQL的映射关系
        // 例如 ds0.tb1 -> select * from tb1 where ....    ds1.tb2 -> select * from tb2 where ....
        SQLRewriteResult rewriteResult = rewrite(logicSQL, metaData, props, routeContext);
        // 创建执行上下文
        // ExecutionContext  主要存储了执行单元ExecutionUnit 其实主要就是对上述得到的结构进行一个简单的转换
        // 可以简单理解为  dataSourceName -> 改写后的sql     跟ds0.tb1 -> select * from tb1 where ....  类似
        ExecutionContext result = createExecutionContext(logicSQL, metaData, routeContext, rewriteResult);
        // 日志打印  输出真实的sql
        logSQL(logicSQL, props, result);
        return result;
    }

    // SQLRewriteEntry#rewrite
    public SQLRewriteResult rewrite(final String sql, final List<Object> parameters, final SQLStatementContext<?> sqlStatementContext, final RouteContext routeContext) {
        // 构建改写上下文
        SQLRewriteContext sqlRewriteContext = createSQLRewriteContext(sql, parameters, sqlStatementContext, routeContext);
        // 如果路由单元不为空，则使用路由改写引擎进行改写
        return routeContext.getRouteUnits().isEmpty()
                ? new GenericSQLRewriteEngine().rewrite(sqlRewriteContext) : new RouteSQLRewriteEngine().rewrite(sqlRewriteContext, routeContext);
    }

    public boolean execute() throws SQLException {
        try {
            if (statementsCacheable && !statements.isEmpty()) {
                resetParameters();
                return statements.iterator().next().execute();
            }
            clearPrevious();
            executionContext = createExecutionContext();
            if (metaDataContexts.getMetaData(connection.getSchema()).getRuleMetaData().getRules().stream().anyMatch(each -> each instanceof RawExecutionRule)) {
                // TODO process getStatement
                Collection<ExecuteResult> executeResults = executor.getRawExecutor().execute(createRawExecutionGroupContext(), executionContext.getLogicSQL(), new RawSQLExecutorCallback());
                return executeResults.iterator().next() instanceof QueryResult;
            }
            if (executionContext.getRouteContext().isFederated()) {
                List<QueryResult> queryResults = executeFederatedQuery();
                return !queryResults.isEmpty();
            }
            // 创建执行组 为什么是组？因为之前的执行单元是  跟ds0.tb1 -> select * from tb1 where ....  这种形式，
            // 但是真正发起请求的时候是以数据源为单位的，也就是如果有4个执行单元
            // ds0.tb1 -> sql1
            // ds0.tb2 -> sql2
            // ds1.tb1 -> sql3
            // ds1.tb2 -> sql4
            // 那么经过执行组的聚合之后就是 ds0 -> [sql1,sql2] ,  ds1 -> [sql3,sql4]
            // 同时会为每条sql分配数据库连接
            ExecutionGroupContext<JDBCExecutionUnit> executionGroupContext = createExecutionGroupContext();
            cacheStatements(executionGroupContext.getInputGroups());
            return executor.getRegularExecutor().execute(executionGroupContext,
                    executionContext.getLogicSQL(), executionContext.getRouteContext().getRouteUnits(), createExecuteCallback());
        } finally {
            clearBatch();
        }
    }

    // AbstractExecutionPrepareEngine#prepare
    public final ExecutionGroupContext<T> prepare(final RouteContext routeContext, final Collection<ExecutionUnit> executionUnits) throws SQLException {
        Collection<ExecutionGroup<T>> result = new LinkedList<>();
        for (Map.Entry<String, List<SQLUnit>> entry : aggregateSQLUnitGroups(executionUnits).entrySet()) {
            // 按照数据源聚合 得出如下结构 ds0 -> [sql1,sql2] ,  ds1 -> [sql3,sql4]
            String dataSourceName = entry.getKey();
            List<SQLUnit> sqlUnits = entry.getValue();
            // 对sql进行分组, 也就是
            List<List<SQLUnit>> sqlUnitGroups = group(sqlUnits);
            // 判断当前模型  如果当前要执行的sql数 小于max-connections-size-per-query(每个查询的最大连接数) , 那么就是连接限制模式 否则为内存限制模式
            ConnectionMode connectionMode = maxConnectionsSizePerQuery < sqlUnits.size() ? ConnectionMode.CONNECTION_STRICTLY : ConnectionMode.MEMORY_STRICTLY;
            // 为sql分配数据库连接 结构大致为
            // [JdbcExecutionUnit(conn1, sql1), JdbcExecutionUnit(conn1, sql2)] 也有可能是conn1和conn2，取决于用户的配置max-connections-size-per-query
            // 假设ds0有2个sql, 且max-connections-size-per-query配置为2 那么每个连接分配一条sql
            // 假设ds0有2个sql, 且max-connections-size-per-query配置为1 那么每个连接分配两条sql
            // 假设ds0有4个sql, 且max-connections-size-per-query配置为2 那么每个连接分配两条sql
            result.addAll(group(dataSourceName, sqlUnitGroups, connectionMode));
        }
        return decorate(routeContext, result);
    }
}
