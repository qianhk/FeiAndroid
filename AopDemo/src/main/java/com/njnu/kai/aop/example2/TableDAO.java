package com.njnu.kai.aop.example2;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class TableDAO {

    public void create(){
        System.out.println("create() is running !");
    }
    public void query(){
        System.out.println("query() is running !");
    }
    public void update(){
        System.out.println("update() is running !");
    }
    public void delete(){
        System.out.println("delete() is running !");
    }

}
