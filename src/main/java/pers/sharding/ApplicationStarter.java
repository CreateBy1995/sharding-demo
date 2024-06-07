package pers.sharding;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"pers.sharding.dao.mapper"})
@SpringBootApplication
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class);
//        for (int i = 0; i < 20; i++) {
//            System.out.println(String.format("i = %d, mod = %d", i , i >> 1 % 4));
//            Closure<?> result = new InlineExpressionParser("users_${(id >> 1) % 4}").evaluateClosure().rehydrate(new Expando(), null, null);
//            result.setResolveStrategy(Closure.DELEGATE_ONLY);
//            result.setProperty("id", i);
//            System.out.println(String.format("i = %d ,script = %s", i, result.call().toString()));
//        }


    }
}
