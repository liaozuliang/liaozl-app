package com.liaozl.dao.service.impl;

import com.liaozl.dao.base.BaseDao;
import com.liaozl.dao.base.Page;
import com.liaozl.dao.module.Test2;
import com.liaozl.dao.service.Test2Dao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
@Repository("test2Dao")
public class Test2DaoImpl extends BaseDao implements Test2Dao {

    private static final String PREFIX = "com.liaozl.dao.Test2Dao.";

    @Override
    public Test2 getTest2(Test2 t2) {
        return (Test2) selectOne(PREFIX + "getTest2", t2);
    }

    /**
     * 分页，不统计总数
     * @param page
     * @param params
     * @return
     */
    @Override
    public Page<Test2> getTest2Page1(Page<Test2> page, Test2 params) {
        page.setQueryRecordCount(false);
        return selectPage(PREFIX + "getTest2Page", params, page);
    }

    /**
     * 分页，统计总数，使用自定义统计sql
     * @param page
     * @param params
     * @return
     */
    @Override
    public Page<Test2> getTest2Page2(Page<Test2> page, Test2 params) {
        return selectPage(PREFIX + "getTest2Page", params, page, PREFIX + "countTest2Page");
    }

    /**
     * 分页，统计总数，使用通用统计sql
     * @param page
     * @param params
     * @return
     */
    @Override
    public Page<Test2> getTest2Page3(Page<Test2> page, Test2 params) {
        page.setQueryRecordCount(true);
        page.setHaveCountSql(false);
        return selectPage(PREFIX + "getTest2Page", params, page);
    }

    /**
     * 分页，统计总数，使用约定统计sql
     * @param page
     * @param params
     * @return
     */
    public Page<Test2> getTest2Page4(Page<Test2> page, Test2 params) {
        page.setQueryRecordCount(true);
        page.setHaveCountSql(true);
        return selectPage(PREFIX + "getTest2Page", params, page);
    }

    @Override
    public Page<Map<String, Object>> getTest2PageMap(Page<Map<String, Object>> page, Map<String, Object> params) {
        return selectPage(PREFIX + "getTest2PageMap", params, page);
    }
}
