package com.nifelee.springdatar2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringDataR2dbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataR2dbcApplication.class, args);
    }

}
