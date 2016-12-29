package com.liaozl.junit.service.impl;

import com.liaozl.junit.service.MyService;
import org.springframework.stereotype.Service;

/**
 * @author liaozuliang
 * @date 2016-12-29
 */
@Service
public class MyServiceImpl implements MyService {

    @Override
    public boolean test(String msg) {
        System.out.println("the msg is: " + msg);
        return true;
    }
}
