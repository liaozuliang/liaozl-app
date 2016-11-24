package com.liaozl.aop.proxy;

import com.liaozl.aop.service.InterfaceProxyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 静态代理, 基于接口代理
 * @author liaozuliang
 * @date 2016-11-18
 */
@Service("staticProxy")
public class StaticProxy implements InterfaceProxyService {

    @Resource
    private InterfaceProxyService interfaceProxyService;

    @Override
    public String testProxy(String str) {
        System.out.println("静态代理，前");
        String result = interfaceProxyService.testProxy(str);
        System.out.println("静态代理，后");

        return result;
    }

    @Override
    public String testProxy2(String str) {
        System.out.println("静态代理，前2");
        String result = interfaceProxyService.testProxy2(str);
        System.out.println("静态代理，后2");

        return result;
    }
}
