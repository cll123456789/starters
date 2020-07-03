package com.jedis;

import com.config.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 4:59 下午
 * @description:
 */
public class JedisPoolClient {

    @Autowired
    private RedisConfig redisConfig;

    private static final Logger logger = LoggerFactory.getLogger(JedisPoolClient.class);
    private static final int maxSize = 100;
    private static final int maxIdle = 10;
    //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；单位毫秒
    //小于零:阻塞不确定的时间,  默认-1
    private static final long maxWaitMillis = 1000;
    private static JedisPool jedisPool = null;

    @PostConstruct
    public void init() {
        initJedisPool();
    }

    /***
     * 获取jedis连接
     * @return jedis对象
     */
    public Jedis getJedis() {
        if (jedisPool == null) {
            initJedisPool();
        }
        if (jedisPool != null) {
            return jedisPool.getResource();
        }
        return null;
    }

    /***
     * 释放jedis
     * @param jedis
     */
    public static void returnJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /***
     * 初始化jedis
     */
    private void initJedisPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxSize);
            config.setMaxIdle(maxIdle);
            config.setMaxWaitMillis(maxWaitMillis);
            config.setTestOnBorrow(true);
            //在borrow(引入)一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //return 一个jedis实例给pool时，是否检查连接可用性（ping()）
            config.setTestOnReturn(true);
            jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), null, redisConfig.getDatabase());
        } catch (Exception e) {
            logger.error("init redis error,e={}", e);
        }
    }
}
