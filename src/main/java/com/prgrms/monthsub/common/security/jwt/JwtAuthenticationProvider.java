package com.prgrms.monthsub.common.security.jwt;

import static org.apache.commons.lang3.ClassUtils.isAssignable;

import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.User;
import java.util.List;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final Jwt jwt;
  private final UserService userService;

  public JwtAuthenticationProvider(
    Jwt jwt,
    UserService userService
  ) {
    this.jwt = jwt;
    this.userService = userService;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return isAssignable(JwtAuthenticationToken.class, authentication);
  }

  @Override
  public Authentication authenticate(Authentication authentication)
    throws AuthenticationException {
    JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
    return processUserAuthentication(
      String.valueOf(jwtAuthentication.getPrincipal()),
      jwtAuthentication.getCredentials()
    );
  }

  private Authentication processUserAuthentication(
    String principal,
    String credentials
  ) {
    try {
      User user = userService.login(principal, credentials);
      List<GrantedAuthority> authorities = user.getPart()
        .getAuthorities();
      String token = getToken(user.getUsername(), authorities);
      JwtAuthenticationToken authenticated =
        new JwtAuthenticationToken(
          new JwtAuthentication(token, user.getId(), user.getUsername()), null,
          authorities
        );
      authenticated.setDetails(user);
      return authenticated;
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException(e.getMessage());
    }
  }

  private String getToken(
    String username,
    List<GrantedAuthority> authorities
  ) {
    String[] roles = authorities.stream()
      .map(GrantedAuthority::getAuthority)
      .toArray(String[]::new);
    return jwt.sign(Jwt.Claims.from(username, roles));
  }

}
