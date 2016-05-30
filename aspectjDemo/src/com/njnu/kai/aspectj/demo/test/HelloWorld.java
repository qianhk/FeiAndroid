package com.njnu.kai.aspectj.demo.test;

public class HelloWorld {

	public static void main(String[] args) {
		System.out.println("test aspectj demo in main");
		printI(5);
		HelloWorld2.main(args);

		HelloWorld hw = new HelloWorld();
		hw.bar();
		hw.foo();
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
