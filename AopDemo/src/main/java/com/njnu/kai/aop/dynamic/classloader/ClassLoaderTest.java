package com.njnu.kai.aop.dynamic.classloader;

import com.njnu.kai.aop.entify.Business;
import javassist.ClassPool;
import javassist.Loader;
import javassist.Translator;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Throwable {

        Translator t = new LogTranslator();
        ClassPool pool = ClassPool.getDefault();
        Loader cl = new Loader();
        cl.addTranslator(pool, t);
        cl.run("com.njnu.kai.aop.dynamic.classloader.ClassLoaderTest$OtherMainClass", args);


    }

    public static class OtherMainClass {

        public static void main(String[] args) throws Throwable {

            System.out.println("\nwill ClassLoaderTest  ---");
            final Business business = new Business();
            business.doSomeThing();
            business.doSomeThing2();
            business.doSomeThing3();
            System.out.println("--- ClassLoaderTest end\n");

        }
    }
}
