package com.liaozl.zookeeper.lock.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 信号量
 *
 * @author liaozuliang
 * @date 2016-10-13
 */
public class InterProcessSemaphoreExample {

    private static String hosts = "119.29.227.196:2181,119.29.227.196:2181,119.29.227.196:2181";
    private static final int MAX_LEASE = 10;
    private static final String PATH = "/examples/locks";

    public static void main(String[] args) throws Exception {

        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(hosts, new ExponentialBackoffRetry(1000, 3));
            client.start();

            InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client, PATH, MAX_LEASE);

            Collection<Lease> leases = semaphore.acquire(5);
            System.out.println("get " + leases.size() + " leases");

            Lease lease = semaphore.acquire();
            System.out.println("get another lease");

            Thread.sleep(10000);

            Collection<Lease> leases2 = semaphore.acquire(5, 10, TimeUnit.SECONDS);
            System.out.println("Should timeout and acquire return " + leases2);

            semaphore.returnLease(lease);
            System.out.println("return one lease");

            semaphore.returnAll(leases);
            System.out.println("return another 5 leases");
        } catch (Exception e) {

        } finally {

        }
    }
}
