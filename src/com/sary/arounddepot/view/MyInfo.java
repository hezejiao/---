package com.sary.arounddepot.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.MainActivity;
import com.sary.arounddepot.activity.MyCareDepotActivity;
import com.sary.arounddepot.activity.UpdatePasswordActivity;
import com.sary.arounddepot.activity.UpdateUserInfoActivity;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.PrefHelper;
import com.sary.arounddepot.view.Desktop.onChangeViewListener;
import com.sary.arounddepot.view.FlipperLayout.OnOpenListener;

public class MyInfo {
	
	private Context mContext;
	
	private View mMyInfo;
	
	public PrefHelper mPrefHelper;// SharedPreferences数据存储
	
	private OnOpenListener mOnOpenListener;
	
	private MyApplication mApplication;
	
	private UserInfo userInfo;
	
	public TextView user_name;
	
	private Button back_to_account,btn_back;
	
	private RelativeLayout edit_pwd,edit_userinfo,mycare;
	
	private onChangeViewListener mOnChangeViewListener;

	public MyInfo(Context context, MyApplication application) {
		mApplication = application;
		mContext = context;
		Log.i("tag", "进来了");
		mMyInfo = LayoutInflater.from(context).inflate(R.layout.myinfo, null);
		initView();
	}
	
	public void initView(){
		mPrefHelper = PrefHelper.getInstance(mContext);
		user_name=(TextView)mMyInfo.findViewById(R.id.user_name);
		back_to_account=(Button)mMyInfo.findViewById(R.id.back_to_account);
		btn_back=(Button)mMyInfo.findViewById(R.id.btn_back);
		edit_pwd=(RelativeLayout)mMyInfo.findViewById(R.id.edit_pwd);
		edit_userinfo=(RelativeLayout)mMyInfo.findViewById(R.id.edit_userinfo);
		mycare=(RelativeLayout)mMyInfo.findViewById(R.id.synchro);
		userInfo=mApplication.getUserInfoFromShared();
		if(userInfo!=null){
			user_name.setText(userInfo.getUsername());
		}
		setListener();
	}
	
	
	
	private void setListener(){
		back_to_account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext)
				.setTitle(mContext.getResources().getString(R.string.exit))
				.setMessage(
						mContext.getResources().getString(
								R.string.exit_message2))
				.setPositiveButton(
						mContext.getResources().getString(R.string.exit_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mApplication.clearUserInfo();
								if (mApplication.getId()== null) {
								((MainActivity)mContext).mRoot.close(((MainActivity)mContext).mView.getView());
								} else {
									Toast.makeText(mContext, "注销失败", Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton(
						mContext.getResources().getString(R.string.exit_cancle),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).show();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		edit_pwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mPrefHelper.readInt("three_login")==1){
					DialogUtil.showAlert(mContext, "第三方账号登陆不支持修改操作");
					return ;
				}
				Intent mycaredepot=new Intent();
				mycaredepot.setClass(mContext, UpdatePasswordActivity.class);
				mContext.startActivity(mycaredepot);
			}
		});
		edit_userinfo.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent mycaredepot=new Intent();
				mycaredepot.setClass(mContext, UpdateUserInfoActivity.class);
				mContext.startActivity(mycaredepot);
			}
		});
		mycare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent mycaredepot=new Intent();
				mycaredepot.setClass(mContext, MyCareDepotActivity.class);
				mContext.startActivity(mycaredepot);
			}
		});
	}
	
	public View getView(){
		return mMyInfo;
	}
	
	/**
	 * 界面修改方法
	 * 
	 * @param onChangeViewListener
	 */
	public void setOnChangeViewListener(
			onChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}

	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

}
