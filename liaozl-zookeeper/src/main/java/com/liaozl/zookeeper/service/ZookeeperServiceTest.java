package com.liaozl.zookeeper.service;

import org.junit.Test;

import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-12
 */
public class ZookeeperServiceTest {

    private ZookeeperService zkService = new ZookeeperServiceImpl();

    @Test
    public void testCreate() {
        boolean res1 = zkService.create("/abc", null);
        boolean res2 = zkService.create("/abc2", "abc2".getBytes());

        System.out.println("res1:" + res1);
        System.out.println("res2:" + res2);
    }

    @Test
    public void testSetData() {
        zkService.setData("/abc2", "abc2".getBytes());
        System.out.println("abc2".equals(new String(zkService.getData("/abc2"))));

        zkService.setData("/abc2", "ab中国上海22c2".getBytes());
        System.out.println("ab中国上海22c2".equals(new String(zkService.getData("/abc2"))));
    }

    @Test
    public void testExists() {
        boolean res1 = zkService.exists("/ab333c");
        boolean res2 = zkService.exists("/abc2");

        System.out.println("res1:" + res1);
        System.out.println("res2:" + res2);
    }

    @Test
    public void testGetData() {
        String res1 = null;
        String res2 = null;

        byte[] data1 = zkService.getData("/abc");
        if (data1 != null) {
            res1 = new String(data1);
        }

        byte[] data2 = zkService.getData("/abc2");
        if (data2 != null) {
            res2 = new String(data2);
        }

        System.out.println("res1:" + res1);
        System.out.println("res2:" + res2);
    }

    @Test
    public void testDelete() {
        boolean res1 = zkService.exists("/ab333c");
        System.out.println("res1:" + res1);

        zkService.delete("/ab333c");

        boolean res2 = zkService.exists("/ab333c");
        System.out.println("res2:" + res2);
    }

    @Test
    public void testGetChildren() {
        List<String> nodeList = zkService.getChildren("/");

        if(nodeList!=null){
            for(String node:nodeList){
                System.out.println(node);
            }
        }
    }
}
