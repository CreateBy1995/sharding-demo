package pers.sharding.config;

import org.apache.shardingsphere.readwritesplitting.spi.ReplicaLoadBalanceAlgorithm;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CustomBalanceAlgorithm implements ReplicaLoadBalanceAlgorithm {
    private Properties props = new Properties();

    @Override
    public String getDataSource(final String name, final String writeDataSourceName, final List<String> readDataSourceNames) {
        return readDataSourceNames.get(1);
    }

    @Override
    public String getType() {
        return "custom-o";
    }
}
