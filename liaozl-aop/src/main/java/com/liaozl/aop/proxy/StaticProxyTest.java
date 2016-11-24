package com.liaozl.aop.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-11-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class StaticProxyTest {

    @Resource
    private StaticProxy staticProxy;

    @Test
    public void testProxy() {
        staticProxy.testProxy("哈哈");
    }

    @Test
    public void testProxy2() {
        staticProxy.testProxy2("呵呵");
    }
}
