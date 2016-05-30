package com.njnu.kai.aspectj.demo.test;

import java.util.ArrayList;
import java.util.List;

import com.njnu.kai.aspectj.demo.different.Animal;
import com.njnu.kai.aspectj.demo.different.Bird;
import com.njnu.kai.aspectj.demo.different.Snake;
import com.njnu.kai.aspectj.demo.different.TestMove;

public class HelloWorld {

	public static void main(String[] args) {
//		System.out.println("test aspectj demo in main");
//		printI(5);
//		HelloWorld2.main(args);
//
//		HelloWorld hw = new HelloWorld();
//		hw.bar();
//		hw.foo();
		
		List<Animal> list = new ArrayList<Animal>();  
        list.add(new Bird());  
        list.add(new Snake());  
        TestMove tm = new TestMove();
        tm.move(list);  
	}

	public static void printI(int i) {
		System.out.println("in other main i = " + i);
	}

	@TestFunction(value = "test foo")
	public void foo() {
		System.out.println("foo......");
	}

	@TestFunction(value = "test bar")
	public void bar() {
		foo();
		System.out.println("bar......");
	}
}
