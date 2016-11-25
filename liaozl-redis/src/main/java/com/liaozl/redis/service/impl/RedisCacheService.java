package com.liaozl.redis.service.impl;

import com.liaozl.redis.model.enums.CacheDataTypeEnum;
import com.liaozl.redis.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author liaozuliang
 * @date 2016-09-09
 */
@Service("redisCacheService")
public class RedisCacheService implements CacheService {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheService.class);

    @Resource(name = "queryRedisTemplate")
    private RedisTemplate<String, Object> queryRedisTemplate;

    @Resource(name = "saveRedisTemplate")
    private RedisTemplate<String, Object> saveRedisTemplate;

    @Override
    public boolean put(CacheDataTypeEnum dataTypeEnum, String key, Object value, int seconds) {
        try {
            key = getRedisCacheKey(dataTypeEnum, key);
            getRedisTemplate(true).opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("更新Redis缓存数据出错：", e);
        }

        return false;
    }

    @Override
    public boolean put(CacheDataTypeEnum dataTypeEnum, String key, Object value) {
        try {
            key = getRedisCacheKey(dataTypeEnum, key);
            getRedisTemplate(true).opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("更新Redis缓存数据出错：", e);
        }

        return false;
    }

    @Override
    public Object get(CacheDataTypeEnum dataTypeEnum, String key) {
        try {
            key = getRedisCacheKey(dataTypeEnum, key);
            return getRedisTemplate(false).opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取Redis缓存数据出错：", e);
        }

        return null;
    }

    @Override
    public boolean remove(CacheDataTypeEnum dataTypeEnum, String key) {
        try {
            key = getRedisCacheKey(dataTypeEnum, key);
            getRedisTemplate(true).delete(key);
            return true;
        } catch (Exception e) {
            log.error("删除Redis缓存数据出错：", e);
        }

        return false;
    }

    /**
     * 获取RedisTemplate
     * @param save
     * @return
     */
    private RedisTemplate<String, Object> getRedisTemplate(boolean save) {
        if (save) {
            return saveRedisTemplate;
        }

        return queryRedisTemplate;
    }

    /**
     * 获取redis缓存key
     * @param dataTypeEnum
     * @param key
     * @return
     */
    private String getRedisCacheKey(CacheDataTypeEnum dataTypeEnum, String key) {
        if (dataTypeEnum == null || StringUtils.isEmpty(key)) {
            throw new RuntimeException("参数错误，无法获取Redis缓存Key");
        }

        return "RedisCacheKey_" + dataTypeEnum + "_" + key;
    }
}
