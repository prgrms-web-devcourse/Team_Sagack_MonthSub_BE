package com.prgrms.monthsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class) // security 임시 제외
public class MonthSubApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonthSubApplication.class, args);
    }

}
