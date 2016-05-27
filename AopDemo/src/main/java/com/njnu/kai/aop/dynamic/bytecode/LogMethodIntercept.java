package com.njnu.kai.aop.dynamic.bytecode;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class LogMethodIntercept implements MethodInterceptor {

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        if (method.getName().equals("doSomeThing3")) {
            System.out.println("before invoke 记录日志");
        }

        //执行原有逻辑，注意这里是invokeSuper
        Object rev = methodProxy.invokeSuper(target, args);

        if (method.getName().equals("doSomeThing3")) {
            System.out.println("after invoke 记录日志");
        }
        return rev;
    }
}
