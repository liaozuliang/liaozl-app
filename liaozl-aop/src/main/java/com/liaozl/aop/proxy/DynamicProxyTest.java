package com.liaozl.aop.proxy;

import com.liaozl.aop.service.InterfaceProxyService;
import org.junit.Before;
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
public class DynamicProxyTest {

    @Resource
    private DynamicProxy dynamicProxy;

    @Resource
    private InterfaceProxyService interfaceProxyService;


    private InterfaceProxyService interfaceProxyService2;

    @Before
    public void init() {
        interfaceProxyService2 = (InterfaceProxyService) dynamicProxy.getProxyInstance(interfaceProxyService);
    }

    @Test
    public void testProxy() {
        interfaceProxyService2.testProxy("哈哈");
    }

    @Test
    public void testProxy2() {
        interfaceProxyService2.testProxy2("呵呵");
    }
}
