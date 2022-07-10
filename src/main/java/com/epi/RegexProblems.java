package com.epi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexProblems {
	public void doSomething1() {
		String input = "abcabcabcxxxxabdaaaaabc";
		Pattern pattern = Pattern.compile("abc+");
		Matcher m = pattern.matcher(input);
		while (m.find()) {
			System.out.println("Match - " + m.group() + " , start - " + m.start() + " , end - " + m.end());
		}
			
		pattern = Pattern.compile("(abc){2}");
		m = pattern.matcher(input);
		while (m.find()) {
			System.out.println("Match - " + m.group() + " , start - " + m.start() + " , end - " + m.end());
		}
	}
	
	public void doSomething2() {
	      // String to be scanned to find the pattern.
	      String line = "This order was placed for QT3000! OK?";
	      String pattern = "([Q-T]+)(\\d+)";

	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(line);
	      if (m.find()) {
			System.out.println("Match - " + m.group() + " , start - " + m.start() + " , end - " + m.end());	         
	      } else {
	         System.out.println("NO MATCH");
	      }
	      System.out.println("Match - " + m.group(0));
	      System.out.println("Match - " + m.group(1));
	      System.out.println("Match - " + m.group(2));
	      System.out.println("Match - " + m.group(3));

	     
	}
}
