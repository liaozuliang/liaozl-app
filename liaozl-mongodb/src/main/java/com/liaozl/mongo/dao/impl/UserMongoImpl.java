package com.liaozl.mongo.dao.impl;

import com.liaozl.mongo.dao.UserMongo;
import com.liaozl.mongo.enums.CollectionEnum;
import com.liaozl.mongo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @author liaozuliang
 * @date 2017-09-09
 */
@Service("userMongo")
public class UserMongoImpl implements UserMongo {

    private static final Logger logger = LoggerFactory.getLogger(UserMongoImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User selectByUserId(int userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "birthday")));
        return mongoTemplate.findOne(query, User.class, CollectionEnum.USER.getCollectionName());
    }

    @Override
    public void add(User user) {
        mongoTemplate.insert(user, CollectionEnum.USER.getCollectionName());
    }

    @Override
    public void update(User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getUserId()));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "birthday")));

        Update update = new Update();
        update.set("name", user.getName());
        update.set("sex", user.getSex());
        update.set("address", user.getAddress());
        update.set("birthday", user.getBirthday());

        mongoTemplate.updateFirst(query, update, CollectionEnum.USER.getCollectionName());
    }

    @Override
    public void delete(int userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "birthday")));

        mongoTemplate.remove(query, CollectionEnum.USER.getCollectionName());
    }
}
