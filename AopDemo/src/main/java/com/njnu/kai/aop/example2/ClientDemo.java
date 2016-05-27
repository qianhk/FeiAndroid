package com.njnu.kai.aop.example2;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-5-26
 */
public class ClientDemo {

    public static void main(String[] args) {
        TableDAO tableDao = TableDAOFactory.getInstance();
        doMethod(tableDao);

        haveAuth();
        haveNoAuth();

        haveAuthByFilter();
    }

    public static void doMethod(TableDAO dao) {
        dao.create();
        dao.query();
        dao.update();
        dao.delete();
    }

    public static void haveAuth() {
        TableDAO tDao = TableDAOFactory.getAuthInstance(new AuthMethodInterceptor("张三"));
        doMethod(tDao);
    }

    public static void haveNoAuth() {
        TableDAO tDao = TableDAOFactory.getAuthInstance(new AuthMethodInterceptor("李四"));
        doMethod(tDao);
    }

    public static void haveAuthByFilter() {
        TableDAO tDao = TableDAOFactory.getAuthInstanceByFilter(new AuthMethodInterceptor("张三"));
        doMethod(tDao);

        tDao = TableDAOFactory.getAuthInstanceByFilter(new AuthMethodInterceptor("李四"));
        doMethod(tDao);
    }
}
