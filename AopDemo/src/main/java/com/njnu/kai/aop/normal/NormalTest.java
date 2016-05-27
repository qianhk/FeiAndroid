package com.njnu.kai.aop.normal;

import com.njnu.kai.aop.entify.Business;

public class NormalTest {

    public static void main(String[] args) {
        System.out.println("\nwill normal test  ---");
        final Business business = new Business();
        business.doSomeThing();
        business.doSomeThing2();
        business.doSomeThing3();
        System.out.println("--- normal test end\n");
    }
}
