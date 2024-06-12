package pers.sharding.config;

import com.google.common.base.Preconditions;
import groovy.lang.Closure;
import groovy.util.Expando;
import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.apache.shardingsphere.sharding.support.InlineExpressionParser;

import java.util.Collection;
import java.util.Properties;

/**
 * @Author: dongcx
 * @CreateTime: 2024-06-12
 * @Description:
 */
public final class EnhanceInlineShardingAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {

    private static final String ALGORITHM_EXPRESSION_KEY = "algorithm-expression";

    private static final String ALLOW_RANGE_QUERY_KEY = "allow-range-query-with-inline-sharding";

    private static final String ALGORITHM_SHARDING_KEY = "shardingKey";


    private String algorithmExpression;

    private boolean allowRangeQuery;

    @Getter
    @Setter
    private Properties props = new Properties();

    @Override
    public void init() {
        algorithmExpression = getAlgorithmExpression();
        allowRangeQuery = isAllowRangeQuery();
    }

    private String getAlgorithmExpression() {
        String expression = props.getProperty(ALGORITHM_EXPRESSION_KEY);
        Preconditions.checkNotNull(expression, "Inline sharding algorithm expression cannot be null.");
        return InlineExpressionParser.handlePlaceHolder(expression.trim());
    }

    private boolean isAllowRangeQuery() {
        return Boolean.parseBoolean(props.getOrDefault(ALLOW_RANGE_QUERY_KEY, Boolean.FALSE.toString()).toString());
    }

    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<Comparable<?>> shardingValue) {
        Closure<?> closure = createClosure();
        closure.setProperty(ALGORITHM_SHARDING_KEY, shardingValue.getValue());
        String result = closure.call().toString();
        for (String each : availableTargetNames) {
            if (each.endsWith(result)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public Collection<String> doSharding(final Collection<String> availableTargetNames, final RangeShardingValue<Comparable<?>> shardingValue) {
        if (allowRangeQuery) {
            return availableTargetNames;
        }
        throw new UnsupportedOperationException("Since the property of `" + ALLOW_RANGE_QUERY_KEY + "` is false, inline sharding algorithm can not tackle with range query.");
    }

    private Closure<?> createClosure() {
        Closure<?> result = new InlineExpressionParser(algorithmExpression).evaluateClosure().rehydrate(new Expando(), null, null);
        result.setResolveStrategy(Closure.DELEGATE_ONLY);
        return result;
    }

    @Override
    public String getType() {
        return "ENHANCE-INLINE";
    }
}
