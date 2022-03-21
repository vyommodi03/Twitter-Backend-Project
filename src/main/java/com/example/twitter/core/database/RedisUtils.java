package com.example.twitter.core.database;

import redis.clients.jedis.JedisPool;

public class RedisUtils {
    private static JedisPool jedisPool= null;

    private RedisUtils() {
        throw new IllegalStateException("RedisUtils");
    }

    public static synchronized JedisPool getJedisPool() {
        if(jedisPool == null){
            jedisPool = new JedisPool("localhost", 6379);
        }
        return jedisPool;
    }

    public static synchronized void closeJedisPool(){
        if(jedisPool != null){
            jedisPool.close();
        }
        System.out.println("Jedis pool closed");
    }
}
