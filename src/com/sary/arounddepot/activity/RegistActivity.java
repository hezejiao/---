package com.sary.arounddepot.activity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.CheckUtil;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.StringUtil;
import com.sary.arounddepot.view.MyInfo;

public class RegistActivity extends BaseActivity{
	
	private EditText regest_username,regest_pwd,regest_repet;
	
	private Button submit;
	
	private ImageButton btn_back;
	
	private String uname,pwd,confirm_pwd;
	
	private ProgressDialog mDialog;//搜索的进度条
	
	private UserInfo userInfo;
	
	private MyApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		this.app=(MyApplication)getApplication();
		initview();
		setListener();
	}
	
	private void initview(){
		regest_username=(EditText)findViewById(R.id.regest_username);
		regest_pwd=(EditText)findViewById(R.id.regest_pwd);
		regest_repet=(EditText)findViewById(R.id.regest_repet);
		submit=(Button)findViewById(R.id.submit);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
//		submit.setOnClickListener(this);
//		btn_back.setOnClickListener(this);
	}
	
	private void setListener(){
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				uname=regest_username.getText().toString();
				pwd=regest_pwd.getText().toString();
				confirm_pwd=regest_repet.getText().toString();
				if(uname==null ||"".equals(uname)){
					Toast.makeText(getApplicationContext(), "账号不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}if(pwd==null ||"".equals(pwd)){
					Toast.makeText(getApplicationContext(), "密码不能为空",
							Toast.LENGTH_LONG).show();
					return ;
				}if(!confirm_pwd.trim().equals(pwd.trim())){
					Toast.makeText(getApplicationContext(), "俩次输入的密码不一样",
							Toast.LENGTH_LONG).show();
					return;
				}if(CheckUtil.isValidEmail(uname)==false){
					Toast.makeText(getApplicationContext(), "邮箱格式不正确",
							Toast.LENGTH_LONG).show();
					return;
				}
				if(CheckUtil.isValidPassword(pwd)==false){
					Toast.makeText(getApplicationContext(), "密码至少为6位",
							Toast.LENGTH_LONG).show();
					return;
				}
				else{
					getRegist(uname,pwd);
				}
				
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RegistActivity.this.finish();
				Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
	}	
	private void getRegist(String uname,String pwd){
		pwd=StringUtil.encrypt(pwd, "SHA");
		pwd=StringUtil.encrypt(pwd, "SHA");
		pwd=StringUtil.encrypt(pwd, "MD5");
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userName",uname));
		key.add(new BasicNameValuePair("passwd",pwd));
		boolean result = sendRequest(Constant.REGIST, key);
		if(result){
			mDialog = DialogUtil.showProgress(this, "正在注册...", null);
		}
	}
	
	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if(data==null){
			return ;
		}
		UserInfo info=(UserInfo)data;
		info.setPassward(pwd);
		String code=info.getResult().getCode();
		if("200".equals(code)){
			RegistActivity.this.app.saveUserInfoToShared(info);
		    Log.i("tag", "保存用户名："+RegistActivity.this.app.getUserInfoFromShared().getUsername());
			finish();
		}else if("6001".equals(code)){
			Toast.makeText(getApplicationContext(), "请使用正确的邮箱",
					Toast.LENGTH_SHORT).show();
		}else if("6002".equals(code)){
			Toast.makeText(getApplicationContext(), "邮箱已经存在，请直接使用",
					Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "注册失败",
					Toast.LENGTH_SHORT).show();
		}
	}

}
