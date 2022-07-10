package com.epi;

interface IronRich {
	public String toString();
}
class Fruit {
	public String toString() {
		return "Fruit";
	}
}
class Apple extends Fruit implements IronRich {
	public String toString() {
		return "Apple rich in Iron";
	}
}

class Fiji extends Apple {
	public String toString() {
		return "Fiji";
	}
}

class Orange extends Fruit {
	public String toString() {
		return "Orange";
	}
}

class BeetRoot implements IronRich {
	public String toString() {
		return "BeetRoot";
	}
}

public class Holder<T> {
	private T value;
	public Holder() {}
	public Holder(T val) { value = val; }
	public void set(T val) { value = val; }
	public T get() { return value; }
	public boolean equals(Object obj) {
		return value.equals(obj);
	}
	public void print(Object o) {
		System.out.println("Printing..." + o.toString());
	}
	
	public void testExtends(Holder<? extends Fruit> upperBoundedHolder) {
		System.out.println("Inside testExtends...");
		//compile error
		//upperBoundedHolder.set(new Orange());
		//upperBoundedHolder.set(new Fruit());
		print(upperBoundedHolder.get());
	}
	
	public void testSuper(Holder<? super Apple> lowerBoundedHolder) {
		System.out.println("Inside testSuper...");
		print(lowerBoundedHolder.get());
		
		lowerBoundedHolder.set(new Apple());
		print(lowerBoundedHolder.get());
		
		lowerBoundedHolder.set(new Fiji());
		print(lowerBoundedHolder.get());
		
		//Compile error
		//lowerBoundedHolder.set(new Fruit());
		//print(lowerBoundedHolder.get());
		
		
		
	}

	
	public void testExtendsAndSuper() {
		Holder<Apple> lowerBoundedHolder = new Holder<>(new Apple());
		testExtends(lowerBoundedHolder);
		
		Holder<Fiji> upperBoundedHolder = new Holder<>(new Fiji());
		//Compile error 
		//testSuper(upperBoundedHolder);
		
		Holder<Fruit> upperBoundedHolder2 = new Holder<>(new Fruit());
		testSuper(upperBoundedHolder2);
		
		Holder<IronRich> upperBoundedHolder3 = new Holder<>(new BeetRoot());
		testSuper(upperBoundedHolder3);
	}
		
	public void run() {
		Holder<Apple> Apple = new Holder<Apple>(new Apple());
		Apple d = Apple.get();
		print(d);
		Apple.set(d);
		// Holder<Fruit> Fruit = Apple; // Cannot upcast
		Holder<? extends Fruit> fruit = Apple; // OK
		Fruit p = fruit.get();
		print(p);
		d = (Apple)fruit.get(); // Returns 'Object'
		print(d);
		try {
			Orange c = (Orange)fruit.get(); // No warning
		} catch(Exception e) { 
			System.out.println(e); 
		}
		// fruit.set(new Apple()); // Cannot call set()
		// fruit.set(new Fruit()); // Cannot call set()
		print(fruit);
		print(d);
		System.out.println(fruit.equals(d)); // OK
	}
}
