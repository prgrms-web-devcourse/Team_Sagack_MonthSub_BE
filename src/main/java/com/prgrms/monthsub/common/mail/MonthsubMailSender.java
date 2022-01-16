package com.prgrms.monthsub.common.mail;

import com.prgrms.monthsub.common.exception.global.EmailException.EmailSendFailException;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MonthsubMailSender {

  private final JavaMailSender javaMailSender;

  public MonthsubMailSender(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendTest(EmailMessage message){
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
      mimeMessageHelper.setTo(message.to());
      mimeMessageHelper.setSubject(message.subject());
      mimeMessageHelper.setText(message.message(), false);
      javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new EmailSendFailException("email : " + message.to());
    }
  }

  public int generateAuthenticationNumber(){
    Random generator = new Random();
    return generator.nextInt(1000000);
  }

}
