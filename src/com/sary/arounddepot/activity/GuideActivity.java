package com.sary.arounddepot.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.MyScrollLayout;
import com.sary.arounddepot.util.OnViewChangeListener;

public class GuideActivity extends BaseActivity implements OnViewChangeListener{
	private boolean isFirstIn = true;
	private MyScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private Button startBtn;
	private RelativeLayout mainRLayout;
	private LinearLayout pointLLayout;
	private MyApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.app=(MyApplication) getApplication();
		setContentView(R.layout.activity_guide);
		initView();
		setListener();
	}
	private void initView(){
		mScrollLayout  = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		mainRLayout = (RelativeLayout) findViewById(R.id.mainRLayout);
		pointLLayout =(LinearLayout)findViewById(R.id.lays);
		startBtn = (Button) findViewById(R.id.startBtn);
		count = mScrollLayout.getChildCount();
		System.out.println("count:"+count);
		imgs = new ImageView[count];
		for(int i = 0; i< count;i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}
	
	private void setListener(){
		startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    mPrefHelper.save(Constant.FIRST_RUN, false);
				Intent intent=new Intent(GuideActivity.this,SplashScreenActivity.class);
				startActivity(intent);
				GuideActivity.this.finish();
			}
		});
	}

	@Override
	public void OnViewChange(int view) {
		
		setcurrentPoint(view);
	}
	private void setcurrentPoint(int position) {
		if(position < 0 || position > count -1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (int i = 0; i < imgs.length; i++){
			ImageView mWelcomeImg=imgs[i];
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
}
