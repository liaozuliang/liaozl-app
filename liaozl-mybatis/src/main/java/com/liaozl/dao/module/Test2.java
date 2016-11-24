package com.liaozl.dao.module;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
public class Test2 extends Entity<Integer> {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Test2{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
