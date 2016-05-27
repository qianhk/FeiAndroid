package com.njnu.kai.aop.dynamic.bytecode;

import com.njnu.kai.aop.entify.Business;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class ByteCodeTest {

    public static void main(String[] args) {

        System.out.println("\nwill ByteCodeTest  ---");

        //创建一个织入器
        Enhancer enhancer = new Enhancer();
        //设置父类
        enhancer.setSuperclass(Business.class);
        //设置需要织入的逻辑
        enhancer.setCallback(new LogMethodIntercept());
        //使用织入器创建子类
        Business newBusiness = (Business) enhancer.create();
        newBusiness.doSomeThing();
        newBusiness.doSomeThing2();
        newBusiness.doSomeThing3();

        System.out.println("--- ByteCodeTest end\n");
    }


}
