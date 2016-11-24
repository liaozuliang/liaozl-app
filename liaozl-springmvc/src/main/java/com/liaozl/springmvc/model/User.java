package com.liaozl.springmvc.model;

import java.util.Date;
import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-28
 */
public class User extends Entity<Long> {

    private String name;
    private Integer age;
    private List<String> address;
    private Date createTime;

    public User() {
    }

    public User(Long id, String name, Integer age, List<String> address, Date createTime) {
        this.setId(id);
        this.name = name;
        this.age = age;
        this.address = address;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", createTime=" + createTime +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
