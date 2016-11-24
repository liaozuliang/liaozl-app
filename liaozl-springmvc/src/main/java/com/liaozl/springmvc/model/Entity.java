package com.liaozl.springmvc.model;

import java.io.Serializable;

/**
 * @author liaozuliang
 * @date 2016-10-28
 */
public class Entity<T> implements Serializable {

    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
