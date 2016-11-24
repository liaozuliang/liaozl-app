package com.liaozl.dao.module;

import java.io.Serializable;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
public class Entity<T> implements Serializable {

    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
