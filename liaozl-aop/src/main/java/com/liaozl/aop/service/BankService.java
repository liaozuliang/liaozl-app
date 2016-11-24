package com.liaozl.aop.service;

/**
 * @author liaozuliang
 * @date 2016-11-17
 */
public interface BankService {

    /**
     * 模拟的银行转账
     * @param from 出账人
     * @param to 入账人
     * @param account 转账金额
     * @return
     */
    public boolean transfer(String form, String to, double account);
}
