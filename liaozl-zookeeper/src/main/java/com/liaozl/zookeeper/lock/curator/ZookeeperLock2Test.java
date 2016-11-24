package com.liaozl.zookeeper.lock.curator;

import com.liaozl.zookeeper.lock.ZookeeperLockCallBack;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author liaozuliang
 * @date 2016-10-13
 */
public class ZookeeperLock2Test {

    private static String hosts = "119.29.227.196:2181,119.29.227.196:2181,119.29.227.196:2181";
    private static String lockPath = "/zklocks/test";

    private static int threadNum = 5;

    public static void testLock() {
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CuratorFramework client = CuratorFrameworkFactory.newClient(hosts, new ExponentialBackoffRetry(1000, 3));
                    try {
                        client.start();

                        ZookeeperLock2 zkLock = new ZookeeperLock2(client, lockPath);

                        for (int j = 0; j < 3; ++j) {
                            zkLock.testLock(60, TimeUnit.SECONDS, new ZookeeperLockCallBack() {
                                @Override
                                public void doLock() {
                                    System.out.println(Thread.currentThread().getId() + " Get the lock");
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        CloseableUtils.closeQuietly(client);
                    }

                    countDownLatch.countDown();
                }
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        testLock();
    }
}
