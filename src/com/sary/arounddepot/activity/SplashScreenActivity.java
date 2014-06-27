package com.sary.arounddepot.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.sary.arounddepot.R;
import com.sary.arounddepot.util.Constant;

public class SplashScreenActivity extends BaseActivity {
	
	private boolean isFirstIn = true;
	
	private ImageView mWelcomeImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isFirstIn=mPrefHelper.readBoolean(Constant.FIRST_RUN);
		if(isFirstIn){
			Log.e("tag", "第一次进来");
			Intent intent=new Intent(this,GuideActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		mWelcomeImg=(ImageView)findViewById(R.id.loading);
		setContentView(R.layout.splashscreen);
		new Handler().postDelayed(new Runnable() {

			public void run() {
				Intent intent = new Intent(SplashScreenActivity.this,
						MainActivity.class); // 从启动动画ui跳转到主ui
				startActivity(intent);
				SplashScreenActivity.this.finish(); // 结束启动动画界面
			}
		}, 800); // 启动动画持续时间
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		   if (mWelcomeImg != null) {
	            BitmapDrawable bmpDrawable = (BitmapDrawable) mWelcomeImg.getBackground();
	            if (bmpDrawable != null) {
	                Bitmap bmp = bmpDrawable.getBitmap();
	                mWelcomeImg.setBackgroundDrawable(null);
	                if (bmp != null && bmp.isRecycled() == false) {
	                    try {
	                        bmpDrawable.getBitmap().recycle(); // 这个生命周期会销毁View，因此在回收被View引用的控件时，也无需先对View引用图片的属性置为空
	                    } catch (Exception e) {
	                    }
	                }
	            }
	        }
	}

}
