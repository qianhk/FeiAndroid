package com.njnu.kai.java.leetcode;

// 排序链表
// https://leetcode.cn/problems/sort-lis
// 合并两有序链表（有虚拟头节点dummyHead方便处理边界条件）https://leetcode.cn/problems/merge-two-sorted-lists/
public class SortLinkList148 {
    public static void entry() {
        System.out.println("\n----------    SortLinkList 148    ----------");
        int[] data1 = {4, 2, 1, 3};
        int[] data2 = {-1, 5, 3, 4, 0};
        int[] data3 = {};
        ListNode listNode1 = ListNode.makeList(data1);
        ListNode listNode2 = ListNode.makeList(data2);
        ListNode listNode3 = ListNode.makeList(data3);
        SortLinkList148 sort = new SortLinkList148();
        System.out.printf("sort link List 1 = %s\n", ListNode.toString(sort.sortList(listNode1)));
        System.out.printf("sort link List 2 = %s\n", ListNode.toString(sort.sortList(listNode2)));
        System.out.printf("sort link List 3 = %s\n", ListNode.toString(sort.sortList(listNode3)));
    }

    public ListNode sortList(ListNode head) {
        if (head == null) {
            return head;
        }
        int length = 0;
        ListNode node = head;
        while (node != null) {
            ++length;
            node = node.next;
        }
        ListNode dummyHead = new ListNode(0, head);
        for (int subLen = 1; subLen < length; subLen <<= 1) {
            ListNode prev = dummyHead, curr = dummyHead.next;
            while (curr != null) {
                ListNode head1 = curr;
                for (int i = 1; i < subLen && curr.next != null; ++i) {
                    curr = curr.next;
                }
                ListNode head2 = curr.next;
                curr.next = null;
                curr = head2;
                for (int i = 1; i < subLen && curr != null && curr.next != null; ++i) {
                    curr = curr.next;
                }
                ListNode next = null;
                if (curr != null) {
                    next = curr.next;
                    curr.next = null;
                }
                prev.next = mergeOrderedLists(head1, head2);
                while (prev.next != null) {
                    prev = prev.next;
                }
                curr = next;
            }
        }
        return dummyHead.next;
    }

    private ListNode mergeOrderedLists(ListNode list1, ListNode list2) {
        ListNode preHeader = new ListNode(-1);
        ListNode cur = preHeader;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                cur.next = list1;
                list1 = list1.next;
            } else {
                cur.next = list2;
                list2 = list2.next;
            }
            cur = cur.next;
        }
        cur.next = list1 != null ? list1 : list2;
        return preHeader.next;
    }
}
