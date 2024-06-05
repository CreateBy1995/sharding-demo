package pers.sharding;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.shardingsphere.driver.jdbc.core.resultset.ShardingSphereResultSet;
import org.apache.shardingsphere.infra.binder.LogicSQL;
import org.apache.shardingsphere.infra.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.binder.statement.dml.SelectStatementContext;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.database.metadata.DataSourceMetaData;
import org.apache.shardingsphere.infra.datanode.DataNode;
import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroup;
import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroupContext;
import org.apache.shardingsphere.infra.executor.kernel.model.ExecutorCallback;
import org.apache.shardingsphere.infra.executor.sql.context.ExecutionContext;
import org.apache.shardingsphere.infra.executor.sql.context.ExecutionUnit;
import org.apache.shardingsphere.infra.executor.sql.context.SQLUnit;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.ConnectionMode;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.SQLExecutorExceptionHandler;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutionUnit;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutorCallback;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.raw.callback.RawSQLExecutorCallback;
import org.apache.shardingsphere.infra.executor.sql.execute.result.ExecuteResult;
import org.apache.shardingsphere.infra.executor.sql.execute.result.query.QueryResult;
import org.apache.shardingsphere.infra.executor.sql.hook.SPISQLExecutionHook;
import org.apache.shardingsphere.infra.executor.sql.hook.SQLExecutionHook;
import org.apache.shardingsphere.infra.executor.sql.process.ExecuteProcessEngine;
import org.apache.shardingsphere.infra.merge.MergeEngine;
import org.apache.shardingsphere.infra.merge.result.MergedResult;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.rewrite.context.SQLRewriteContext;
import org.apache.shardingsphere.infra.rewrite.engine.GenericSQLRewriteEngine;
import org.apache.shardingsphere.infra.rewrite.engine.RouteSQLRewriteEngine;
import org.apache.shardingsphere.infra.rewrite.engine.result.SQLRewriteResult;
import org.apache.shardingsphere.infra.rewrite.engine.result.SQLRewriteUnit;
import org.apache.shardingsphere.infra.route.SQLRouter;
import org.apache.shardingsphere.infra.route.context.RouteContext;
import org.apache.shardingsphere.infra.route.context.RouteMapper;
import org.apache.shardingsphere.infra.route.context.RouteUnit;
import org.apache.shardingsphere.infra.route.engine.SQLRouteExecutor;
import org.apache.shardingsphere.infra.route.engine.impl.AllSQLRouteExecutor;
import org.apache.shardingsphere.infra.route.engine.impl.PartialSQLRouteExecutor;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.infra.rule.identifier.type.RawExecutionRule;
import org.apache.shardingsphere.sharding.route.engine.condition.ShardingCondition;
import org.apache.shardingsphere.sharding.route.engine.condition.ShardingConditions;
import org.apache.shardingsphere.sharding.route.engine.condition.engine.ShardingConditionEngine;
import org.apache.shardingsphere.sharding.route.engine.condition.engine.ShardingConditionEngineFactory;
import org.apache.shardingsphere.sharding.route.engine.condition.value.ShardingConditionValue;
import org.apache.shardingsphere.sharding.route.engine.type.ShardingRouteEngineFactory;
import org.apache.shardingsphere.sharding.route.engine.validator.ShardingStatementValidator;
import org.apache.shardingsphere.sharding.route.engine.validator.ShardingStatementValidatorFactory;
import org.apache.shardingsphere.sharding.route.strategy.ShardingStrategy;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sharding.rule.TableRule;
import org.apache.shardingsphere.spi.ordered.OrderedSPIRegistry;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dal.DALStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.DMLStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author: dongcx
 * @CreateTime: 2024-05-28
 * @Description:
 */
public class Test {
    public RouteContext route(final LogicSQL logicSQL, final ShardingSphereMetaData metaData) {
        // 先执行PartialSQLRouteExecutor的构造函数
        // 再该构造函数中回去
        // 调用PartialSQLRouteExecutor的route方法
        return new PartialSQLRouteExecutor(rules, props).route(logicSQL, metaData);
    }

