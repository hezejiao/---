package com.sary.arounddepot.view;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.LoginActivity;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.MyNewsListData;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.Constant;

public class Desktop extends MyBasicView{
	
	private MyApplication mApplication;
	private Activity mActivity;
	private Context mContext;
	private View mDesktop;
	/**
	 * 接口对象,用来修改显示的View
	 */
	private onChangeViewListener mOnChangeViewListener;
	private ListView mDisplay;
	private DesktopAdapter mAdapter;
	private LinearLayout mWallpager;
	private UserInfo userInfo;
    int[] mrows=new int[6];
    private MyNewsListData listData;
	
	public Desktop(Context context, Activity activity, MyApplication application) {
		super(application, context, activity);
		mApplication = application;
		mContext = context;
		mActivity=activity;
		mDesktop = LayoutInflater.from(context).inflate(R.layout.left_menu, null);
		System.out.println("mmmmmmmmmmmmmm");
		init();
	}
	
	/**
	 * 界面初始化
	 */
	private void init() {
	
		mDisplay = (ListView) mDesktop.findViewById(R.id.desktop_display);
		mDisplay.setDivider(mContext.getResources().getDrawable(R.drawable.my_line));
		NetworkInfo network = ((ConnectivityManager)mContext.getSystemService(mContext.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if ((network == null) || (!network.isAvailable())){			
			for (int i = 0; i < mrows.length; i++){
				if(i==1){
					mrows[1]=R.drawable.right_tip;
				}else{
					mrows[i]=R.drawable.right_row;
				}
			}
			mAdapter = new DesktopAdapter(mContext);
			mDisplay.setAdapter(mAdapter);
			return ;
		}
//		mDisplay.setDivider(mContext.getResources().getDrawable(R.drawable.line));
//		mAdapter = new DesktopAdapter(mContext);
//		mDisplay.setAdapter(mAdapter);
		getMyNewsInfo();
	}
	private void getMyNewsInfo(){
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		boolean result = sendRequest(Constant.NEWS, key);
	}
	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if(data==null){
			return;
		}
		listData=(MyNewsListData)data;
		if(!"200".equals(listData.code)){
			Toast.makeText(mContext, "数据加载出错，请重试", Toast.LENGTH_SHORT).show();
			return;
		}
		if(listData.datalist.size()==0){
			for (int i = 0; i < mrows.length; i++){
				if(i==1){
					mrows[1]=R.drawable.right_tip;
				}else{
					mrows[i]=R.drawable.right_row;
				}
			}
			mAdapter = new DesktopAdapter(mContext);
			mDisplay.setAdapter(mAdapter);
		}else{
			for (int i = 0; i < mrows.length; i++){
				if(i==1){
					mrows[1]=R.drawable.right_have_tip;
				}else{
					mrows[i]=R.drawable.right_row;
				}
			}
			mAdapter = new DesktopAdapter(mContext);
			mDisplay.setAdapter(mAdapter);
		}
	}
	
	/**
	 * 界面修改方法
	 * 
	 * @param onChangeViewListener
	 */
	public void setOnChangeViewListener(
			onChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}

	/**
	 * 获取菜单界面
	 * 
	 * @return 菜单界面的View
	 */
	public View getView() {
		return mDesktop;
	}

	/**
	 * 切换显示界面的接口
	 * 
	 * @author rendongwei
	 * 
	 */
	public interface onChangeViewListener {
		public abstract void onChangeView(int arg0);
	}
	
	/**
	 * 菜单适配器
	 * 
	 * @author rendongwei
	 * 
	 */
	  class DesktopAdapter extends BaseAdapter {

		private Context mContext;
		private String[] mName = { "主页(我的周边)", "我的消息", "个人中心", "停车助手", "意见反馈", "设置"};
		private int[] mIcon = { R.drawable.main_pic,
				R.drawable.sms, R.drawable.info,
				R.drawable.car_help, R.drawable.sugesstion,
				R.drawable.set};
//		private int[] mrows = { R.drawable.right_row,
//				R.drawable.right_tip, R.drawable.right_row,
//				R.drawable.right_row, R.drawable.right_row,
//				R.drawable.right_row};
		private int mChoose = 0;

		public DesktopAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return 6;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public void setChoose(int choose) {
			mChoose = choose;
		}
		
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.desktop_item, null);
				holder = new ViewHolder();
				holder.layout = (LinearLayout) convertView
						.findViewById(R.id.desktop_item_layout);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.left_item);
				holder.name = (TextView) convertView
						.findViewById(R.id.title);
				holder.rows=(ImageView)convertView.findViewById(R.id.rows);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(mName[position]);
		    holder.rows.setImageResource(mrows[position]);
		
			if (position == mChoose){
				holder.name.setTextColor(Color.parseColor("#505050"));
				holder.icon.setImageResource(mIcon[position]);
				holder.layout.setBackgroundColor(Color.parseColor("#ECF2E6"));
			} else {
				holder.name.setTextColor(Color.parseColor("#505050"));
				holder.icon.setImageResource(mIcon[position]);
				holder.layout.setBackgroundColor(Color.parseColor("#F4F4F4"));
//				holder.name.setTextColor(Color.parseColor("#7fffffff"));
//				holder.icon.setImageResource(mIcon[position]);
//				holder.layout.setBackgroundResource(Color
//						.parseColor("#00000000"));
			}
			
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (mOnChangeViewListener != null) {
						switch (position) {
						case Constant.MAIN:
							mOnChangeViewListener.onChangeView(Constant.MAIN);
							break;
						case Constant.MY_NEWS:
							mOnChangeViewListener.onChangeView(Constant.MY_NEWS);
							break;
						case Constant.MY_INFO:
							System.out.println("xxxxxxxxxxxxxx");
							userInfo=mApplication.getUserInfoFromShared();
							if(userInfo==null || "".equals(userInfo)){
								mActivity.startActivityForResult(new Intent(mContext, LoginActivity.class),30);
								mActivity.overridePendingTransition(R.anim.roll_up,
										R.anim.roll);
								return ;
							}
							mOnChangeViewListener.onChangeView(Constant.MY_INFO);
							break;
						case Constant.PARK_HELPER:
							mOnChangeViewListener.onChangeView(Constant.PARK_HELPER);
							break;
						case Constant.SUGGESTION_BACK:
							Log.e("tag", "ssss");
							mOnChangeViewListener.onChangeView(Constant.SUGGESTION_BACK);
							break;
						case Constant.SET:
							Log.e("tag", "ssss");
							mOnChangeViewListener.onChangeView(Constant.SET);
							break;
						default:
							mOnChangeViewListener.onChangeView(Constant.MAIN);
							break;
						}
						mChoose = position;
						notifyDataSetChanged();
					}

				}
			});
			return convertView;
		}
		class ViewHolder {
			LinearLayout layout;
			ImageView icon;
			TextView name;
			ImageView rows;
		}
	}
	

}

