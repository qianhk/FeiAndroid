package com.njnu.kai.aop.dynamic.proxy;

import com.njnu.kai.aop.entify.Business;
import com.njnu.kai.aop.entify.IBusiness;
import com.njnu.kai.aop.entify.IBusiness2;

import java.lang.reflect.Proxy;

public class DynamicProxyTest {

    public static void main(String[] args) {

        System.out.println("\nwill dynamic proxy test  ---");

        //需要代理的接口，被代理类实现的多个接口都必须在这里定义
        Class[] proxyInterface = new Class[]{IBusiness.class, IBusiness2.class};

        //构建AOP的Advice，这里需要传入业务类的实例
        LogInvocationHandler handler = new LogInvocationHandler(new Business());

        //生成代理类的字节码加载器
        ClassLoader classLoader = DynamicProxyTest.class.getClassLoader();

        //织入器，织入代码并生成代理类
        final Object proxyInstance = Proxy.newProxyInstance(classLoader, proxyInterface, handler);

        ((IBusiness) proxyInstance).doSomeThing();
        ((IBusiness2) proxyInstance).doSomeThing2();

        System.out.println("--- dynamic proxy test end\n");
    }
}
