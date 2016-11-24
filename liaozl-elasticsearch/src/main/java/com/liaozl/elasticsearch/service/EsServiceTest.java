package com.liaozl.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.liaozl.elasticsearch.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-10-25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class EsServiceTest {

    private static final String indexName = "user";
    private static final String indexName2 = "user2";
    private static final String indexId = "es_user_2";

    @Resource
    private EsService esService;

    @Test
    public void testAdd() {
        User user = new User(1, "abc中国123", 11L, new Date(), Arrays.asList(new String[]{"0707-8500896", "13430474852"}));
        boolean res = esService.add(indexName2, user);
        System.out.println("testAdd: " + res);
    }

    @Test
    public void testAdd2() {
        User user = new User(2, "123abc中国123abc", 22L, new Date(), Arrays.asList(new String[]{"0755-8500896", "13830474852"}));
        boolean res = esService.add(indexName, indexId, user);
        System.out.println("testAdd2: " + res);
    }

    @Test
    public void testUpdate() {
        User user = new User(2, "123abc中vabcv国123abc", 222L, new Date(), Arrays.asList(new String[]{"0755-8500896", "13830474852", "中国abc123"}));
        boolean res = esService.update(indexName, indexId, user);
        System.out.println("testUpdate: " + res);
    }

    @Test
    public void testDelete() {
        boolean res = esService.delete(indexName, indexId);
        System.out.println("testDelete: " + res);
    }

    @Test
    public void testGetJsonString() {
        String res = esService.getJsonString(indexName, indexId);
        System.out.println("testGetJsonString: " + res);
    }

    @Test
    public void testGetMap() {
        Map<String, Object> res = esService.getMap(indexName, indexId);
        System.out.println("testGetMap: " + res);
    }

    @Test
    public void testGetJsonObj() {
        JSONObject res = esService.getJsonObj(indexName, indexId);
        System.out.println("testGetJsonObj: " + res);
    }

    @Test
    public void testGetBean() {
        User res = esService.getBean(indexName, indexId, User.class);
        System.out.println("testGetBean: " + res);
    }

    @Test
    public void testSearch() {
        List<String> dataList = esService.search(new String[]{indexName, indexName2}, new String[]{"name", "telephone"}, "国");
        System.out.println("testSearch: " + dataList);
    }
}
