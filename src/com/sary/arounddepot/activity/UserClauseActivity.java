package com.sary.arounddepot.activity;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageButton;

import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.About_us_WebviewActivity.WebViewC;

public class UserClauseActivity extends BaseActivity{
	
	private WebView webview;
	
    private ImageButton btn_back;
	
	private WebSettings ws;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_clause);
		initView();
	}
	
	private void initView(){
		btn_back=(ImageButton)findViewById(R.id.user_back);
		webview=(WebView)findViewById(R.id.user_webview);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserClauseActivity.this.finish();
			}
		});
		initInfoWebView("file:///android_asset/index.html");
	}
	
	private void initInfoWebView(String url) {
		webview.setVerticalScrollBarEnabled(false);
		webview.setHorizontalScrollBarEnabled(false);
		ws = webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(true);//放大按钮
		ws.setSupportZoom(true);//是否支持放大
		
		int   screenDensity   = getResources().getDisplayMetrics(). densityDpi ;
	    WebSettings.ZoomDensity   zoomDensity   = WebSettings.ZoomDensity. MEDIUM ;
		switch (screenDensity){
		case   DisplayMetrics.DENSITY_LOW :
		    zoomDensity = WebSettings.ZoomDensity.CLOSE;
		      break ;
		case   DisplayMetrics.DENSITY_MEDIUM :
		    zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		      break ;
		case   DisplayMetrics.DENSITY_HIGH :
		    zoomDensity = WebSettings.ZoomDensity.FAR;
		      break ;
		}
		ws.setDefaultZoom(zoomDensity);
		
		ws.setBlockNetworkImage(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setAppCacheEnabled(true);
	    ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
	    webview.loadUrl(url);
	}
	
	//android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent( String param )
	{
	Intent intent = new Intent("android.intent.action.VIEW");
	intent.addCategory("android.intent.category.DEFAULT");
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	Uri uri = Uri.fromFile(new File(param ));
	intent.setDataAndType(uri, "application/pdf");
	return intent;
	}

}
