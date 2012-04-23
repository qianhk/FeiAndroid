package com.njnu.kai.collection;

import java.util.BitSet;

public class Sieve {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 20000000;
		long start = System.currentTimeMillis();
		BitSet b = new BitSet(n + 1);
		int count = 0;
		int i;
		for (i = 2; i <= n; ++i) {
			b.set(i);
		}

		i = 2;
		while (i * i <= n) {
			if (b.get(i)) {
				++count;
				int k = 2 * i;
				while (k <= n) {
					b.clear(k);
					k += i;
				}
			}
			++i;
		}
		while (i <= n) {
			if (b.get(i)) ++count;
			++i;
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " primes, " + (end - start) + " milliSeconds.");
		//Java 1270607 primes, 426 milliSeconds.
		//C++ 优化到最高O3或者O2级别211\230ms左右,O1 1188.89 milliSeconds  不优化：2153.58ms
	}

}
