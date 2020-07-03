package com.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 11:16 上午
 * @description: 过滤器相关配置
 */
@Data
@ConfigurationProperties(prefix = "web.corsfilter")
public class CorsFilterConfig {

    /**
     * 排除路径
     */
    private String exclusionUrls;

}
