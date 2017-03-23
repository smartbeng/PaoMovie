package com.pdy.mobile;

import android.content.Context;
import android.content.SharedPreferences;

public class HelperSP {

	public HelperSP() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * 删除字段
	 * 
	 * @param ctx
	 * @param title
	 */
	public static void deleteToSP(Context ctx, String title) {
		SharedPreferences.Editor se = ctx.getSharedPreferences(title,
				Context.MODE_PRIVATE).edit();
		se.clear().commit();
	}

	/**
	 * 保存字段
	 * 
	 * @param ctx
	 * @param title
	 * @param key
	 * @param value
	 */
	public static void saveToSP(Context ctx, String title, String key,
			String value) {
		SharedPreferences.Editor se = ctx.getSharedPreferences(title,
				Context.MODE_PRIVATE).edit();
		se.putString(key, value);
		se.commit();
	}

	/**
	 * 读出字段
	 * @param ctx
	 * @param title
	 * @param key
	 * @return String 如果未保存则返回null
	 */
	public static String getFromSP(Context ctx , String title,String key){
		SharedPreferences se = ctx.getSharedPreferences(title, Context.MODE_PRIVATE);
		if(se==null){
			return null;
		}
		String value = se.getString(key, "");
		if(value.equals("")){
			return null;
		}else{
			return value;
		}
	}
	
}
