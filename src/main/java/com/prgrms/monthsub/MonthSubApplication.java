package com.prgrms.monthsub;

import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class MonthSubApplication {

  public static void main(String[] args) {
    SpringApplication.run(MonthSubApplication.class, args);
  }

  @PostConstruct
  public void start() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    System.out.println("Now: " + new Date());
  }

}
