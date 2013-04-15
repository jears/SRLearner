package jears.cslt.subtopic.util;

public class EncodeProcesser {
	public static String utf82Ascii(String in) {
//		System.out.println(in.length());
		String out = "";
		
		char[] c = in.toCharArray();
//		System.out.println(c.length);
		
		for (int i = 0; i < c.length; ++i) {
//			System.out.print(i +c[i]);
//			System.out.println((int)c[i] + " " + c[i]);
			if (c[i] > 127 || c[i] <0) {
				c[i] = ' ';
				if (i > 0 && c[i-1] == ' ') {
					
				} else {
					out += ' ';
				}
			} else
			{
				out += c[i];
			}
		}
		
		return out.trim();
	}
	
	public static boolean isAsicii(char c) {
		if (c > 127 || c <0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isAllAsicii(String s) {
		char[] c = s.toCharArray();
		
		for (int i = 0; i < c.length; ++i) {

			if (!isAsicii(c[i])) {
				return false;
			} 
		}
		
		return true;
	}
	
	public static boolean hasAsicii(String s) {
		char[] c = s.toCharArray();
		
		for (int i = 0; i < c.length; ++i) {

			if (isAsicii(c[i])) {
				return true;
			} 
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isAllAsicii("in Love"));
	}
}
