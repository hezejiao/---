package com.sary.arounddepot.activity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.Result;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.StringUtil;

public class UpdatePasswordActivity extends BaseActivity{
	
	private MyApplication app;
	private Context context;
	private EditText old_pwd,new_pwd,new_pwd_again;
	private Button to_save;
	private ImageButton btn_back;
	private UserInfo userInfo;
	private String userId = "", oldstr = "", p1str = "", p2str = "";
	private ProgressDialog mDialog;//搜索的进度条
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_pwd);
		app=(MyApplication)getApplication();
		userInfo=app.getUserInfoFromShared();
		context=getBaseContext();
		initView();
		setListener();
		if(userInfo!=null){
//			old_pwd.setText(userInfo.getPassward());
			userId=userInfo.getId();
		}
	}
	
	private void initView(){
		old_pwd=(EditText)findViewById(R.id.old_pwd);
		new_pwd=(EditText)findViewById(R.id.new_pwd);
		new_pwd_again=(EditText)findViewById(R.id.new_pwd_again);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		to_save=(Button)findViewById(R.id.to_save);
	}
	
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UpdatePasswordActivity.this.finish();
			}
		});
		to_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pwSave();
			}
		});
	}
	
	private void pwSave() {
		oldstr = old_pwd.getText().toString();
		p1str = new_pwd.getText().toString();
		p2str = new_pwd_again.getText().toString();
		if (false == validatorData()){
			return;
		}
		updatepwd(oldstr,p1str,userId);
	}
	
	private boolean validatorData() {
//		if (oldstr == null || oldstr.equals("") || oldstr.equals("null")){
//			Toast.makeText(context,"原密码不能为空", Toast.LENGTH_SHORT).show();
//			return false;
//		}
		if (p1str == null || p1str.equals("") || p1str.equals("null")){
			Toast.makeText(context,"新密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (p2str == null || p2str.equals("") || p2str.equals("null")){
			Toast.makeText(context,"密码确认不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!p1str.equals(p2str)) {
			Toast.makeText(context,"两次密码输入不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void updatepwd(String old_pwd,String new_pwd,String userId){
		old_pwd=StringUtil.encrypt(old_pwd, "SHA");
		old_pwd=StringUtil.encrypt(old_pwd, "SHA");
		old_pwd=StringUtil.encrypt(old_pwd, "MD5");
		
		new_pwd=StringUtil.encrypt(new_pwd, "SHA");
		new_pwd=StringUtil.encrypt(new_pwd, "SHA");
		new_pwd=StringUtil.encrypt(new_pwd, "MD5");
		
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("curPassword",old_pwd));
		key.add(new BasicNameValuePair("newPassword",new_pwd));
		key.add(new BasicNameValuePair("userId",userId));
		boolean result = sendRequest(Constant.UPDATE_PWDS, key);
		if(result){
			mDialog = DialogUtil.showProgress(this, "正在保存...", null);
		}
	}
	
	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if(data==null){
			return;
		}
		Result result=(Result)data;
		if("200".equals(result.getCode())){
			Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
			UserInfo info=app.getUserInfo();
			info.setPassward(p1str);
			app.saveUserInfoToShared(info);
			UpdatePasswordActivity.this.finish();
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		old_pwd.clearFocus();
		new_pwd.clearFocus();
		new_pwd_again.clearFocus();
	}

}
