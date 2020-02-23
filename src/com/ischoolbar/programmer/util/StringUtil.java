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
}