package com.njnu.kai.java.sort;

public class InsertionSort implements IArraySort {
    @Override
    public String name() {
        return "插入排序";
    }

    @Override
    public void sort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int tmp = array[i];

            int j = i;
            while (j > 0 && tmp < array[j - 1]) {
                array[j] = array[j - 1];
                --j;
            }

            if (j != i) {
                array[j] = tmp;
            }

        }
    }
}
