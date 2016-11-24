package com.liaozl.dao.service.impl;

import com.liaozl.dao.base.BaseDao;
import com.liaozl.dao.base.Page;
import com.liaozl.dao.module.Test1;
import com.liaozl.dao.service.Test1Dao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
@Repository("test1Dao")
public class Test1DaoImpl extends BaseDao implements Test1Dao {

    private static final String PREFIX = "com.liaozl.dao.Test1Dao.";

    @Override
    public boolean add(Test1 t1) {
        return insert(PREFIX + "add", t1) > 0;
    }

    @Override
    public boolean update(Test1 t1) {
        return update(PREFIX + "update", t1) > 0;
    }

    @Override
    public boolean del(int id) {
        return delete(PREFIX + "del", id) > 0;
    }

    @Override
    public Page<Test1> getTest1(Page<Test1> page, Map<String, Object> params) {
        return selectPage(PREFIX + "getTest1", params, page);
    }
}
