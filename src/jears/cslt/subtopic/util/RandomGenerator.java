package jears.cslt.subtopic.util;

import java.util.Random;

public class RandomGenerator {
	private static Random random = new Random();
	public static int getNextRandomNum() {
		int r = random.nextInt();
		if (r < 0)
			r = -r;
		r = r%5;
		if (r == 0)
			r = 5;
		return r;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getNextRandomNum());
		}
	}
}
