package com.sary.arounddepot.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 利用SharedPreferences存储数据
 * @author Administrator
 *
 */
public class PrefHelper {
	private static PrefHelper instance = null;
	private Context mContext;
	private SharedPreferences pref;

	private PrefHelper(Context paramContext) {
		mContext = paramContext;
		pref = mContext.getSharedPreferences(Constant.APP_NMAE, Context.MODE_PRIVATE);
	}

	public static PrefHelper getInstance(Context context) {
		if (instance == null)
			instance = new PrefHelper(context);
		return instance;
	}

	public String read(String key) {
		return pref.getString(key, "");
	}

	public boolean readBoolean(String key) {
		return pref.getBoolean(key, true);
	}

	public int readInt(String key) {
		return pref.getInt(key, 0);
	}
	
	public long readLong(String key) {
		return pref.getLong(key, 0);
	}

	public float readFloat(String key) {
		return pref.getFloat(key, 0);
	}

	public void save(String key, String value) {
		pref.edit().putString(key, value).commit();
	}

	public void save(String key, boolean value) {
		pref.edit().putBoolean(key, value).commit();
	}

	public void save(String key, float value) {
		pref.edit().putFloat(key, value).commit();
	}

	public void save(String key, int value) {
		pref.edit().putInt(key, value).commit();
	}
	
	public void save(String key, long value) {
		pref.edit().putLong(key, value).commit();
	}
		
	public void remove(String key) {
		pref.edit().remove(key).commit();
	}
}
