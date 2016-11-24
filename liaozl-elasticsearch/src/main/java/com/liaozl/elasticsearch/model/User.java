package com.liaozl.elasticsearch.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-10-25
 */
public class User implements Serializable {

    private Integer id;
    private String name;
    private Long age;
    private Date createTime;
    private List<String> telephone;

    public User() {

    }

    public User(Integer id, String name, Long age, Date createTime, List<String> telephone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.createTime = createTime;
        this.telephone = telephone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getTelephone() {
        return telephone;
    }

    public void setTelephone(List<String> telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", createTime=" + createTime +
                ", telephone=" + telephone +
                '}';
    }
}
