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
//            int i1 = i >> 1;
//            System.out.println(String.format("i = %d, result = %d, mod = %d", i , i1, i1 % 4));
//        }

    }
}
