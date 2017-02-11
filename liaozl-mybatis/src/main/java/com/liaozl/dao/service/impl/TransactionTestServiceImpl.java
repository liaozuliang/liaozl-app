package com.liaozl.dao.service.impl;

import com.liaozl.dao.module.Test2;
import com.liaozl.dao.service.Test1Dao;
import com.liaozl.dao.service.Test2Dao;
import com.liaozl.dao.service.TransactionTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2017-02-11
 */
@Service
public class TransactionTestServiceImpl implements TransactionTestService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTestServiceImpl.class);

    @Resource
    private Test1Dao test1Dao;

    @Resource
    private Test2Dao test2Dao;

    /**
        TransactionDefinition接口中定义传播特性(propagation)，共有7种选项可用：
         1、PROPAGATION_REQUIRED：支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择, 默认值。
         2、PROPAGATION_SUPPORTS：支持当前事务，如果当前没有事务，就以非事务方式执行。
         3、PROPAGATION_MANDATORY：支持当前事务，如果当前没有事务，就抛出异常。
         4、PROPAGATION_REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起。
         5、PROPAGATION_NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
         6、PROPAGATION_NEVER：以非事务方式执行，如果当前存在事务，则抛出异常。
         7、PROPAGATION_NESTED：支持当前事务，如果当前事务存在，则执行一个嵌套事务，如果当前没有事务，就新建一个事务。
     */

     /**
        TransactionDefinition中定义的隔离（isolation）级别，有5种：
         1、ISOLATION_DEFAULT 默认的隔离级别
         2、ISOLATION_READ_UNCOMMITTED Connection.TRANSACTION_READ_UNCOMMITTED 指示防止发生脏读的常量；不可重复读和虚读有可能发生。
         3、ISOLATION_READ_COMMITTED Connection.TRANSACTION_READ_COMMITTED 指示可以发生脏读 (dirty read)、不可重复读和虚读 (phantom read) 的常量。
         4、ISOLATION_REPEATABLE_READ Connection.TRANSACTION_REPEATABLE_READ 指示防止发生脏读和不可重复读的常量；虚读有可能发生。
         5、ISOLATION_SERIALIZABLE Connection.TRANSACTION_SERIALIZABLE 指示防止发生脏读、不可重复读和虚读的常量。
     */


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    @Override
    public void testTransaction(int test1Id, int test2Id) {
        try {
            test1Dao.del(test1Id);

            Test2 t2 = new Test2();
            t2.setId(test2Id);

            Test2 test2 = test2Dao.getTest2(t2);
            if (test2 == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 事务回滚
            }

        } catch (Exception e) {
            logger.error("testTransaction error: ", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 事务回滚
        }
    }
}
