package com.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 5:02 下午
 * @description: redis配置
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private String host = "localhost";

    private int port = 6379;

    private int database = 0;

    private int timeout = 5000;
}
