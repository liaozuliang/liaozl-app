package com.liaozl.zookeeper.lock;

import java.util.concurrent.CountDownLatch;

/**
 * @author liaozuliang
 * @date 2016-10-12
 */
public class ZookeeperLockTest {

    public static void testLock() {
        final CountDownLatch countDownLatch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ZookeeperLock().lock("zkLock1", 20000,new ZookeeperLockCallBack() {
                        @Override
                        public void doLock() {
                            System.out.println("Get zkLock1, " + Thread.currentThread().getName());
                            try {
                                Thread.sleep(5000);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }

                            countDownLatch.countDown();
                        }
                    });
                }
            }).start();
        }

        try {
            countDownLatch.await();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testLock();
    }
}
