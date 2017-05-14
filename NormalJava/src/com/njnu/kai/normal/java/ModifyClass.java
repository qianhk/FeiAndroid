package com.njnu.kai.normal.java;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * //http://www.jianshu.com/p/ee837e9a987b
 * <p>
 * 如果只是要修改某个字符串，可以使用JBE（Java Bytecode Editor）。 JBE是一个Java字节码编辑工具
 *
 * @author hongkai.qian
 * @version 1.0.0
 * @since 2017/5/14
 */
public class ModifyClass {

    public static void main(String[] args) {
        ClassPool pool = ClassPool.getDefault();
        try {
            //取得需要反编译的jar文件，设定路径
            pool.insertClassPath("G://abc//XXSDK_Android.jar");

            //取得需要反编译修改的文件，注意是完整路径（注意：因为代码在内部类中，所以我读取的是WebViewActivity$2文件
//            CtClass cc1 = pool.get("com.objectplanet.chart.a");
            CtClass cc1 = pool.get("com.xx.webview.WebViewActivity$2");

            //取得需要修改的方法
            CtMethod method = cc1.getDeclaredMethod("shouldUrlLoading");

            //插入修改项，我们让他直接返回(注意：根据方法的具体返回值返回，因为这个方法返回值是void，所以直接return；)
            method.instrument(
                    new ExprEditor() {
                        public void edit(MethodCall m)
                                throws CannotCompileException {

                            System.out.println(m.getClassName() + ", " + m.getMethodName());
                            // 在这里搜索class中符合条件的逻辑代码，并替换成我想改的
                            if (m.getClassName().equals("java.lang.String")
                                    && m.getMethodName().equals("startsWith")) {
                                m.replace(
                                        "$_ = $proceed(\"http\") ;");
                            }
                        }
                    });

            //写入保存
            cc1.writeFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