    public RouteContext route(final LogicSQL logicSQL, final ShardingSphereMetaData metaData) {
        RouteContext result = new RouteContext();
        // PartialSQLRouteExecutor的路由(route方法)采用了装饰器模式，SQLRouter有多个实现类
        // 在PartialSQLRouteExecutor构造方法中会根据当前表使用的规则(比如分片、主从等)去匹配规则对应的路由器，加载到routers中
        // 其中优先级从大到小分别为分片路由器 > 单表路由器 > 主从路由器 从getOrder方法中体现
        // 例如链表routers如果为ShardingRule -> ReadwriteSplittingSQLRouter
        // 那么就是先通过ShardingRule进行路由，路由得到的结果再由ReadwriteSplittingSQLRouter进行路由
        // 假设 存在一条SQL  select * from tb ，现在仅做了分库操作，数据库ds 对应的物理库为 ds0, ds1
        // 则经过分片路由器解析后得出2个路由单元ds0.tb, ds1.tb
        // 主从配置为
        // ds0 :  master -> ds0  slave -> ds0-slave
        // ds1 :  master -> ds1  slave -> ds1-slave
        // 然后由于是读操作再经过主从路由器解析后，从ds0.tb, ds1.tb变成ds0-slave.tb,  ds1-slave.tb
        for (Map.Entry<ShardingSphereRule, SQLRouter> entry : routers.entrySet()) {
            if (result.getRouteUnits().isEmpty()) {
                result = entry.getValue().createRouteContext(logicSQL, metaData, entry.getKey(), props);
            } else {
                entry.getValue().decorateRouteContext(result, logicSQL, metaData, entry.getKey(), props);
            }
        }

        // 没有匹配到任何规则且当前的分片规则配置下，只存在一个物理数据源，会默认生成一个 RouteUnit，指向唯一的数据源，也就是没有配置分表分库的情况。
        // RouteUnits表示路由单元 指向了要查询的库和表
        if (result.getRouteUnits().isEmpty() && 1 == metaData.getResource().getDataSources().size()) {
            String singleDataSourceName = metaData.getResource().getDataSources().keySet().iterator().next();
            result.getRouteUnits().add(new RouteUnit(new RouteMapper(singleDataSourceName, singleDataSourceName), Collections.emptyList()));
        }
        return result;
    }



    public RouteContext createRouteContext(final LogicSQL logicSQL, final ShardingSphereMetaData metaData, final ShardingRule rule, final ConfigurationProperties props) {
        SQLStatement sqlStatement = logicSQL.getSqlStatementContext().getSqlStatement();
        // 解析分片条件 也就是说解析出一条sql中哪些条件可以作为分片的依据
        // 比如存在如下sql ： select * from tb1 where id in (2,4,6,8) and age in (1,2,3,4)， 切配置了分库算法为ds$->{id % 2}
        // 那么此时解析出来的条件就是 tb1.id in (2,4,6,8) 需要结合具体的分片算法和策略分析
        ShardingConditions shardingConditions = createShardingConditions(logicSQL, metaData, rule);
        Optional<ShardingStatementValidator> validator = ShardingStatementValidatorFactory.newInstance(sqlStatement, shardingConditions);
        validator.ifPresent(v -> v.preValidate(rule, logicSQL.getSqlStatementContext(), logicSQL.getParameters(), metaData.getSchema()));
        if (sqlStatement instanceof DMLStatement && shardingConditions.isNeedMerge()) {
            shardingConditions.merge();
        }
        // ShardingRouteEngineFactory会根据sql创建对应的路由规则，如果是标配的单表查询，那么此处使用的是ShardingStandardRoutingEngine
        RouteContext result = ShardingRouteEngineFactory.newInstance(rule, metaData, logicSQL.getSqlStatementContext(), shardingConditions, props).route(rule);
        validator.ifPresent(v -> v.postValidate(rule, logicSQL.getSqlStatementContext(), result, metaData.getSchema()));
        return result;
    }

    // ShardingSQLRouter的createShardingConditions方法
    // 由于ShardingSQLRouter的优先级是最高的，所以只有createShardingConditions方法没有decorateRouteContext方法
    private ShardingConditions createShardingConditions(final LogicSQL logicSQL, final ShardingSphereMetaData metaData, final ShardingRule rule) {
        // 分片条件引擎ShardingConditionEngine有2个实现，如果是读操作使用WhereClauseShardingConditionEngine
        // 如果是写操作则使用InsertClauseShardingConditionEngine
        ShardingConditionEngine shardingConditionEngine = ShardingConditionEngineFactory.createShardingConditionEngine(logicSQL, metaData, rule);
        List<ShardingCondition> shardingConditions = shardingConditionEngine.createShardingConditions(logicSQL.getSqlStatementContext(), logicSQL.getParameters());
        return new ShardingConditions(shardingConditions, logicSQL.getSqlStatementContext(), rule);
    }

