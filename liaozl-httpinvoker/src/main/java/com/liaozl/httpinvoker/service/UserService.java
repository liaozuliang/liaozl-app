package com.liaozl.httpinvoker.service;

import com.liaozl.httpinvoker.model.User;

import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-10
 */
public interface UserService {

    public List<User> getUser(int id, String name);

}
