package com.njnu.kai.java.sort;

import java.util.Arrays;

public class MergeSort implements IArraySort {
    @Override
    public String name() {
        return "归并排序";
    }

    @Override
    public void sort(int[] array) {
        boolean useRecursion = false;
        if (useRecursion) {
            int[] resultList = recursionSort(array);
            System.arraycopy(resultList, 0, array, 0, array.length);
        } else {
            iterationMergeSort(array);
        }
    }

    private void iterationMergeSort(int[] arr) {
        int n = arr.length;
        // i 用于 for 循环迭代,temp 用来存储临时数组，所以要给他分配内存
        int i, next, left_min, left_max, right_min, right_max;
        int[] temp = new int[n];

        // i 是步长，第一次是1个元素与1个元素相比，第2个是两个元素与2个元素相比
        for (i = 1; i < n; i *= 2) {
            // left_min 最后是要小于 n-i 的，这一点可以通过画图得到
            // left_min = right_max 是指上一组排序完成之后，将上一组的 right 赋值给下一组的 left_min
            for (left_min = 0; left_min < n - i; left_min = right_max) {
                right_min = left_max = left_min + i;
                right_max = left_max + i;

                // 因为奇数的数组最后很有可能 right_max > n，所以将它限制到最大为 n
                if (right_max > n) {
                    right_max = n;
                }

                next = 0;

                // 在这里跟递归中的归并操是一样的，从两个数组中取小的出来放入临时数组
                while (left_min < left_max && right_min < right_max) {
                    if (arr[left_min] < arr[right_min]) {
                        temp[next++] = arr[left_min++];
                    } else {
                        temp[next++] = arr[right_min++];
                    }
                }

                // 但是上面的过程并不会同时将左右两个数组的元素都放入临时存储数组 temp 中
                // 要么左边会剩一点，要么右边会剩一点，所以这个时候要对剩余的进行操作
                // 如果左边剩了，说明这些应该是最大的，应该放在数组的右边
                while (left_min < left_max) {
                    arr[--right_min] = arr[--left_max];
                }
                // 如果 right_min < right_max，此时右边大，但已经在右边了，不需要挪

                // 将临时存储的元素放入数组中得到最后的结果
                while (next > 0) {
                    arr[--right_min] = temp[--next];
                }
            }
        }
    }


    // 递归法 空间复杂度高、占用程序运行栈空间层级
    private int[] recursionSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int middle = array.length / 2;

        int[] left = Arrays.copyOfRange(array, 0, middle);
        int[] right = Arrays.copyOfRange(array, middle, array.length);
//        System.out.printf("merge left=%s right=%s\n", Arrays.toString(left), Arrays.toString(right));
        return merge(recursionSort(left), recursionSort(right));
    }

    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        while (left.length > 0 && right.length > 0) {
            if (left[0] <= right[0]) {
                result[i++] = left[0];
                left = Arrays.copyOfRange(left, 1, left.length);
            } else {
                result[i++] = right[0];
                right = Arrays.copyOfRange(right, 1, right.length);
            }
        }

        while (left.length > 0) {
            result[i++] = left[0];
            left = Arrays.copyOfRange(left, 1, left.length);
        }

        while (right.length > 0) {
            result[i++] = right[0];
            right = Arrays.copyOfRange(right, 1, right.length);
        }

        return result;
    }
}
