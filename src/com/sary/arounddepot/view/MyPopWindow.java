package com.sary.arounddepot.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sary.arounddepot.R;

public class MyPopWindow extends PopupWindow{
	
	public Button btn_edit_care, btn_remove_care, btn_cancel;
	private View mMenuView;
	
	public MyPopWindow(Context context){
		Log.i("tag", "进入popwindow初始化");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_popwindow, null);
		btn_edit_care=(Button)mMenuView.findViewById(R.id.btn_edit_care);
		btn_remove_care=(Button)mMenuView.findViewById(R.id.btn_remove_care);
		btn_cancel=(Button)mMenuView.findViewById(R.id.btn_cancel);
		//取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {
		    public void onClick(View v){
						//销毁弹出框
						dismiss();
					}
				});
	    //设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stat));
		// 设置允许在外点击消失 
		this.setOutsideTouchable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
	}

}
