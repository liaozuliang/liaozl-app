package com.liaozl.netty.demo.protocol.http.httpxml.pojo;

/**
 * 邮件方式
 * @author liaozuliang
 * @date 2016-12-13
 */
public enum Shipping {

    STANDARD_MAIL("普通邮寄"),
    PRIORITY_MAIL("宅急送"),
    INTERNATIONAL_MAIL("国际邮递"),
    DOMESTIC_EXPRESS("国内快递"),
    INTERNATIONAL_EXPRESS("国际快递");

    private String desc;

    Shipping(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
