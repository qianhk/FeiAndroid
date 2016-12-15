package com.njnu.kai.normal.java;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipOperation {

    public static void main(String[] args) {
        String ret = "";
        ZipFile zipfile = null;
        String sourceDir = "/the/path/of/apk.apk";
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                System.out.println("entryName=" + entryName);
                if (entryName.contains("ydzConfig")) {
                    System.err.println("found");
                    ret = entryName;
                    BufferedReader bis = new BufferedReader(new InputStreamReader(zipfile.getInputStream(entry)));
                    String str = bis.readLine();
                    System.err.println("str1=" + str);
                    str = bis.readLine();
                    System.err.println("str2=" + str);
                    str = bis.readLine();
                    System.err.println("str3=" + str);
                    str = bis.readLine();
                    System.err.println("str4=" + str);
                    bis.close();
                    break;
                }
            }
            System.err.println("\n\n");
            Thread.sleep(100);
            ZipEntry entry = zipfile.getEntry("res/layout/*");
//            System.out.println("\nentryName*=" + entry.getName());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.err.println("\nret=" + ret);
        String[] split = ret.split("_");
        if (split.length >= 3) {
        }
    }
}
