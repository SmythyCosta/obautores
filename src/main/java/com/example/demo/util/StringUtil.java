package com.example.demo.util;

public class StringUtil {
	
	/**
	 * 
	 * Lib Helpers String methods
	 * 
	 * https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringUtils.html#isBlank(java.lang.String)
	 * 
	 * https://stackoverflow.com/questions/32567208/best-way-to-verify-string-is-empty-or-null
	 * 
	 * */
	
	public static boolean isNullOrEmpty(String s){
		return (s==null || s.trim().isEmpty());
    }

}
