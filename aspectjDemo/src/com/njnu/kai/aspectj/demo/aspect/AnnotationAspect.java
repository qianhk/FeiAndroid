package com.njnu.kai.aspectj.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.njnu.kai.aspectj.demo.different.Snake;
import com.njnu.kai.aspectj.demo.different.TestMove;

@Aspect
public class AnnotationAspect {

	@Before("execution(* move(..)) && !within(com.njnu.kai.aspectj.demo.different.TestMove)") //包名要写全
	public void printLog(JoinPoint joinPoint) {
		System.out.println("Entering " + joinPoint.getSourceLocation());
	}
	
	@Before("call(* move(..)) && target(com.njnu.kai.aspectj.demo.different.Snake) && this(_this) && target(_target)")
	public void printLog2(JoinPoint joinPoint, Object _this, Object _target) {
		System.out.println(String.format("Entering2 point=%s this=%s target=%s", joinPoint.getSourceLocation(), _this, _target));
	}
	
	@Before("call(* move(..)) && target(com.njnu.kai.aspectj.demo.different.Snake) && this(testMove) && target(snake)")
	public void printLog3(JoinPoint joinPoint, TestMove testMove, Snake snake) {
		System.out.println(String.format("Entering3 point=%s this=%s target=%s", joinPoint.getSourceLocation(), testMove, snake));
	}
}
