package com.liaozl.redis.jedis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * @author liaozuliang
 * @date 2017-02-09
 */
public class RedisLock {

    protected static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    //配置文件的文件名
    public static final String CONFIG_PATH = "config/redis.properties";

    //redis主机地址
    public static final String CONFIG_HOST = "redis.contact.host";

    //redis 端口
    public static final String CONFIG_PORT = "redis.contact.port";

    //加锁标志
    public static final String LOCKED = "TRUE";
    public static final long ONE_MILLI_NANOS = 1000000L;

    //默认超时时间（毫秒）
    public static final long DEFAULT_TIME_OUT = 15000;
    public static JedisPool pool;
    public static final Random r = new Random();

    //刪除 锁的超时时间（秒）
    public static final int EXPIRE = 5 * 60;

    protected static Properties p = new Properties();

    static {
        InputStream in = null;
        try {
            in = RedisLock.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
            if (in != null)
                p.load(in);
        } catch (IOException e) {
            logger.error("load " + CONFIG_PATH + " into Constants error!");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("close " + CONFIG_PATH + " error!");
                }
            }
        }

        String host = p.getProperty(CONFIG_HOST);
        host = StringUtils.isEmpty(host) ? "192.168.18.13" : host;
        String port = p.getProperty(CONFIG_PORT);
        port = StringUtils.isEmpty(port) ? "8379" : port;

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(500);
        config.setMinIdle(200);
        config.setMaxTotal(1000);
        pool = new JedisPool(config, host, Integer.parseInt(port));
    }

    private Jedis jedis;
    private String key;

    //锁状态标志
    private boolean locked = false;

    public RedisLock(String key) {
        this.key = key;
        this.jedis = pool.getResource();
    }

    public boolean lock(long timeout) {
        long nano = System.nanoTime();
        timeout *= ONE_MILLI_NANOS;

        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (jedis.setnx(key, LOCKED) == 1) {
                    jedis.expire(key, EXPIRE);
                    locked = true;
                    return locked;
                }
                //短暂休眠，nano避免出现活锁
                Thread.sleep(3, r.nextInt(500));
            }
        } catch (Exception e) {
        }

        return false;
    }

    public boolean lock() {
        return lock(DEFAULT_TIME_OUT);
    }

    //无论是否加锁成功，必须调用
    public void unlock() {
        try {
            if (locked)
                jedis.del(key);
        } finally {
            pool.returnResource(jedis);
        }
    }
}
