package com.sary.arounddepot.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.adapter.MyCareDepotAdapter;
import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.view.XListView;
import com.sary.arounddepot.view.XListView.IXListViewListener;

public class MyCareDepotActivity extends BaseActivity implements IXListViewListener{
	
	private MyCareDepotAdapter adapter;
	
	private AroundParkListData listData;
	
	public List<AroundParkData> list=new ArrayList<AroundParkData>();
	
	private MyApplication app;
	
	private Context context;
	
	private UserInfo info=null;
	
	private String userId;
	
    private Button edit_caredepot;
    
    private ImageButton btn_back;
    
    private ImageView my_listview_null;
	
	public XListView mListView;
	
	private AroundParkData parkData=new AroundParkData();
	
	private ProgressDialog mDialog;//搜索的进度条
	
	private Handler mHandler;
	
	private int start = 0;
	
	private boolean isEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_caredepot);
		app=(MyApplication)getApplication();
		context=getBaseContext();
		info=app.getUserInfoFromShared();
		if(info!=null){
			userId=info.getId();
		}
		initView();
		setListener();
	}
	
	//获得用户关注过的停车场
	private void getAttentionPark(String userId){
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId",userId));
		boolean result = sendRequest(Constant.ATTENTION_PARK, key);
//		if(result){
//			mDialog = DialogUtil.showProgress(this, "数据加载中...", null);
//		}
	}
	//获得某一个停车场详情
	private void getParkDetail(int pid){
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("pid",pid+""));
		boolean result = sendRequest(Constant.GET_DETAIL, key);
	}
		
	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (mDialog != null){
			mDialog.cancel();
			mDialog = null;
		}
		if(data==null){
			return;
		}
		if(msg==Constant.ATTENTION_PARK){
			
			listData=(AroundParkListData)data;
			Log.i("LXH","size:"+listData.datalist.size());
			if(!"200".equals(listData.code)){
				Toast.makeText(getApplicationContext(), "数据加载出错，请重试", Toast.LENGTH_SHORT).show();
				return;
			}
			if(listData.datalist.size()==0){
				mListView.setVisibility(View.GONE);
				my_listview_null.setVisibility(View.VISIBLE);
				edit_caredepot.setEnabled(false);
//				Toast.makeText(getApplicationContext(), "目前暂无关注的停车场，你可以手动添加", Toast.LENGTH_SHORT).show();
				return;
			}
			geneItems();
			adapter.setMyCareDepotList(list);
			adapter.notifyDataSetChanged();
			mListView.setAdapter(adapter);
		    mListView.setXListViewListener(this);
		    mHandler = new Handler();
		}if(msg==Constant.GET_DETAIL){
			AroundParkListData  mylistData=(AroundParkListData)data;
			Log.i("tag", "进入:"+listData.datalist.size());
			if(!"200".equals(mylistData.code)){
				Toast.makeText(getApplicationContext(), "数据加载出错，请重试", Toast.LENGTH_SHORT).show();
				return;
			}
			Log.i("tag", "进入:"+mylistData.datalist.size());
			AroundParkData parkData=mylistData.datalist.get(0);
			Intent intent=new Intent(MyCareDepotActivity.this,ParkDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("park_data", (Serializable)parkData);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}	
	
	private void geneItems(){
		for (int i = 0; i != 15; ++i){
			if(start<listData.datalist.size()){
				list.add(listData.datalist.get(start++));
			}
		}
	}
	
	private void initView(){
		isEdit = false;
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		edit_caredepot=(Button)findViewById(R.id.edit_caredepot);
		my_listview_null=(ImageView)findViewById(R.id.my_listview_null);
		mListView=(XListView)findViewById(R.id.my_list_caredepot);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
//		mListView.setDivider(getResources().getDrawable(R.drawable.my_line));
//		mListView.setEmptyView(my_listview_null);
		adapter=new MyCareDepotAdapter(context,userId);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("tag", "进入onresume");
		if(info!=null){
			start=0;
			list.clear();
			userId=info.getId();
			getAttentionPark(userId);
		}
	}
	
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyCareDepotActivity.this.finish();
			}
		});
		edit_caredepot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isEdit) {
					isEdit = true;
//					edit_caredepot.setBackgroundResource(R.drawable.top_button6);
				} else {
					isEdit = false;
//					edit_caredepot.setBackgroundResource(R.drawable.top_button5);
				}
				adapter.setEdit(isEdit);
				adapter.notifyDataSetChanged();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Log.i("tag", "进入:"+position);
				if(!isEdit){
					parkData=listData.datalist.get(position-1);
					int pid=parkData.getId();
					Log.i("tag", "停车场id"+pid);
					getParkDetail(pid);
				}else{
					parkData=listData.datalist.get(position-1);
					Intent intent=new Intent(MyCareDepotActivity.this,SetCareActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("shortname", parkData.getShortName());
					bundle.putString("userid", userId);
					bundle.putInt("pid", parkData.getId());
					intent.putExtras(bundle);
					startActivity(intent);
				}
//				Log.i("tag", "信息："+parkData.getLatitude()+","+parkData.getShortName());
			}
		});
	}

	@Override
	public void onRefresh() {
		mListView.setPullLoadEnable(false);
		mHandler.postDelayed(new Runnable(){
			@Override
			public void run(){
				start=0;
				list.clear();
//				geneItems();
				getAttentionPark(userId);
//				adapter.setMyCareDepotList(list);
//				mListView.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
		
	}

	@Override
	public void onLoadMore() {
		mListView.setPullLoadEnable(false);
	}
	
	private void onLoad(){
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(new Date().toLocaleString());
	}
}
