package com.sary.arounddepot.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.CheckUtil;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.JsonUtils;
import com.sary.arounddepot.util.StringUtil;
import com.sary.arounddepot.view.MyInfo;

public class LoginActivity extends BaseActivity implements Callback,PlatformActionListener {

	private EditText username, passward;

	private Button btn_login, btn_regest,xinlang, tengxun;
	
	private ImageButton btn_cancel;

	private TextView froget_pwd;

	// private ImageView xinlang,tengxun;

	private String uname, pwd;

	private ProgressDialog mDialog;// 搜索的进度条

	private MyApplication app;

	private AroundParkListData listData;
	
	private String userId,three_name;
	
	private int type;
	
	private static final int MSG_USERID_FOUND = 1;
	
	private Context context;
	
	private static final int MSG_AUTH_COMPLETE = 5;
	
	private static final int MSG_AUTH_COMPLETE_TENCENT = 6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		context=getBaseContext();
		//初始化ShareSDK
		ShareSDK.initSDK(this);		
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		app = (MyApplication) getApplication();
		init();
		setListener();
	}

	private void init() {
		username = (EditText) findViewById(R.id.username);
		passward = (EditText) findViewById(R.id.passward);
		btn_login = (Button) findViewById(R.id.login);
		froget_pwd = (TextView) findViewById(R.id.froget_pwd);
		btn_regest = (Button) findViewById(R.id.regist);
		btn_cancel = (ImageButton) findViewById(R.id.btn_cancel);
		xinlang = (Button) findViewById(R.id.xinlang);
		tengxun = (Button) findViewById(R.id.tengxun);
	}

	private void setListener() {
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uname = username.getText().toString();
				pwd = passward.getText().toString();
				System.out.println("mmm" + uname + "," + pwd);
				if (uname == null || "".equals(uname)) {
					Toast.makeText(getApplicationContext(), "账号不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (pwd == null || "".equals(pwd)) {
					Toast.makeText(getApplicationContext(), "密码不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if(CheckUtil.isValidEmail(uname)==false){
					Toast.makeText(getApplicationContext(), "邮箱格式不正确",
							Toast.LENGTH_LONG).show();
					return;
				}
				if(CheckUtil.isValidPassword(pwd)==false){
					Toast.makeText(getApplicationContext(), "密码至少为6位",
							Toast.LENGTH_LONG).show();
					return;
				}
				else {
					login(uname, pwd);
				}

			}
		});
		froget_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,
						ForgetPwdActivity.class));
				finish();

			}
		});
		btn_regest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(LoginActivity.this,
						RegistActivity.class));
				finish();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});
		xinlang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){		
				//初始化新浪平台,这两种方式都可以
				//Platform pf = ShareSDK.getPlatform(MainActivity.this, SinaWeibo.NAME);
				SinaWeibo pf = new SinaWeibo(LoginActivity.this);
				//关闭SSO授权，调用网页授权；				
				pf.SSOSetting(true);//true-关闭；flase，开启
				//设置授权监听
				pf.setPlatformActionListener(LoginActivity.this);
				//执行授权
				pf.authorize();
////				LoginActivity.this.finish();
//				authorize(new SinaWeibo(LoginActivity.this));
			}
		});
		tengxun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TencentWeibo tf=new TencentWeibo(LoginActivity.this);
				//关闭SSO授权，调用网页授权；				
				tf.SSOSetting(true);//true-关闭；flase，开启
				//设置授权监听
				tf.setPlatformActionListener(LoginActivity.this);
				//执行授权
				tf.authorize();	
