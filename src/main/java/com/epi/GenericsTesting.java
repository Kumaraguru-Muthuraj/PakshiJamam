package com.epi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class Base {
	public String doSomething() {
		return "Base...";
	}
}

class Derived1 extends Base {
	public String doSomething() {
		return "Derived1...";
	}
}
class Derived12 extends Derived1 {
	public String doSomething() {
		return "Derived12...";
	}
}

class Derived2 extends Base {
	public String doSomething() {
		return "Derived2...";
	}
}

class A {
	public String m(Base b) {
		return "A::m(Base) " + b.doSomething();
	}
}

class DerivedA extends A {
	public String m(Base b) {
		return "DerivedA::m(Base) " + b.doSomething();
	}
	public String m(Derived1 d) {
		return "DerivedA::m(Derived1) " + d.doSomething();
	}
}

class Shape { /* ... */ }
class Circle extends Shape { /* ... */ }
class Rectangle extends Shape { /* ... */ }

class Node<T extends Shape>{
    public int compareTo(T obj) { /* ... */ return 1;}
    // ...
}

public class GenericsTesting {
	
	public void test1() {
		Node<Circle> node = new Node<>();
		//Comparable<String> comp = node;
		
		Node<Shape> node1 = new Node<>();
		//node = node1;

	}
	
	public static void print(List<? extends Number> list) {
	    for (Number n : list)
	        System.out.print(n + " ");
	    System.out.println();
	}
	
	public void testHidingOverriding() {
		/*FOR OVERRIDING TO WORK, THE FUNCTION SIGNATURE SHOULD BE SAME. 
		 * SUBCLASS IN FUNCTION SIGNATURE OF CHILD WILL NOT OVERRIDE
		*/
		/*A::m(Base) Base...
		A::m(Base) Derived1...
		DerivedA::m(Base) Base...
		DerivedA::m(Base) Derived1...*/
		A a = new DerivedA();
		System.out.println(a.m(new Base()));
		System.out.println(a.m(new Derived1()));
	}
	public void acceptGenerics(List<? extends Base> list) {
		for (Base b : list) {
			System.out.println(b.doSomething());
		}
		//list.add();ADD is prohibited because you can have a list of D1 and you might add D2, where D1 and D2 are 2 subclasses of Base
	}
	public void doSomething() {
		List<Base> list = new LinkedList<Base>();
		list.add(new Base());
		list.add(new Derived1());
		list.add(new Derived2());
		
		acceptGenerics(list);
		
		List<Derived1> list2 = new LinkedList<Derived1>();
		list2.add(new Derived1());
		list2.add(new Derived12());
		
		acceptGenerics(list2);
		
		
		LinkedList<Base> l3 = new LinkedList<>();
		l3.add(new Base());
		l3.add(new Derived1());
		l3.add(new Derived12());
		
		System.out.println("Step 3...");
		try {
		for (Base b : l3) {
			System.out.println(b.doSomething());
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void dosomething1() {
		List<Derived1> ld = new ArrayList<>();
		List<? extends Base> ln = ld;
		//ln.add(new Derived1());WHY THIS IS WRONG IS A MYSTERY!!
		
		
	}
	
	public void doSomething3() {
		Random r = new Random();
		List<Node2> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(new Node2(r.nextInt(10)));
		}
		
		for (Node2 node : list) {
			System.out.print(node.getVal() + ", ");
		}
		
		System.out.println("\nMax is " + this.getMax(list, 0, 10).getVal());
	}
	
	
	class Node2 implements Comparable<Node2> {
		private Integer i = 0;
		public Node2 (Integer i) {
			this.i = i;
		}
		public Integer getVal() {
			return this.i;
		}
		public int compareTo(Node2 n) {
			return this.i.compareTo(n.i);
		}
		public boolean equals(Object n) {
			Node2 that = (Node2) n;
			return this.i.equals(that.i); 
		}
	}
	
	//Why T is super of ? and not ? extends T
	public <T extends Comparable<T>> T getMax(List<? extends T> list, int begin, int end) {
        
		T maxElem = list.get(begin);
		
		for (++begin; begin < end; ++begin) {
			T curElem = list.get(begin);
			if (maxElem.compareTo(curElem) < 0) {
				maxElem = curElem;
			}
		}
		
		return maxElem;
	}
	
	public void doGeneralTesting() {
		Base[] array = (Base[]) Array.newInstance(Base.class, 10);
		Base[] array1 = (Derived1[]) Array.newInstance(Base.class, 10);
		
				
				
				
	}
}
