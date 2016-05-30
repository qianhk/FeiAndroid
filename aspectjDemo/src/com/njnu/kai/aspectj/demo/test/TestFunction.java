package com.njnu.kai.aspectj.demo.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/5/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestFunction {
    String value();
}
