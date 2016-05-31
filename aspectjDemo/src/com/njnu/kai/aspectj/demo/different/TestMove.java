package com.njnu.kai.aspectj.demo.different;

import java.util.List;

public class TestMove {

	public void move(List<Animal> list) {
		for(Animal a : list) {
			a.move();
		}
	}
	
}
