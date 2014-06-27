package com.sary.arounddepot.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.AboutDepotActivity;
import com.sary.arounddepot.activity.AboutNavigationSetActivity;
import com.sary.arounddepot.activity.AboutVoiceSetActivity;
import com.sary.arounddepot.view.FlipperLayout.OnOpenListener;

public class Set {
	
    private Context mContext;
	
	private View mSet;
	
	private OnOpenListener mOnOpenListener;
	
	private RelativeLayout about_navigation,about_speech,about_park;
	
	private ImageButton btn_back;
	
	public Set(Context context) {
		mContext = context;
		mSet = LayoutInflater.from(context).inflate(R.layout.set, null);
		initView();
		setListener();
	}
	
	private void initView(){
		btn_back=(ImageButton)mSet.findViewById(R.id.btn_back);
		about_navigation=(RelativeLayout)mSet.findViewById(R.id.about_navigation);
		about_park=(RelativeLayout)mSet.findViewById(R.id.about_park);
		about_speech=(RelativeLayout)mSet.findViewById(R.id.about_speech);
	}
	
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v){
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		about_navigation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent navigation=new Intent();
				navigation.setClass(mContext, AboutNavigationSetActivity.class);
				mContext.startActivity(navigation);
			}
		});
		about_park.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent navigation=new Intent();
				navigation.setClass(mContext, AboutDepotActivity.class);
				mContext.startActivity(navigation);
			}
		});
		about_speech.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent navigation=new Intent();
				navigation.setClass(mContext, AboutVoiceSetActivity.class);
				mContext.startActivity(navigation);
			}
		});
	}
	
	public View getView() {
		return mSet;
	}

	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

}
