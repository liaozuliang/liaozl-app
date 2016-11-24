package com.liaozl.aop.proxy;

import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理, 基于接口代理
 * @author liaozuliang
 * @date 2016-11-18
 */
@Service("dynamicProxy")
public class DynamicProxy implements InvocationHandler {

    // 目标对象，也就是我们主要的业务，主要目的要做什么事
    private Object delegate;

    /**
     * 和你额外需要做得事情，进行绑定，返回一个全新的对象（写法，基本上固定的）
     * @param delegate
     * @return
     */
    public Object getProxyInstance(Object delegate) {
        this.delegate = delegate;
        return Proxy.newProxyInstance(this.delegate.getClass().getClassLoader(),
                this.delegate.getClass().getInterfaces(), this);
    }

    /**
     * 你刚才需要执行的方法，都需要通过该方法进行动态调用
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        System.out.println("动态代理，前");

        // 通过反射，执行目标方法，也就是你的主要目的
        Object obj = method.invoke(this.delegate, args);

        System.out.println("动态代理，后");

        return obj;
    }

}
