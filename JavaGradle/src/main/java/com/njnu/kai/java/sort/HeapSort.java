package com.njnu.kai.java.sort;

public class HeapSort implements IArraySort {
    @Override
    public String name() {
        return "堆排序";
    }

    @Override
    public void sort(int[] array) {
        int len = array.length;

        buildMaxHeap(array, len);

        for (int i = len - 1; i > 0; --i) {
            swap(array, 0, i);
            --len;
            heapify(array, 0, len);
        }
    }

    private void buildMaxHeap(int[] arr, int len) {
        for (int i = len / 2 - 1; i >= 0; --i) {
            heapify(arr, i, len);
        }
    }

    private void heapify(int[] arr, int i, int len) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;

        if (left < len && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < len && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, largest, len);
        }
    }

    private void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
