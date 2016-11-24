package com.liaozl.redis.service;

import com.liaozl.redis.model.enums.CacheDataTypeEnum;

/**
 * @author liaozuliang
 * @date 2016-09-09
 */
public interface CacheService {

    /**
     * 更新缓存数据
     * @param dataTypeEnum 数据类型
     * @param key 缓存key
     * @param value 数据
     * @param seconds 超时时间，秒
     * @return
     */
    public boolean put(CacheDataTypeEnum dataTypeEnum, String key, Object value, int seconds);

    /**
     * 更新缓存数据
     * @param dataTypeEnum 数据类型
     * @param key 缓存key
     * @param value 数据
     * @return
     */
    public boolean put(CacheDataTypeEnum dataTypeEnum, String key, Object value);

    /**
     * 获取缓存数据
     * @param dataTypeEnum 数据类型
     * @param key 缓存key
     * @return
     */
    public Object get(CacheDataTypeEnum dataTypeEnum, String key);

    /**
     * 删除缓存数据
     * @param dataTypeEnum 数据类型
     * @param key 缓存key
     * @return
     */
    public boolean remove(CacheDataTypeEnum dataTypeEnum, String key);
}
