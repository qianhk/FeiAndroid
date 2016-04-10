package com.njnu.kai.normal.java;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/4/8
 */
public class SplitPoint {

    public static void main(String[] args) {
        String version = "3.0.2.2016040710";
        String[] split = version.split("\\.");
        for (String s : split) {
            System.out.println(s);
        }
        int pos = version.lastIndexOf('.');
        String versionWithoutDate = version.substring(0, pos);
        System.out.println("versionWithoutDate=" + versionWithoutDate);

        System.out.println("\n\n\n");

        String version2 = "3.0.2.2016040710";
        String versionWithoutDate2 = version2;
        if (version2 != null) {
            int pos2 = version2.lastIndexOf('.');
            if (pos2 > 0) {
                versionWithoutDate2 = version2.substring(0, pos2);
            }
        }
        System.out.println("versionWithoutDate2=" + versionWithoutDate2);

    }
}
