package com.liaozl.aop.service.impl;

import com.liaozl.aop.service.InterfaceProxyService;
import org.springframework.stereotype.Service;

/**
 * 接口代理实现类
 * @author liaozuliang
 * @date 2016-11-18
 */
@Service("interfaceProxyService")
public class InterfaceProxyServiceImpl implements InterfaceProxyService {

    @Override
    public String testProxy(String str) {
        str = "hello " + str + ", this is interface proxy";
        System.out.println(str);
        return str;
    }

    @Override
    public String testProxy2(String str) {
        str = "hello " + str + ", this is interface proxy2";
        System.out.println(str);
        return str;
    }
}
