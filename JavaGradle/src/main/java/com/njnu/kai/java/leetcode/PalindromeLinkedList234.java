package com.njnu.kai.java.leetcode;

import java.util.ArrayDeque;

public class PalindromeLinkedList234 {
    static public void entry() {
        System.out.println("\n----------    PalindromeLinkedList 234    ----------");
        int[] data1 = {1, 2, 2, 1};
        int[] data2 = {1, 2, 3, 1};
        int[] data3 = {1};
        ListNode listNode1 = ListNode.makeList(data1);
        ListNode listNode2 = ListNode.makeList(data2);
        ListNode listNode3 = ListNode.makeList(data3);
        PalindromeLinkedList234 palindrome = new PalindromeLinkedList234();
        System.out.printf("isPalindrome1=%b\n", palindrome.isPalindrome(listNode1));
        System.out.printf("isPalindrome2=%b\n", palindrome.isPalindrome(listNode2));
        System.out.printf("isPalindrome3=%b\n", palindrome.isPalindrome(listNode3));
    }

    public boolean isPalindrome(ListNode head) {
        ArrayDeque<ListNode> stack = new ArrayDeque<>();
        ListNode node = head;
        while (node != null) {
            stack.push(node);
            node = node.next;
        }
        ListNode node1 = head, node2 = stack.pop();
        boolean result = true;
        for (; node2 != null && node2.next != node1; node1 = node1.next, node2 = stack.poll()) {
            if (node1.val != node2.val) {
                result = false;
                break;
            }
        }
        return result;
    }
}
