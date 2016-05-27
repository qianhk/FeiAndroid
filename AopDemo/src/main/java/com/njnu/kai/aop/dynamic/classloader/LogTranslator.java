package com.njnu.kai.aop.dynamic.classloader;

import javassist.*;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class LogTranslator implements Translator {

    @Override
    public void start(ClassPool classPool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool classPool, String classname) throws NotFoundException, CannotCompileException {
        if (!"com.njnu.kai.aop.entify.Business".equals(classname)) {
            return;
        }
        //通过获取类文件
        CtClass cc = classPool.get(classname);
        //获得指定方法名的方法
        CtMethod m = cc.getDeclaredMethod("doSomeThing");
        //在方法执行前插入代码
        m.insertBefore("{ System.out.println(\"before invoke 记录日志1\"); }");
        m.insertAfter("{ System.out.println(\"after invoke 记录日志1\"); }");
    }
}
