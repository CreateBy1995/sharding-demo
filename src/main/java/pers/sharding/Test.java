//package pers.sharding;
//
//import com.mysql.cj.protocol.a.TextResultsetReader;
//import org.apache.shardingsphere.infra.binder.statement.dml.InsertStatementContext;
//import org.apache.shardingsphere.infra.executor.sql.context.ExecutionUnit;
//import org.apache.shardingsphere.infra.executor.sql.context.SQLUnit;
//import org.apache.shardingsphere.infra.executor.sql.execute.engine.ConnectionMode;
//import org.apache.shardingsphere.infra.executor.sql.execute.result.query.impl.driver.jdbc.type.memory.JDBCMemoryQueryResult;
//import org.apache.shardingsphere.infra.route.context.RouteContext;
//import org.apache.shardingsphere.sharding.route.engine.condition.ShardingCondition;
//
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
//    // ShardingSpherePreparedStatement#getQueryResults
//    // 是否也要取决于底层驱动使用的查询方式？即如果底层驱动没有开启流式查询，那么在sharding层面开启流式也没意义？
////    JDBCMemoryQueryResult#
////    TextResultsetReader#read
//    public final ExecutionGroupContext<T> prepare(final RouteContext routeContext, final Collection<ExecutionUnit> executionUnits) throws SQLException {
//        Collection<ExecutionGroup<T>> result = new LinkedList<>();
//        for (Map.Entry<String, List<SQLUnit>> entry : aggregateSQLUnitGroups(executionUnits).entrySet()) {
//            String dataSourceName = entry.getKey();
//            List<SQLUnit> sqlUnits = entry.getValue();
//            List<List<SQLUnit>> sqlUnitGroups = group(sqlUnits);
//            ConnectionMode connectionMode = maxConnectionsSizePerQuery < sqlUnits.size() ? ConnectionMode.CONNECTION_STRICTLY : ConnectionMode.MEMORY_STRICTLY;
//            result.addAll(group(dataSourceName, sqlUnitGroups, connectionMode));
//        }
//        return decorate(routeContext, result);
//    }
//
//}
