package com.prgrms.monthsub.common.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

  private final ScheduledHandler seriesScheduledHandler;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public Scheduler(ScheduledHandler seriesScheduledHandler) {
    this.seriesScheduledHandler = seriesScheduledHandler;
  }

  @Scheduled(cron = "0 0 0/1 * * *")
  @Async
  public void changeSeriesStatus() {
    logger.info("=======1시간 마다 실행=======");
    seriesScheduledHandler.changeSeriesStatus();
    logger.info("==========================");
  }

}
