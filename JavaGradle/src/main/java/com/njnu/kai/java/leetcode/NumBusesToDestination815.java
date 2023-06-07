package com.njnu.kai.java.leetcode;

import java.util.*;

// 公交路线
// https://leetcode.cn/problems/bus-routes
public class NumBusesToDestination815 {

    public static void entry() {
        System.out.println("\n----------    NumBusesToDestination 815    ----------");

        int[][] route1 = {{1, 2, 7}, {3, 6, 7}};
        int[][] route2 = {{7, 12}, {4, 5, 15}, {6}, {15, 19}, {9, 12, 13}};
        NumBusesToDestination815 buses = new NumBusesToDestination815();
        System.out.printf("method1 route1 result=%d\n", buses.numBusesToDestination1(route1, 1, 6));
        System.out.printf("method1 route2 result=%d\n", buses.numBusesToDestination1(route2, 15, 12));
        System.out.printf("method2 route1 result=%d\n", buses.numBusesToDestination2(route1, 1, 6));
        System.out.printf("method2 route2 result=%d\n", buses.numBusesToDestination2(route2, 15, 12));
    }

    public int numBusesToDestination2(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }

        int n = routes.length;
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int site : routes[i]) {
                List<Integer> list = map.getOrDefault(site, new ArrayList<>());
                list.add(i);
                map.put(site, list);
            }
        }

        if (!map.containsKey(source) && !map.containsKey(target)) {
            return -1;
        }

        Queue<Integer> queue = new LinkedList<>();  //存储公交线路
        HashSet<Integer> visited = new HashSet<>();  //记录公交路线有没有重复

        for (int bus : map.get(source)) {
            queue.offer(bus);
            visited.add(bus);
        }

        int step = 0;
        while (!queue.isEmpty()) {
            step++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int bus = queue.poll();
                for (int stop : routes[bus]) {
                    if (stop == target) {
                        return step;
                    }
                    for (int transfer : map.getOrDefault(stop, new ArrayList<>())) {
                        if (visited.contains(transfer)) {
                            continue;
                        }
                        visited.add(transfer);
                        queue.offer(transfer);
                    }
                }
            }

        }
        return -1;
    }

    public int numBusesToDestination1(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }

        int n = routes.length;
        boolean[][] edge = new boolean[n][n];
        Map<Integer, List<Integer>> rec = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int site : routes[i]) {
                List<Integer> list = rec.getOrDefault(site, new ArrayList<>());
                for (int j : list) {
                    edge[i][j] = edge[j][i] = true;
                }
                list.add(i);
                rec.put(site, list);
            }
        }

        int[] dis = new int[n];
        Arrays.fill(dis, -1);
        Queue<Integer> que = new LinkedList<>();
        for (int bus : rec.getOrDefault(source, new ArrayList<>())) {
            dis[bus] = 1;
            que.offer(bus);
        }
        while (!que.isEmpty()) {
            int x = que.poll();
            for (int y = 0; y < n; y++) {
                if (edge[x][y] && dis[y] == -1) {
                    dis[y] = dis[x] + 1;
                    que.offer(y);
                }
            }
        }

        int ret = Integer.MAX_VALUE;
        for (int bus : rec.getOrDefault(target, new ArrayList<>())) {
            if (dis[bus] != -1) {
                ret = Math.min(ret, dis[bus]);
            }
        }
        return ret == Integer.MAX_VALUE ? -1 : ret;
    }

}
