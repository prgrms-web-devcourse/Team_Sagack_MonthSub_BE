package com.prgrms.monthsub.common.security.jwt;

public class JwtAuthentication {

  public final String token;
  public final Long userId;
  public final String email;

  JwtAuthentication(
    String token,
    Long userId,
    String email
  ) {
    this.token = token;
    this.userId = userId;
    this.email = email;
  }

}
