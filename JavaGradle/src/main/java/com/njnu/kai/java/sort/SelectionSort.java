package com.njnu.kai.java.sort;

public class SelectionSort implements IArraySort {
    @Override
    public String name() {
        return "选择排序";
    }

    @Override
    public void sort(int[] array) {

        for (int i = 0; i < array.length - 1; ++i) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }

            if (i != min) {
                array[i] ^= array[min];
                array[min] ^= array[i];
                array[i] ^= array[min];
            }

        }
    }
}
