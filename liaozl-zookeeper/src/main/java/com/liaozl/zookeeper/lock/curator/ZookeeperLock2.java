package com.liaozl.zookeeper.lock.curator;

import com.liaozl.zookeeper.lock.ZookeeperLockCallBack;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

import java.util.concurrent.TimeUnit;

/**
 * 不可重入分布式锁
 * @author liaozuliang
 * @date 2016-10-13
 */
public class ZookeeperLock2 {

    private InterProcessSemaphoreMutex lock;

    public ZookeeperLock2(CuratorFramework zkClient, String lockPath) {
        this.lock = new InterProcessSemaphoreMutex(zkClient, lockPath);
    }

    public void lock(long waitTime, TimeUnit waitTimeUnit, ZookeeperLockCallBack callBack) throws Exception {
        if (!lock.acquire(waitTime, waitTimeUnit)) {
            throw new IllegalStateException("could not acquire the lock");
        }

        try {
            callBack.doLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
        }
    }

    /**
     * 模拟锁重入
     * @param waitTime
     * @param waitTimeUnit
     * @param callBack
     * @throws Exception
     */
    public void testLock(long waitTime, TimeUnit waitTimeUnit, ZookeeperLockCallBack callBack) throws Exception {
        if (!lock.acquire(waitTime, waitTimeUnit)) {
            throw new IllegalStateException("1 could not acquire the lock");
        }

        if (!lock.acquire(waitTime, waitTimeUnit)) {
            throw new IllegalStateException("2 could not acquire the lock");
        }

        try {
            callBack.doLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
            lock.release();
        }
    }
}
