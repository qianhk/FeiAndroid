package com.njnu.kai.collection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class LinkedListTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> a = new LinkedList<String>();
		a.add("Amy");
		a.add("Carl");
		a.add("Erica");
		System.out.println(String.format("ori a is: %s", a.toString()));

		List<String> b = new LinkedList<String>();
		b.add("Bob");
		b.add("Doug");
		b.add("Frances");
		b.add("Gloria");
		System.out.println(String.format("ori b is: %s", b.toString()));

		ListIterator<String> aIter = a.listIterator();
		Iterator<String> bIter = b.iterator();
		while (bIter.hasNext()) {
			if (aIter.hasNext()) aIter.next();
			aIter.add(bIter.next());
		}
		System.out.println(String.format("a1 is: %s", a.toString()));

		bIter = b.iterator();
		while (bIter.hasNext()) {
			bIter.next();
			if (bIter.hasNext()) {
				bIter.next();
				bIter.remove();
			}
		}
		System.out.println(String.format("b1 is: %s", b.toString()));

		a.removeAll(b);
		System.out.println(String.format("a2 is: %s", a.toString()));

		System.out.println(a.get(1)); //对链接列表效率低，宜用数组列表
	}

}
