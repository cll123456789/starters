package com.config;

import com.jedis.JedisPoolClient;
import com.jedis.JedisUtil;
import com.util.JwtTokenUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 10:52 上午
 * @description:
 */
@Configuration
@EnableConfigurationProperties({JwtTokenConfig.class, CorsFilterConfig.class, RedisConfig.class})
public class CoreAutoConfiguration {

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public JedisPoolClient jedisPoolClient() {
        return new JedisPoolClient();
    }

    @Bean
    public JedisUtil jedisUtil() {
        return new JedisUtil();
    }

}
