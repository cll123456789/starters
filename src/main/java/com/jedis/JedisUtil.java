package com.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 4:59 下午
 * @description:
 */
public class JedisUtil {

    @Autowired
    private JedisPoolClient jedisPoolClient;

    /*缓存生存时间  */
    private static final int expire = 1728000;

    /*
     * 获取redis key value
     */
    public String get(String key) {
        Jedis jedis = jedisPoolClient.getJedis();
        String value = jedis.get(key);
        JedisPoolClient.returnJedis(jedis);
        return value;
    }

}
