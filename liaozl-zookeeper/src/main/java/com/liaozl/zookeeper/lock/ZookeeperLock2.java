package com.liaozl.zookeeper.lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 监听节点变化，判断当前节点是否排序后第一个节点
 * @author liaozuliang
 * @date 2016-10-12
 */
public class ZookeeperLock2 {

    // zookeeper server列表
    private static String hosts = "119.29.227.196:2181,119.29.227.196:2181,119.29.227.196:2181";
    private static final String locksRootNode = "ZookeeperLocks";

    private ZooKeeper zk;
    // 当前client创建的子节点
    private volatile String thisPath;

    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * 连接zookeeper
     */
    public void lock(final String lockName, int timeOut, final ZookeeperLockCallBack callBack) {
        if (StringUtils.isBlank(lockName) || timeOut <= 0 || callBack == null) {
            throw new NullPointerException("参数错误");
        }

        try {
            zk = new ZooKeeper(hosts, timeOut, new Watcher() {
                public void process(WatchedEvent event) {
                    try {
                        // 连接建立时, 打开latch, 唤醒wait在该latch上的线程
                        if (event.getState() == Event.KeeperState.SyncConnected) {
                            latch.countDown();
                        }

                        // 子节点发生变化
                        if (event.getType() == Event.EventType.NodeChildrenChanged && event.getPath().equals("/" + locksRootNode)) {
                            List<String> childrenNodes = zk.getChildren("/" + locksRootNode, true);
                            String thisNode = thisPath.substring(("/" + locksRootNode + "/").length());

                            List<String> nodeList = new ArrayList<String>();
                            for (String node : childrenNodes) {
                                if (node.startsWith(lockName) && node.length() == thisNode.length()) {
                                    nodeList.add(node);
                                }
                            }

                            // 排序
                            Collections.sort(nodeList);
                            if (nodeList.indexOf(thisNode) == 0) {
                                lockCallBack(callBack);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // 等待连接建立
            latch.await();

            synchronized (locksRootNode) {
                if (zk.exists("/" + locksRootNode, true) == null) {
                    zk.create("/" + locksRootNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    Thread.sleep(100);
                }
            }

            // 创建子节点
            thisPath = zk.create("/" + locksRootNode + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            // wait一小会, 让结果更清晰一些
            Thread.sleep(100);

            // 监听子节点的变化
            List<String> childrenNodes = zk.getChildren("/" + locksRootNode, true);

            // 列表中只有一个子节点, 那肯定就是thisPath, 说明client获得锁
            if (childrenNodes.size() == 1) {
                lockCallBack(callBack);
            } else {
                String thisNode = thisPath.substring(("/" + locksRootNode + "/").length());

                List<String> nodeList = new ArrayList<String>();
                for (String node : childrenNodes) {
                    if (node.startsWith(lockName) && node.length() == thisNode.length()) {
                        nodeList.add(node);
                    }
                }

                // 排序
                Collections.sort(nodeList);
                if (nodeList.indexOf(thisNode) == 0) {
                    lockCallBack(callBack);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lockCallBack(ZookeeperLockCallBack callBack) {
        try {
            callBack.doLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 将thisPath删除, 监听thisPath的client将获得通知
                // 相当于释放锁
                zk.delete(this.thisPath, -1);
                zk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
