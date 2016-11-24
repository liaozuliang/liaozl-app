package com.liaozl.aop.aspect;

import com.liaozl.aop.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-11-17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class UserServiceAspectTest {

    @Resource
    private UserService userService;


    @Test
    public void testGetUserName() {
        String userName = userService.getUserName("abc");
        System.out.println(userName);
    }

    @Test
    public void testGetUserAge() {
        int age = userService.getUserAge("bbb");
        System.out.println(age);
    }
}
