package pers.sharding;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

@MapperScan({"pers.sharding.dao.mapper"})
@SpringBootApplication
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class);
//        Queue<Integer> queue =  new PriorityQueue<>(5);
//        queue.offer(7);
//        queue.offer(4);
//        queue.offer(10);
//        queue.offer(5);
//        while (!queue.isEmpty()){
//            System.out.println(queue.poll());
//        }
    }
}
