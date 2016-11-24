package com.liaozl.redis.service.impl;

import com.liaozl.redis.model.Student;
import com.liaozl.redis.model.Teacher;
import com.liaozl.redis.model.enums.CacheDataTypeEnum;
import com.liaozl.redis.service.CacheService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author liaozuliang
 * @date 2016-09-12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-application.xml"})
public class RedisCacheServiceTest {

    @Resource(name = "redisCacheService")
    private CacheService cacheService;

    private static final String key = "redisTest";

    @Before
    public void init() {
        System.out.println("------start test------");
    }

    @Test
    public void putTest() {
        System.out.println("-------putTest-------");
        boolean result = cacheService.put(CacheDataTypeEnum.TEACHER, key, new Teacher(1, "aa", 11));
        Assert.assertTrue(result);
    }

    @Test
    public void putTest2() {
        System.out.println("-------putTest2-------");
        boolean result = cacheService.put(CacheDataTypeEnum.STUDENT, key, new Student("S2", "中国", 33, "aaaaaa", new Date()), 60);
        Assert.assertTrue(result);
    }

    @Test
    public void getTest() {
        System.out.println("-------getTest-------");
        putTest2();
        Student student = (Student) cacheService.get(CacheDataTypeEnum.STUDENT, key);
        System.out.println(student.toString());
        Assert.assertTrue(student != null);
    }

    @Test
    public void removeTest() {
        System.out.println("-------removeTest-------");
        boolean result = cacheService.remove(CacheDataTypeEnum.STUDENT, key);
        Student student = (Student) cacheService.get(CacheDataTypeEnum.STUDENT, key);
        Assert.assertTrue(result && student == null);
    }

    @After
    public void destroy() {
        System.out.println("------finished test------");
    }
}
