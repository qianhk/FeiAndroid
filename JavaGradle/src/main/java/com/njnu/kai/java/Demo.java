package com.njnu.kai.java;

import com.njnu.kai.java.btc.BtcAddressTest;
import com.njnu.kai.java.leetcode.*;
import com.njnu.kai.java.sort.SortDemo;
import com.njnu.kai.java.string.KMP;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 17/2/4
 */
public class Demo {

    public static void main(String[] args) {
        System.out.println("\nkai: this is a gradle build java project.");

        SortDemo.entry();
        TopKFrequent692.entry();
        PalindromeLinkedList234.entry();
        KMP.entry();
        SortLinkList148.entry();
        LongestPalindrome2131.entry();
        NumBusesToDestination815.entry();
        BtcAddressTest.entry();

        System.out.println("\nkai : end main");
    }
}
