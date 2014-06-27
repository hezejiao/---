package com.sary.arounddepot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	/*
	 * 登陆邮箱验证
	 */
	public static boolean isValidEmail(String email) {
		String regEmail = "^(?:\\w+\\.{1})*\\w+@(\\w+\\.)*\\w+$";
		Pattern pat = Pattern.compile(regEmail);
		Matcher mat = pat.matcher(email);
		if (mat.find()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 登陆密码验证
	 */
	public static boolean isValidPassword(String password) {
		if (password.equals("")) {
			return false;
		}
		if (password.length() < 6) {
			return false;
		}
		return true;
	}


}
