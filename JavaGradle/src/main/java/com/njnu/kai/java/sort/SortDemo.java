package com.njnu.kai.java.sort;

import java.util.ArrayList;
import java.util.Arrays;

public class SortDemo {
    static public void entry() {
        System.out.println("\n----------    Sort Demo    ----------");

        int[] originNumberList = {8, 7, 3, -4, 2, 1, -1, 4, 6, -5, -3, -2, 5, 9, 0};
        System.out.println("Original number list : " + Arrays.toString(originNumberList));

        ArrayList<IArraySort> list = new ArrayList<>();
        list.add(new BubbleSort());
        list.add(new SelectionSort());
        list.add(new InsertionSort());
        list.add(new ShellSort());
        for (IArraySort sort : list) {
            System.out.println(sort.name());
            int[] tmpArray = Arrays.copyOf(originNumberList, originNumberList.length);
            sort.sort(tmpArray);
            System.out.println(Arrays.toString(tmpArray));
        }
    }
}