    // ShardingStandardRoutingEngine的route方法
    public RouteContext route(final ShardingRule shardingRule) {
        RouteContext result = new RouteContext();
        // 将getDataNodes方法返回的节点(DataNode)列表封装成路由单元RouteUnit返回
        // 这两者的结构类似，都是表示数据源+表名，例如ds0.tb1
        // 所以核心逻辑还是在getDataNodes方法中
        Collection<DataNode> dataNodes = getDataNodes(shardingRule, shardingRule.getTableRule(logicTableName));
        result.getOriginalDataNodes().addAll(originalDataNodes);
        for (DataNode each : dataNodes) {
            result.getRouteUnits().add(
                    new RouteUnit(new RouteMapper(each.getDataSourceName(), each.getDataSourceName()), Collections.singleton(new RouteMapper(logicTableName, each.getTableName()))));
        }
        return result;
    }
    private Collection<DataNode> getDataNodes(final ShardingRule shardingRule, final TableRule tableRule) {
        // 获取分库策略  比如此处配置的分片策略为standard即标准分片
        // 配置的分片键为 id 分片算法的表达式为 ds${id % 2}
        ShardingStrategy databaseShardingStrategy = createShardingStrategy(shardingRule.getDatabaseShardingStrategyConfiguration(tableRule),
                shardingRule.getShardingAlgorithms(), shardingRule.getDefaultShardingColumn());
        // 获取分表策略  比如此处配置的分片策略为standard即标准分片
        // 配置的分片键为 id 分片算法的表达式为 tb${id % 4}
        ShardingStrategy tableShardingStrategy = createShardingStrategy(shardingRule.getTableShardingStrategyConfiguration(tableRule),
                shardingRule.getShardingAlgorithms(), shardingRule.getDefaultShardingColumn());
        if (isRoutingByHint(shardingRule, tableRule)) {
            return routeByHint(tableRule, databaseShardingStrategy, tableShardingStrategy);
        }
        if (isRoutingByShardingConditions(shardingRule, tableRule)) {
            return routeByShardingConditions(shardingRule, tableRule, databaseShardingStrategy, tableShardingStrategy);
        }
        return routeByMixedConditions(shardingRule, tableRule, databaseShardingStrategy, tableShardingStrategy);
    }

    private Collection<DataNode> routeByShardingConditionsWithCondition(final ShardingRule shardingRule, final TableRule tableRule,
                                                                        final ShardingStrategy databaseShardingStrategy, final ShardingStrategy tableShardingStrategy) {
        Collection<DataNode> result = new LinkedList<>();
        // 遍历解析出来的分片条件 也就是上述语句中的 user.id in (2,4,6,8)
        // 将这个条件代入到配置的分库分表策略中计算，也就是route0方法，得出需要路由的节点
        for (ShardingCondition each : shardingConditions.getConditions()) {
            Collection<DataNode> dataNodes = route0(tableRule,
                    databaseShardingStrategy, getShardingValuesFromShardingConditions(shardingRule, databaseShardingStrategy.getShardingColumns(), each),
                    tableShardingStrategy, getShardingValuesFromShardingConditions(shardingRule, tableShardingStrategy.getShardingColumns(), each));
            result.addAll(dataNodes);
            originalDataNodes.add(dataNodes);
        }
        return result;
    }

    private Collection<DataNode> route0(final TableRule tableRule,
                                        final ShardingStrategy databaseShardingStrategy, final List<ShardingConditionValue> databaseShardingValues,
                                        final ShardingStrategy tableShardingStrategy, final List<ShardingConditionValue> tableShardingValues) {
        // 先对数据源路由 路由规则根据分片策略(standard、hint、complex、inline)以及分片算法而定
        Collection<String> routedDataSources = routeDataSources(tableRule, databaseShardingStrategy, databaseShardingValues);
        // 再遍历每个数据源 对表进行路由
        // routeTables跟routeDataSources类似，此处仅解析routeDataSources
        Collection<DataNode> result = new LinkedList<>();
        for (String each : routedDataSources) {
            result.addAll(routeTables(tableRule, each, tableShardingStrategy, tableShardingValues));
        }
        return result;
    }


    // routeTables
    private Collection<String> routeDataSources(final TableRule tableRule, final ShardingStrategy databaseShardingStrategy, final List<ShardingConditionValue> databaseShardingValues) {
        if (databaseShardingValues.isEmpty()) {
            return tableRule.getActualDatasourceNames();
        }
        // 根据选定的分片策略执行doSharding
        // standard => StandardShardingStrategy
        // hint => HintShardingStrategy
        // complex => ComplexShardingStrategy
        // inline => NoneShardingStrategy
        // 如果此处是StandardShardingStrategy的话会根据条件 user.id in (2,4,6,8) 以及 算法ds${id % 2} 得出需要路由的数据源为 ds0,ds1
        Collection<String> result = new LinkedHashSet<>(databaseShardingStrategy.doSharding(tableRule.getActualDatasourceNames(), databaseShardingValues, properties));
        Preconditions.checkState(!result.isEmpty(), "no database route info");
        Preconditions.checkState(tableRule.getActualDatasourceNames().containsAll(result),
                "Some routed data sources do not belong to configured data sources. routed data sources: `%s`, configured data sources: `%s`", result, tableRule.getActualDatasourceNames());
        return result;
    }
}
