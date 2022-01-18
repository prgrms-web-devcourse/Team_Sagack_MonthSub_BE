package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.common.mail.EmailMessage;
import com.prgrms.monthsub.common.mail.MonthsubMailSender;
import com.prgrms.monthsub.module.part.user.dto.AccountEmail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccountService {

  private final MonthsubMailSender mailSender;
  private static final String VERIFICATION_EMAIL_SUBJECT = "MonthSub 이메일 인증 메일";

  public AccountService(
    MonthsubMailSender mailSender
  ){
    this.mailSender = mailSender;
  }

  @Transactional
  public void sendEmail(AccountEmail.Request request){
    EmailMessage emailMessage = EmailMessage.builder()
      .to(request.email())
      .subject(VERIFICATION_EMAIL_SUBJECT)
      .message(String.valueOf(mailSender.generateAuthenticationNumber()))
      .build();

    mailSender.sendTest(emailMessage);
  }

}
