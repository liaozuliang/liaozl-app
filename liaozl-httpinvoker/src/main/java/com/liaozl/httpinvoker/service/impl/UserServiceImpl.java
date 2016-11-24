package com.liaozl.httpinvoker.service.impl;

import com.liaozl.httpinvoker.model.User;
import com.liaozl.httpinvoker.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-10
 */
public class UserServiceImpl implements UserService {

    private static final List<User> userList = new ArrayList<User>();

    static {
        userList.add(new User(1, "aa", "23234afasdfa", new Date()));
        userList.add(new User(2, "中国", "上海", new Date()));
        userList.add(new User(3, "aa中国123", "232北京asdfa", new Date()));
    }

    @Override
    public List<User> getUser(int id, String name) {
        System.out.println("this is httpinvoker server, [id:" + id + ", name:" + name + "]");
        return userList;
    }

}
