package com.sary.arounddepot.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.adapter.MyNewsAdapter;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.MyNewsInfo;
import com.sary.arounddepot.entity.MyNewsListData;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.view.FlipperLayout.OnOpenListener;
import com.sary.arounddepot.view.XListView.IXListViewListener;

public class MyNews extends MyBasicView implements IXListViewListener{
	
    private Context mContext;
	
	private View mNews;
	
	private Activity mActivity;
	
	private MyNewsListData listData;
	
	private List<MyNewsInfo> list=new ArrayList<MyNewsInfo>();
	
	private MyNewsInfo info;
	
	private MyNewsAdapter adapter;
	
	private OnOpenListener mOnOpenListener;
	
	private MyApplication app;
	
	private ImageButton btn_back;
	
	public XListView mListView;
	
	private ProgressDialog mDialog;//搜索的进度条
	
	private Handler mHandler;
	
	private int start = 0;
	
	private static int refreshCnt = 0;

	public MyNews(Context context,Activity activity,MyApplication app){
		super(app, context, activity);
		this.app=app;
		mContext = context;
		mActivity=activity;
		mNews = LayoutInflater.from(context).inflate(R.layout.mynews, null);
		initView();
	}
	
	public void initView(){
		btn_back=(ImageButton)mNews.findViewById(R.id.btn_back);
		mListView=(XListView)mNews.findViewById(R.id.list_news);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
		adapter=new MyNewsAdapter(mContext);
		getMyNewsInfo();
		setListener();
	}
	
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
	}
	
	private void getMyNewsInfo(){
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		boolean result = sendRequest(Constant.NEWS, key);
		if(result){
			mDialog = DialogUtil.showProgress(mContext, "数据加载中...", null);
		}
	}
	@Override
	protected void handleNearByMsg(int msg, BaseData data){
		
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if(data==null){
			return;
		}
		listData=(MyNewsListData)data;
		if(!"200".equals(listData.code)){
			Toast.makeText(mContext, "数据加载出错，请重试", Toast.LENGTH_SHORT).show();
			return;
		}
		if(listData.datalist.size()==0){
			Toast.makeText(mContext, "目前暂无消息", Toast.LENGTH_SHORT).show();
			return;
		}
		geneItems();
		adapter.setNewsInfoList(list);
		mListView.setAdapter(adapter);
	    mListView.setXListViewListener(this);
	    mHandler = new Handler();
	}
	
	public View getView() {
		return mNews;
	}

	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
	
	private void geneItems(){
		for (int i = 0; i != 15; ++i){
			if(start<listData.datalist.size()){
				list.add(listData.datalist.get(start++));
			}
		}
	}

	@Override
	public void onRefresh() {
		 mListView.setPullLoadEnable(false);
			mHandler.postDelayed(new Runnable(){
				@Override
				public void run(){
					start=0;
					list.clear();
					geneItems();
//					adapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.list_item, items);
					adapter.setNewsInfoList(list);
					mListView.setAdapter(adapter);
					onLoad();
				}
			}, 2000);
	}

	@Override
	public void onLoadMore(){
		
	}
	
	private void onLoad(){
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(new Date().toLocaleString());
	}

}
