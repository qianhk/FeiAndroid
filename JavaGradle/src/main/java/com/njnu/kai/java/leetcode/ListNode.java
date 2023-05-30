package com.njnu.kai.java.leetcode;

public class ListNode {
    int val;
    ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public static ListNode makeList(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return null;
        }
        ListNode head = new ListNode(numbers[0]);
        ListNode lastNode = head;
        for (int i = 1; i < numbers.length; ++i) {
            lastNode.next = new ListNode(numbers[i]);
            lastNode = lastNode.next;
        }
        return head;
    }
}
