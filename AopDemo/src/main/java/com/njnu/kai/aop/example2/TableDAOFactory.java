package com.njnu.kai.aop.example2;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class TableDAOFactory {

    private static TableDAO sDao = new TableDAO();

    public static TableDAO getInstance() {
        return sDao;
    }

    public static TableDAO getAuthInstance(AuthMethodInterceptor authProxy) {
        Enhancer en = new Enhancer();
        //进行代理
        en.setSuperclass(TableDAO.class);
        en.setCallback(authProxy);
        //生成代理实例
        return (TableDAO) en.create();
    }

    public static TableDAO getAuthInstanceByFilter(AuthMethodInterceptor authProxy) {
        Enhancer en = new Enhancer();
        en.setSuperclass(TableDAO.class);
        en.setCallbacks(new Callback[]{authProxy, NoOp.INSTANCE});
//        en.setCallback(authProxy);
        en.setCallbackFilter(new AuthFilter()); //java.lang.IllegalArgumentException: Callback filter returned an index that is too large: 1
        return (TableDAO) en.create();
    }

}
