package com.njnu.kai.collection;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {
		Student stu1 = new Student("kai", 33);
		Student stu2 = new Student("fei", 32);
		Student stu3 = new Student("lei", 35);
		Student stu4 = new Student("song", 34);
		Map<String, Student> staff = new HashMap<String, Student>();
		staff.put("kai33", stu1);
		staff.put("fei32", stu2);
		staff.put("lei35", stu3);
		staff.put("song34", stu4);
		System.out.println(staff);

		staff.remove("song34");
		System.out.println(staff);

		staff.put("lei35", stu4);
		System.out.println(staff);

		for (Map.Entry<String, Student> entry : staff.entrySet()) {
			String key = entry.getKey();
			Student value = entry.getValue();
			System.out.println("key=" + key + ", value" + value);
		}
	}

}
