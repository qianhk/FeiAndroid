package com.njnu.kai.normal.java;

/**
 * Created by kai
 * since 2017/5/12
 */

import sun.misc.Launcher;

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
            while(classLoader1.getParent() != null) {
                classLoader1 = classLoader1.getParent();
                System.out.println("parent1: " + classLoader1.getClass().getName());  //sun.misc.Launcher$ExtClassLoader
            }
        } catch (ClassNotFoundException e) {
            System.err.println("1 error: " + e.toString());
        }

        ClassLoader classLoader2 = Class.forName("com.njnu.kai.normal.java.Person2").getClassLoader();
        System.out.println("pojo2: " + classLoader2.getClass().getName());  //sun.misc.Launcher$AppClassLoader
        while(classLoader2.getParent() != null) {
            classLoader2 = classLoader2.getParent();
            System.out.println("parent2: " + classLoader2.getClass().getName());  //sun.misc.Launcher$ExtClassLoader
        }



        /*
javac com.njnu.kai.normal.java.Person2
jar cvf KaiPerson.jar  ./src/com/njnu/kai/normal/java/Person.class
mv KaiPerson.jar $JAVA_HOME/jre/lib/ext/
 */

        new HelloClassLoaderTest().onTest03();

    }

    public void onTest03() {
        try {
            KaiClassLoader kaiClassLoader = new KaiClassLoader();
            Class<?> person2 = kaiClassLoader.loadClass("com.njnu.kai.compiled.Person");
            if (person2 != null) {
                ClassLoader classLoader = person2.getClassLoader();
                System.out.println("compiled person2 load success, loader is " + classLoader.getClass().getName());
                while (classLoader.getParent() != null) {
                    classLoader = classLoader.getParent();
                    System.out.println("compiled parent: " + classLoader.getClass().getName());  //java.lang.BootClassLoader
                }
            } else {
                System.out.println("compiledperson2 load failed");
            }
        } catch (Exception e) {
            //UnsupportedOperationException can't load this type of class file
            //http://blog.csdn.net/jiangwei0910410003/article/details/17679823
            System.err.println(e);
        }
    }


}

