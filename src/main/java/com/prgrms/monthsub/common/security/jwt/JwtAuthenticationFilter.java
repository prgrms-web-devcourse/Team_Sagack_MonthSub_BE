package com.prgrms.monthsub.common.security.jwt;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.prgrms.monthsub.common.exception.global.AuthenticationException.UnAuthorize;
import com.prgrms.monthsub.module.part.user.app.AuthenticationService;
import com.prgrms.monthsub.module.part.user.domain.User;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {

  private final String BEARER = "Bearer";
  private final Logger log = LoggerFactory.getLogger(getClass());
  private final AuthenticationService authenticationService;
  private final String headerKey;
  private final Jwt jwt;

  public JwtAuthenticationFilter(
    AuthenticationService authenticationService,
    String headerKey,
    Jwt jwt
  ) {
    this.authenticationService = authenticationService;
    this.headerKey = headerKey;
    this.jwt = jwt;
  }

  @Override
  public void doFilter(
    ServletRequest req,
    ServletResponse res,
    FilterChain chain
  )
    throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    Map<String, String> headers = Collections
      .list(request.getHeaderNames())
      .stream()
      .collect(Collectors.toMap(header -> header, request::getHeader));

    log.info(
      String.format(
        "[%s] %s %s",
        request.getMethod(),
        request.getRequestURI().toLowerCase(),
        (request.getQueryString() != null) ? request.getQueryString() : ""
      )
    );

    log.info(String.format("header=%s", headers));

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String token = getToken(request);

    if (authentication != null) {
      log.debug(
        "SecurityContextHolder not populated with security token, as it already contained: '{}'",
        authentication
      );
    }

    if (authentication == null && token != null) {
      try {
        authenticate(request, token);
      } catch (Exception e) {
        log.error("Invalid token:{}", e.getMessage());
        throw new UnAuthorize();
      }
    }

    chain.doFilter(request, response);
  }

  private void authenticate(
    HttpServletRequest request,
    String token
  ) {
    Jwt.Claims claims = verify(token);
    log.debug("Jwt parse result: {}", claims);

    String email = claims.email;

    List<GrantedAuthority> authorities = getAuthorities(claims);

    if (isEmpty(email) || authorities.size() <= 0) {
      return;
    }

    User user = this.authenticationService.findByEmail(email);
    JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(
      new JwtAuthentication(token, user.getId(), email),
      null,
      authorities
    );

    log.info("userId: {}", user.getId());

    jwtToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(jwtToken);
  }

  private String getToken(HttpServletRequest request) {
    String token = request.getHeader(headerKey);
    String decodedToken = null;

    if (isEmpty(token)) {
      return null;
    }

    log.debug("Jwt token detected: {}", token);

    if (!token.startsWith(this.BEARER)) {
      throw new UnAuthorize();
    }

    try {
      decodedToken = URLDecoder.decode(token.replace(BEARER + " ", ""), "UTF-8");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return decodedToken;
  }

  private Jwt.Claims verify(String token) {
    return jwt.verify(token);
  }

  private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
    String[] roles = claims.roles;
    boolean emptyRole = roles == null || roles.length == 0;

    return emptyRole
      ? Collections.emptyList()
      : Arrays.stream(roles)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

}
