package test;

import java.text.DecimalFormat;

public class Test_1 {

	public void print(Object obj) {
		System.out.println("this is Object!");
	}

	public void print(String obj) {
		System.out.println("this is String!");
	}

//	public void print(double[] obj) {
//		System.out.println("this is double[]!");
//	}

	public static void main(String[] args) {
		DecimalFormat dFormat = new DecimalFormat("#.000000");
		String aString = dFormat.format(45.12586274544361);
		System.out.println("a: "+aString);
//		Test_1 test = new Test_1();
//		test.print(null);
	}
}
