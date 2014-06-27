package com.sary.arounddepot.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.sary.arounddepot.R;

public class VoiceTypeSelectActivity extends BaseActivity{
	
	private ImageButton btn_back;
	
	private RelativeLayout voice_set,voice_set_girl;
	
	private ImageButton boy_voice_select,girls_voice_select;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicetypeselect);
		initView();
		setListener();
	}
	private void initView(){
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		voice_set=(RelativeLayout)findViewById(R.id.voice_set);
		voice_set_girl=(RelativeLayout)findViewById(R.id.voice_set_girl);
		boy_voice_select=(ImageButton)findViewById(R.id.boy_voice_select);
		girls_voice_select=(ImageButton)findViewById(R.id.girls_voice_select);
		if(mPrefHelper.readInt("vioce_type")==0){
			boy_voice_select.setVisibility(View.VISIBLE);
			girls_voice_select.setVisibility(View.GONE);
		}else{
			girls_voice_select.setVisibility(View.VISIBLE);
			boy_voice_select.setVisibility(View.GONE);
		}
	}
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VoiceTypeSelectActivity.this.setResult(22);
				VoiceTypeSelectActivity.this.finish();
			}
		});
		voice_set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boy_voice_select.setVisibility(View.VISIBLE);
				girls_voice_select.setVisibility(View.GONE);
				mPrefHelper.save("vioce_type", 0);
			}
		});
		voice_set_girl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				girls_voice_select.setVisibility(View.VISIBLE);
				boy_voice_select.setVisibility(View.GONE);
				mPrefHelper.save("vioce_type", 1);
			}
		});
	}

}
