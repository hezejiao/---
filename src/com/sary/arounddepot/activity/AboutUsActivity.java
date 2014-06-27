package com.sary.arounddepot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sary.arounddepot.R;

public class AboutUsActivity extends BaseActivity{
	
	private LinearLayout about_us_image;
	
	private ImageButton btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		about_us_image=(LinearLayout)findViewById(R.id.about_us_image);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		setListener();
	}
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AboutUsActivity.this.finish();
			}
		});
		about_us_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent us=new Intent(AboutUsActivity.this,About_us_WebviewActivity.class);
				startActivity(us);
			}
		});
	}

}
