package com.njnu.kai.aop.dynamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-25
 */
public class LogInvocationHandler implements InvocationHandler {

    private Object mTarget; //目标对象

    public LogInvocationHandler(Object target) {
        mTarget = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("doSomeThing2")) {
            System.out.println("before invoke 记录日志");
        }
        Object result = method.invoke(mTarget, args);
        //执行织入的日志，你可以控制哪些方法执行切入逻辑
        if (method.getName().equals("doSomeThing2")) {
            System.out.println("after invoke 记录日志");
        }
        return result;
    }
}
