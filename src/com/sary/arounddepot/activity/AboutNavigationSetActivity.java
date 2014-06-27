package com.sary.arounddepot.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sary.arounddepot.R;

public class AboutNavigationSetActivity extends BaseActivity{
	
	private ImageButton btn_back;
	
	private Context context;
	
	private RelativeLayout map_google,map_gaode,map_baidu,empty;
	
	private ImageButton btn_google_map,btn_gaode_map,
	
	btn_baidu_map,btn_empty;
	
	private List<String> name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_navigation_set);
		context=getBaseContext();
		name=isAvilible(context);
		initView();
		setNavigation(name);
		setListener();
		Log.i("tag", "已选择导航软件："+mPrefHelper.readInt("navigation"));
	}
	
	private void initView(){
		btn_empty=(ImageButton)findViewById(R.id.btn_empty);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		btn_google_map=(ImageButton)findViewById(R.id.btn_google_map);
		btn_gaode_map=(ImageButton)findViewById(R.id.btn_gaode_map);
		btn_baidu_map=(ImageButton)findViewById(R.id.btn_baidu_map);
		map_google=(RelativeLayout)findViewById(R.id.map_google);
		map_gaode=(RelativeLayout)findViewById(R.id.map_gaode);
		map_baidu=(RelativeLayout)findViewById(R.id.map_baidu);
		empty=(RelativeLayout)findViewById(R.id.empty);
		if(mPrefHelper.readInt("navigation")==1){
			btn_baidu_map.setBackgroundResource(R.drawable.navigation_overlay);
			btn_baidu_map.setVisibility(View.VISIBLE);
		}if(mPrefHelper.readInt("navigation")==2){
			btn_gaode_map.setBackgroundResource(R.drawable.navigation_overlay);
			btn_gaode_map.setVisibility(View.VISIBLE);
			
		}
		if(mPrefHelper.readInt("navigation")==3){
			btn_google_map.setBackgroundResource(R.drawable.navigation_overlay);
			btn_google_map.setVisibility(View.VISIBLE);
		}
		if(mPrefHelper.readInt("navigation")==0){
			btn_empty.setBackgroundResource(R.drawable.navigation_overlay);
			btn_empty.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		Log.i("tag", "进入。。。。");
//		name=isAvilible(context);
//		if(name.contains("com.baidu.BaiduMap")){
//			btn_baidu_map.setBackgroundResource(R.drawable.areadly_install);
//		}
//		if(name.contains("com.autonavi.minimap")){
//			btn_gaode_map.setBackgroundResource(R.drawable.areadly_install);
//		}
//		if(name.contains("com.google.android.apps.maps")){
//			btn_google_map.setBackgroundResource(R.drawable.areadly_install);
//		}
	}
	
	public void setNavigation(List<String> name){
		if(!name.contains("com.baidu.BaiduMap")){
			btn_baidu_map.setBackgroundResource(R.drawable.install);
		}
		if(!name.contains("com.autonavi.minimap")){
			btn_gaode_map.setBackgroundResource(R.drawable.install);
		}
		if(!name.contains("com.google.android.apps.maps")){
			btn_google_map.setBackgroundResource(R.drawable.install);
		}
	}
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AboutNavigationSetActivity.this.finish();
			}
		});
		map_google.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				btn_baidu_map.setVisibility(View.GONE);
//				btn_gaode_map.setVisibility(View.GONE);
				btn_empty.setBackgroundResource(R.drawable.a_empty);
				if(name.contains("com.google.android.apps.maps")){
					btn_google_map.setBackgroundResource(R.drawable.navigation_overlay);
					btn_google_map.setVisibility(View.VISIBLE);
					mPrefHelper.save("navigation", 3);
					if(name.contains("com.baidu.BaiduMap")){
						btn_baidu_map.setBackgroundResource(R.drawable.a_empty);
					}
					if(name.contains("com.autonavi.minimap")){
						btn_gaode_map.setBackgroundResource(R.drawable.a_empty);
					}
				}else{
					btn_google_map.setBackgroundResource(R.drawable.install);
					btn_google_map.setVisibility(View.VISIBLE);
					mPrefHelper.save("navigation", 0);
					new AlertDialog.Builder(AboutNavigationSetActivity.this)
					.setTitle(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.install_title))
					.setMessage(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.install_message))
					.setPositiveButton(
							AboutNavigationSetActivity.this.getResources().getString(R.string.exit_ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");//id为包名 
						                Intent it = new Intent(Intent.ACTION_VIEW, uri); 
						                startActivityForResult(it, 1); 
									} catch (Exception e) {
										Toast.makeText(context, "未检测到市场", Toast.LENGTH_LONG).show();
									}
								}
							})
					.setNegativeButton(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.exit_cancle),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();
				}
			}
		});
		map_gaode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("tag", "进入");
				btn_empty.setBackgroundResource(R.drawable.a_empty);
