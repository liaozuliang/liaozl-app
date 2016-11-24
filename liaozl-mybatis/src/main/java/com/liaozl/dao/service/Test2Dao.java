package com.liaozl.dao.service;

import com.liaozl.dao.base.Page;
import com.liaozl.dao.module.Test2;

import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
public interface Test2Dao {

    public Test2 getTest2(Test2 t2);

    /**
     * 分页，不统计总数
     * @param page
     * @param params
     * @return
     */
    public Page<Test2> getTest2Page1(Page<Test2> page, Test2 params);

    /**
     * 分页，统计总数，使用自定义统计sql
     * @param page
     * @param params
     * @return
     */
    public Page<Test2> getTest2Page2(Page<Test2> page, Test2 params);

    /**
     * 分页，统计总数，使用通用统计sql
     * @param page
     * @param params
     * @return
     */
    public Page<Test2> getTest2Page3(Page<Test2> page, Test2 params);

    /**
     * 分页，统计总数，使用约定统计sql
     * @param page
     * @param params
     * @return
     */
    public Page<Test2> getTest2Page4(Page<Test2> page, Test2 params);

    public Page<Map<String, Object>> getTest2PageMap(Page<Map<String, Object>> page, Map<String, Object> params);
}
