package com.njnu.kai.normal.java;

/**
 * Created by kai
 * since 2017/5/12
 */

import sun.misc.Launcher;

import java.lang.reflect.Field;
import java.net.URL;

public class HelloClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException {
        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].toExternalForm());
        }


        try {
            ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
            System.out.println("ClassLoader.getSystemClassLoader(): " + appClassLoader.getClass().getName());  //sun.misc.Launcher$AppClassLoader
            ClassLoader classLoader1 = appClassLoader.loadClass("com.njnu.kai.normal.java.Person").getClassLoader();
            System.out.println("pojo1: " + classLoader1.getClass().getName());  //sun.misc.Launcher$AppClassLoader
            while (classLoader1.getParent() != null) {
                classLoader1 = classLoader1.getParent();
                System.out.println("parent1: " + classLoader1.getClass().getName());  //sun.misc.Launcher$ExtClassLoader
            }
        } catch (ClassNotFoundException e) {
            System.err.println("1 error: " + e.toString());
        }

        ClassLoader classLoader2 = Class.forName("com.njnu.kai.normal.java.Person2").getClassLoader();
        System.out.println("pojo2: " + classLoader2.getClass().getName());  //sun.misc.Launcher$AppClassLoader
        while (classLoader2.getParent() != null) {
            classLoader2 = classLoader2.getParent();
            System.out.println("parent2: " + classLoader2.getClass().getName());  //sun.misc.Launcher$ExtClassLoader
        }



        /*
javac com.njnu.kai.compiled.Person
jar cvf KaiPerson.jar  com/njnu/kai/compiled/Person.class
mv KaiPerson.jar $JAVA_HOME/jre/lib/ext/

//http://www.jianshu.com/p/61cfa1347894 jar打包
 */

        HelloClassLoaderTest test = new HelloClassLoaderTest();
        test.onTest03();
        test.onTest04();

    }

    public void onTest03() {
        try {
            KaiClassLoader kaiClassLoader = new KaiClassLoader();
            Class<?> person2 = kaiClassLoader.loadClass("com.njnu.kai.compiled.Person");
            if (person2 != null) {
                ClassLoader classLoader = person2.getClassLoader();
                System.out.println("onTest03 load success, loader is " + classLoader.getClass().getName());
                while (classLoader.getParent() != null) {
                    classLoader = classLoader.getParent();
                    System.out.println("onTest03 compiled parent: " + classLoader.getClass().getName());  //java.lang.BootClassLoader
                }
                Field userIdField = person2.getDeclaredField("mUserId");
                Field nameField = person2.getDeclaredField("mName");
                Object obj = person2.newInstance();
                userIdField.set(obj, 1224);
                nameField.set(obj, "KaiKai");
                System.out.println("onTest03 instance is: " + obj);
            } else {
                System.out.println("onTest03 load failed");
            }

            Class<?> aClass = kaiClassLoader.loadClass("java.lang.String");
            System.out.println("onTest03 load : " + aClass
                    + " loader=" + (aClass.getClassLoader() != null ? aClass.getClassLoader().getClass().getName() : "null"));

        } catch (Exception e) {
            //UnsupportedOperationException can't load this type of class file
            //http://blog.csdn.net/jiangwei0910410003/article/details/17679823
            System.err.println("onTest03: " + e);
        }
    }

    private void onTest04() {
        Class<?> aClass = null;
        try {
            ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
            aClass = appClassLoader.loadClass("com.njnu.kai.compiled.Person");
            if (aClass != null) {
                System.out.println("onTest04 load success");
                ClassLoader classLoader = aClass.getClassLoader();
                System.out.println("onTest04, loader is " + classLoader.getClass().getName());
                Object obj = aClass.newInstance();
                System.out.println("onTest04 obj=" + obj);
            } else {
                System.out.println("onTest04 load failed");
            }
        } catch (Exception e) {
            System.err.println("onTest04 exception: " + e);
        }
    }


}

