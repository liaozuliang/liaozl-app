package com.liaozl.aop.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * CGLib代理，基于类代理
 *
 * @author liaozuliang
 * @date 2016-11-18
 */
@Service("cglibProxy")
public class CglibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxyInstance(Class clazz) {
        //设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);

        //通过字节码技术动态创建子类实例
        return enhancer.create();
    }

    //实现MethodInterceptor接口方法
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("CGLib代理，前");

        //通过代理类调用父类中的方法
        Object result = proxy.invokeSuper(obj, args);

        System.out.println("CGLib代理，后");
        return result;
    }
}
