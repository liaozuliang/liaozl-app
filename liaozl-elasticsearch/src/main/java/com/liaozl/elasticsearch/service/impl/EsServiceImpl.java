package com.liaozl.elasticsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liaozl.elasticsearch.es.EsClientFactory;
import com.liaozl.elasticsearch.service.EsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author liaozuliang
 * @date 2016-10-18
 */
@Service("esService")
public class EsServiceImpl implements EsService {

    private static final Logger logger = Logger.getLogger(EsServiceImpl.class);

    private Client client;

    @PostConstruct
    public void init() {
        client = EsClientFactory.getEsClient();
    }

    @PreDestroy
    public void destory() {
        client.close();
    }

    @Override
    public boolean add(String indexName, Object obj) {
        if (StringUtils.isBlank(indexName)) {
            logger.error("参数indexName不能为空");
            return false;
        }

        if (obj == null) {
            logger.error("参数obj不能为空");
            return false;
        }

        try {
            String jsonObj = JSONObject.toJSONString(obj);
            IndexResponse response = client.prepareIndex(indexName + "_index", indexName + "_type").setSource(jsonObj).execute().actionGet();

            //if (response.isCreated()) {
            return true;
            //}
        } catch (Exception e) {
            logger.error("创建ES索引[indexName:" + indexName + "]出错：", e);
        }

        return false;
    }

    @Override
    public boolean add(String indexName, String indexId, Object obj) {
        if (StringUtils.isBlank(indexName)) {
            logger.error("参数indexName不能为空");
            return false;
        }

        if (obj == null) {
            logger.error("参数obj不能为空");
            return false;
        }

        if (StringUtils.isBlank(indexId)) {
            logger.error("参数indexId不能为空");
            return false;
        }

        try {
            String jsonObj = JSON.toJSONString(obj);
            IndexResponse response = client.prepareIndex(indexName + "_index", indexName + "_type", indexId).setSource(jsonObj).execute().actionGet();

            //if (response.isCreated()) {
            return true;
            //}
        } catch (Exception e) {
            logger.error("创建ES索引[indexName:" + indexName + ", indexId:" + indexId + "]出错：", e);
        }

        return false;
    }

    @Override
    public boolean update(String indexName, String indexId, Object obj) {
        if (StringUtils.isBlank(indexName)) {
            logger.error("参数indexName不能为空");
            return false;
        }

        if (obj == null) {
            logger.error("参数obj不能为空");
            return false;
        }

        if (StringUtils.isBlank(indexId)) {
            logger.error("参数indexId不能为空");
            return false;
        }

        try {
            String jsonObj = JSON.toJSONString(obj);
            IndexResponse response = client.prepareIndex(indexName + "_index", indexName + "_type", indexId).setSource(jsonObj).execute().actionGet();

            //if (!response.isCreated()) {
            return true;
            //}
        } catch (Exception e) {
            logger.error("更新ES索引[indexName:" + indexName + ", indexId:" + indexId + "]出错：", e);
        }

        return false;
    }

    @Override
    public boolean delete(String indexName, String indexId) {
        if (StringUtils.isBlank(indexId)) {
            logger.error("参数indexId不能为空");
            return false;
        }

        try {
            client.prepareDelete(indexName + "_index", indexName + "_type", indexId).execute().actionGet();
            return true;
        } catch (Exception e) {
            logger.error("删除ES索引[indexName:" + indexName + ", indexId:" + indexId + "]出错：", e);
        }

        return false;
    }

    @Override
    public String getJsonString(String indexName, String indexId) {
        if (StringUtils.isBlank(indexName)) {
            logger.error("参数indexName不能为空");
            return null;
        }

        if (StringUtils.isBlank(indexId)) {
            logger.error("参数indexId不能为空");
            return null;
        }

        try {
            GetResponse response = client.prepareGet(indexName + "_index", indexName + "_type", indexId).execute().actionGet();
            return response.getSourceAsString();
        } catch (Exception e) {
            logger.error("查询ES索引[indexName:" + indexName + ", indexId:" + indexId + "]出错：", e);
        }

        return null;
    }

    public Map<String, Object> getMap(String indexName, String indexId) {
        if (StringUtils.isBlank(indexName)) {
            logger.error("参数indexName不能为空");
            return null;
        }

        if (StringUtils.isBlank(indexId)) {
            logger.error("参数indexId不能为空");
            return null;
        }

        try {
            GetResponse response = client.prepareGet(indexName + "_index", indexName + "_type", indexId).execute().actionGet();
            return response.getSourceAsMap();
        } catch (Exception e) {
            logger.error("查询ES索引[indexName:" + indexName + ", indexId:" + indexId + "]出错：", e);
        }

        return null;
    }

    @Override
    public JSONObject getJsonObj(String indexName, String indexId) {
        String jsonStr = getJsonString(indexName, indexId);

        if (StringUtils.isNotBlank(jsonStr)) {
            try {
                return JSON.parseObject(jsonStr);
            } catch (Exception e) {
                logger.error("查询ES索引[indexName:" + indexName + ", indexId:" + indexId + "]出错：", e);
            }
        }

        return null;
    }

    @Override
    public <T> T getBean(String indexName, String indexId, Class<T> clazz) {
        String jsonStr = getJsonString(indexName, indexId);

        if (StringUtils.isNotBlank(jsonStr)) {
            try {
                return JSON.parseObject(jsonStr, clazz);
            } catch (Exception e) {
                logger.error("查询ES索引[indexName:" + indexName + ", indexId:" + indexId + "]出错：", e);
            }
        }

        return null;
    }

    @Override
    public List<String> search(String[] indexNames, String[] fieldNames, String searchContent) {
        List<String> dataList = new ArrayList<String>();

        if (indexNames == null || indexNames.length <= 0) {
            logger.error("参数indexName不能为空");
            return dataList;
        }

        if (fieldNames == null || fieldNames.length <= 0) {
            logger.error("参数fieldNameList不能为空");
            return dataList;
        }

        if (StringUtils.isBlank(searchContent)) {
            logger.error("参数searchContent不能为空");
            return dataList;
        }

        try {
            String[] indexs = new String[indexNames.length];
            String[] types = new String[indexNames.length];

            for (int i = 0; i < indexNames.length; i++) {
                indexs[i] = indexNames[i] + "_index";
                types[i] = indexNames[i] + "_type";
            }

            QueryBuilder qb = QueryBuilders.multiMatchQuery(searchContent, fieldNames);

            SearchResponse response = client.prepareSearch(indexs).setTypes(types).setQuery(qb).execute().actionGet();
            SearchHits hits = response.getHits();

            if (hits.totalHits() > 0) {
                for (SearchHit hit : hits) {
                    String jsonStr = hit.getSourceAsString();
                    dataList.add(jsonStr);
                }
            }
        } catch (Exception e) {
            logger.error("搜索ES索引[indexNames:" + indexNames + ", fieldNames:" + fieldNames + ", searchContent:" + searchContent + "]出错：", e);
        }

        return dataList;
    }
}
