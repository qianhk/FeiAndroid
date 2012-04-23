package com.njnu.kai.collection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class SetTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		System.out.println(System.getProperty("line.separator"));
		File file= new File("AliceFULL.txt");
		String path=file.getAbsolutePath();
		System.out.println(path);
		System.out.println("args length: "+ args.length);
		if (args.length > 0) {
			System.out.println("first args is: " + args[0]);
		}
		Set<String> words = new HashSet<String>();
		long totalTime = 0;
		FileInputStream inputStream = new FileInputStream(file);
		Scanner in = new Scanner(inputStream);
		int allWordAmount = 0;
		while (in.hasNext()) {
			String word = in.next();
			long callTime = System.currentTimeMillis();
			words.add(word);
			callTime = System.currentTimeMillis() - callTime;
			totalTime += callTime;
			++allWordAmount;
		}
		inputStream.close();

		Iterator<String> iter = words.iterator();
		for (int i = 1; i <= 20; ++i) {
			System.out.println(iter.next());
		}
		System.out.println(words.size() + " distinct worlds of All "
				+ allWordAmount + ". " + totalTime + " milliSeconds.");
	}

}
