package com.liaozl.aop.proxy;

import com.liaozl.aop.service.impl.ClassProxyService;
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
public class CglibProxyTest {

    @Resource
    private CglibProxy cglibProxy;

    @Test
    public void testProxy() {
        ClassProxyService classProxyService = (ClassProxyService) cglibProxy.getProxyInstance(ClassProxyService.class);
        classProxyService.testProxy("哈哈");
        classProxyService.testProxy2("呵呵");
    }

}
