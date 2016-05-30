package com.njnu.kai.aspectj.demo.aspect;

import com.njnu.kai.aspectj.demo.test.*;

public aspect HelloAspect {
	
	pointcut HelloWorld2PointCut(int i) : call(* com.njnu.kai.aspectj.demo.test.HelloWorld.printI(int)) && args(i);
	
//	before(int x) : HelloWorld2PointCut(x) {
//		x += 2;
//		System.out.println(String.format("Hello world, Aspect before x=%d entering:%s", x, thisJoinPoint.getSourceLocation()));
//	}
	
//	after(int x) : HelloWorld2PointCut(x) {
//		System.out.println("Hello world, Aspect after x=" + x);
//	}
	
//	void around(int x) : HelloWorld2PointCut(x) {
//		System.out.println("Enter:" + thisJoinPoint);
//		proceed(x * 3);
//	}
	
	
	pointcut barPoint() : execution(* bar(..));
	pointcut fooPoint() : execution(* foo(..));
	pointcut barCfow() : cflow(barPoint()) && !within(HelloAspect);
	pointcut fooInBar() : barCfow() && fooPoint();
	
//	before() : barCfow() {
//		System.out.println("Enter:" + thisJoinPoint);
//	}
	
//	before() : fooInBar() {
//		System.out.println("Enter:" + thisJoinPoint);
//	}
	
//	pointcut testFunctionPoint() : execution(@TestFunction * foo());
	pointcut testFunctionPoint() : execution(@TestFunction * *(..));
	
	after() : testFunctionPoint() {
		System.out.println("Leave:" + thisJoinPoint);
	}
}