//				LoginActivity.this.finish();
//				authorize(new TencentWeibo(LoginActivity.this));
			}
		});
	}
	
	
	
	
	
	public UserInfo parseDataforSina(String content) throws JSONException {
		UserInfo info = new UserInfo();
		JSONObject obj = new JSONObject(content);
		info.setUsername(obj.getString("username"));
		info.setId(obj.getString("social_uid"));
		info.setSex(obj.getInt("sex"));
		return info;
	}
	
	private void login(String uname, String pwd) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		pwd=StringUtil.encrypt(pwd, "SHA");
		pwd=StringUtil.encrypt(pwd, "SHA");
		pwd=StringUtil.encrypt(pwd, "MD5");
		key.add(new BasicNameValuePair("userName", uname));
		key.add(new BasicNameValuePair("passwd", pwd));
		boolean result = sendRequest(Constant.LOGIN, key);
		if (result) {
			mDialog = DialogUtil.showProgress(this, "正在登录...", null);
		}
	}
	
	private void three_logins(String userId,String type){
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId", userId));
		key.add(new BasicNameValuePair("type", type));
		boolean result = sendRequest(Constant.ThREE_FORLOGIN, key);
		if (result) {
			mDialog = DialogUtil.showProgress(this, "正在登录...", null);
		}
	}

	private void getCarelist(String userId) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId", userId));
		boolean result = sendRequest(Constant.ATTENTION_PARK, key);
	}

	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (msg == Constant.LOGIN){
			if (mDialog != null){
				mDialog.cancel();
				mDialog = null;
			}
			if (data == null) {
				return;
			}
			UserInfo info = (UserInfo) data;
			String code = info.getResult().getCode();
			if ("200".equals(code)) {
				info.setPassward(pwd);
				app.setFlag(true);
				LoginActivity.this.app.saveUserInfoToShared(info);
				mPrefHelper.save("three_login", 0);

				Log.i("tag", "用户id:" + app.getUserInfoFromShared().getId());
				getCarelist(info.getId());
			} else if ("6012".equals(code)) {
				Toast.makeText(getApplicationContext(), "登录邮箱不存在",
						Toast.LENGTH_SHORT).show();
			} else if ("6014".equals(code)) {
				Toast.makeText(getApplicationContext(), "登录密码错误",
						Toast.LENGTH_SHORT).show();
			} else if ("6015".equals(code)) {
				Toast.makeText(getApplicationContext(), "用户不存在",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "登录失败",
						Toast.LENGTH_SHORT).show();
			}
		}
		if (msg == Constant.ATTENTION_PARK) {
			if (data == null) {
				return;
			}
			listData = (AroundParkListData) data;
			LoginActivity.this.app.setListData(listData.datalist);
			Intent intent = getIntent();
			int num = intent.getIntExtra("type", 0);
			if (num == 1) {
				Intent login = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("park_care_list",
						(Serializable) listData);
				login.putExtras(bundle);
				LoginActivity.this.setResult(9, login);
				LoginActivity.this.finish();
				return;
			}
			LoginActivity.this.setResult(31);
			LoginActivity.this.finish();
		}
		if(msg==Constant.ThREE_FORLOGIN){
			if (mDialog != null){
				mDialog.cancel();
				mDialog = null;
			}
			if (data == null) {
				return;
			}
			UserInfo info = (UserInfo) data;
			String code = info.getResult().getCode();
			if("200".equals(code)){
				Toast.makeText(getBaseContext(), "登录成功",
						Toast.LENGTH_SHORT).show();
				info.setUsername(three_name+"登录");
				LoginActivity.this.app.saveUserInfoToShared(info);
				mPrefHelper.save("three_login", 1);
				LoginActivity.this.setResult(31);
				LoginActivity.this.finish();
			}
		}
		
	}
	
	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}
//		if(plat.isValid()){
//			 userId = plat.getDb().getUserId();
//			if (userId != null) {
//				three_name=plat.getDb().getPlatformNname();
//				if("SinaWeibo".equalsIgnoreCase(three_name)){
//					type=1;
//					three_name="新浪微博";
//					three_logins(userId,type+"");
//				}if("TencentWeibo".equalsIgnoreCase(three_name)){
//					type=2;
//					three_name="腾讯微博";
//					three_logins(userId,type+"");
//				}
//				return;
//			}
//		}
		plat.setPlatformActionListener(LoginActivity.this);
		plat.SSOSetting(true);
		plat.showUser(null);
	}
	

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(Platform platform, int arg1, HashMap<String, Object> res) {
		
		Platform pf = ShareSDK.getPlatform(LoginActivity.this, SinaWeibo.NAME);
		if(platform.getName().equals(SinaWeibo.NAME)){
			userId=pf.getDb().getUserId();
			type=1;
			three_name="新浪微博";
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
		}
		if(platform.getName().equals(TencentWeibo.NAME)){
			userId=pf.getDb().getUserId();
			type=2;
			three_name="腾讯微博";
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE_TENCENT, this);
			
		}
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
		case MSG_AUTH_COMPLETE:
			three_logins(userId,type+"");
			break;
		case MSG_AUTH_COMPLETE_TENCENT:
			three_logins(userId,type+"");
			break;
		}
		return false;
	}
	
	
}
