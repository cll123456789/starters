package com.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 10:42 上午
 * @description: JwtToken相关配置
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenConfig {

    private String header = "Authorization";

    private String secret = "mySecret";

    private long expiration = 86400000L;

}
