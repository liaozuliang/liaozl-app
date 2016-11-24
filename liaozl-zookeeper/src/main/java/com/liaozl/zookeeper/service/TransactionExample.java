package com.liaozl.zookeeper.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorMultiTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Collection;

/**
 * @author liaozuliang
 * @date 2016-10-13
 */
public class TransactionExample {

    private static String hosts = "119.29.227.196:2181,119.29.227.196:2181,119.29.227.196:2181";

    public static void main(String[] args) {
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(hosts, new ExponentialBackoffRetry(1000, 3));
            client.start();

            CuratorTransaction transaction = startTransaction(client);

            CuratorTransactionFinal ctf = null;

            ctf = addCreateToTransaction(transaction);
            ctf = addDeleteToTransaction(transaction);

            commitTransaction(ctf);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        // this example shows how to use ZooKeeper's new transactions
        Collection<CuratorTransactionResult> results = client.inTransaction().create().forPath("/a/path", "some data".getBytes())
                .and().setData().forPath("/another/path", "other data".getBytes())
                .and().delete().forPath("/yet/another/path")
                .and().commit(); // IMPORTANT!
        // called
        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
        return results;
    }

    /*
     * These next four methods show how to use Curator's transaction APIs in a
     * more traditional - one-at-a-time - manner
     */
    public static CuratorTransaction startTransaction(CuratorFramework client) {
        // start the transaction builder
        return client.inTransaction();
    }

    public static CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction) throws Exception {
        // add a create operation
        return transaction.create().forPath("/a/path", "some data".getBytes()).and();
    }

    public static CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction) throws Exception {
        // add a delete operation
        return transaction.delete().forPath("/a").and();
    }

    public static void commitTransaction(CuratorTransactionFinal transaction) throws Exception {
        // commit the transaction
        transaction.commit();
    }
}
