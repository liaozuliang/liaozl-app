package com.liaozl.dao.service;

import com.liaozl.dao.base.DataSourceContext;
import com.liaozl.dao.base.Page;
import com.liaozl.dao.module.Test1;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class Test1DaoTest {

    @Resource
    private Test1Dao test1Dao;

    @Before
    public void init(){
        DataSourceContext.useTest1();
    }

    @Test
    public void testAdd(){
        Test1 t1 = new Test1();
        t1.setName("4Test4");
        t1.setAge(44);
        t1.setBirthday(new Date());

        boolean result = test1Dao.add(t1);

        System.out.println("the new data id is:" + t1.getId());

        Assert.assertTrue(result);
    }

    @Test
    public void testUpdate(){
        Test1 t1 = new Test1();
        t1.setId(1);
        t1.setName("1111Test1111");

        boolean result = test1Dao.update(t1);
        Assert.assertTrue(result);
    }

    @Test
    public void testDel(){
        boolean result = test1Dao.del(3);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetTest1(){

        Page<Test1> page = new Page<Test1>();
        page.setPage(1);
        page.setRowNumber(100);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "中国3");
        //params.put("age", 112);

        test1Dao.getTest1(page, params);

        for (Test1 t1 : page.getData()) {
            System.out.println(t1.toString());
        }

        Assert.assertTrue(page.getData().size()>0);
    }

}
