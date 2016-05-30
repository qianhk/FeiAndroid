package com.njnu.kai.aspectj.demo.aspect;

import com.njnu.kai.aspectj.demo.test.*;

public aspect HelloMainAspect {
	
//	pointcut HelloWorld1PointCut() : execution(* main(..)) && !within(HelloWorld);
//	
//	before() : HelloWorld1PointCut() {
//		System.out.println("int main point aspect before");
//	}
}