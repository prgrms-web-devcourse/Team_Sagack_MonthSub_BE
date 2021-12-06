package com.prgrms.monthsub.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class Security {

    private Jwt jwt;

    private Cors cors;

    @Getter
    @Setter
    public static class Jwt {

        private String header;

        private String issuer;

        private String clientSecret;

        private int expirySeconds;

    }

    @Getter
    @Setter
    public static class Cors {

        private String[] origin;

    }

}

