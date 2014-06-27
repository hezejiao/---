package com.sary.arounddepot.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.SearchFragment.TitlesListFragmentCallBack;
import com.sary.arounddepot.adapter.SearchAdapter;
import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.resolver.BaseResolver;
import com.sary.arounddepot.resolver.ResolverFactory;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.PrefHelper;
import com.sary.arounddepot.util.StringUtil;

public class SearchParkActivity extends FragmentActivity implements
		TitlesListFragmentCallBack {

	private ViewPager viewPager;// 页卡内容

	private List<Fragment> views;// Tab页面列表

	private int currIndex = 0;// 当前页卡编号

	private Fragment view1, view2;

	public AutoCompleteTextView autoCompleteTextView;

	public String serchkey;

	public boolean mNetConnected;// 网络状态是否连接

	private static AsyncHttpClient client = new AsyncHttpClient();// 异步线程访问网络

	private AroundParkListData listData;

	private Button btn_cancel;

	ViewPagerAdapter viewPagerAdapter;

	public SearchAdapter sAdapter;

	private TextView tip;

	public ArrayAdapter<String> adapter_history;
	public String[] hisArrays;

	public LinearLayout first_in;
	
	public ImageView move_image;

	private boolean isFirstImage = true;

	public PrefHelper mPrefHelper;// SharedPreferences数据存储

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_dialog);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		NetworkInfo network = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if ((network == null) || (!network.isAvailable())) {
			mNetConnected = false;
		} else {
			mNetConnected = true;
		}
		initViewPager();
		setListener();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		if (!isFirstImage){
//			autoCompleteTextView.setFocusable(true);
//		}
	}
	

	// 初始化页卡内容
	private void initViewPager() {
		mPrefHelper = PrefHelper.getInstance(getApplicationContext());
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		views = new ArrayList<Fragment>();
		view1 = new SearchFragment();
		view2 = new HotShopFragment();
		views.add(view1);
		views.add(view2);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new PageChangeLisener());
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_content);
		autoCompleteTextView.setThreshold(1);
		autoCompleteTextView.addTextChangedListener(new TextChangeListener());
		autoCompleteTextView.setText(((SearchFragment) view1).text);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		tip = (TextView) findViewById(R.id.tip);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});
		move_image=(ImageView) findViewById(R.id.move_image);
		first_in = (LinearLayout) findViewById(R.id.search_first_in);
		isFirstImage = mPrefHelper.readBoolean(Constant.FIRST_SEARCH);
		if (isFirstImage) {
//			autoCompleteTextView.setFocusable(false);
			first_in.setVisibility(View.VISIBLE);
			Animation mAnimation =  AnimationUtils.loadAnimation(this, R.anim.search_translate);
			move_image.startAnimation(mAnimation);
		}
	}

	private void setListener() {
		first_in.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				first_in.setVisibility(View.GONE);
				mPrefHelper.save(Constant.FIRST_SEARCH, false);
			}
		});
	}

	private void Save() {
		String text = autoCompleteTextView.getText().toString();
		SharedPreferences sp = getSharedPreferences("history_strs", 0);
		String save_Str = sp.getString("history", "");
		String[] hisArrays = save_Str.split(",");
		for (int i = 0; i < hisArrays.length; i++) {
			if (hisArrays[i].equals(text)) {
				return;
			}
		}
		StringBuilder sb = new StringBuilder(save_Str);
		sb.append(text + ",");
		sp.edit().putString("history", sb.toString()).commit();
	}
	
	
	class ViewPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> views;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			views = SearchParkActivity.this.views;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return views.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

	}

	class PageChangeLisener implements OnPageChangeListener {
		// int one = offset * 2 + bmpw;// 页卡1 -> 页卡2 偏移量
		// int two = offset * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub

		}

	}

	public class TextChangeListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			serchkey = autoCompleteTextView.getText().toString();
			Log.i("tag", "搜索字段：" + serchkey);

			if (serchkey == null || "".equals(serchkey.trim())) {
				return;
			}
			getSearchResult(serchkey);
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	}

	private void getSearchResult(String content) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("keyword", content));
		boolean result = sendRequest(Constant.GET_SEARCH, key, null);
		// if(result){
		// mDialog = DialogUtil.showProgress(this, "数据加载中...", null);
		// }
	}

	protected void handleNearByMsg(int msg, BaseData data) {
		tip.setVisibility(View.VISIBLE);
		if (data == null) {
			tip.setText("未匹配到相应结果");
			return;
		}
		((SearchFragment) view1).clean_hostorys.setVisibility(View.GONE);
		listData = (AroundParkListData) data;
		tip.setText("搜索结果:");
		if (listData.datalist.size() == 0) {
			if (view1 == viewPagerAdapter.getItem(viewPager.getCurrentItem())) {
				((SearchFragment) view1).listview.setVisibility(View.GONE);
				((SearchFragment) view1).my_search_txt
						.setVisibility(View.VISIBLE);
			}
			if (view2 == viewPagerAdapter.getItem(viewPager.getCurrentItem())) {
				((HotShopFragment) view2).hotList.setVisibility(View.GONE);
				((HotShopFragment) view2).my_search_txt
						.setVisibility(View.VISIBLE);
			}
			return;
		} else {
			if (view1 == viewPagerAdapter.getItem(viewPager.getCurrentItem())) {
				((SearchFragment) view1).listview.setVisibility(View.VISIBLE);
				((SearchFragment) view1).my_search_txt.setVisibility(View.GONE);
			}
			if (view2 == viewPagerAdapter.getItem(viewPager.getCurrentItem())) {
				((HotShopFragment) view2).hotList.setVisibility(View.VISIBLE);
				((HotShopFragment) view2).my_search_txt
						.setVisibility(View.GONE);
			}
		}
		if (sAdapter == null) {
			sAdapter = new SearchAdapter(this, listData.datalist);
		} else {
			sAdapter.setAroundParkList(listData.datalist);
		}
		Log.v("tag", viewPagerAdapter.getItem(viewPager.getCurrentItem())
				.getClass().toString());
		if (view1 == viewPagerAdapter.getItem(viewPager.getCurrentItem())) {
			((SearchFragment) view1).listview.setAdapter(sAdapter);
			((SearchFragment) view1).listview
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long arg3) {
							AroundParkData aroundParkData = (AroundParkData) parent
									.getItemAtPosition(position);
							Intent intent = new Intent();
							Bundle b = new Bundle();
							b.putSerializable("aroundpark", aroundParkData);
							intent.putExtras(b);
							SearchParkActivity.this.setResult(17, intent);
							Save();
							finish();
						}
					});
		}
		if (view2 == viewPagerAdapter.getItem(viewPager.getCurrentItem())) {
			((HotShopFragment) view2).hotList.setAdapter(sAdapter);
			((HotShopFragment) view2).hotList
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long arg3) {
							AroundParkData aroundParkData = (AroundParkData) parent
									.getItemAtPosition(position);
							Intent intent = new Intent();
							Bundle b = new Bundle();
							b.putSerializable("aroundpark", aroundParkData);
							intent.putExtras(b);
							SearchParkActivity.this.setResult(17, intent);
							Save();
							finish();
						}
					});
		}

		// ((SearchFragment)view1).searchAdapter.setAroundParkList(listData.datalist);

		if (!"200".equals(listData.code)) {
			Toast.makeText(getBaseContext(), "数据加载出错，请重试", Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}

	// 通过此方法获得后台的数据
	protected boolean sendRequest(int msg, ArrayList<NameValuePair> getParam,
			ArrayList<NameValuePair> postParam) {
		System.out.println("yyyyyyyyyyyy");
		if (!mNetConnected) {
			Toast.makeText(getApplicationContext(), "未检测到网络，请检测网络连接",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		Log.e("tag", "msg" + msg);
		BaseResolver resolver = ResolverFactory.getResolver(msg);
		Log.e("tag", "resolver" + resolver);
		String url = resolver.getRequestURL() + "?";// 通过解析工厂得到实例后然后拿到访问的URL
		String strHash = "";// annel的值为test2,后台判断用哪个秘钥。也参与加密
		for (int i = 0; i < getParam.size(); i++) {
			NameValuePair param = (NameValuePair) getParam.get(i);
			if (i == 0) {
				url += param.getName();
			} else {
				url += "&" + param.getName();
			}

			url += "=" + param.getValue();
			strHash += param.getValue();
		}
		strHash += Constant.CHANNEL;
		Log.e("tag", strHash);
		// strHash = StringUtil.toUTF8(strHash);//先UTF-8编码
		strHash = StringUtil.urlEncode(strHash);
		Log.e("tag", strHash);
		strHash += "@" + Constant.BASIC_KEY;// 加入秘钥

		Log.e("tag", strHash);

		strHash = StringUtil.encrypt(strHash, "SHA");// SHA1加密
		strHash = StringUtil.encrypt(strHash, "MD5");// MD5加密
		strHash = strHash.toUpperCase();// 变为大写
		url += "&channel=" + Constant.CHANNEL;
		url += "&sign=" + strHash;
		Log.e("base", "=====" + url);
		// url =
		// "http://192.168.50.251/phone/parklist?location=121.437441,31.184826";
		if (postParam == null) {
			client.get(url, new RequestHandler(msg));
		} else {
			RequestParams req = new RequestParams();
			for (int i = 0; i < postParam.size(); i++) {
				NameValuePair param = postParam.get(i);
				req.put(param.getName(), param.getValue());
			}
			client.post(url, req, new RequestHandler(msg));
		}
		return true;
	}

	// 请求的处理
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
				data = resolver.parseData(response);// 解析数据
				System.out.println("zzzzzzzzzzzzzzzz");
			} catch (JSONException e) {
				Log.e("tag", e.toString());
				Log.e("tag", "content:" + response);
			}

			handleNearByMsg(mMessage, data);
		}

		@Override
		public void onFailure(Throwable e, String response) {
			Log.e("tag", "onFailure");
			Log.e("tag", "response" + response);
			e.printStackTrace();
			mNetConnected = false;
			Toast.makeText(SearchParkActivity.this, "网络连接失败，请稍后重试",
					Toast.LENGTH_SHORT).show();
			handleNearByMsg(mMessage, null);
		}
	}

	@Override
	public void onItemSelected(String index) {
		Log.i("tag", "index:" + index);
		autoCompleteTextView.setText(index);
	}
}
