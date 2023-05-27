package com.njnu.kai.java.sort;

import java.util.Arrays;

public class BucketSort implements IArraySort {

    private final IArraySort innerSort;
    private final int bucketSize;

    public BucketSort(IArraySort arraySort, int bucketSize) {
        this.innerSort = arraySort;
        this.bucketSize = bucketSize;
    }

    @Override
    public String name() {
        return String.format("桶排序 bucketSize=%d", bucketSize <= 0 ? 10 : bucketSize);
    }

    @Override
    public void sort(int[] array) {
        if (array.length == 0 || innerSort == null) {
            return;
        }
        int bs = bucketSize;
        if (bs <= 0) {
            bs = 10;
        }
        bucketSort(array, bs);
    }

    private void bucketSort(int[] arr, int bucketSize) {
        int[] minMaxValue = getMinMaxValue(arr);
        final int minValue = minMaxValue[0], maxValue = minMaxValue[1];

        int bucketCount = (maxValue - minValue) / bucketSize + 1;
        int[][] buckets = new int[bucketCount][0];

        // 利用映射函数将数据分配到各个桶中
        for (int value : arr) {
            int index = (value - minValue) / bucketSize;
            buckets[index] = arrAppend(buckets[index], value);
        }

        int arrIndex = 0;
        for (int[] bucket : buckets) {
            if (bucket.length <= 0) {
                continue;
            }
            // 对每个桶进行排序，这里使用了插入排序
            innerSort.sort(bucket);
            for (int value : bucket) {
                arr[arrIndex++] = value;
            }
        }

    }

    /**
     * 自动扩容，并保存数据
     */
    private int[] arrAppend(int[] arr, int value) {
        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = value;
        return arr;
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
