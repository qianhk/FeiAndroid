package com.njnu.kai.java.sort;

public class CountingSort implements IArraySort {
    @Override
    public String name() {
        return "计数排序";
    }

    @Override
    public void sort(int[] array) {
        int[] minMaxValue = getMinMaxValue(array);
        countingSort(array, minMaxValue[0], minMaxValue[1]);
    }

    private void countingSort(int[] arr, int minValue, int maxValue) {
        int bucketLen = maxValue - minValue + 1;
        int[] bucket = new int[bucketLen];

        for (int value : arr) {
            bucket[value - minValue]++;
        }

        int sortedIndex = 0;
        for (int j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                arr[sortedIndex++] = j + minValue;
                bucket[j]--;
            }
        }
    }

    private int[] getMinMaxValue(int[] arr) {
        int maxValue = arr[0];
        int minValue = arr[0];
        for (int value : arr) {
            if (value < minValue) {
                minValue = value;
            }
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return new int[]{minValue, maxValue};
    }
}
