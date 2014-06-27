package com.sary.arounddepot;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.LocationManagerProxy;
import com.sary.arounddepot.activity.MainActivity;
import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.CyptoUtils;
import com.sary.arounddepot.util.MyLocationListenner;

public class MyApplication extends Application{
	
	public static MainActivity  mMain;
	
	public static MyApplication _instance=new MyApplication();
	
	public double longitude;

	public double latitude;
	
	public String address;
	
	
	public AMapLocation aLocation;
	
	public int screen =1;//保存地图页的当前页
	
	private int screenHeight;

	private int screenWidth;
	
	private String id;//用户id,唯一标识
	
	private UserInfo userInfo=null;
	
	public List<AroundParkData> listData=new ArrayList<AroundParkData>();
	
	public AroundParkListData listDatas=new AroundParkListData();
	
	private LocationManagerProxy locationManager;
	
	private MyLocationListenner myListener;
	
	public boolean flag=false;
	
	public String curVersionName = "";
	
	public int curVersionCode;
	
	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true); 	//设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
		_instance = this;
		getScreenHW();
		getCurrentVersion(); 
	}
	
		public void getScreenHW(){
			DisplayMetrics dm = new DisplayMetrics();
			dm = this.getApplicationContext().getResources().getDisplayMetrics();
			this.screenWidth = dm.widthPixels;
			this.screenHeight = dm.heightPixels;
		}
		
		/**
		 * 
		 * 保存到缓存
		 */
		public void saveUserInfoToShared(UserInfo info) {

			if (info == null) {
				return;
			}
			String id = info.getId();
			this.id = id;
			String username = info.getUsername();
			String password = info.getPassward();
			String nickname=info.getNickname();
			String phone=info.getPhone();
			String email=info.getEmail();
			int sex=info.getSex();
			SharedPreferences success_sharedpre = getSharedPreferences(
					"userInfo", Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = success_sharedpre.edit();
			edit.clear().commit();
			edit.putString("password", CyptoUtils.encode(Constant.Key, password))
					.commit();
			edit.putString("id", id).commit();
			edit.putString("username", username).commit();
			edit.putString("nickname", nickname).commit();
			edit.putString("phone", phone).commit();
			edit.putString("email", email).commit();
			edit.putInt("sex", sex).commit();
			this.userInfo = info;
		}
		
		/**
		 * 
		 * 从缓存中获取
		 */
		public UserInfo getUserInfoFromShared() {

			SharedPreferences autologinSp = getSharedPreferences("userInfo",
					Context.MODE_PRIVATE);
			String id=autologinSp.getString("id", null);
			if (id==null) {
				return null;
			}
			String password = autologinSp.getString("password", null);
			String autoPass = "";
			if (password == null || "".equals(password)) {
				autoPass = "";
			} else {
				autoPass = CyptoUtils.decode(Constant.Key, password);
			}
			String username = autologinSp.getString("username", null);
			String nickname = autologinSp.getString("nickname", null);
			String phone = autologinSp.getString("phone", null);
			String email = autologinSp.getString("email", null);
			int sex = autologinSp.getInt("sex", 0);
			
			UserInfo info = null;
			if (id != null && !("").equals(id)){
				info = new UserInfo();
				info.setId(id);
				info.setUsername(username);
				info.setPassward(autoPass);
				info.setNickname(nickname);
				info.setPhone(phone);
				info.setSex(sex);
				info.setEmail(email);
				this.userInfo = info;
			}

			return userInfo;
		}
		
		/**
		 * 清除用户信息，但是不清除登录账号
		 */
		@SuppressLint("NewApi")
		public void clearUserInfo() {
			SharedPreferences success_sharedpre = getSharedPreferences(
					"userInfo", Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = success_sharedpre.edit();
			edit.putString("id", null).commit();
			edit.putString("username", null).commit();
			edit.putString("password", null).commit();
			edit.putString("nickname", null).commit();
			edit.putString("phone", null).commit();
			edit.putString("email", null).commit();
			edit.putInt("sex", 0).commit();
			this.userInfo = null;
			this.id = null;
			// 清空缓存
			SharedPreferences sharedPreferences = getSharedPreferences(
					getPackageName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.clear().apply();
		}
		
		/**
		 * 获取当前客户端版本信息
		 */
		private void getCurrentVersion(){
	        try { 
	        	PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
	        	curVersionName = info.versionName;
	        	curVersionCode = info.versionCode;
	        } catch (NameNotFoundException e) {    
				e.printStackTrace(System.err);
			} 
		}
		
	public double getLongitude() {
		return longitude;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public List<AroundParkData> getListData() {
		return listData;
	}
	public void setListData(List<AroundParkData> listData) {
		this.listData = listData;
	}
	public int getScreenHeight() {
		return screenHeight;
	}
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	public int getScreenWidth() {
		return screenWidth;
	}
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
}
