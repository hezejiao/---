package com.sary.arounddepot.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.sary.arounddepot.R;
import com.sary.arounddepot.util.DialogUtil;

public class About_us_WebviewActivity extends BaseActivity{
	
	private WebView webview;
	
	private ImageButton btn_back;
	
	private WebSettings ws;
	
	public ProgressDialog mDialog;//搜索的进度条
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.us_webview);
		initView();
	}
	
	private void initView(){
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		webview=(WebView)findViewById(R.id.webview);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				About_us_WebviewActivity.this.finish();
			}
		});
		initInfoWebView("http://www.591park.com");
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
	    webview.setWebViewClient(new WebViewC());
	    webview.loadUrl(url);
		progress();
	
	}
	
	class WebViewC extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler)
		{
			handler.proceed();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			ws.setBlockNetworkImage(false);
			super.onPageFinished(view, url);
			if (mDialog != null) {
				mDialog.cancel();
				mDialog = null;
			}
		}
	}
	
	public void progress()
	{
		mDialog = DialogUtil.showProgress(this, "正在加载...", null);
	}
	
	/**
	 * 返回上一个Html
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(webview != null && event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
            case KeyEvent.KEYCODE_BACK:
                if(webview.canGoBack() == true){
                	webview.goBack();
                }else{
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		webview.clearCache(true);
		super.onDestroy();
	}
	

}
