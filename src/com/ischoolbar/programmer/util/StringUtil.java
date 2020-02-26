package com.ischoolbar.programmer.util;

public class StringUtil {
	public static boolean isEmpty(String str) {
		 if(str == null) return true;
		 if("".equals(str)) return true;
		return false;
	}
	public static String generateSn(String prefix, String suffix) {
		String sn = prefix + System.currentTimeMillis() + suffix;
		return sn;
	}
	
	public static String convertToUnderLine(String str) {
		String newStr = "";
		if(isEmpty(str)) return "";
		for(char i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				if (i == 0) {
					newStr += Character.toLowerCase(ch);
					continue;
				}
				newStr += "_" + Character.toLowerCase(ch);
				continue;
			}
			newStr += ch;
		}
		return newStr;
	}
}