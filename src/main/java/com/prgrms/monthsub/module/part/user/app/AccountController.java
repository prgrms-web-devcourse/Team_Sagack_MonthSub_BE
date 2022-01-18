package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.module.part.user.dto.AccountEmail;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private final AccountService accountService;

  public AccountController(
    AccountService accountService
  ) {
    this.accountService = accountService;
  }

  @PostMapping("/send-email")
  public void sendEmail(@RequestBody @Valid AccountEmail.Request email){
    this.accountService.sendEmail(email);
  }

}
