package com.liaozl.aop.service.impl;

import org.springframework.stereotype.Service;

/**
 * 类代理
 * @author liaozuliang
 * @date 2016-11-18
 */
public class ClassProxyService {

    public String testProxy(String str) {
        str = "hello " + str + ", this is class proxy";
        System.out.println(str);
        return str;
    }

    public String testProxy2(String str) {
        str = "hello " + str + ", this is class proxy2";
        System.out.println(str);
        return str;
    }

}
