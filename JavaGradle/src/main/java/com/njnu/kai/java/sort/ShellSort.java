package com.njnu.kai.java.sort;

public class ShellSort implements IArraySort {
    @Override
    public String name() {
        return "希尔排序";
    }

    @Override
    public void sort(int[] array) {
        int length = array.length;
        int temp;
        for (int gap = length / 2; gap >= 1; gap /= 2) {
            for (int i = gap; i < length; ++i) {
                temp = array[i];
                int j = i - gap;
                while (j >= 0 && array[j] > temp) {
                    array[j + gap] = array[j];
                    j -= gap;
                }
                array[j + gap] = temp;
            }
        }
    }
}
