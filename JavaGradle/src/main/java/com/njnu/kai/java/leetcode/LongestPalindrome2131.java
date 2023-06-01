package com.njnu.kai.java.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LongestPalindrome2131 {
    static public void entry() {
        System.out.println("\n----------    LongestPalindrome 2131    ----------");

        String[] strList1 = {"lc", "cl", "gg"};
        String[] strList2 = {"ab", "ty", "yt", "lc", "cl", "ab"};
        String[] strList3 = {"cc", "ll", "xx"};
        String[] strList4 = {"cc", "ll", "xx", "xx"};

        LongestPalindrome2131 instance = new LongestPalindrome2131();
        System.out.printf("longestPalindrome2 strList1 result=%d\n", instance.longestPalindrome2(strList1));
        System.out.printf("longestPalindrome2 strList2 result=%d\n", instance.longestPalindrome2(strList2));
        System.out.printf("longestPalindrome2 strList3 result=%d\n", instance.longestPalindrome2(strList3));
        System.out.printf("longestPalindrome2 strList4 result=%d\n", instance.longestPalindrome2(strList4));

        System.out.printf("longestPalindrome strList1 result=%d\n", instance.longestPalindrome(strList1));
        System.out.printf("longestPalindrome strList2 result=%d\n", instance.longestPalindrome(strList2));
        System.out.printf("longestPalindrome strList3 result=%d\n", instance.longestPalindrome(strList3));
        System.out.printf("longestPalindrome strList4 result=%d\n", instance.longestPalindrome(strList4));
    }

    public int longestPalindrome(String[] words) {
        // 统计每个字符串出现的次数
        Map<String, Integer> map = new HashMap<>();
        // 出现次数为奇数的最大值
        int max = Integer.MIN_VALUE;
        // 作为中心的字符串
        String center = "";
        int ans = 0;
        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        Set<String> set = map.keySet();
        /* 遍历map，分为2种情况
         1.是叠词 则如果出现次数为偶数，直接计入答案，如果是奇数，要判断如果可以作为中心点
         （出现次数是max） 则直接计入答案，如果不是中心点，则舍弃一次，计入答案
         2.不是叠词，判断map中是否有对应的逆转串，有就取两个中次数最少的计入答案
         */
        for (String word : set) {
            // 情况1，叠词
            if (word.charAt(0) == word.charAt(1)) {
                // 出现偶次
                if (map.get(word) % 2 == 0) {
                    ans = ans + map.get(word) * 2;
//                    System.out.println("word=" + word + " ans=" + ans);
                } else {
                    // 更新max
                    if (map.get(word) > max) {
                        // 之前的奇数次的叠词次数-1（因为之前已经计入答案，现在更新center，要-2）
                        if (!center.equals("")) {
                            ans = ans - 2;
                        }
                        max = map.get(word);
                        center = word;
                    }
                    // 是中心点，直接计入答案
                    if (word.equals(center)) {
                        ans = ans + map.get(word) * 2;
                    } else { // 不是，-1 只取偶数个拿来用
                        ans = ans + (map.get(word) - 1) * 2;
                    }
                }
            } else { // 情况2，不是叠词
                String reverse = new StringBuilder(word).reverse().toString();
                if (map.containsKey(reverse)) {
                    int count = Math.min(map.get(word), map.get(reverse));
                    map.put(word, 0); // 避免重复计数
                    ans = ans + count * 4;
                }
            }
        }
        return ans;
    }

    public int longestPalindrome2(String[] words) {
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        int add = 0;
        int ans = 0;
        for (String s : map.keySet()) {
            if (s.charAt(0) == s.charAt(1)) {
                ans += ((map.get(s) >> 1) << 2);
                if (((map.get(s) & 1) == 1)) {
                    add = 2;
                }
            } else {
                String t = new StringBuilder(s).reverse().toString();
                if (map.containsKey(t)) {
                    ans += Math.min(map.get(s), map.get(t)) * 2;
                }
            }
        }
        return ans + add;
    }
}
