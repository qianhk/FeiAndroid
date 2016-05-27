package com.njnu.kai.aop.dynamic.classloader;

import com.njnu.kai.aop.entify.Business;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class ClassLoaderTest0 {

    public static void main(String[] args) throws Throwable {

        System.out.println("\nwill ClassLoaderTest0  ---");

//        final Business business3 = new Business(); //会导致下面LinkageError, 因为一个类加载两次

        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.njnu.kai.aop.entify.Business");
        CtMethod m = cc.getDeclaredMethod("doSomeThing3");
        m.insertBefore("{ System.out.println(\"before invoke 记录日志0\"); }");
        m.insertAfter("{ System.out.println(\"after invoke 记录日志0\"); }");
        final Business business = (Business) cc.toClass().newInstance();
        business.doSomeThing();
        business.doSomeThing2();
        business.doSomeThing3();

        System.out.println(" \n test business2");
        final Business business2 = new Business(); //执行了cc.toClass()后可直接new,否则先加载了原来的Business类则不起作用

        business2.doSomeThing();
        business2.doSomeThing2();
        business2.doSomeThing3();

        System.out.println("--- ClassLoaderTest0 end\n");
    }
}
