package jears.cslt.util.fileOperation;

public class Format {
	public static String get0X(int i) {
		if (i >= 10)
			return String.valueOf(i);
		else if(i >= 0 && i < 10) {
			return String.valueOf(0)+String.valueOf(i);
		} else {
			System.err.println("[illegal parameter: i < 0]");
			return null;
		}
	}
	
	public static String get000X(int i) {
		if (i >= 10)
			return "00" + String.valueOf(i);
		else if(i >= 0 && i < 10) {
			return "00" + String.valueOf(0)+String.valueOf(i);
		} else {
			System.err.println("[illegal parameter: i < 0]");
			return null;
		}
	}
	
	public static String addQuotation(String q) {
		return "\"" + q + "\"";
	}
}
