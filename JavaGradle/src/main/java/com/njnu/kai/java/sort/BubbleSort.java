package com.njnu.kai.java.sort;

// 冒泡排序
public class BubbleSort implements IArraySort {

    @Override
    public String name() {
        return "冒泡排序";
    }

    @Override
    public void sort(int[] array) {
        for (int i = 1; i < array.length; ++i) {
            boolean swaped = false;

            for (int j = 0; j < array.length - i; ++j) {
                if (array[j] > array[j + 1]) {
                    array[j] ^= array[j + 1];
                    array[j + 1] ^= array[j];
                    array[j] ^= array[j + 1];
                    swaped = true;
                }
            }

            if (!swaped) {
                break;
            }
        }
    }
}
