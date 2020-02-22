package com.ischoolbar.programmer.util;

public class StringUtil {
	public static boolean isEmpty(String str) {
		 if(str == null) return true;
		 if("".equals(str)) return true;
		return false;
	}
}
