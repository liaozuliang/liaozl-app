package com.liaozl.zookeeper.lock.curator;

import com.liaozl.zookeeper.lock.ZookeeperLockCallBack;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * 可重入分布式锁
 * @author liaozuliang
 * @date 2016-10-13
 */
public class ZookeeperLock1 {

    private InterProcessMutex lock;

    public ZookeeperLock1(CuratorFramework zkClient, String lockPath) {
        this.lock = new InterProcessMutex(zkClient, lockPath);
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
}
