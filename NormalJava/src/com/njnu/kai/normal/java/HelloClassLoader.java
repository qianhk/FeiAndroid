package com.njnu.kai.normal.java;

/**
 * Created by kai
 * since 2017/5/12
 */

import sun.misc.Launcher;

import java.net.URL;

public class HelloClassLoader {

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

    }


}

