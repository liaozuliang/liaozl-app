package com.liaozl.aop.service.impl;

import com.liaozl.aop.service.BankService;
import org.springframework.stereotype.Service;

/**
 *
 * @author liaozuliang
 * @date 2016-11-17
 */
@Service("bankService")
public class BCMBankServiceImpl implements BankService {

    public boolean transfer(String form, String to, double account) {
        System.out.println(form+"向"+to+"交行账户转账"+account+"元");
        if(account<100) {
            throw new IllegalArgumentException("最低转账金额不能低于100元");
        }
        return false;
    }

}