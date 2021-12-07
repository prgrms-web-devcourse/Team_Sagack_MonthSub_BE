package com.prgrms.monthsub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "cloud.aws")
public class AWS {

    private Credentials credentials;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Getter
    @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

}

