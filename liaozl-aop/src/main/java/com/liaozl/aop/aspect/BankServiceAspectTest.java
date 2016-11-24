package com.liaozl.aop.aspect;

import com.liaozl.aop.service.BankService;
import com.liaozl.aop.service.UserService;
import com.liaozl.aop.service.impl.ClassProxyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-11-17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class BankServiceAspectTest {

    @Resource
    private BankService bankService;

    @Test
    public void testTransfer() {
        System.out.println(bankService.transfer("张三", "李四", 300));
    }

    @Test
    public void testTransfer2() {
        System.out.println(bankService.transfer("abc", "李四", 10));
    }

}
