package com.liaozl.aop.service.impl;

import com.liaozl.aop.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author liaozuliang
 * @date 2016-11-17
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Override
    public String getUserName(String id) {
        return id + ", welcome to spring aop";
    }

    @Override
    public int getUserAge(String id) {
        return 25 / 0;
    }
}
