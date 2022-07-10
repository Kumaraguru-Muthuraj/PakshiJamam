package com.epi;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class Name {
	Name(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals (Object that) {
		if (this == that) {
			return true;
		}
		if (that instanceof Name) {
			return this.name.equals(((Name)that).name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 10;//.name.length();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	String name;
}

public class TesHashMaps {
	HashMap<Name, Name> namesWithSameLength = new HashMap<Name, Name>();
	Hashtable<Name, Name> ht = new Hashtable<Name, Name>();
	
	public void fill() {
		this.namesWithSameLength.put(new Name("123456"), new Name("abcdef"));
		this.namesWithSameLength.put(new Name("345231"), new Name("abcdf"));
		this.namesWithSameLength.put(new Name("345621"), new Name("acbde"));
		this.namesWithSameLength.put(new Name("222222"), new Name("acdb"));
		this.namesWithSameLength.put(new Name("2341567"), new Name("acdbfed"));
		this.namesWithSameLength.put(new Name("12345678"), new Name("cadbfed3"));
		this.namesWithSameLength.put(new Name("123456789"), new Name("cadbfed4"));
		
		ht.put(new Name("123456"), new Name("abcdef"));
		ht.put(new Name("345231"), new Name("abcde"));
		ht.put(new Name("345621"), new Name("acbd"));
		ht.put(new Name("222222"), new Name("acd1"));
	}
	
	public void get () {
		System.out.println(namesWithSameLength.get(new Name("123456")));
		System.out.println(namesWithSameLength.get(new Name("345231")));
		System.out.println(namesWithSameLength.get(new Name("345621")));
		System.out.println(namesWithSameLength.get(new Name("222222")));
		System.out.println(namesWithSameLength.get(new Name("2341567")));
		
		System.out.println("-----------");
		
		/*System.out.println(ht.get("123456"));
		System.out.println(ht.get("345231"));
		System.out.println(ht.get("345621"));
		System.out.println(ht.get("222222"));
	
		System.out.println("-----------");
		
		for (Object o : ht.values()) {
			Name n = (Name) o;
			System.out.println(n.toString());
		}*/

	}
	
	private ConcurrentHashMap<String, Set<String>> hmp = new ConcurrentHashMap<String, Set<String>>();
	
	private Set<String> getPermForString(String str) {
		return hmp.get(str);
	}
	
	private void putPermForString(String str, Set<String> perms) {
		hmp.put(str, perms);
	}

	private String stringWithoutPivot(String str, Character pivot) {
		String[] s = str.split(pivot.toString());
		String ret = str.join("", s);
		return ret;	
	}
	
	private Set<String> getPermutations(String str) {
		Set<String> strPerms = getPermForString(str);
		if (strPerms != null && !strPerms.isEmpty()) {
			System.out.print("Getting " + str + " perms from cache \n");
			return strPerms;
		}
		
		strPerms = new LinkedHashSet<String>();

		if (str.length() == 1) {
			strPerms.add(str);	
		/*} else if (str.length() == 2) {
			strPerms.add(str);
			String f = Character.toString(str.charAt(0));
			String l = Character.toString(str.charAt(1));
			String rev = l.concat(f);
			strPerms.add(rev);*/
		} else {
			for (int idx = 0; idx < str.length(); idx++) {
				char pivot = str.charAt(idx);	
				String strMinusPivot = stringWithoutPivot(str, pivot);
				Set<String> trsPerm = getPermutations(strMinusPivot);
				for (String per : trsPerm) {
					strPerms.add(Character.toString(pivot).concat(per));
				}
			}
		}
		
		System.out.print("Generating " + str + " perms and adding to cache \n");
		putPermForString(str, strPerms);
		return strPerms;
	}
		
	void testPerms() {
		Set<String> perms = getPermutations("987654");
		System.out.print(perms.toString());
		System.out.print("\n---\n----" + perms.size());
				
	}

}
