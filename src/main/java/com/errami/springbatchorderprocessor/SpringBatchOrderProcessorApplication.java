package com.errami.springbatchorderprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBatchOrderProcessorApplication {

    public static void main(String[] args) {
        System.out.println("FILE : " + new ClassPathResource("orders.csv").exists());

        SpringApplication.run(SpringBatchOrderProcessorApplication.class, args);
    }
}
