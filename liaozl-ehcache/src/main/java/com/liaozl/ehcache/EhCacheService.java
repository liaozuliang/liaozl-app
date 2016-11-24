package com.liaozl.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 基于ehcache缓存数据
 *
 * @author liaozuliang
 * @date 2015年8月14日
 */
@Service("ehCacheService")
public class EhCacheService implements CacheService {

    private CacheManager cm;

    @PostConstruct
    private void init() {
        // cm = CacheManager.create("src/config/ehcache.xml");
        cm = CacheManager.create();
        System.out.println("........ehcache start finished.......");
    }

    @PreDestroy
    private void destory() {
        cm.shutdown();
        System.out.println("........ehcache stop finished.......");
    }

    private Cache getCache(String cacheName) {
        if (StringUtils.isBlank(cacheName)) {
            throw new RuntimeException("cacheName is null");
        }

        if (!cm.cacheExists(cacheName)) {
            cm.addCache(cacheName);
        }

        return cm.getCache(cacheName);
    }

    @Override
    public Object get(String cacheName, String key) {
        Element element = getCache(cacheName).get(key);

        if (element != null) {
            return element.getValue();
        }

        return null;
    }

    @Override
    public void put(String cacheName, String key, Object value) {
        getCache(cacheName).put(new Element(key, value));
    }

    @Override
    public void update(String cacheName, String key, Object value) {
        getCache(cacheName).replace(new Element(key, value));
    }

    @Override
    public void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

}
