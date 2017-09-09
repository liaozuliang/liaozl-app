package com.liaozl.mongo.model;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * @author liaozuliang
 * @date 2017-09-09
 */
public class User {

    @Indexed
    private Integer userId;

    private String name;

    private int sex;

    private String address;

    private Date birthday;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
