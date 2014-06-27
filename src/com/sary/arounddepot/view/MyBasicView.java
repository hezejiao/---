package com.sary.arounddepot.view;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.autonavi.aps.amapapi.Const;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.resolver.BaseResolver;
import com.sary.arounddepot.resolver.ResolverFactory;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.PrefHelper;
import com.sary.arounddepot.util.StringUtil;

public class MyBasicView{
	
    private static AsyncHttpClient client = new AsyncHttpClient();//异步线程访问网络
	
	public boolean mNetConnected;//网络状态是否连接
	
	protected  PrefHelper mPrefHelper;//SharedPreferences数据存储
	
	protected MyApplication app;
	
	private Activity macActivity;
	
	private Context mContext;
	
	public MyBasicView(MyApplication application, Context context,
			Activity activity) {
		mContext = context;
		this.app=application;
		macActivity=activity;
	}
	
	/*
	 * 以下是异步网络请求的数据操作（发送-接收-数据解析）
	 */
	    protected void handleNearByMsg(int msg, BaseData data) {
		}
		
		protected boolean sendRequest(int msg, ArrayList<NameValuePair> key) {
			return sendRequest(msg, key, null);
		}

	//通过此方法获得后台的数据
		protected boolean sendRequest(int msg, ArrayList<NameValuePair> getParam, ArrayList<NameValuePair> postParam){
			
			NetworkInfo network = ((ConnectivityManager)mContext.getSystemService(mContext.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
			if ((network == null) || (!network.isAvailable())){			
				mNetConnected = false;
			}
			else{			
				mNetConnected = true;
			}
			System.out.println("yyyyyyyyyyyy");
			if(!mNetConnected){
				Toast.makeText(mContext, "未检测到网络，请检测网络连接", Toast.LENGTH_SHORT).show();
				return false;
			}
			Log.e("tag", "msg"+msg);
			BaseResolver resolver = ResolverFactory.getResolver(msg);
			Log.e("tag", "resolver"+resolver);
			String url = resolver.getRequestURL()+"?";//通过解析工厂得到实例后然后拿到访问的URL
			
			String strHash = "";//annel的值为test2,后台判断用哪个秘钥。也参与加密

//			Iterator iter = getParam.iterator();
//			while (iter.hasNext()) {
//				NameValuePair param = (NameValuePair) iter.next();
//				url=
//				url += "&"+param.getName();
//			    url += "="+param.getValue();
//			}
			for(int i =0;i<getParam.size();i++){
				NameValuePair param = (NameValuePair) getParam.get(i);
				if(i==0){
					url += param.getName();
				}else{
					url += "&"+param.getName();
				}
				
			    url += "="+param.getValue();
			    strHash += param.getValue();
			}
			strHash +=Constant.CHANNEL;
			Log.e("tag", strHash);
//			strHash = StringUtil.toUTF8(strHash);//先UTF-8编码
			strHash = StringUtil.urlEncode(strHash);
			Log.e("tag", strHash);
			strHash += "@"+Constant.BASIC_KEY;//加入秘钥
			
			Log.e("tag", strHash);
			
			strHash = StringUtil.encrypt(strHash, "SHA");//SHA1加密
			strHash = StringUtil.encrypt(strHash, "MD5");//MD5加密
			strHash = strHash.toUpperCase();//变为大写		
			url += "&channel="+Constant.CHANNEL;
			url += "&sign="+strHash;

			Log.e("tag", "====="+url);
//			url = "http://192.168.50.251/phone/parklist?location=121.437441,31.184826";
			if (postParam == null){
				client.get(url, new RequestHandler(msg));
			}
			else {
				RequestParams req = new RequestParams();
				for (int i = 0; i < postParam.size(); i++) {
				    NameValuePair param = postParam.get(i);
				    req.put(param.getName(), param.getValue());
				}
				client.post(url, req, new RequestHandler(msg));
			}
			return true;
		}
		//请求的处理
		public class RequestHandler extends AsyncHttpResponseHandler {

			private int mMessage;
			
			RequestHandler(int msg) {
				mMessage = msg;
			}
			@Override
			public void onSuccess(String response) {
				Log.e("tag", "onSuccess");
				BaseData data = null;
				try {
					BaseResolver resolver = ResolverFactory.getResolver(mMessage);
					data = resolver.parseData(response);//解析数据
					System.out.println("zzzzzzzzzzzzzzzz");
				} catch (JSONException e) {
					Log.e("tag",e.toString());
					Log.e("tag","content:"+response);
				}
				
				handleNearByMsg(mMessage, data);
			}
			@Override
			 public void onFailure(Throwable e, String response) {
				Log.e("tag", "onFailure");
				Log.e("tag", "response"+response);
				e.printStackTrace();
				mNetConnected = false;
				Toast.makeText(mContext, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT).show();
				handleNearByMsg(mMessage, null);
			}
		}

}
