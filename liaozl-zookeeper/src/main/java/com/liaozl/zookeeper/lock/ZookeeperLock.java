package com.liaozl.zookeeper.lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 删除的节点是上一个获取锁的节点，排序后，当前节点的上一节点就是被删除的节点
 * @author liaozuliang
 * @date 2016-10-12
 */
public class ZookeeperLock {

    // zookeeper server列表
    private static String hosts = "119.29.227.196:2181,119.29.227.196:2181,119.29.227.196:2181";

    private static final String locksRootNode = "ZookeeperLocks";

    private ZooKeeper zk;

    // 当前client创建的子节点
    private volatile String thisPath;

    // 当前client等待的子节点
    private volatile String waitPath;

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

                        // 发生了waitPath的删除事件
                        if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(waitPath)) {
                            // 确认thisPath是否真的是列表中的最小节点
                            List<String> childrenNodes = zk.getChildren("/" + locksRootNode, false);
                            String thisNode = thisPath.substring(("/" + locksRootNode + "/").length());

                            List<String> nodeList = new ArrayList<String>();
                            for (String node : childrenNodes) {
                                if (node.startsWith(lockName) && node.length() == thisNode.length()) {
                                    nodeList.add(node);
                                }
                            }

                            // 排序
                            Collections.sort(nodeList);
                            int index = nodeList.indexOf(thisNode);
                            if (index == 0) {
                                // 确实是最小节点
                                lockCallBack(callBack);
                            } else {
                                // 说明waitPath是由于出现异常而挂掉的
                                // 更新waitPath
                                waitPath = "/" + locksRootNode + "/" + nodeList.get(index - 1);
                                // 重新注册监听, 并判断此时waitPath是否已删除
                                if (zk.exists(waitPath, true) == null) {
                                    lockCallBack(callBack);
                                }
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
                if (zk.exists("/" + locksRootNode, false) == null) {
                    zk.create("/" + locksRootNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    Thread.sleep(100);
                }
            }

            // 创建临时子节点
            thisPath = zk.create("/" + locksRootNode + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            // wait一小会, 让结果更清晰一些
            Thread.sleep(10);

            // 注意, 没有必要监听"/locks"的子节点的变化情况
            List<String> childrenNodes = zk.getChildren("/" + locksRootNode, false);

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
                int index = nodeList.indexOf(thisNode);
                if (index == -1) {
                    // never happened
                } else if (index == 0) {
                    // inddx == 0, 说明thisNode在列表中最小, 当前client获得锁
                    lockCallBack(callBack);
                } else {
                    // 获得排名比thisPath前1位的节点
                    this.waitPath = "/" + locksRootNode + "/" + nodeList.get(index - 1);
                    // 在waitPath上注册监听器, 当waitPath被删除时, zookeeper会回调监听器的process方法
                    zk.getData(waitPath, true, new Stat());
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
