package com.liaozl.dao.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2017-02-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class TransactionTestServiceTest {

    @Resource
    private TransactionTestService transactionTestService;

    @Test
    public void testTransaction() {
        transactionTestService.testTransaction(1, 2);
    }
}
