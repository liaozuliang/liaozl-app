package com.liaozl.hessian.service;

import com.liaozl.hessian.model.User;

import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-10
 */
public interface UserService {

    public List<User> getUser(int id, String name);

}
