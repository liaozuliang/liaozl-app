package com.liaozl.junit.test;

import com.liaozl.junit.service.MyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-12-29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class MyServiceTest {

    @Resource
    private MyService myService;

    @Test
    public void test() {
        Assert.assertTrue(myService.test("哈哈，中国"));
    }
}
