package pers.sharding;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@MapperScan({"pers.sharding.dao.mapper"})
@SpringBootApplication
@EnableTransactionManagement
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class);
//        for (int i = 0; i < 20; i++) {
////            System.out.println(String.format("i = %d, mod = %d", i , ));
////            Closure<?> result = new InlineExpressionParser("users_${(id >> 1) % 4}").evaluateClosure().rehydrate(new Expando(), null, null);
////            result.setResolveStrategy(Closure.DELEGATE_ONLY);
////            result.setProperty("id", i);
////            System.out.println(String.format("i = %d ,script = %s", i, result.call().toString()));
//            System.out.println(String.format("ds = %d ,tb = %d", i % 2, i%4));
//        }
//        for (long i = 1010901224867758080L; i < 1010901224867758090L; i++) {
//            log.info("value is :{}, result:{}, % is :{}", i, i>>1, (i>>1) %4);
//        }

    }
}
