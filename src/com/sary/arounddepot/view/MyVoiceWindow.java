package com.sary.arounddepot.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.sary.arounddepot.R;

public class MyVoiceWindow extends PopupWindow{
	
	public Button btn_record,player,send,stop;
	private View mMenuView;
	public LinearLayout record_type1,record_type2;
	
	public MyVoiceWindow(Context context){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.voice_popwindow, null);
		btn_record=(Button)mMenuView.findViewById(R.id.record_sounce);
		player=(Button)mMenuView.findViewById(R.id.player);
		send=(Button)mMenuView.findViewById(R.id.send);
		record_type1=(LinearLayout)mMenuView.findViewById(R.id.record_type1);
		record_type2=(LinearLayout)mMenuView.findViewById(R.id.record_type2);
		stop=(Button)mMenuView.findViewById(R.id.stop);
		
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stat));
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置允许在外点击消失 
		this.setOutsideTouchable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
	}

}
