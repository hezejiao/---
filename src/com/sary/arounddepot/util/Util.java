package com.sary.arounddepot.util;

import com.sary.arounddepot.MyApplication;

public class Util {

	static MyApplication app = null;

	/**
	 * 
	 * @return
	 */
	public static MyApplication getApp() {
		app = MyApplication._instance;
		return app;
	}

	

	/**
	 * 屏幕高度
	 * 
	 * @return
	 */
	public static int getScreenHeight() {
		if (app == null) {
			app = MyApplication._instance;
		}
		return app.getScreenHeight();
	}

	public static void setScreenHeight(int screenHeight) {
		if (app == null) {
			app = MyApplication._instance;
		}
		app.setScreenHeight(screenHeight);
	}

	public static int getScreenWidth() {
		if (app == null) {
			app = MyApplication._instance;
		}
		return app.getScreenWidth();
	}

	public static void setScreenWidth(int screenWidth) {
		if (app == null) {
			app = MyApplication._instance;
		}
		app.setScreenWidth(screenWidth);
	}

}
