package com.liaozl.mongo.dao;

import com.liaozl.mongo.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


/**
 * @author liaozuliang
 * @date 2017-09-09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class UserMongoTest {

    @Autowired
    private UserMongo userMongo;

    @Test
    public void test() {
        int userId = 1;

        User user = userMongo.selectByUserId(userId);
        System.out.println("原始数据：" + user);

        if (user == null) {
            user = new User();
        }

        if (user.getUserId() == null) {
            user.setUserId(userId);
            user.setName("测试一下新增1111");
            user.setSex(1);
            user.setAddress("hahahah");
            user.setBirthday(new Date());
            userMongo.add(user);

            user = userMongo.selectByUserId(userId);
            System.out.println("新增后：" + user);

            user.setName("测试一下修改2222");
            user.setSex(2);
            user.setAddress("hahahah");
            user.setBirthday(new Date());
            userMongo.update(user);

            user = userMongo.selectByUserId(userId);
            System.out.println("修改后：" + user);
        } else {
            user.setName("测试一下修改2222");
            user.setSex(2);
            user.setAddress("hahahah");
            user.setBirthday(new Date());
            userMongo.update(user);

            user = userMongo.selectByUserId(userId);
            System.out.println("修改后：" + user);
        }

        userMongo.delete(userId);

        user = userMongo.selectByUserId(userId);
        System.out.println("删除后：" + user);

    }

}
