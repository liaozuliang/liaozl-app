package com.liaozl.redis.model.enums;

/**
 * @author liaozuliang
 * @date 2016-09-09
 */
public enum CacheDataTypeEnum {

    TEACHER("老师"),
    STUDENT("学生");

    private String desc;

    private CacheDataTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
