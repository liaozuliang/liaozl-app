package com.liaozl.mongo.dao;

import com.liaozl.mongo.model.User;

/**
 * @author liaozuliang
 * @date 2017-09-09
 */
public interface UserMongo {

    User selectByUserId(int userId);

    void add(User user);

    void update(User user);

    void delete(int userId);
}
