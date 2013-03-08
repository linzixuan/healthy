package com.zerodo.base.util;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigUtil {
	
	public static final String CONFIG_NAME="serverConfig";
	public static final String KEY_SACCID="sAccid";
	public static final String KEY_SREGKEY="sRegKey";
	public static final String KEY_INFODONE="infoDone";
	
	private static SharedPreferences sp;
	
	public static String getConfig(Context context,String key){
		sp=context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
	
	public static void setConfig(Context context,String key,String value){
		sp=context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void setConfig(Context context,Map<String, String> map){
		sp=context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		for(Iterator<String> it=map.keySet().iterator();it.hasNext();){
			String key = it.next();
			String value = map.get(key);
			editor.putString(key, value);
		}
		editor.commit();
	}
}
