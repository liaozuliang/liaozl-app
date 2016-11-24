package com.liaozl.redis.redisson;

import org.apache.log4j.Logger;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.core.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Reids分布式锁
 *
 * @author liaozuliang
 * @date 2016-09-12
 */
public class RedissonLock {

    private static final Logger log = Logger.getLogger(RedissonLock.class);

    private static String ip = "192.168.18.13";
    private static int port = 8379;

    private static Redisson redisson;

    private static final RedissonLock instance = new RedissonLock();

    private RedissonLock() {

    }

    public static void close() {
        if (redisson != null) {
            redisson.shutdown();
            redisson = null;
        }
    }

    public static synchronized RedissonLock getInstance() {
        if (redisson == null) {
            init();
        }

        return instance;
    }

    public static synchronized RedissonLock getInstance(String ip, int port) {
        RedissonLock.ip = ip;
        RedissonLock.port = port;
        return getInstance();
    }

    private static void init() {
        Config conf = new Config();
        conf.useSingleServer().setAddress(ip + ":" + port).setConnectionPoolSize(10);

        redisson = Redisson.create(conf);
    }

    public void lock(String lockKey, long timeout, RedissonLockCallBack callBack) {
        RLock lock = redisson.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);

        try {
            callBack.doLock();
        } catch (Exception e) {
            log.error("使用Redisson锁出错：", e);
        } finally {
            lock.unlock();
        }
    }

}
