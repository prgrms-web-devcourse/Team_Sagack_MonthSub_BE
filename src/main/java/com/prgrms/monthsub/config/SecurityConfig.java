package com.prgrms.monthsub.config;

import com.prgrms.monthsub.common.exception.FilterExceptionHandler;
import com.prgrms.monthsub.common.jwt.Jwt;
import com.prgrms.monthsub.common.jwt.JwtAuthenticationFilter;
import com.prgrms.monthsub.common.jwt.JwtAuthenticationProvider;
import com.prgrms.monthsub.module.part.user.app.AuthenticationService;
import com.prgrms.monthsub.module.part.user.app.UserService;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final Security security;
  private final AuthenticationService authenticationService;

  @Autowired
  private FilterExceptionHandler exceptionHandlerFilter;

  public SecurityConfig(
    Security security,
    AuthenticationService authenticationService
  ) {
    this.security = security;
    this.authenticationService = authenticationService;
  }

  @Override
  public void configure(WebSecurity web) {
    web

      .ignoring()
      .antMatchers(
        this.security
          .getAllows()
          .toArray(String[]::new)
      );
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, e) -> {
      Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
      Object principal = authentication != null ? authentication.getPrincipal() : null;
      log.error("{} is denied", principal, e);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter()
        .write("ACCESS DENIED");
      response.getWriter()
        .flush();
      response.getWriter()
        .close();
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Jwt jwt() {
    return new Jwt(
      security.getJwt()
        .getIssuer(),
      security.getJwt()
        .getClientSecret(),
      security.getJwt()
        .getExpirySeconds()
    );
  }

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(
    UserService userService,
    Jwt jwt
  ) {
    return new JwtAuthenticationProvider(jwt, userService);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    Jwt jwt = getApplicationContext().getBean(Jwt.class);
    return new JwtAuthenticationFilter(
      authenticationService, security.getJwt()
      .getHeader(), jwt);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .antMatchers("/users/me")
      .hasAnyRole("USER")
      .anyRequest()
      .fullyAuthenticated()
      .and()
      .cors()
      .configurationSource(corsConfigurationSource())
      .and()
      .csrf()
      .disable()
      .headers()
      .disable()
      .formLogin()
      .disable()
      .httpBasic()
      .disable()
      .rememberMe()
      .disable()
      .logout()
      .disable()
      .exceptionHandling()
      .accessDeniedHandler(accessDeniedHandler())
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
      .addFilterBefore(exceptionHandlerFilter, jwtAuthenticationFilter().getClass());
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    Arrays.stream(this.security.getCors()
        .getOrigin())
      .toList()
      .forEach(configuration::addAllowedOrigin);
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
