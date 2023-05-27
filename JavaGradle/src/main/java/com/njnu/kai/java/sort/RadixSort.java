package com.njnu.kai.java.sort;

import java.util.Arrays;

public class RadixSort implements IArraySort {
    @Override
    public String name() {
        return "基数排序";
    }

    @Override
    public void sort(int[] array) {
        radixSort(array);
    }

    private void radixSort(int[] arr) {
        // 基数，在循环过程中根据数的大小自动增长
        int digitNumber = 1;
        // 桶，正数和负数共20个桶
        int[][] bucket = new int[20][arr.length];
        // i 代表当前循环的基数，如 1,10，100....
        for (int radix = 1; radix <= digitNumber; radix *= 10) {
            // 表示本次循环中基数是否已经扩大
            boolean digitExpand = false;
            // 本次循环中 20 个桶每个桶中存的数的个数
            int[] numberAmount = new int[20];
            // 放入桶中
            for (int num : arr) {
                // digit表示 num 要放在 20 格桶中的哪一个
                int digit = (num / radix) % 10;
                // 这里是加 10 ，即正数用后 10 个桶，负数用前 10 个桶
                digit += 10;
                // numberAmount[digit] 初始值为 0 ，可以直接使用
                bucket[digit][numberAmount[digit]++] = num;
                // 本次循环中遇到有以下条件时最外层循环条件需要扩大一次，即基数需要乘以10
                // 比如第一次循环时digitNumber = 1,当前 num = 2，则不需扩大
                // 若 num = 10 则需要扩大一次最外层循环
                if (!digitExpand && num >= (digitNumber * 10)) {
                    digitNumber *= 10;
                    digitExpand = true;
                }
            }
            // 从 20 个桶中取出数据，完成一次排序
            for (int j = 0, order = 0; j < 20; j++) {
                for (int k = 0; k < numberAmount[j]; k++) {
                    arr[order++] = bucket[j][k];
                }
            }
        }
    }

}
