package com.njnu.kai.normal.java;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class PhantomReferenceTest {
    private ReferenceQueue<Data> mQueue = new ReferenceQueue<Data>();
    private boolean mIsRun = true;

    public void test() {

        final ArrayList<PhantomReference<Data>> blocks = new ArrayList<PhantomReference<Data>>();
        ArrayList<Data> dataArray = new ArrayList<Data>();
        System.out.println("occupied mem 1 = " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        for (int i = 0; i < 10; i++) {
            Data data = new Data(i);
            PhantomReference<Data> ref = new PhantomReference<Data>(data, mQueue);
            blocks.add(ref);
            dataArray.add(data);
        }
        System.out.println("occupied mem 2 = " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        System.out.println("remove strong ref to object 0 and 1");
        dataArray.remove(0);
        dataArray.remove(0);
        new Thread() {
            public void run() {
                System.out.println("thread started");
                while (mIsRun) {
                    Reference<?> ref = null;
                    try {
                        ref = mQueue.remove(1000);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ref != null) {
                        System.out.println(">> removed ref = " + ref);
                        blocks.remove(ref);
                        Field f = null;
                        try {
                            f = Reference.class.getDeclaredField("referent");
                            f.setAccessible(true);
                            System.out.println("I see dead objects! --> " + f.get(ref));
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("thread ended");
            }
        }.start();
        System.out.println("objects created");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("occupied mem 3 = " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        System.out.println("run gc() to make object enqueued");
        System.gc();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("collect memory");
        System.runFinalization();
        System.gc();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("occupied mem 4 = " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        mIsRun = false;
    }

    private static class Data {
        int id;
        byte[] memoryBlock = new byte[1048576];

        public Data(int id) {
            this.id = id;
            System.out.println("Data created: " + this);
        }

        @Override
        public String toString() {
            return "{id=" + id + " " + super.toString() + "}";
        }
    }

    public static void main(String[] args) {
        PhantomReferenceTest phantomReferenceTest = new PhantomReferenceTest();
        phantomReferenceTest.test();
    }
}