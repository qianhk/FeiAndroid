package com.njnu.kai.aop.aspectj;

import com.njnu.kai.aop.entify.Business;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-27
 */
public class AspectjTest {

    public static void main(String[] args) {
        System.out.println("\nwill AspectjTest  ---");
        final Business business = new Business();
        business.doSomeThing();
        business.doSomeThing2();
        business.doSomeThing3();
        System.out.println("--- AspectjTest end\n");
    }
}
