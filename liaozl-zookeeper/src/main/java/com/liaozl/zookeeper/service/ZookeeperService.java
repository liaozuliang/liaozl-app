package com.liaozl.zookeeper.service;

import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-12
 */
public interface ZookeeperService {

    public boolean create(String nodePath, byte[] data);

    public boolean setData(String nodePath, byte[] data);

    public boolean exists(String nodePath);

    public byte[] getData(String nodePath);

    public boolean delete(String nodePath);

    public List<String> getChildren(String parentPath);
}
