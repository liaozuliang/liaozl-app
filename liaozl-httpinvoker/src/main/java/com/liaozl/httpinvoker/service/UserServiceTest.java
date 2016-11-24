package com.liaozl.httpinvoker.service;

import com.liaozl.httpinvoker.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class UserServiceTest {

    @Resource(name = "userHttpService")
    private UserService userService;

    @Test
    public void testGetUser() {
        List<User> userList = userService.getUser(12, "abc");

        for (User u : userList) {
            System.out.println(u.toString());
        }

        Assert.assertTrue(userList != null && userList.size() > 0);
    }
}
