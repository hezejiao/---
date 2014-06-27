package com.sary.arounddepot.activity;

import com.sary.arounddepot.R;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MyNewsWebViewActivity extends BaseActivity{
	
    private WebView webview;
	
	private WebSettings ws;
	
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_news_webview);
		Bundle b1 = getIntent().getExtras();
		url = b1.getString("url");
		//实例化WebView对象 
		webview=(WebView)findViewById(R.id.webview);
		ws = webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(true);//放大按钮
		ws.setSupportZoom(true);//是否支持放大
		webview.loadUrl(url);
	}

}
