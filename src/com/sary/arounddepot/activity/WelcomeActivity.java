package com.sary.arounddepot.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sary.arounddepot.R;
import com.sary.arounddepot.util.MyScrollLayout;
import com.sary.arounddepot.util.OnViewChangeListener;

public class WelcomeActivity extends BaseActivity implements OnViewChangeListener{
	
	private MyScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private LinearLayout mainRLayout;
	private LinearLayout pointLLayout;
	private ImageButton btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		initView();
		setListener();
	}
	
	private void initView(){
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		mScrollLayout  = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		mainRLayout = (LinearLayout) findViewById(R.id.mainRLayout);
		pointLLayout =(LinearLayout)findViewById(R.id.lays);
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
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WelcomeActivity.this.finish();
			}
		});
	}

	@Override
	public void OnViewChange(int view) {
		// TODO Auto-generated method stub
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

}
