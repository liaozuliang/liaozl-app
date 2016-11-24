package com.liaozl.ehcache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-10-09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class EhCacheServiceTest {

    @Resource
    private CacheService ehCacheService;

    private static final String cacheName = "t_user";
    private static final String key = "userName";

    @Test
    public void testGet() {
        ehCacheService.put(cacheName, key, "000");
        Object value = ehCacheService.get(cacheName, key);
        Assert.isTrue(value != null);
    }

    @Test
    public void testPut() {
        ehCacheService.put(cacheName, key, "abc");
        String value = ehCacheService.get(cacheName, key).toString();
        Assert.isTrue("abc".equals(value));
    }

    @Test
    public void testUpdate() {
        ehCacheService.put(cacheName, key, "abc");
        ehCacheService.update(cacheName, key, "abc123");
        String value = ehCacheService.get(cacheName, key).toString();
        Assert.isTrue("abc123".equals(value));
    }

    @Test
    public void testRemove() {
        ehCacheService.remove(cacheName, key);
        Object value = ehCacheService.get(cacheName, key);
        Assert.isTrue(value == null);
    }

}
