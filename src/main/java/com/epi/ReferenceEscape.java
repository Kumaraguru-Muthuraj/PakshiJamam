package com.epi;

import java.util.ArrayList;
import java.util.List;

public class ReferenceEscape {

	public static void test2() {
		Customer c = new Customer(23, "Kumaraguru");
		String name = c.getName();
		System.out.println("Customer is  - " + c.toString());
		name = "Muthuraj";
		System.out.println("Customer is  - " + c.toString());
	}
	
	public static void test1() {
		 CustomersList cl = new CustomersList();
		 cl.addCustomer(new Customer(2, new String("Kumar")));
		 cl.addCustomer(new Customer(3, new String("Muthuraj")));
		 cl.addCustomer(new Customer(4, new String("Guru")));

		 List<Customer> ocl = cl.getCustomers();
		 System.out.println("Original customer list");
		 for (Customer c : ocl) {
			System.out.println(c.toString());
		 }
		 
		 List<Customer> ncl = cl.getNewCustomers();
		 System.out.println("\nNON modifiable customer list");
		 for (Customer c : ncl) {
			 System.out.println(c.toString());
		 }

		 Customer c1 = ncl.get(1);
		 c1.setName(new String("Shalini"));
		 
		 System.out.println("\nNON modifiable list after inserting Shalini");
		 for (Customer c : ncl) {
			 System.out.println(c.toString());
		 }
		 
		 System.out.println("\nOld list after inserting Shalini");	
		 ocl = cl.getCustomers();
		 for (Customer c : cl.getCustomers()) {
			 System.out.println(c.toString());
		 }
		 
	}

}

class CustomersList {
	public CustomersList() {
		l = new ArrayList<Customer>();
	}
	public void addCustomer(Customer c) {
		l.add(c);
	}
	
	public List<Customer> getCustomers() {
		return l;
	}
	
	public List<Customer> getNewCustomers() {
		List<Customer> ncl = new ArrayList<Customer>();
		for (Customer c : l) {
			ncl.add(new Customer(c.getId(), c.getName()));
		}
		return ncl;
	}
	private List<Customer> l;
}

class Customer {
	public Customer(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return this.id + ", " + this.name + ", Address is " + Integer.toHexString(this.name.hashCode());
	}
	private int id;
	private String name;
}