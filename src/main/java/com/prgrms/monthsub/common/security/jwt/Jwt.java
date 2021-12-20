package com.prgrms.monthsub.common.security.jwt;

import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.require;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jwt {

  private final String issuer;
  private final int expirySeconds;
  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  public Jwt(
    String issuer,
    String clientSecret,
    int expirySeconds
  ) {
    this.issuer = issuer;
    this.expirySeconds = expirySeconds;
    this.algorithm = Algorithm.HMAC512(clientSecret);
    this.jwtVerifier = require(algorithm).withIssuer(issuer).build();
  }

  public String sign(Claims claims) {
    Date now = new Date();
    JWTCreator.Builder builder = create();

    builder.withIssuer(issuer);
    builder.withIssuedAt(now);

    if (expirySeconds > 0) {
      builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
    }

    builder.withClaim("email", claims.email);
    builder.withArrayClaim("roles", claims.roles);

    return builder.sign(algorithm);
  }

  public Claims verify(String token) throws JWTVerificationException {
    return new Claims(jwtVerifier.verify(token));
  }

  static public class Claims {
    String email;
    String[] roles;
    Date iat;
    Date exp;

    private Claims() {/*no-op*/}

    Claims(DecodedJWT decodedJWT) {
      Claim email = decodedJWT.getClaim("email");

      if (!email.isNull()) {
        this.email = email.asString();
      }

      Claim roles = decodedJWT.getClaim("roles");

      if (!roles.isNull()) {
        this.roles = roles.asArray(String.class);
      }

      this.iat = decodedJWT.getIssuedAt();
      this.exp = decodedJWT.getExpiresAt();
    }

    public static Claims from(
      String email,
      String[] roles
    ) {
      Claims claims = new Claims();
      claims.email = email;
      claims.roles = roles;

      return claims;
    }

    public Map<String, Object> asMap() {
      Map<String, Object> map = new HashMap<>();
      map.put("email", email);
      map.put("roles", roles);
      map.put("iat", iat());
      map.put("exp", exp());

      return map;
    }

    long iat() {
      return iat != null ? iat.getTime() : -1;
    }

    long exp() {
      return exp != null ? exp.getTime() : -1;
    }
  }

}

