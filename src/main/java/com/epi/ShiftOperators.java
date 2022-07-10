package com.epi;


public class ShiftOperators {
	
	public String getResult() {
		return "Kumaraguru Muthuraj";
	}
	
	public short getParity(int number) {
		short parity = 0;
		while (number != 0) {
			number &= (number -1);
			parity ^= 1;
		}
		
		return parity;
	}
	
	public void doSomething() {
		final int k = 0;
		int i = 1172028778;
		System.out.println("Value of i = " + i);
		int l = Integer.toBinaryString(i).length();
		System.out.println("Binary of i = " + Integer.toBinaryString(i) + " - " + l);
		int shiftedi = (i << 5);
		System.out.println("i  << 5 = " + shiftedi + " Binary = " + Integer.toBinaryString(shiftedi));
		System.out.println("i = " + i);
		int rshiftedi = (i >> 5);
		String binrshifted = Integer.toBinaryString(rshiftedi);
		System.out.println("i >> 5 = " + rshiftedi + " Binary = " + binrshifted + " - " + binrshifted.length());		
		
		i = -1172028778;
		System.out.println("Value of i = " + i);
		System.out.println("Binary of i = " + Integer.toBinaryString(i) + " - " + Integer.toBinaryString(i).length());
		int rshiftedVal = i >> 5;
		System.out.println("i >> 5 = " + rshiftedVal + " Binary = " + Integer.toBinaryString(rshiftedVal)  + 
				" - " + Integer.toBinaryString(rshiftedVal).length());
		
		int ZEROEXTNshiftedVal = i >>> 5;
		System.out.println("i >>> 5 = " + ZEROEXTNshiftedVal + " Binary = " + Integer.toBinaryString(ZEROEXTNshiftedVal)  + 
				" - " + Integer.toBinaryString(ZEROEXTNshiftedVal).length());
	
	}

}
