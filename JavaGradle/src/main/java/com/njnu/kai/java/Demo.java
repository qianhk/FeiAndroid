package com.njnu.kai.java;

import com.njnu.kai.java.leetcode.LongestPalindrome2131;
import com.njnu.kai.java.leetcode.PalindromeLinkedList234;
import com.njnu.kai.java.leetcode.SortLinkList148;
import com.njnu.kai.java.leetcode.TopKFrequent692;
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
        LongestPalindrome2131.entry();;

        System.out.println("\nkai : end main");
    }
}
