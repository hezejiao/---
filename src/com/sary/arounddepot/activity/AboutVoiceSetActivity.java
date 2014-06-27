package com.sary.arounddepot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sary.arounddepot.R;

public class AboutVoiceSetActivity extends BaseActivity{
	
	private ImageButton btn_back;
	private Button voice;
	private RelativeLayout voice_sets;
	private TextView voice_type;
	private boolean flag=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_set);
		initView();
		setListener();
	}
	
	private void initView(){
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		voice=(Button)findViewById(R.id.voice_btn);
		voice_sets=(RelativeLayout)findViewById(R.id.voice_sets);
		voice_type=(TextView)findViewById(R.id.voice_type);
		if(mPrefHelper.readInt("have_voice")==0){
			voice.setBackgroundDrawable(getResources().getDrawable(R.drawable.on));
		}else{
			voice.setBackgroundDrawable(getResources().getDrawable(R.drawable.off));
		}
		if(mPrefHelper.readInt("vioce_type")==0){
			voice_type.setText("男声");
		}else{
			voice_type.setText("女声");
		}
		
	}
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AboutVoiceSetActivity.this.finish();
			}
		});
		voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(flag){
					voice.setBackgroundDrawable(getResources().getDrawable(R.drawable.off));
					mPrefHelper.save("have_voice", 1);
					flag=false;
				}else{
					voice.setBackgroundDrawable(getResources().getDrawable(R.drawable.on));
					mPrefHelper.save("have_voice", 0);
					flag=true;
				}
				
			}
		});
		voice_sets.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(AboutVoiceSetActivity.this, VoiceTypeSelectActivity.class);
				startActivityForResult(intent, 21);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==21){
			int voice_types=mPrefHelper.readInt("vioce_type");
			if(voice_types==0){
				voice_type.setText("男声");
			}
			if(voice_types==1){
				voice_type.setText("女生");
			}
		}
	}

}
