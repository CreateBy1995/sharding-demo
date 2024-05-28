//package pers.sharding;
//
//import com.google.common.base.Strings;
//import org.apache.shardingsphere.driver.executor.DriverExecutor;
//import org.apache.shardingsphere.driver.executor.batch.BatchPreparedStatementExecutor;
//import org.apache.shardingsphere.driver.jdbc.core.connection.ShardingSphereConnection;
//import org.apache.shardingsphere.driver.jdbc.core.statement.metadata.ShardingSphereParameterMetaData;
//import org.apache.shardingsphere.driver.jdbc.exception.SQLExceptionErrorCode;
//import org.apache.shardingsphere.infra.context.kernel.KernelProcessor;
//import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
//import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroupContext;
//import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutionUnit;
//import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutor;
//import org.apache.shardingsphere.infra.executor.sql.execute.engine.raw.callback.RawSQLExecutorCallback;
//import org.apache.shardingsphere.infra.executor.sql.execute.result.ExecuteResult;
//import org.apache.shardingsphere.infra.executor.sql.execute.result.query.QueryResult;
//import org.apache.shardingsphere.infra.executor.sql.prepare.driver.jdbc.StatementOption;
//import org.apache.shardingsphere.infra.parser.ShardingSphereSQLParserEngine;
//import org.apache.shardingsphere.infra.rule.identifier.type.RawExecutionRule;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
///**
// * @Author: dongcx
// * @CreateTime: 2024-05-28
// * @Description:
// */
//public class Test {
//    public boolean execute() throws SQLException {
//        try {
//            if (statementsCacheable && !statements.isEmpty()) {
//                resetParameters();
//                return statements.iterator().next().execute();
//            }
//            clearPrevious();
//            // 创建执行单位上下文
//            executionContext = createExecutionContext();
//            if (metaDataContexts.getMetaData(connection.getSchema()).getRuleMetaData().getRules().stream().anyMatch(each -> each instanceof RawExecutionRule)) {
//                // TODO process getStatement
//                Collection<ExecuteResult> executeResults = executor.getRawExecutor().execute(createRawExecutionGroupContext(), executionContext.getLogicSQL(), new RawSQLExecutorCallback());
//                return executeResults.iterator().next() instanceof QueryResult;
//            }
//            if (executionContext.getRouteContext().isFederated()) {
//                List<QueryResult> queryResults = executeFederatedQuery();
//                return !queryResults.isEmpty();
//            }
//            ExecutionGroupContext<JDBCExecutionUnit> executionGroupContext = createExecutionGroupContext();
//            cacheStatements(executionGroupContext.getInputGroups());
//            // SQL 执行
//            return executor.getRegularExecutor().execute(executionGroupContext,
//                    executionContext.getLogicSQL(), executionContext.getRouteContext().getRouteUnits(), createExecuteCallback());
//        } finally {
//            clearBatch();
//        }
//    }
//
//    private ShardingSpherePreparedStatement(final ShardingSphereConnection connection, final String sql,
//                                            final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability, final boolean returnGeneratedKeys) throws SQLException {
//        if (Strings.isNullOrEmpty(sql)) {
//            SQLExceptionErrorCode errorCode = SQLExceptionErrorCode.SQL_STRING_NULL_OR_EMPTY;
//            throw new SQLException(errorCode.getErrorMessage(), errorCode.getSqlState(), errorCode.getErrorCode());
//        }
//        this.connection = connection;
//        metaDataContexts = connection.getContextManager().getMetaDataContexts();
//        this.sql = sql;
//        statements = new ArrayList<>();
//        parameterSets = new ArrayList<>();
//        ShardingSphereSQLParserEngine sqlParserEngine = new ShardingSphereSQLParserEngine(
//                DatabaseTypeRegistry.getTrunkDatabaseTypeName(metaDataContexts.getMetaData(connection.getSchema()).getResource().getDatabaseType()), metaDataContexts.getProps());
//        // sql解析
//        sqlStatement = sqlParserEngine.parse(sql, true);
//        parameterMetaData = new ShardingSphereParameterMetaData(sqlStatement);
//        statementOption = returnGeneratedKeys ? new StatementOption(true) : new StatementOption(resultSetType, resultSetConcurrency, resultSetHoldability);
//        executor = new DriverExecutor(connection);
//        JDBCExecutor jdbcExecutor = new JDBCExecutor(metaDataContexts.getExecutorEngine(), connection.isHoldTransaction());
//        batchPreparedStatementExecutor = new BatchPreparedStatementExecutor(metaDataContexts, jdbcExecutor, connection.getSchema());
//        kernelProcessor = new KernelProcessor();
//        statementsCacheable = isStatementsCacheable(metaDataContexts.getMetaData(connection.getSchema()).getRuleMetaData().getConfigurations());
//    }
//}
