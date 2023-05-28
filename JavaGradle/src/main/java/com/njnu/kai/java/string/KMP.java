package com.njnu.kai.java.string;

public class KMP {
    static public void entry() {
        System.out.println("\n----------    KMP    ----------");
        String s1 = "ababa", p1 = "aba";
        String s2 = "xababa", p2 = "aba";
        System.out.printf("s=%s p=%s method1=%d\n", s1, p1, KMP.bf(s1, p1));
        System.out.printf("s=%s p=%s method1=%d\n", s2, p2, KMP.bf(s2, p2));

        System.out.printf("s=%s p=%s method2=%d\n", s1, p1, KMP.kmp(s1, p1));
        System.out.printf("s=%s p=%s method2=%d\n", s2, p2, KMP.kmp(s2, p2));
    }

    public static int kmp(String ts, String ps) {
        char[] t = ts.toCharArray();
        char[] p = ps.toCharArray();
        int i = 0; // 主串的位置
        int j = 0; // 模式串的位置
        int[] next = getNext(ps);
        // 开始判断（设置边界值）
        while (i < t.length && j < p.length) {
            // 当j为-1时，要移动的是i，当然j也要归0
            // 如果匹配成功，两者都进行移动，开始下一位比对
            if (j == -1 || t[i] == p[j]) {
                i++;
                j++;
            } else {
                j = next[j]; // j回到指定位置，i不需要回溯了
            }
        }

        // 最后同样进行判断，是否符合条件
        if (j == p.length) {
            return i - j;
        } else {
            return -1;
        }
    }

    /**
     * next数组求解
     */
    public static int[] getNext(String ps) {
        char[] p = ps.toCharArray();
        int[] next = new int[p.length];
        // 这里的next[0]需要等于-1
        // 因为j在最左边时，不可能再移动j了，这时候要应该是i指针后移。所以在代码中才会有next[0] = -1;这个初始化。
        next[0] = -1;
        // 这里设置j的初始值从第一个开始（我们需要得到全部next数组）
        int j = 0;
        // 这里设置k，k就是应该返回的位置，也就是我们常说的前缀和后缀匹配区域的前缀的后一个位置
        int k = -1;
        // 进行循环，得到next数组
        while (j < p.length - 1) {
            // 首先是k==-1时，说明前面已无匹配状态，我们重新开始
            // 然后是p[j] == p[k]，说明循环时新添加的值，与我们应该返回比对的位置相同
            // 同时由于我们之前的部分都是已经匹配成功的，所以加上这个数使我们的匹配长度又增加一位
            if (k == -1 || p[j] == p[k]) {
                // 当两个字符相等时要跳过
                //（因为p[k]与S[i]不符合的话，由于我们的p[j]=p[k]，所以肯定也不符合，我们直接跳下一步）
                if (p[++j] == p[++k]) {
                    next[j] = next[k];
                } else {
                    // 因为在P[j]之前已经有P[0 ~ k-1] == p[j-k ~ j-1]。（next[j] == k）
                    // 这时候现有P[k] == P[j]，我们是不是可以得到P[0 ~ k-1] + P[k] == p[j-k ~ j-1] + P[j]。
                    // 即：P[0 ~ k] == P[j-k ~ j]，即next[j+1] == k + 1 == next[j] + 1
                    // 前面我们已经进行了j++和k++，所以这里直接赋值即可
                    next[j] = k;
                }
            } else {
                // 如果当前状态无法匹配，我们就跳回上一个前缀后缀相同部分再来判断是否前后缀相同
                k = next[k];
            }
        }
        return next;
    }

    /**
     * 暴力破解法 只算第一个
     *
     * @param ts 主串
     * @param ps 模式串
     * @return 如果找到，返回在主串中第一个字符出现的下标，否则为-1
     */
    public static int bf(String ts, String ps) {
        char[] t = ts.toCharArray();
        char[] p = ps.toCharArray();
        int i = 0; // 主串的位置
        int j = 0; // 模式串的位置
        while (i < t.length && j < p.length) {
            if (t[i] == p[j]) {
                // 当两个字符相同，就比较下一个
                i++;
                j++;
            } else {
                i = i - j + 1; // 一旦不匹配，i后退（从之前i的下一位开始，也是遍历所有i）
                j = 0; // j归0
            }
        }

        // 当上面循环结束，必定是i到头或者j到头，如果是j到头，则说明存在子串符合父串，我们就将头位置i返回
        if (j == p.length) {
            return i - j;
        } else {
            return -1;
        }
    }
}
