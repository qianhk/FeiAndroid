package com.njnu.kai.collection;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

class Student implements Comparable<Student> {

	public Student(String aD, int aP) {
		_stuId = aP;
		_stuName = aD;
	}

	// @Override
	public String toString() {
		return String.format("[Student: id: %d name: %s]", _stuId, _stuName);
	}

	public boolean equals(Object otherObject) {
		if (this == otherObject)
			return true;
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		Student other = (Student) otherObject;
		return _stuId == other._stuId && _stuName.equals(other._stuName);
	}

	public int hashCode() {
		return 13 * _stuName.hashCode() + 17 * _stuId;
	}

	@Override
	public int compareTo(Student arg0) {
		return _stuId - arg0._stuId;
//		return 0; //相等的只放一个，说明TreeSet也是不可重复的
	}

	public String getStudentName() {
		return _stuName;
	}

	private String _stuName;
	private int _stuId;
}

public class TreeSetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SortedSet<Student> students = new TreeSet<Student>();
		Student stu1 = new Student("kai", 33);
		Student stu2 = new Student("fei", 32);
		Student stu3 = new Student("lei", 35);
		Student stu4 = new Student("song", 34);
//		System.out.println(stu1);
		students.add(stu1);
		students.add(stu2);
		students.add(stu3);
		students.add(stu4);
		System.out.println(students);

		SortedSet<Student> sortByName = new TreeSet<Student>(new Comparator<Student>() {

			@Override
			public int compare(Student arg0, Student arg1) {
				return arg0.getStudentName().compareTo(arg1.getStudentName());
			}

		});
		sortByName.addAll(students);
		System.out.println(sortByName);
	}

}
