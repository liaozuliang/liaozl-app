package com.liaozl.dao.mapper;

import com.liaozl.dao.base.DataSourceContext;
import com.liaozl.dao.module.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Before
    public void init() {
        DataSourceContext.useTest1();
    }

    @Test
    public void testGetUserByIdAndName() {
        User user = userMapper.getUserByIdAndName(4, "中国");
        System.out.println(user.toString());
        Assert.assertTrue(user != null);
    }
}
