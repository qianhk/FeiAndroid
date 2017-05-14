package com.njnu.kai.normal.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 2017/5/13
 */
public class KaiClassLoader extends ClassLoader {

    public KaiClassLoader() {
        super();
    }

    public KaiClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getClassData(new File(String.format("class/%s.class", name.replace('.', File.separatorChar))));
        if (classData == null) {
            super.findClass(name); // 未找到，抛异常
        } else {
            return defineClass(name, classData, 0, classData.length);
        }
        return null;
    }

    private byte[] getClassData(File file) {
        System.out.println("classPath = " + file.getAbsolutePath());
        try {
            FileInputStream ins = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead;
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
