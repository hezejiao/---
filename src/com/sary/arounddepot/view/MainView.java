package com.sary.arounddepot.view;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.app.AbRotate3dAnimation;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.MainActivity;
import com.sary.arounddepot.activity.ParkDetailActivity;
import com.sary.arounddepot.activity.SearchParkActivity;
import com.sary.arounddepot.adapter.AroundParkAdapter;
import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DBHelper;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.MyCountdownTimer;
import com.sary.arounddepot.util.MyScrollLayout;
import com.sary.arounddepot.util.PrefHelper;
import com.sary.arounddepot.view.FlipperLayout.OnOpenListener;
import com.sary.arounddepot.view.XListView.IXListViewListener;

public class MainView extends MyBasicView implements IXListViewListener,
		OnLongClickListener, LocationSource, AMapLocationListener,
		OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter,
		OnMapLoadedListener, OnGeocodeSearchListener,
		SynthesizerPlayerListener, OnMapClickListener{
	public MyApplication mApplication;
	private Context mContext;
	private Activity mActivity;
	private View mainView;
	private Button btn_tomap;
	private ImageButton mydrawer;
	public ImageButton btn_search, location_again;
	public LinearLayout sort_power, sort_state, sort_distance, sort_price;
	public XListView mListView;
	public TextView current_location;
	private OnOpenListener mOnOpenListener;
	private Dialog dialog;
	private ImageView dialog_img;
	private AnimationDrawable anim;
	private Animation animation ;

	public AroundParkListData listData;
	private AroundParkData parkData;
	public AroundParkAdapter adapter;
	public List<AroundParkData> list = new ArrayList<AroundParkData>();
	public ProgressDialog mDialog,dialogs;// 搜索的进度条
	public DBHelper dbHelper;
	public boolean flag = false;
	private Handler mHandler;
	private int start = 0;
	int a = 0;// 记录请求数据的次数
	private static int refreshCnt = 0;
	// private LocationManagerProxy mAMapLocManager = null;
	private double latitude, longitude;
	private String detailAddres;
	public String serchkey;
	private GeocodeSearch geocoderSearch;
	private Interpolator accelerator = new AccelerateInterpolator();
	private Interpolator decelerator = new DecelerateInterpolator();
	private LinearLayout my_firstview, my_map;
	private ViewGroup mContainer = null;

	public MapView mapView;
	public AMap aMap;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private MarkerOptions markerOption;
	private Marker marker;
	private Bundle bundle;

	// 合成对象.
	public SynthesizerPlayer mSynthesizerPlayer;
	public PrefHelper mPrefHelper;// SharedPreferences数据存储
	public boolean fag=true;
	public List<Marker> my_map_mark=new ArrayList<Marker>();

	//首页引导页
	private MyScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private RelativeLayout main_guide,mainviews;
	private LinearLayout pointLLayout;
	private boolean firstin=true;
	private List<String> name;
	
	private ImageView power,state,distances,prices;
	private TextView txt_power,txt_state,txt_distances,txt_prices;
	
	private MyCount myCount;// 定时类对象
	
	private int sort_way;
	
	private ImageView my_listview_null;
	
	public ImageView tip_image;
	
	private boolean isFirstImage = true;
	
	public static final CameraPosition SHANGHAI_STATUS = new CameraPosition.Builder()
	.target(Constant.SHANGHAI_STATUS).zoom(15).bearing(70).tilt(0)
	.build();
	
	public MainView(MyApplication application, Context context,
			Activity activity, Bundle savedInstanceState) {
		super(application, context, activity);
		mApplication = application;
		mContext = context;
		mActivity = activity;
		bundle = savedInstanceState;
		mainView = LayoutInflater.from(context).inflate(R.layout.main, null);
		name=isAvilible(mContext);
		initView();
		setListener();
	}

	private void initView() {
		mainviews=(RelativeLayout) mainView.findViewById(R.id.mainviews);
		mContainer = (ViewGroup) mainView.findViewById(R.id.container);
		mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		my_listview_null=(ImageView) mainView.findViewById(R.id.my_listview_nulls);
		
		mPrefHelper = PrefHelper.getInstance(mContext);
		my_firstview = (LinearLayout) mainView.findViewById(R.id.my_firstview);
		my_map = (LinearLayout) mainView.findViewById(R.id.my_map);
		adapter = new AroundParkAdapter(mContext);
		dbHelper=new DBHelper(mContext);
		dbHelper = DBHelper.getInstance(mContext);
		mydrawer = (ImageButton) mainView.findViewById(R.id.mydrawer);
		sort_power = (LinearLayout) this.mainView.findViewById(R.id.sort_power);
		sort_state = (LinearLayout) this.mainView.findViewById(R.id.sort_state);
		sort_distance = (LinearLayout) this.mainView
				.findViewById(R.id.sort_distance);
		sort_price = (LinearLayout) this.mainView.findViewById(R.id.sort_price);
		sort_way=mPrefHelper.readInt("sort_way");
		
		mListView = (XListView) this.mainView.findViewById(R.id.list_park);
//		mListView.setDividerHeight(20);
		
		mListView.setPullLoadEnable(true);
		current_location = (TextView) this.mainView
				.findViewById(R.id.current_location);
		location_again = (ImageButton) this.mainView
				.findViewById(R.id.location_again);
		btn_search = (ImageButton) this.mainView.findViewById(R.id.btn_search);
		btn_tomap = (Button) this.mainView.findViewById(R.id.btn_tomaps);
		
		power=(ImageView) this.mainView.findViewById(R.id.power);
		state=(ImageView) this.mainView.findViewById(R.id.state);
		distances=(ImageView) this.mainView.findViewById(R.id.distances);
		prices=(ImageView) this.mainView.findViewById(R.id.prices);
		txt_power=(TextView) this.mainView.findViewById(R.id.txt_power);
		txt_state=(TextView) this.mainView.findViewById(R.id.txt_state);
		txt_distances=(TextView) this.mainView.findViewById(R.id.txt_distances);
		txt_prices=(TextView) this.mainView.findViewById(R.id.txt_prices);
	
		mapView = (MapView) mainView.findViewById(R.id.map);
		mapView.onCreate(bundle);// 此方法必须重写
		mHandler = new Handler();
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}

		if (app.screen == 1) {
			my_firstview.setVisibility(View.VISIBLE);
			my_map.setVisibility(View.GONE);
		} else if (app.screen == 2) {
			my_firstview.setVisibility(View.GONE);
			// btn_tomap.setBackground(mContext.getResources().getDrawable(R.drawable.list));
			my_map.setVisibility(View.VISIBLE);
		}
		// 初始化逆地理参数
		geocoderSearch = new GeocodeSearch(mContext);
		geocoderSearch.setOnGeocodeSearchListener(this);
		
		tip_image=(ImageView) mainView.findViewById(R.id.my_first_in);
		isFirstImage=mPrefHelper.readBoolean(Constant.FIRST_IMAGE);
		if(isFirstImage){
			tip_image.setVisibility(View.VISIBLE);
		}
	}

	public void getNearByPark(String lat_long) {

		if (app.listDatas.datalist != null && app.listDatas.datalist.size() > 0
				&& a++ == 0) {
			Log.i("tag", "进来的经纬度：" + app.longitude + "," + app.latitude);
			// dbHelper.clearPark();
			listData = app.listDatas;
			// list = app.listData;
			geneItems();
			adapter.setAroundParkList(list);
			addMarkersToMap();
			mListView.setAdapter(adapter);
			mListView.setXListViewListener(this);
			return;
		}
		Log.i("tag", "经纬度......：" + app.longitude + "," + app.latitude);
		dbHelper.clearPark();
		System.out.println("xxxxxxxxxxx");
		Log.e("tag", "mmmmmmmmm");
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("location", lat_long));
		boolean result = sendRequest(Constant.GET_NEARBY_PARK, key);
		if (result) {
			if(!mPrefHelper.readBoolean(Constant.FIRST_IMAGE)){
				mDialog = DialogUtil.showProgress(mContext, "数据加载中...", null);
			}
		}
	}

	protected void handleNearByMsg(int msg, BaseData data) {
		Log.e("tag", "handleNearByMsg");
		System.out.println("ddddddddddddddddd");
		if (msg == Constant.GET_NEARBY_PARK) {
			if (mDialog != null) {
				mDialog.cancel();
				mDialog = null;
			}
			if (data == null) {
				return;
			}
			AroundParkListData listDatas = (AroundParkListData) data;
			Log.e("tag", "listview中的数据条数：" + listDatas.datalist.size());
			if (!"200".equals(listDatas.code)) {
				Toast.makeText(mContext, "数据加载出错，请重试", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if(listDatas.datalist.size()==0){
				mListView.setVisibility(View.GONE);
				my_listview_null.setVisibility(View.VISIBLE);
			}
			int p=0;
			for (int i = 0; i < listDatas.datalist.size(); i++) {
				Log.i("tag", "mmmmm:"+listDatas.datalist.size());
//				Log.i("tag", "数据："
//				+listDatas.datalist.get(i).getShortName()+","+listDatas.datalist.get(i).getSortPrice());
				dbHelper.savePark(listDatas.datalist.get(i));
				p++;
			}
			Log.i("tag", "p="+p);
			if(mPrefHelper.readInt("sort_way")==0){
				listData=dbHelper.getParkByIntelligence();
			}
			if(mPrefHelper.readInt("sort_way")==1){
				listData = dbHelper.getParkByStatus();
			}
			if(mPrefHelper.readInt("sort_way")==2){
				listData = dbHelper.getParkByCondition("distance");
			}
			if(mPrefHelper.readInt("sort_way")==3){
				listData = dbHelper.getParkByCondition("sortPrice");
			}
			app.listDatas = listData;
			
			if(listData.datalist.size()<15){
				mListView.stopLoadMore();
				mListView.mFooterView.mHintView.setText("");
			}
			
			geneItems();
			Log.i("tag", "list中的数据：" + list.size());
			System.out.println("listdata:" + listData);
			// app.listData=list;
			Log.i("tag", "搜索后list中的数据："+list.size());
			addMarkersToMap();
			adapter.setAroundParkList(list);
			adapter.notifyDataSetChanged();
			mListView.setAdapter(adapter);
			mListView.setXListViewListener(this);
		}
	}

	private void geneItems() {
		Log.i("tag", "数据：" + listData.datalist.size());
		for (int i = 0; i != 15; ++i) {
			if (start < listData.datalist.size()) {
				list.add(listData.datalist.get(start++));
			}
		}
	}

	private void setListener() {
		tip_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tip_image.setVisibility(View.GONE);
				mPrefHelper.save(Constant.FIRST_IMAGE, false);
			}
		});
		
		mydrawer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mOnOpenListener != null){
					mOnOpenListener.open();
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
//				AroundParkData parkDatas= list.get(position-1);
				AroundParkData parkDatas = (AroundParkData) parent.getItemAtPosition(position);
				Intent intent = new Intent(mContext, ParkDetailActivity.class);
				Bundle bundle = new Bundle();
				// System.out.println("shops="+shops);
				bundle.putSerializable("park_data", (Serializable) parkDatas);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		sort_power.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPrefHelper.save("sort_way", 0);
				sort_power.setBackgroundResource(R.drawable.btn_down);
				sort_state.setBackgroundDrawable(null);
				sort_distance.setBackgroundDrawable(null);
				sort_price.setBackgroundDrawable(null);
				power.setImageResource(R.drawable.sort_zhineng_press);
				state.setImageResource(R.drawable.sort_state);
				distances.setImageResource(R.drawable.sort_distance);
				prices.setImageResource(R.drawable.sort_price);
				txt_power.setTextColor(mContext.getResources().getColor(R.color.main_text_press));
				txt_state.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_distances.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_prices.setTextColor(mContext.getResources().getColor(R.color.main_text));
				
				if(my_map_mark!=null &&my_map_mark.size()>0){
					Log.i("tag", "数量："+my_map_mark.size());
					for (int i = 0; i < my_map_mark.size(); i++) {
						Log.i("tag", "marker:"+my_map_mark.get(i).getTitle());
						my_map_mark.get(i).destroy();
					}
					my_map_mark.clear();
					list.clear();
				}
				start = 0;
				list.clear();
				listData=new AroundParkListData();
				listData = dbHelper.getParkByIntelligence();
				Log.i("tag", "数据："+listData.datalist.size());
				geneItems();
				adapter.setAroundParkList(list);
				mListView.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
			}
		});
		sort_state.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPrefHelper.save("sort_way", 1);
				sort_state.setBackgroundResource(R.drawable.btn_down);
				sort_power.setBackgroundDrawable(null);
				sort_distance.setBackgroundDrawable(null);
				sort_price.setBackgroundDrawable(null);
				power.setImageResource(R.drawable.sort_zhineng);
				state.setImageResource(R.drawable.sort_state_press);
				distances.setImageResource(R.drawable.sort_distance);
				prices.setImageResource(R.drawable.sort_price);
				txt_power.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_state.setTextColor(mContext.getResources().getColor(R.color.main_text_press));
				txt_distances.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_prices.setTextColor(mContext.getResources().getColor(R.color.main_text));
				
				if(my_map_mark!=null &&my_map_mark.size()>0){
					Log.i("tag", "数量："+my_map_mark.size());
					for (int i = 0; i < my_map_mark.size(); i++) {
						Log.i("tag", "marker:"+my_map_mark.get(i).getTitle());
						my_map_mark.get(i).destroy();
					}
					my_map_mark.clear();
					list.clear();
				}
				start = 0;
				list.clear();
				listData=new AroundParkListData();
				listData = dbHelper.getParkByStatus();
				System.out.println("size:" + listData.datalist.size());
				Log.i("tag", "数据："+listData.datalist.size());
				geneItems();
				adapter.setAroundParkList(list);
				mListView.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
			}
		});
		sort_distance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPrefHelper.save("sort_way", 2);
				sort_distance.setBackgroundResource(R.drawable.btn_down);
				sort_power.setBackgroundDrawable(null);
				sort_state.setBackgroundDrawable(null);
				sort_price.setBackgroundDrawable(null);
				power.setImageResource(R.drawable.sort_zhineng);
				state.setImageResource(R.drawable.sort_state);
				distances.setImageResource(R.drawable.sort_distance_down);
				prices.setImageResource(R.drawable.sort_price);
				txt_power.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_state.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_distances.setTextColor(mContext.getResources().getColor(R.color.main_text_press));
				txt_prices.setTextColor(mContext.getResources().getColor(R.color.main_text));
				
				if(my_map_mark!=null &&my_map_mark.size()>0){
					Log.i("tag", "数量："+my_map_mark.size());
					for (int i = 0; i < my_map_mark.size(); i++) {
						Log.i("tag", "marker:"+my_map_mark.get(i).getTitle());
						my_map_mark.get(i).destroy();
					}
					my_map_mark.clear();
					list.clear();
				}
				start = 0;
				list.clear();
				listData=new AroundParkListData();
				listData = dbHelper.getParkByCondition("distance");
				Log.i("tag", "数据："+listData.datalist.size());
				geneItems();
				adapter.setAroundParkList(list);
				mListView.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
			}
		});

		sort_price.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPrefHelper.save("sort_way", 3);
				sort_price.setBackgroundResource(R.drawable.btn_down);
				sort_power.setBackgroundDrawable(null);
				sort_state.setBackgroundDrawable(null);
				sort_distance.setBackgroundDrawable(null);
				power.setImageResource(R.drawable.sort_zhineng);
				state.setImageResource(R.drawable.sort_state);
				distances.setImageResource(R.drawable.sort_distance);
				prices.setImageResource(R.drawable.sort_price_down);
				txt_power.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_state.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_distances.setTextColor(mContext.getResources().getColor(R.color.main_text));
				txt_prices.setTextColor(mContext.getResources().getColor(R.color.main_text_press));
				
				if(my_map_mark!=null &&my_map_mark.size()>0){
					Log.i("tag", "数量："+my_map_mark.size());
					for (int i = 0; i < my_map_mark.size(); i++) {
						Log.i("tag", "marker:"+my_map_mark.get(i).getTitle());
						my_map_mark.get(i).destroy();
					}
					my_map_mark.clear();
					list.clear();
				}
				start = 0;
				list.clear();
				listData=new AroundParkListData();
				listData = dbHelper.getParkByCondition("sortPrice");
				geneItems();
				Log.i("tag", "数据："+listData.datalist.size());
				adapter.setAroundParkList(list);
				mListView.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
			}
		});
		location_again.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//新增
				start = 0;
				list.clear();
				//