//				btn_google_map.setVisibility(View.GONE);
//				btn_baidu_map.setVisibility(View.GONE);
				if(name.contains("com.autonavi.minimap")){
					btn_gaode_map.setBackgroundResource(R.drawable.navigation_overlay);
					btn_gaode_map.setVisibility(View.VISIBLE);
					mPrefHelper.save("navigation", 2);
					if(name.contains("com.baidu.BaiduMap")){
						btn_baidu_map.setBackgroundResource(R.drawable.a_empty);
					}
					if(name.contains("com.google.android.apps.maps")){
						btn_google_map.setBackgroundResource(R.drawable.a_empty);
					}
				}else{
					btn_gaode_map.setBackgroundResource(R.drawable.install);
					btn_gaode_map.setVisibility(View.VISIBLE);
					mPrefHelper.save("navigation", 0);
					new AlertDialog.Builder(AboutNavigationSetActivity.this)
					.setTitle(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.install_title))
					.setMessage(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.install_message))
					.setPositiveButton(
							AboutNavigationSetActivity.this.getResources().getString(R.string.exit_ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");//id为包名 
						                Intent it = new Intent(Intent.ACTION_VIEW, uri); 
						                startActivityForResult(it, 2);  
									} catch (Exception e) {
										Toast.makeText(context, "未检测到市场", Toast.LENGTH_LONG).show();
									}
								}
							})
					.setNegativeButton(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.exit_cancle),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();
				}
			}
		});
		map_baidu.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
//				btn_google_map.setVisibility(View.GONE);
//				btn_gaode_map.setVisibility(View.GONE);
				btn_empty.setBackgroundResource(R.drawable.a_empty);
				if(name.contains("com.baidu.BaiduMap")){
					btn_baidu_map.setBackgroundResource(R.drawable.navigation_overlay);
					btn_baidu_map.setVisibility(View.VISIBLE);
					mPrefHelper.save("navigation", 1);
					if(name.contains("com.autonavi.minimap")){
						btn_gaode_map.setBackgroundResource(R.drawable.a_empty);
					}
					if(name.contains("com.google.android.apps.maps")){
						btn_google_map.setBackgroundResource(R.drawable.a_empty);
					}
				}else{
					btn_baidu_map.setBackgroundResource(R.drawable.install);
					btn_baidu_map.setVisibility(View.VISIBLE);
					mPrefHelper.save("navigation", 0);
					new AlertDialog.Builder(AboutNavigationSetActivity.this)
					.setTitle(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.install_title))
					.setMessage(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.install_message))
					.setPositiveButton(
							AboutNavigationSetActivity.this.getResources().getString(R.string.exit_ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");//id为包名 
						                Intent it = new Intent(Intent.ACTION_VIEW, uri); 
						                startActivityForResult(it, 3); 
									} catch (Exception e) {
										Toast.makeText(context, "未检测到市场", Toast.LENGTH_LONG).show();
									}
								}
							})
					.setNegativeButton(
							AboutNavigationSetActivity.this.getResources().getString(
									R.string.exit_cancle),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();
				}
			}
		});
		empty.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPrefHelper.save("navigation", 0);
				btn_empty.setVisibility(View.VISIBLE);
				btn_empty.setBackgroundResource(R.drawable.navigation_overlay);
				if(name.contains("com.autonavi.minimap")){
					btn_gaode_map.setBackgroundResource(R.drawable.a_empty);
				}
				if(name.contains("com.google.android.apps.maps")){
					btn_google_map.setBackgroundResource(R.drawable.a_empty);
				}
				if(name.contains("com.baidu.BaiduMap")){
					btn_baidu_map.setBackgroundResource(R.drawable.a_empty);
				}
//				btn_gaode_map.setBackgroundResource(R.drawable.a_empty);
//				btn_baidu_map.setBackgroundResource(R.drawable.a_empty);
//				btn_google_map.setBackgroundResource(R.drawable.a_empty);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		name=isAvilible(context);
		if(requestCode==1){
			if(name.contains("com.google.android.apps.maps")){
				btn_google_map.setBackgroundResource(R.drawable.navigation_overlay);
				btn_baidu_map.setBackgroundResource(R.drawable.a_empty);
				btn_gaode_map.setBackgroundResource(R.drawable.a_empty);
				btn_empty.setBackgroundResource(R.drawable.a_empty);
				mPrefHelper.save("navigation", 3);
			}
		}
		if(requestCode==2){
			if(name.contains("com.autonavi.minimap")){
				btn_gaode_map.setBackgroundResource(R.drawable.navigation_overlay);
				btn_baidu_map.setBackgroundResource(R.drawable.a_empty);
				btn_google_map.setBackgroundResource(R.drawable.a_empty);
				btn_empty.setBackgroundResource(R.drawable.a_empty);
				mPrefHelper.save("navigation", 2);
			}
		}if(requestCode==3){
			if(name.contains("com.baidu.BaiduMap")){
				btn_baidu_map.setBackgroundResource(R.drawable.navigation_overlay);
				btn_gaode_map.setBackgroundResource(R.drawable.a_empty);
				btn_google_map.setBackgroundResource(R.drawable.a_empty);
				btn_empty.setBackgroundResource(R.drawable.a_empty);
				mPrefHelper.save("navigation", 1);
			}
		}
	}
	
	private List<String> isAvilible(Context context){ 
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager 
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息 
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名 
       //从pinfo中将包名字逐一取出，压入pName list中 
            if(pinfo != null){ 
            for(int i = 0; i < pinfo.size(); i++){ 
                String pn = pinfo.get(i).packageName;
                Log.i("tag", "已安装的应用："+pn);
                pName.add(pn); 
            } 
        } 
//        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE 
          return pName;
  } 

}
