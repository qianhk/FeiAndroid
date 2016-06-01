package com.njnu.kai.aspectj.demo.aspect;

import com.njnu.kai.aspectj.demo.different.*;

public aspect MoveAspect {
	
//	pointcut MoveAspect() : execution(* move(..)); //拦截3个move方法
//	pointcut MoveAspect() : execution(* move(..)) && !within(TestMove); //拦截除TestMove类的其他move方法
//	pointcut MoveAspect() : execution(* move(..)) && withincode(void move()); //未试验出用法
	

	//this 用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配；	  它干啥
//    this()是指： 我们pointcut 所选取的Join point 的调用的所有者，就是说：方法是在那个类中被调用的。
//	pointcut MoveAspect() : call(* move(..)) && this(Animal); //用call拦截不到方法
//	pointcut MoveAspect() : execution(* move(..)) && this(Animal); //用execution拦截2个方法 Entering Move Bird.java:6  Entering Move Snake.java:6
//	pointcut MoveAspect() : call(* move(..)) && this(TestMove); //拦截2个方法(testmove里调用了2个派生类的move方法) Entering Move TestMove.java:9 Entering Move TestMove.java:9
//	pointcut MoveAspect() : execution(* move(..)) && this(TestMove); //拦截1个方法(testmove里调用了自己的move方法) Entering Move TestMove.java:7
	
	//target：用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口也类型匹配；  它被干啥
//    target()是指：我们pointcut 所选取的Join point 的所有者，直白点说就是： 指明拦截的方法属于那个类。
	pointcut MoveAspect() : call(* move(..)) && target(Bird); //拦截2个move方法 target获取继承关系 Entering Move TestMove.java:9  Entering Move TestMove.java:9
//	pointcut MoveAspect() : execution(* move(..)) && target(Animal); //拦截2个move方法 target获取继承关系  Entering Move Bird.java:6 Entering Move Snake.java:6
//	pointcut MoveAspect() : call(* move(..)) && target(TestMove); //拦截1个方法(HelloWorld.java:26里调用了testmove里的move方法) Entering Move HelloWorld.java:26
//	pointcut MoveAspect() : execution(* move(..)) && target(TestMove); //拦截1个方法(testmove里调用了自己的move方法)  Entering Move TestMove.java:7
	
//	pointcut MoveAspect() :   execution(* move(..)) && within(Animal); //无拦截： Animal是接口，无法直接调用move().方法
//	pointcut MoveAspect() :   execution(* move(..)) && within(Snake); //Entering Move Snake.java:6
//	pointcut MoveAspect() :   execution(* move(..)) && within(Bird); //Entering Move Bird.java:6
//	pointcut MoveAspect() :   call(* move(..)) && target(Animal) && this(TestMove);  
	
	before() : MoveAspect() {
		System.out.println("MoveAspect Entering Move " + thisJoinPoint.getSourceLocation());
	}
	
	pointcut MoveAspectWithParameter(Animal a, TestMove t) :   call(* move(..)) && target(a) && this(t);  
    
    
//    before(Animal a,TestMove t) : MoveAspectWithParameter(a,t){  
//        System.out.println("Entering " + thisJoinPoint.getSourceLocation() + "  target:" + a + "  this:" + t);  
//    }
    
}