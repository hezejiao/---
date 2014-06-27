package com.sary.arounddepot.activity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.Result;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;

public class ForgetPwdActivity extends BaseActivity{
	
	private Button submit;
	
	private ImageButton btn_back;
	
	private EditText acount;
	
	private String email;
	
	private ProgressDialog mDialog;//搜索的进度条
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_pwd);
		initView();
		setListener();
	}
	
	private void initView(){
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		submit=(Button)findViewById(R.id.submit);
		acount=(EditText)findViewById(R.id.email);
	}
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ForgetPwdActivity.this.finish();
				
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getPwd();
			}
		});
	}
	private void getPwd(){
		email=acount.getText().toString();
		if(email==null ||"".equals(email)){
			Toast.makeText(getApplicationContext(), 
					"请输入邮箱地址", Toast.LENGTH_SHORT).show();
			return ;
		}
		findPwd(email);
	}
	
	private void findPwd(String email){
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("email",email));
		boolean result = sendRequest(Constant.FORGET, key);
		if(result){
			mDialog = DialogUtil.showProgress(this, "邮件发送中...", null);
		}
	}
	
	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if(data==null){
			Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
			return;
		}
		Result result=(Result)data;
		if("200".equals(result.getCode())){
			Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
			ForgetPwdActivity.this.finish();
			}
	}

}
