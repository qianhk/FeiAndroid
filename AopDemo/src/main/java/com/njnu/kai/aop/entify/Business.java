package com.njnu.kai.aop.entify;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-25
 */
public class Business implements IBusiness, IBusiness2 {

    @Override
    public boolean doSomeThing() {
        System.out.println("do some thing");
        return true;
    }

    @Override
    public void doSomeThing2() {
        System.out.println("do some thing 2");
    }

    public void doSomeThing3() {
        System.out.println("do some thing 3");
    }
}