//				aMap.clear();
				if(my_map_mark!=null &&my_map_mark.size()>0){
					Log.i("tag", "数量："+my_map_mark.size());
					for (int i = 0; i < my_map_mark.size(); i++) {
						my_map_mark.get(i).destroy();
					}
					my_map_mark.clear();
				}
				app.listData.clear();
				Log.i("tag", "重新加载数据时的经纬度：" + app.longitude + ","
						+ app.latitude);
				getNearByPark(app.longitude + "," + app.latitude);
			}
		});
		location_again.setOnLongClickListener(this);

		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent search = new Intent();
				search.setClass(mContext, SearchParkActivity.class);
				mActivity.startActivityForResult(search, 18);
				mActivity
						.overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});
		btn_tomap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (my_firstview.getVisibility() == View.VISIBLE) {
					app.screen = 2;
					((MainActivity)mActivity).mRoot.setCanSliding(false);
//					applyRotation(0,0,90);
					addMarkersToMap();
					if(mPrefHelper.readInt("first")==0){
						dialogs = DialogUtil.showProgress(mContext, "地图加载中...", null);
						mPrefHelper.save("first", 1);
						myCount = new MyCount(100, 1000);
						myCount.start();// 开始计时
					}
					btn_tomap.setBackgroundResource(R.drawable.list);
					my_map.setVisibility(View.VISIBLE);
					my_firstview.setVisibility(View.GONE);
				} else {
					app.screen = 1;
					((MainActivity)mActivity).mRoot.setCanSliding(true);
//					applyRotation(-1, 360, 270);
					btn_tomap.setBackgroundResource(R.drawable.top_map);
					my_firstview.setVisibility(View.VISIBLE);
					my_map.setVisibility(View.GONE);
					
				}
			}
		});
	}

	public View getView(){
		return mainView;
	}

	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

	@Override
	public void onRefresh() {
		mListView.setPullLoadEnable(true);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				start = 0;
				list.clear();
				geneItems();
				//---新增--//
//				aMap.clear();
				if(my_map_mark!=null &&my_map_mark.size()>0){
					Log.i("tag", "数量："+my_map_mark.size());
					for (int i = 0; i < my_map_mark.size(); i++) {
						Log.i("tag", "marker:"+my_map_mark.get(i).getTitle());
						my_map_mark.get(i).destroy();
					}
					my_map_mark.clear();
				}
				addMarkersToMap();
				//---结束--//
				
				adapter.setAroundParkList(list);
				mListView.setAdapter(adapter);
				onLoad();
			}
		}, 1500);
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(new Date().toLocaleString());
	}

	@Override
	public void onLoadMore() {
		if (start >= listData.datalist.size()) {
			Toast.makeText(mContext, "已全部加载完毕", Toast.LENGTH_SHORT).show();
			mListView.stopLoadMore();
			mListView.mFooterView.mHintView.setText("已全部加载完毕");
			return;
		}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				adapter.notifyDataSetChanged();
				onLoad();

			}
		}, 1500);
	}

	@Override
	public boolean onLongClick(View v) {
		mListView.setPullLoadEnable(true);
		NetworkInfo network = ((ConnectivityManager) mContext
				.getSystemService(mContext.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if ((network == null) || (!network.isAvailable())) {
			Toast.makeText(mContext, "未检测到网络，请检测网络连接", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		showVoiceDialog();
//		anim.start();
		if(animation!=null){
			dialog_img.startAnimation(animation);
		}
		myCount = new MyCount(10000, 1000);
		myCount.start();// 开始计时
		mAMapLocationManager = LocationManagerProxy.getInstance(mContext);
		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
		return true;
	}

	// 重新定位时显示Dialog
	public void showVoiceDialog() {
		dialog = new Dialog(mContext, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_animationdialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_imgs);
//		anim = (AnimationDrawable) dialog_img.getBackground();
		//新增
		animation = AnimationUtils.loadAnimation(mContext, R.anim.main_rorate);  
		LinearInterpolator lin = new LinearInterpolator();  
		animation.setInterpolator(lin); 
		dialog.show();
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.moveCamera(CameraUpdateFactory.
				newCameraPosition(SHANGHAI_STATUS));
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		// myLocationStyle.radiusFillColor(color)//设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(5);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMapClickListener(this);
		// addMarkersToMap();// 往地图上添加marker
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		Log.i("tag", "搜索后list："+list.size());
		for (int i = 0; i < list.size(); i++) {
			markerOption = new MarkerOptions();
			markerOption.position(new LatLng(list.get(i).getLatitude(), list
					.get(i).getLongitude()));
			markerOption.title(list.get(i).getShortName());
			if (list.get(i).getIsFree() == 0) {
				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.for_charge));
			} else {
				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.for_free));
			}
			markerOption.perspective(true);
			markerOption.draggable(true);
			marker = aMap.addMarker(markerOption);
			my_map_mark.add(marker);
			marker.setObject((AroundParkData) list.get(i));
		}

	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation){
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if(myCount!=null){
			myCount.cancel();
		}
		//---新增-----//
//		aMap.clear();
		if(my_map_mark!=null &&my_map_mark.size()>0){
			Log.i("tag", "数量："+my_map_mark.size());
			for (int i = 0; i < my_map_mark.size(); i++) {
				Log.i("tag", "marker:"+my_map_mark.get(i).getTitle());
				my_map_mark.get(i).destroy();
			}
			my_map_mark.clear();
			list.clear();
		}
		Log.i("tag", "清空后的数量："+my_map_mark.size());
//		Log.i("tag", "数量2："+my_map_mark.size());
		start = 0;
		list.clear();
		app.listDatas = new AroundParkListData();
		if(listData!=null){
			listData.datalist.clear();
		}
		dbHelper.clearPark();
		//------结束---//
		if (aLocation != null){
			if (mListener != null){
				mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			}
			 aMap.moveCamera(CameraUpdateFactory
			 .newLatLngZoom(new LatLng
			 (aLocation.getLatitude(), aLocation.getLongitude()), 15));// 设置指定的可视区域地图
			 
			latitude = aLocation.getLatitude();
			longitude = aLocation.getLongitude();
			// 保存坐标信息
			app.aLocation = aLocation;
			app.latitude = latitude;
			app.longitude = longitude;
			Log.i("tag", "listview中数据：" + app.listDatas.datalist.size());

			getNearByPark(longitude + "," + latitude);
			Log.i("tag", "定位成功后的经纬度：" + app.longitude + "," + app.latitude);
			// getNearByPark("121.437441,31.184826");
			current_location.setText(longitude + "," + latitude);
			if (animation != null) {
				dialog_img.clearAnimation();
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}

			deactivate();
			new Thread() {
				@Override
				public void run() {
					RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(
							latitude, longitude), 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
					geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
				}
			}.start();
		} else {
			Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
			deactivate();
			if (animation != null) {
				dialog_img.clearAnimation();
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}
	}
	/**
	 * 激活定位
	 */
	int i = 0;

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (app.longitude != 0 && app.latitude != 0 && i == 0){
			if(app.aLocation!=null){
				mListener.onLocationChanged(app.aLocation);
				 aMap.moveCamera(CameraUpdateFactory
				 .newLatLngZoom(new LatLng
				 (app.aLocation.getLatitude(), app.aLocation.getLongitude()), 15));// 设置指定的可视区域地图
			}
			Log.i("tag", "经纬度：" + app.longitude + "," + app.latitude);
			getNearByPark(app.longitude + "," + app.latitude);
			i++;
			detailAddres = app.address;
			current_location.setText(detailAddres);
			return;
		}

		NetworkInfo network = ((ConnectivityManager) mContext
				.getSystemService(mContext.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if ((network == null) || (!network.isAvailable())) {
			Toast.makeText(mContext, "未检测到网络，请检测网络连接", Toast.LENGTH_SHORT)
					.show();
			mListView.setPullRefreshEnable(false);
			mListView.setPullLoadEnable(false);
			return;
		}
		if(!mPrefHelper.readBoolean(Constant.FIRST_IMAGE)){
			mDialog = DialogUtil.showProgress(mContext, "正在为您定位...", null);
		}
		myCount = new MyCount(10000, 1000);
		myCount.start();// 开始计时
		if (mAMapLocationManager == null) {
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
				mAMapLocationManager = LocationManagerProxy.getInstance(mContext);
				mAMapLocationManager.requestLocationUpdates(
						LocationProviderProxy.AMapNetwork, 5000, 10, this);
		}
	}
	
	class MyCount extends MyCountdownTimer {
		
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish(){
			if (mDialog != null) {
				mDialog.cancel();
				mDialog = null;
			}
			if(dialogs!=null){
				dialogs.cancel();
				dialogs=null;
				return;
			}
			if (animation != null){
				dialog_img.clearAnimation();
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
			Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
			deactivate();
			
		}

		@Override
		public void onTick(long millisUntilFinished, int percent) {

		}
		
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate(){
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		AroundParkData data = (AroundParkData) marker.getObject();
		Intent intent = new Intent(mContext, ParkDetailActivity.class);
		Bundle bundle = new Bundle();
		// System.out.println("shops="+shops);
		bundle.putSerializable("park_data", (Serializable) data);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker marker){
		View view = LayoutInflater.from(mContext)
				.inflate(R.layout.marker, null);
		final AroundParkData data = (AroundParkData) marker.getObject();
		System.out.println("hehe=" + data.getPrice() + "distance:"
				+ data.getDistance() + "price:" + data.getPrice() + " address:"
				+ data.getShortAddres());
		TextView title = (TextView) view.findViewById(R.id.marker_title);
		TextView distance = (TextView) view.findViewById(R.id.marker_distance);
		TextView price = (TextView) view.findViewById(R.id.park_price);
		TextView address = (TextView) view.findViewById(R.id.park_address);
		Button navigation = (Button) view.findViewById(R.id.navigation);
		navigation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(mContext,
				// "你点击了infoWindow窗口"+data.getLatitude(),
				// Toast.LENGTH_SHORT).show();
				String start_location ="latlng:"+app.aLocation.getLatitude() + ","
						+ app.aLocation.getLongitude()+"|name:起点";
				String location = "latlng:"+data.getLatitude() + ","
						+ data.getLongitude()+"|name:"+data.getShortAddres();
				try {
					if(mPrefHelper.readInt("navigation")>0){
						if (mPrefHelper.readInt("navigation") == 1){
							// 移动APP调起Android百度地图方式举例
							Intent intent = Intent
									.getIntent("intent://map/direction?origin="
											+ start_location
											+ "&destination="
											+ location
											+ "&mode=driving&region=上海&src=夏瑞|停哪儿#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
							mContext.startActivity(intent); // 启动调用
						} else if (mPrefHelper.readInt("navigation") == 2) {
							Intent intent = new Intent(
									"android.intent.action.VIEW",
									android.net.Uri
											.parse("androidamap://route?sourceApplication=停哪儿&slat="
													+ app.aLocation.getLatitude()
													+ "&slon="
													+ app.aLocation.getLongitude()
													+ "&sname="+"我的位置"+"&dlat="
													+ data.getLatitude()
													+ "&dlon="
													+ data.getLongitude()
													+ "&dname="+data.getShortAddres()+"&dev=0&m=0&t=2&showType=1"));
							intent.setPackage("com.autonavi.minimap");
							mContext.startActivity(intent);
						}else if(mPrefHelper.readInt("navigation") == 3){
							Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="+
						     app.aLocation.getLatitude()+","+app.aLocation.getLongitude()+"&daddr="+
						     data.getLatitude()+","+data.getLongitude()+"&hl=zh"));
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK   
							        & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);   
							i.setClassName("com.google.android.apps.maps",   
							        "com.google.android.maps.MapsActivity");  
							mContext.startActivity(i);
						} 
					}else{
						if(name.contains("com.baidu.BaiduMap")){
							// 移动APP调起Android百度地图方式举例
							Intent intent = Intent
									.getIntent("intent://map/direction?origin="
											+ start_location
											+ "&destination="
											+ location
											+ "&mode=driving&region=上海&src=夏瑞|停哪儿#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
							mContext.startActivity(intent); // 启动调用
							return ;
						}else if(name.contains("com.autonavi.minimap")){
							Intent intent = new Intent(
									"android.intent.action.VIEW",
									android.net.Uri
											.parse("androidamap://route?sourceApplication=停哪儿&slat="
													+ app.aLocation.getLatitude()
													+ "&slon="
													+ app.aLocation.getLongitude()
													+ "&sname="+"我的位置"+"&dlat="
													+ data.getLatitude()
													+ "&dlon="
													+ data.getLongitude()
													+ "&dname="+data.getShortAddres()+"&dev=0&m=0&t=2&showType=1"));
							intent.setPackage("com.autonavi.minimap");
							mContext.startActivity(intent);
							return ;
						}else if(name.contains("com.google.android.apps.maps")){
							Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="+
								     app.aLocation.getLatitude()+","+app.aLocation.getLongitude()+"&daddr="+
								     data.getLatitude()+","+data.getLongitude()+"&hl=zh"));
									i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK   
									        & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);   
									i.setClassName("com.google.android.apps.maps",   
									        "com.google.android.maps.MapsActivity");  
									mContext.startActivity(i);
									return ;
						}else{
							Toast.makeText(mContext, "你没有安装导航软件，请在设置-导航软件中选择安装",
									Toast.LENGTH_LONG).show();
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		title.setText(data.getShortName());
		distance.setText(data.getDistance() + "m");
		price.setText(data.getPrice());
		address.setText(data.getShortAddres());
		return view;
	}
	
	private List<String> isAvilible(Context context){ 
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager 
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息 
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名 
       //从pinfo中将包名字逐一取出，压入pName list中 
            if(pinfo != null){ 
            for(int i = 0; i < pinfo.size(); i++){ 
                String pn = pinfo.get(i).packageName;
//                Log.i("tag", "已安装的应用："+pn);
                pName.add(pn); 
            } 
        } 
//        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE 
          return pName;
  } 

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				detailAddres = result.getRegeocodeAddress().getFormatAddress();
//				String address=result.getRegeocodeAddress().getCity();
				
				detailAddres=detailAddres.substring(3);
				app.address = detailAddres;
				current_location.setText(detailAddres);
				String voice_content = "定位已成功,你当前位于" + detailAddres;
				if (null == mSynthesizerPlayer) {
					// 创建合成对象.
					mSynthesizerPlayer = SynthesizerPlayer
							.createSynthesizerPlayer(mContext, "appid="
									+ mContext.getString(R.string.app_id));
				}
				int voice_Type = mPrefHelper.readInt("vioce_type");
				int have_voice = mPrefHelper.readInt("have_voice");
				if (voice_Type == 0){
					mSynthesizerPlayer.setVoiceName("xiaoyu");
				} else {
					mSynthesizerPlayer.setVoiceName("xiaoyan");
				}
				if (have_voice == 0){
					mSynthesizerPlayer.setVolume(50);
				} else {
					mSynthesizerPlayer.setVolume(0);
				}
				mSynthesizerPlayer.setSpeed(50);
				mSynthesizerPlayer.playText(voice_content, null, this);
			}
		} else {
			// Toast.makeText(getApplicationContext(), "索搜失败，请检查网络",
			// Toast.LENGTH_LONG).show();
			current_location.setText("自动检索失败");
		}

	}

	/**
	 * SynthesizerPlayerListener的"播放进度"回调接口.
	 * 
	 * @param percent
	 *            ,beginPos,endPos
	 */
	@Override
	public void onBufferPercent(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnd(SpeechError arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * SynthesizerPlayerListener的"开始播放"回调接口.
	 * 
	 * @param
	 */
	@Override
	public void onPlayBegin() {
		// TODO Auto-generated method stub

	}

	/**
	 * SynthesizerPlayerListener的"暂停播放"回调接口.
	 * 
	 * @param
	 */
	@Override
	public void onPlayPaused() {
		// TODO Auto-generated method stub

	}

	/**
	 * SynthesizerPlayerListener的"播放进度"回调接口.
	 * 
	 * @param percent
	 *            ,beginPos,endPos
	 */
	@Override
	public void onPlayPercent(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * SynthesizerPlayerListener的"恢复播放"回调接口，对应onPlayPaused
	 * 
	 * @param
	 */
	@Override
	public void onPlayResumed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0){
		Log.i("tag", "弹出信息：");
		for (int i = 0; i < my_map_mark.size(); i++){
			if(my_map_mark.get(i).isInfoWindowShown()){
				Log.i("tag", "弹出信息："+my_map_mark.get(i).getTitle());
				my_map_mark.get(i).hideInfoWindow();
			}
		}
	}
}
