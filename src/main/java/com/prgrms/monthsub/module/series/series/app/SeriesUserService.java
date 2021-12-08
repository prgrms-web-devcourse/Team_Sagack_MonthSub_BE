package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesUser;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeriesUserService {

  private final SeriesUserRepository seriesUserRepository;

  public SeriesUserService(SeriesUserRepository seriesUserRepository) {
    this.seriesUserRepository = seriesUserRepository;
  }

  public List<SeriesUser> findAllMySubscribeByUserId(Long userId) {
    return this.seriesUserRepository.findAllMySubscribeByUserId(userId);
  }

}
