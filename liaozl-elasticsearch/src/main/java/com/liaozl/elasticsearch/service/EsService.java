package com.liaozl.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-10-18
 */
public interface EsService {

    public boolean add(String indexName, Object obj);

    public boolean add(String indexName, String indexId, Object obj);

    public boolean update(String indexName, String indexId, Object obj);

    public boolean delete(String indexName, String indexId);

    public String getJsonString(String indexName, String indexId);

    public Map<String, Object> getMap(String indexName, String indexId);

    public JSONObject getJsonObj(String indexName, String indexId);

    public <T> T getBean(String indexName, String indexId, Class<T> clazz);

    public List<String> search(String[] indexNames, String[] fieldNames, String searchContent);

}
