package com.annotation;

import com.filter.CorsFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 11:30 上午
 * @description: 启动自定义过滤器配置
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({CorsFilter.class})
public @interface EnableCorsFilter {

}
