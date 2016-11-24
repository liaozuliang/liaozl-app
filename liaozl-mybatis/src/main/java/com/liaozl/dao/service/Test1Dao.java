package com.liaozl.dao.service;

import com.liaozl.dao.base.Page;
import com.liaozl.dao.module.Test1;

import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
public interface Test1Dao {

    public boolean add(Test1 t1);

    public boolean update(Test1 t1);

    public boolean del(int id);

    public Page<Test1> getTest1(Page<Test1> page, Map<String, Object> params);
}
