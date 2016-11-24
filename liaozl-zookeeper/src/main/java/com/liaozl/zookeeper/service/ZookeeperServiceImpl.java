package com.liaozl.zookeeper.service;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-12
 */
public class ZookeeperServiceImpl implements ZookeeperService {

    private static String hosts = "119.29.227.196:2181,119.29.227.196:2181,119.29.227.196:2181";
    private static final int timeOut = 5 * 60 * 1000;

    private CuratorFramework zkClient;

    public ZookeeperServiceImpl() {
        //zkClient = getClient2(); //推荐使用这种方式
        zkClient = getClient();
    }

    @Override
    public boolean create(String nodePath, byte[] data) {
        try {
            //zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(nodePath, data);
            zkClient.create().creatingParentsIfNeeded().forPath(nodePath, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean setData(String nodePath, byte[] data) {
        try {
            //zkClient.setData().inBackground().forPath(nodePath, data);
            zkClient.setData().forPath(nodePath, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean exists(String nodePath) {
        try {
            //Stat stat = zkClient.checkExists().watched().inBackground().forPath(nodePath);
            Stat stat = zkClient.checkExists().forPath(nodePath);
            return stat != null ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public byte[] getData(String nodePath) {
        try {
            //return zkClient.getData().watched().inBackground().forPath(nodePath);
            return zkClient.getData().forPath(nodePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean delete(String nodePath) {
        try {
            //zkClient.delete().guaranteed().deletingChildrenIfNeeded().inBackground().forPath(nodePath);
            zkClient.delete().deletingChildrenIfNeeded().forPath(nodePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<String> getChildren(String parentPath) {
        try {
            return zkClient.getChildren().forPath(parentPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private CuratorFramework getClient() {
        // 连接时间 和重试次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(hosts, retryPolicy);
        client.start();

        return client;
    }

    private CuratorFramework getClient2() {
        //默认创建的根节点是没有做权限控制的--需要自己手动加权限???
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl;

            @Override
            public List<ACL> getDefaultAcl() {
                if (acl == null) {
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "admin:admin")));
                    this.acl = acl;
                }
                return acl;
            }

            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };

        byte[] auth = "admin:admin".getBytes();
        int connectionTimeoutMs = 5000;

        String scheme = "digest";
        String namespace = "myNameSpace";

        CuratorFramework client = CuratorFrameworkFactory.builder().aclProvider(aclProvider).
                authorization(scheme, auth).
                connectionTimeoutMs(connectionTimeoutMs).
                connectString(hosts).
                namespace(namespace).
                retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();

        client.start();

        return client;
    }
}
