package com.zerodo.base.util;

public class StringUtil {
	public static boolean isNotNull(String args){
		return !isNull(args);
	}
	public static boolean isNull(String args){
		if(args==null||args.trim().length()==0)
			return true;
		else
			return false;
	}
}
