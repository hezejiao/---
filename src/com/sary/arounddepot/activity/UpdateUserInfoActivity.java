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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.Result;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;

public class UpdateUserInfoActivity extends BaseActivity {

	private MyApplication app;
	private Context context;
	private EditText nickname;
	private UserInfo userInfo;
	private Button to_save;
	private ImageButton btn_back;
	private String nicknames = "", userId = "";
	private int user_sex = -1;
	private ProgressDialog mDialog;// 搜索的进度条
	// private ToggleButton sex;
	private Button sex;
	private boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_userinfo);
		app = (MyApplication) getApplication();
		userInfo = app.getUserInfoFromShared();
		context = getBaseContext();
		initView();
		setListener();
		if (userInfo != null) {
			userId = userInfo.getId();
			if(!"null".equals(userInfo.getNickname())){
				nickname.setText(userInfo.getNickname());
			}
			if (userInfo.getSex() == 1){
				sex.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.boy));
				user_sex=1;
			} else {
				sex.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.girl));
				user_sex=0;
			}
		}
	}

	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		to_save = (Button) findViewById(R.id.to_saves);
		nickname = (EditText) findViewById(R.id.nickname);
		sex = (Button)findViewById(R.id.sex);
	}

	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UpdateUserInfoActivity.this.finish();
			}
		});
		sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					sex.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.boy));
					user_sex=1;
					flag = false;
				} else {
					sex.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.girl));
					user_sex=0;
					flag = true;
				}

			}
		});
		// sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// if(isChecked){
		// user_sex=1;
		// sex.setBackgroundDrawable(getResources().getDrawable(R.drawable.boy));
		// }else{
		// user_sex=0;
		// sex.setBackgroundDrawable(getResources().getDrawable(R.drawable.girl));
		// }
		// }
		// });
		to_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveUserInfo();
			}
		});

	}

	private void saveUserInfo() {
		nicknames = nickname.getText().toString();
		if (userInfo.getNickname().equals(nicknames) && user_sex==userInfo.getSex()) {
			Toast.makeText(context, "请修改昵称或性别", Toast.LENGTH_SHORT).show();
			return;
		}
		updateUserInfo(nicknames, userId, user_sex + "");
	}

	private void updateUserInfo(String nickName, String userId, String sex) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId", userId));
		key.add(new BasicNameValuePair("nickName", nickName));
		key.add(new BasicNameValuePair("sex", sex));
		boolean result = sendRequest(Constant.UPDATE_USER, key);
		if (result) {
			mDialog = DialogUtil.showProgress(this, "正在保存...", null);
		}
	}

	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if (data == null) {
			return;
		}
		Result result = (Result) data;
		if ("200".equals(result.getCode())) {
			Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
			UserInfo info = app.getUserInfo();
			info.setNickname(nicknames);
			info.setSex(user_sex);
			app.saveUserInfoToShared(info);
			UpdateUserInfoActivity.this.finish();
		}
	}

}
