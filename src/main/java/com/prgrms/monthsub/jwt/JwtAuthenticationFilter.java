package com.prgrms.monthsub.jwt;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.prgrms.monthsub.common.error.exception.global.UnAuthorizedException;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.service.AuthenticationService;
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
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        /**
         * HTTP 요청 헤더에 JWT 토큰이 있는지 확인
         * JWT 토큰이 있다면, 주어진 토큰을 디코딩하고,
         * username, roles 데이터를 추출하고, UsernamePasswordAuthenticationToken 생성
         * 그리고 이렇게 만들어진 UsernamePasswordAuthenticationToken 참조를 SecurityContext 넣어줌
         */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Map<String, String> headers = Collections
            .list(request.getHeaderNames())
            .stream()
            .collect(Collectors.toMap(header -> header, request::getHeader));

        log.info(String.format(
            "[%s] %s %s",
            request.getMethod(),
            request.getRequestURI().toLowerCase(),
            (request.getQueryString() != null) ? request.getQueryString() : ""
        ));
        log.info(String.format("header=%s", headers));

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getToken(request);
            if (token != null) {
                try {
                    Jwt.Claims claims = verify(token);
                    log.debug("Jwt parse result: {}", claims);

                    String username = claims.username;
                    List<GrantedAuthority> authorities = getAuthorities(claims);

                    if (isNotEmpty(username) && authorities.size() > 0) {
                        User user = this.authenticationService.findByUserName(username);

                        JwtAuthenticationToken authentication
                            = new JwtAuthenticationToken(
                            new JwtAuthentication(token, user.getId(), username), null,
                            authorities
                        );
                        authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                } catch (Exception e) {
                    log.error("Invalid token:{}", e.getMessage());
                    throw new UnAuthorizedException();
                }
            }
        } else {
            log.debug(
                "SecurityContextHolder not populated with security token, as it already contained: '{}'",
                SecurityContextHolder.getContext().getAuthentication()
            );
        }

        chain.doFilter(request, response);
    }


    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(headerKey);
        if (isNotEmpty(token)) {
            log.debug("Jwt token detected: {}", token);

            if (!token.startsWith(this.BEARER)) {
                throw new UnAuthorizedException();
            }

            try {
                return URLDecoder.decode(token.replace(BEARER + " ", ""), "UTF-8");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0 ?
            Collections.emptyList() :
            Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


}
