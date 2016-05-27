package com.njnu.kai.aop.example2;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class AuthFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        if ("query".equalsIgnoreCase(method.getName())) {
            return 1;
        }
        return 0;
    }
}
