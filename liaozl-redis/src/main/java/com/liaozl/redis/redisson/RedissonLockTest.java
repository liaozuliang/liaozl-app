package com.liaozl.redis.redisson;

import java.util.concurrent.CountDownLatch;

/**
 * @author liaozuliang
 * @date 2016-09-12
 */
public class RedissonLockTest {

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(10);

        final String lockKey = "RedisLockKey_Test";

        final RedissonLockCallBack callBack = new RedissonLockCallBack() {
            @Override
            public void doLock() {
                try {
                    System.out.println(countDownLatch.getCount() + ", I get the redis lock, ThreadName:" + Thread.currentThread().getName());
                    Thread.sleep(1 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }
        };

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RedissonLock.getInstance().lock(lockKey, 3, callBack);
                }
            }).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RedissonLock.getInstance("192.168.18.13", 8379).lock(lockKey, 3, callBack);
                }
            }).start();
        }

        try {
            countDownLatch.await();

            Thread.sleep(1 * 1000); //需等待最后一个线程持有的redis锁释放后才能关闭，否则会报错

            RedissonLock.close();

            System.out.println("--------over--------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
