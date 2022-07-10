package com.epi;

import java.util.ArrayList;
import java.util.Collections;

class X {
	X() throws Exception{
		throw new Exception("Exception X");
	}
	X(int x) throws Exception {
		throw new Exception("Exception X.int");
	}
}

class Y extends X {
	Y() throws Exception {
		super(22);
		try {
			
			System.out.println("Inside Y");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class MakeStaticOfMe {
	private MakeStaticOfMe() {}
	public MakeStaticOfMe(int x) {
		System.out.println("MakeStaticOfMe " + x);
	}
}

class B {
	static MakeStaticOfMe o1 = new MakeStaticOfMe(10);
	B () {
		System.out.println("B()");
	}
}

class D extends B {
	static MakeStaticOfMe o1 = new MakeStaticOfMe(11);
	D () {
		System.out.println("D()");
	}
}

public class StaticInitialization {
	public void doSomething() {
		D d = new D();
		
		Integer i = Integer.valueOf(10);
		ArrayList<Integer> al = new ArrayList<Integer> (Collections.nCopies(10, 222));
		for (Integer a : al) {
			System.out.println(a);
		}
		try {
			new Y();			
		} catch (Exception e) {
			System.out.println("Herer...");
		}
	}
}
