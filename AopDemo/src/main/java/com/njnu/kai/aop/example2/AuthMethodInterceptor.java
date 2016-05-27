package com.njnu.kai.aop.example2;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class AuthMethodInterceptor implements MethodInterceptor {

    private String mName;

    public AuthMethodInterceptor(String name) {
        mName = name;
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!"张三".equals(mName)) {
            System.out.println("你没有权限！");
            return null;
        }
        return methodProxy.invokeSuper(target, args);
    }

}
