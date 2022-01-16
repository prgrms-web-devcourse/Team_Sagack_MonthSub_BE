package com.prgrms.monthsub.common.mail;

import lombok.Builder;

public record EmailMessage(
  String to,
  String subject,
  String message
) {
  @Builder
  public EmailMessage {}
}
