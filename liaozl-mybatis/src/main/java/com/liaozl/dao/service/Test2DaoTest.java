package com.liaozl.dao.service;

import com.liaozl.dao.base.DataSourceContext;
import com.liaozl.dao.base.Page;
import com.liaozl.dao.module.Test2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class Test2DaoTest {

    @Resource
    private Test2Dao test2Dao;

    @Before
    public void init(){
        DataSourceContext.useTest2();
    }

    @Test
    public void testGetTest2() {
        Test2 t2 = new Test2();
        //t2.setId(2);
        t2.setName("测试2");
        t2.setAddress("中国北京");

        Test2 t22 = test2Dao.getTest2(t2);
        System.out.println(t22.toString());

        Assert.assertTrue(t22!=null);
    }

    @Test
    public void testGetTest2Page1(){
        Page<Test2> page = new Page<Test2>();

        Test2 params = new Test2();
        params.setName("测试2");
        params.setAddress("中国北京");

        page = test2Dao.getTest2Page1(page, params);

        for(Test2 t2:page.getData()){
            System.out.println(t2.toString());
        }

        Assert.assertTrue(page.getData().size()>0);
    }

    @Test
    public void testGetTest2Page2(){
        Page<Test2> page = new Page<Test2>();

        Test2 params = new Test2();
        params.setName("test2");

        page = test2Dao.getTest2Page2(page, params);

        for(Test2 t2:page.getData()){
            System.out.println(t2.toString());
        }

        Assert.assertTrue(page.getData().size()>0);
    }

    @Test
    public void testGetTest2Page3(){
        Page<Test2> page = new Page<Test2>(1, 10);

        Test2 params = new Test2();
        params.setName("test2");

        page = test2Dao.getTest2Page3(page, params);

        for(Test2 t2:page.getData()){
            System.out.println(t2.toString());
        }

        Assert.assertTrue(page.getData().size()>0);
    }

    @Test
    public void testGetTest2Page4(){
        Page<Test2> page = new Page<Test2>();
        page.setPage(1);
        page.setRowNumber(2);

        Test2 params = new Test2();
        params.setName("test2");

        page = test2Dao.getTest2Page4(page, params);

        for(Test2 t2:page.getData()){
            System.out.println(t2.toString());
        }

        Assert.assertTrue(page.getData().size()>0);
    }

    @Test
    public void testGetTest2PageMap(){
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        page.setPage(1);
        page.setRowNumber(2);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "测试2");


        page = test2Dao.getTest2PageMap(page, params);

        for(Map<String, Object> t2:page.getData()){
            System.out.println(t2.toString());
        }

        Assert.assertTrue(page.getData().size()>0);
    }
}
