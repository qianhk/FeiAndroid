package com.njnu.kai.java.leetcode;

import java.util.*;

public class TopKFrequent692 {
    static public void entry() {
        System.out.println("\n----------    TopKFrequent 692    ----------");
        String[] words = {"i", "love", "leetcode", "i", "love", "coding"};
        TopKFrequent692 instance = new TopKFrequent692();
        System.out.println(instance.topKFrequent(words, 20));
    }

    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> countMap = new HashMap<>();
        for (String word : words) {
            countMap.put(word, countMap.getOrDefault(word, 0) + 1);
        }
        PriorityQueue<String> minHeap = new PriorityQueue<>((s1, s2) -> {
            if (countMap.get(s1).equals(countMap.get(s2))) {
                return s2.compareTo(s1);
            } else {
                return countMap.get(s1) - countMap.get(s2);
            }
        });
        for (String word : countMap.keySet()) {
            minHeap.offer(word);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        List<String> result = new ArrayList<>(k);
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll());
        }
        Collections.reverse(result);
        return result;
    }
}
