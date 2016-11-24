package com.liaozl.zookeeper.lock.curator;

import com.liaozl.zookeeper.lock.ZookeeperLockCallBack;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * 可重入读写分布式锁, 写锁可重入读锁
 *
 * @author liaozuliang
 * @date 2016-10-13
 */
public class ZookeeperLock3 {

    private InterProcessReadWriteLock lock;
    private InterProcessMutex readLock;
    private InterProcessMutex writeLock;

    public ZookeeperLock3(CuratorFramework zkClient, String lockPath) {
        this.lock = new InterProcessReadWriteLock(zkClient, lockPath);
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public void lock(long waitTime, TimeUnit waitTimeUnit, ZookeeperLockCallBack callBack) throws Exception {
        if (!writeLock.acquire(waitTime, waitTimeUnit)) {
            throw new IllegalStateException("could not acquire the write lock");
        }

        if (!readLock.acquire(waitTime, waitTimeUnit)) {
            throw new IllegalStateException("could not acquire the read lock");
        }

        try {
            callBack.doLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.release();
            writeLock.release();
        }
    }

    public void readLock(long waitTime, TimeUnit waitTimeUnit, ZookeeperLockCallBack callBack) throws Exception {
        if (!readLock.acquire(waitTime, waitTimeUnit)) {
            throw new IllegalStateException("could not acquire the read lock");
        }

        try {
            callBack.doLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.release();
        }
    }

    public void writeLock(long waitTime, TimeUnit waitTimeUnit, ZookeeperLockCallBack callBack) throws Exception {
        if (!writeLock.acquire(waitTime, waitTimeUnit)) {
            throw new IllegalStateException("could not acquire the write lock");
        }

        try {
            callBack.doLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.release();
        }
    }
}
