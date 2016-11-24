package com.liaozl.redis.jedis;


import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Jedis工厂
 * @author liaozuliang
 * @date 2015年8月18日
 */
public class JedisFactory {

    private static final Logger log = Logger.getLogger(JedisFactory.class);

    private static final int TIME_OUT = 30 * 1000;// 30秒
    private static final int MAX_COUNT = 5;

    private static Map<String, JedisPool> maps = new HashMap<String, JedisPool>();

    private static JedisFactory instance;

    private JedisFactory() {

    }

    public static JedisFactory getInstance() {
        if (instance == null) {
            synchronized (JedisFactory.class) {
                if (instance == null) {
                    instance = new JedisFactory();
                }
            }
        }

        return instance;
    }

    /**
     * 连接池配置
     * @return
     * @author liaozuliang
     * @date 2015年8月18日
     */
    private static JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxIdle(8);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        return config;
    }

    /**
     * 创建Jedis连接池
     * @param ip
     * @param port
     * @return
     * @author liaozuliang
     * @date 2015年8月18日
     */
    private JedisPool getPool(String ip, int port) {
        String key = ip + ":" + port;
        JedisPool pool = null;

        if (!maps.containsKey(key)) {
            try {
                pool = new JedisPool(getJedisPoolConfig(), ip, port, TIME_OUT);
                maps.put(key, pool);
            } catch (Exception e) {
                log.error("getPool[" + ip + ":" + port + "] error:", e);
            }
        } else {
            pool = maps.get(key);
        }

        return pool;
    }

    /**
     * 创建Jedis连接
     * @param ip
     * @param port
     * @return
     * @author liaozuliang
     * @date 2015年8月18日
     */
    public Jedis getJedis(String ip, int port) {
        Jedis jedis = null;
        int count = 0;

        do {
            try {
                jedis = getPool(ip, port).getResource();
            } catch (Exception e) {
                log.error("getJedis[" + ip + ":" + port + "] error:", e);
                getPool(ip, port).returnBrokenResource(jedis);
            }
            count++;
        } while (jedis == null && count < MAX_COUNT);

        return jedis;
    }

    /**
     * 关闭Jedis连接
     * @param jedis
     * @param ip
     * @param port
     * @author liaozuliang
     * @date 2015年8月18日
     */
    public void closeJedis(Jedis jedis, String ip, int port) {
        if (jedis != null) {
            getPool(ip, port).returnResource(jedis);
        }
    }
}
