package com.njnu.kai.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i <= 49; ++i) {
			numbers.add(i);
		}
		System.out.println(numbers);

		Collections.shuffle(numbers);
		System.out.println(numbers);

		List<Integer> inCombination = numbers.subList(5, 10);
		Collections.sort(inCombination);
		System.out.println(numbers);
	}

}
