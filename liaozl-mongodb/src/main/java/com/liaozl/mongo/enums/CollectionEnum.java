package com.liaozl.mongo.enums;


public enum CollectionEnum {

    USER(TableName.T_USER, "用户信息");

    private String collectionName;

    private String desc;

    private CollectionEnum(String collectionName, String desc) {
        this.collectionName = collectionName;
        this.desc = desc;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getDesc() {
        return desc;
    }

}
