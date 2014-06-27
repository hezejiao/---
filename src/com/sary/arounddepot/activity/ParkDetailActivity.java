package com.sary.arounddepot.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.NewsInfo;
import com.sary.arounddepot.entity.Result;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.AnimationController;
import com.sary.arounddepot.util.AsyncImageLoader;
import com.sary.arounddepot.util.AsyncImageLoader.ImageCallback;
import com.sary.arounddepot.util.AudioRecorder;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.MyGallery;
import com.sary.arounddepot.util.StringUtil;
import com.sary.arounddepot.util.UploadUtil;
import com.sary.arounddepot.view.MyPopWindow;
import com.sary.arounddepot.view.MyVoiceWindow;

public class ParkDetailActivity extends BaseActivity implements LocationSource,
		AMapLocationListener, OnMarkerClickListener, OnInfoWindowClickListener,
		InfoWindowAdapter, OnMapLoadedListener,OnMapClickListener{
	private AroundParkData datas;
	private MapView mapView;
	private AMap aMap;
	private MyApplication app;
	private UserInfo info = null;
	private String userid;
	private int pid;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private MarkerOptions markerOption;
	private Marker marker;
	private Polyline polyline;
	private SlidingDrawer slidingDrawer;
	private ImageView sliding_icon;
	private TextView parkname, parkaddress, park_price, park_num;
	private ImageView park_num_one, park_num_two, car_userone, car_usertwo,
			car_userthree, youhui;
	private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9,
			txt10;
	private TextView park_detail_price, police, park_phone;
	private RelativeLayout park_detail_news1, park_detail_news2,
			park_detail_news3, my_buttons;
	private TextView care, play, talk_error;
	private Button change_large;
	private ImageButton btn_back;
	// 屏幕的宽度
	public static int screenWidth;
	// 屏幕的高度
	public static int screenHeight;
	private MyGallery gallery;
	private boolean isScale = false; // 是否缩放
	private float beforeLenght = 0.0f; // 两触点距离
	private float afterLenght = 0.0f;
	private float currentScale = 1.0f;

	private HorizontalScrollView hor;
	private LinearLayout linear;
	private AsyncImageLoader asyncImageLoader = AsyncImageLoader.getInstance();
	private String[] imageURL = null;

	// private GalleryAdapter adapter;
	private Context context;
	private AroundParkListData listData;
	private List<AroundParkData> list = new ArrayList<AroundParkData>();
	private String shortName, price, talk_content;
	private int total, remainNum, turn, wc, lift, slopeCharge, twoFloor,
			cmsingal, cusingal, ctsingal, wheel, chargenot;
	private boolean flag = true;
	public MyPopWindow menuWindow;
	public MyVoiceWindow voiceWindow;
	private boolean openwindow = true;
	private boolean voice_finish = false;
	private boolean voice_play = true;
	private boolean isfullscreen = true;
	private boolean menu_display = false;

	// 合成对象.
	public SynthesizerPlayer mSynthesizerPlayer;
	public SynthesizerPlayerListener listener;
	// 录音及上传
	private Dialog dialog;
	private AudioRecorder mr;
	private MediaPlayer mediaPlayer;
	// private TextView luyin_txt,luyin_path;
	private Thread recordThread;

	private static int MAX_TIME = 15; // 最长录制时间，单位秒，0为无时间限制
	private static int MIX_TIME = 3; // 最短录制时间，单位秒，0为无时间限制，建议设为1

	private static int RECORD_NO = 0; // 不在录音
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音

	private static int RECODE_STATE = 0; // 录音的状态

	private static float recodeTime = 0.0f; // 录音的时间
	private static double voiceValue = 0.0; // 麦克风获取的音量值

	private ImageView dialog_img;
	private static boolean playState = false; // 播放状态
	private AnimationController animationController;

	// popuwindow
	public TextView btn_edit_care, btn_remove_care, btn_cancel;

	public Button btn_record, player, send, stop;

	public LinearLayout record_type1, record_type2;
	//关注弹出pop
	public PopupWindow popupWindow;
	
	private List<String> name;
	
	private double latitude,longtitude;
	
	private FrameLayout my_framelayout;
	
	private ProgressDialog mDialog;
	
	public ImageView first_in;
	
	private boolean isFirstImage = true;
	
	public static final CameraPosition SHANGHAI_STATUS = new CameraPosition.Builder()
	.target(Constant.SHANGHAI_STATUS).zoom(16).bearing(70).tilt(0)
	.build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		datas = (AroundParkData) bundle.getSerializable("park_data");
		if (datas != null) {
			pid = datas.getId();
		}
		Log.i("tag", "data:" + datas.getShortName());
		setContentView(R.layout.park_detail);
		context = getBaseContext();
		animationController = new AnimationController();
		app = (MyApplication) getApplication();
		info = app.getUserInfoFromShared();
		name=isAvilible(context);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		setListener();
		if (info != null) {
			userid = info.getId();
			Log.i("tag", "获取到的id:" + userid);
			getAttentionPark(userid);
		}
		// 获取屏幕的大小
		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();
	}

	// 获得用户关注过的停车场
	private void getAttentionPark(String userId) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId", userId));
		boolean result = sendRequest(Constant.ATTENTION_PARK, key);
	}

	// 移除用户已关注的某个停车场
	private void removeAttentionPark(String userid, int pid) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId", userid));
		key.add(new BasicNameValuePair("pid", "" + pid));
		boolean result = sendRequest(Constant.REMOVE, key);
	}

	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (msg == Constant.ATTENTION_PARK) {
			if (data == null) {
				return;
			}
			listData = (AroundParkListData) data;

			for (int i = 0; i < listData.datalist.size(); i++) {
				System.out.println("name="
						+ listData.datalist.get(i).getShortName());
				AroundParkData park = listData.datalist.get(i);
				if (park.getId() == datas.getId()) {
					care.setText("已关注");
					flag = false;
				}
			}
		}
		if (msg == Constant.REMOVE) {
			if (data == null) {
				return;
			}
			Result result = (Result) data;
			if ("200".equals(result.getCode())) {
				Toast.makeText(context, "移除成功", Toast.LENGTH_SHORT).show();
				popupWindow.dismiss();
				care.setText("关注");
				flag = true;
			}
		}
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}

		StringBuffer sp = new StringBuffer();
		shortName = datas.getShortName();
		price = datas.getPrice();
		total = datas.getTotal();
		remainNum = datas.getRemainNum();
		turn = datas.getTurn();
		wc = datas.getWc();
		lift = datas.getLift();
		slopeCharge = datas.getSlopeCharge();
		twoFloor = datas.getTwoFloor();
		cmsingal = datas.getCmsingal();
		cusingal = datas.getCusingal();
		ctsingal = datas.getCtsingal();
		wheel = datas.getWheel();
		chargenot = datas.getChargenot();

		sp.append(shortName + ",共有" + total + "车位,目前剩余" + remainNum + "车位,价格"
				+ price);
		if (turn == 1 || wc == 1 || lift == 1 || slopeCharge == 1
				|| twoFloor == 1 || cmsingal == 1 || cusingal == 1
				|| ctsingal == 1 || wheel == 1 || chargenot == 1){
			sp.append(",停哪儿特别提醒您,此停车场:");
			if (twoFloor == 1) {
				sp.append("内有双层停车位,");
			}
			if (turn == 1) {
				sp.append("转弯难度大,");
			}
			if (wc == 1) {
				sp.append("有厕所,");
			}
			if (lift == 1) {
				sp.append("有电梯,");
			}
			if (slopeCharge == 1) {
				sp.append("出口坡道收费,");
			}
			if (wheel == 1) {
				sp.append("有轮椅通道,");
			}
			if (chargenot == 1) {
				sp.append("收费点不在出口处,");
			}
			if (cusingal == 1) {
				sp.append("联通信号覆盖,");
			}
			if (cmsingal == 1) {
				sp.append("移动信号覆盖,");
			}
			if (ctsingal == 1) {
				sp.append("电信信号覆盖,");
			}
			
		}
		sp.append(" 感谢您的使用");
		talk_content = sp.toString();
		my_framelayout=(FrameLayout) findViewById(R.id.my_framelayout);
		sliding_icon = (ImageView) findViewById(R.id.sliding_icon);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		change_large = (Button) findViewById(R.id.change_large);
		my_buttons = (RelativeLayout) findViewById(R.id.my_buttons);

		
		// 设置view在水平滚动时，水平边不淡出。
		this.hor = (HorizontalScrollView) findViewById(R.id.hor);
		this.linear = (LinearLayout) findViewById(R.id.linearImg);

		care = (TextView) findViewById(R.id.care);
		play = (TextView) findViewById(R.id.play);
		talk_error = (TextView) findViewById(R.id.talk_error);

		parkname = (TextView) findViewById(R.id.parkname);
		parkaddress = (TextView) findViewById(R.id.parkaddress);
		park_num_one = (ImageView) findViewById(R.id.park_num_one);
		park_num_two = (ImageView) findViewById(R.id.park_num_two);
		park_price = (TextView) findViewById(R.id.park_price);
		park_num = (TextView) findViewById(R.id.park_num);
		car_userone = (ImageView) findViewById(R.id.car_userone);
		car_usertwo = (ImageView) findViewById(R.id.car_usertwo);
		car_userthree = (ImageView) findViewById(R.id.car_userthree);
		youhui = (ImageView) findViewById(R.id.youhui);
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt5 = (TextView) findViewById(R.id.txt5);
		txt6 = (TextView) findViewById(R.id.txt6);
		txt7 = (TextView) findViewById(R.id.txt7);
		txt8 = (TextView) findViewById(R.id.txt8);
		txt9 = (TextView) findViewById(R.id.txt9);
		txt10 = (TextView) findViewById(R.id.txt10);
		park_detail_news1 = (RelativeLayout) findViewById(R.id.park_detail_news1);
		park_detail_news2 = (RelativeLayout) findViewById(R.id.park_detail_news2);
		park_detail_news3 = (RelativeLayout) findViewById(R.id.park_detail_news3);
		park_detail_price = (TextView) findViewById(R.id.park_detail_price);
		police = (TextView) findViewById(R.id.police);
		park_phone = (TextView) findViewById(R.id.park_phone);

		Log.i("tag",
				"停车场：" + datas.getShortName() + " 地址：" + datas.getShortAddres()
						+ " 图片数组：" + datas.getImageURL() + " 第一个："
						+ datas.getTurn() + " 第2个:" + datas.getWc() + " 第3个："
						+ datas.getLift() + " 消息：" + datas.getNewsInfo());
		// if(datas.getNewsInfo()!=null){
		// Log.i("tag", "消息数量："+datas.getNewsInfo().size());
		// }
		String imageurl = datas.getImageURL();
		if (imageurl != null) {
			imageURL = imageurl.split("_");
		}
		hor.post(new Runnable() {
			public void run() {
				int width = hor.getChildAt(0).getWidth();
				hor.scrollTo(width, 0);
			}
		});
		/**
		 * 横向滑动浏览图片
		 */
		if (imageURL != null) {
			for (int i = 0; i < imageURL.length; i++) {
				final ImageView horImg = new ImageView(context);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						200, 200);
				lp.setMargins(5, 5, 5, 5);
				horImg.setLayoutParams(lp);
				final String imageIcon = imageURL[i];
				System.out.println("imageIcon===" + imageIcon);
				horImg.setTag(imageIcon);
				Drawable dw = asyncImageLoader.loadDrawableToSD(context,
						imageIcon, horImg, new ImageCallback() {
							@Override
							public void imageLoaded(Drawable imageDrawable,
									View imageView, String imageUrl) {
								System.out.println("imageDrawable=="
										+ imageDrawable);
								if (!horImg.getTag().equals(imageUrl)) {
									return;
								}
								if (null != imageDrawable) {
									((ImageView) imageView)
											.setImageDrawable(imageDrawable);
								}
							}
						});
				if (null != dw) {
					horImg.setImageDrawable(dw);
					Log.i("tag", "图片缓存地址：" + (String) horImg.getTag());
				}
				Log.i("tag", "图片缓存地址2：" + (String) horImg.getTag());
				horImg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(context, "被点击",
						// Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ParkDetailActivity.this,
								LargePicForParkdetailActivity.class);
						Bundle b = new Bundle();
						b.putString("imagePath", imageIcon);
						intent.putExtras(b);
						startActivity(intent);
					}
				});
				linear.addView(horImg);
			}
		} else {
			ImageView images = new ImageView(context);
			images.setImageResource(R.drawable.camera_pic);
			linear.addView(images);
		}

		// adapter=new GalleryAdapter(context, list);
		// gallery.setAdapter(adapter);

		parkname.setText(datas.getShortName());
		parkaddress.setText(datas.getShortAddres());
		park_price.setText(datas.getPrice());
		park_num.setText("车位总数:" + datas.getTotal());

		if (datas.getVip1() == 0) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.car_notuser_one);
			car_userone.setImageBitmap(bm);
		} else {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.caruser_one);
			car_userone.setImageBitmap(bm);
		}
		if (datas.getVip2() == 0) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.car_notuser_two);
			car_usertwo.setImageBitmap(bm);
		} else {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.caruser_two);
			car_usertwo.setImageBitmap(bm);
		}
		if (datas.getVip3() == 0) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.car_notuser_three);
			car_userthree.setImageBitmap(bm);
		} else {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.caruser_three);
			car_userthree.setImageBitmap(bm);
		}
		if (datas.getVip4() == 0) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.have_no_youhui);
			youhui.setImageBitmap(bm);
		} else {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.have_youhui);
			youhui.setImageBitmap(bm);
		}
        int color1=Color.rgb(255, 218, 191);
        int color2=Color.rgb(203, 231, 255);
        int mColor;
		if (datas.getTwoFloor() == 1){
			mColor=StringUtil.getRandomColor(color1, color2);
			txt1.setBackgroundColor(mColor);
		} else {
			txt1.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getTurn() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt2.setBackgroundColor(mColor);
		} else {
			txt2.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getWc() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt3.setBackgroundColor(mColor);
		} else {
			txt3.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getLift() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt4.setBackgroundColor(mColor);
		} else {
			txt4.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getSlopeCharge() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt5.setBackgroundColor(mColor);
		} else {
			txt5.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getWheel() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt6.setBackgroundColor(mColor);
		} else {
			txt6.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getChargenot() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt7.setBackgroundColor(mColor);
		} else {
			txt7.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getCusingal() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt8.setBackgroundColor(mColor);
		} else {
			txt8.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getCmsingal() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt9.setBackgroundColor(mColor);
		} else {
			txt9.setBackgroundColor(getResources().getColor(R.color.text));
		}
		if (datas.getCtsingal() == 1) {
			mColor=StringUtil.getRandomColor(color1, color2);
			txt10.setBackgroundColor(mColor);
		} else {
			txt10.setBackgroundColor(getResources().getColor(R.color.text));
		}
		List<NewsInfo> newsInfo = new ArrayList<NewsInfo>();
		String content = datas.getNewsInfo();
		try {
			if (content != null) {
				JSONArray arr = new JSONArray(content);
				for (int n = 0; n < arr.length(); n++) {
					NewsInfo info = new NewsInfo();
					JSONObject ps = (JSONObject) arr.get(n);
					info.setId(ps.getInt("id"));
					info.setTitle(ps.getString("title"));
					info.setType(ps.getInt("type"));
					info.setUrl(ps.getString("url"));
					newsInfo.add(info);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (newsInfo != null) {
			for (int i = 0; i < newsInfo.size(); i++) {
				NewsInfo info = newsInfo.get(i);
				if (info.getType() == 1) {
					park_detail_news1.setVisibility(View.VISIBLE);
					park_detail_price.setText(info.getTitle());
				}
				if (info.getType() == 2) {
					park_detail_news2.setVisibility(View.VISIBLE);
					police.setText(info.getTitle());
				}
				if (info.getType() == 3) {
					park_detail_news3.setVisibility(View.VISIBLE);
					park_phone.setText(info.getTitle());
				}
			}
		}

		int total = datas.getTotal();
		int remain = datas.getRemainNum();
		int online = datas.getOnline();
		if (online == 0) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.gray_zero);
			park_num_one.setImageBitmap(bm);
			park_num_two.setImageBitmap(bm);
		} else {
			if (remain == 0) {
				Bitmap bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.red_zero);
				park_num_one.setImageBitmap(bm);
				park_num_two.setImageBitmap(bm);
			}
			if (remain > 0 && remain < 10) {
				if ((remain / (double) total) < 0.1) {
					park_num_one.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.red_zero));
					if (remain == 1) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_one));
					} else if (remain == 2) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_two));
					} else if (remain == 3) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_three));
					} else if (remain == 4) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_four));
					} else if (remain == 5) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_five));
					} else if (remain == 6) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_six));
					} else if (remain == 7) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_sev));
					} else if (remain == 8) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_eight));
					} else {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_nine));
					}
				} else {
					park_num_one.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.green_zero));
					if (remain == 1) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_one));
					} else if (remain == 2) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_two));
					} else if (remain == 3) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_three));
					} else if (remain == 4) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_four));
					} else if (remain == 5) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_five));
					} else if (remain == 6) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_six));
					} else if (remain == 7) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_sev));
					} else if (remain == 8) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_eight));
					} else {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_nine));
					}
				}
			}
			if (remain >= 10 && remain < 100) {
				int m = remain / 10;
				int n = remain % 10;
				if ((remain / (double) total) > 0.1) {
					if (m == 1) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_one));
					}
					if (m == 2) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_two));
					}
					if (m == 3) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_three));
					}
					if (m == 4) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_four));
					}
					if (m == 5) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_five));
					}
					if (m == 6) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_six));
					}
					if (m == 7) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_sev));
					}
					if (m == 8) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_eight));
					}
					if (m == 9) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_nine));
					}
					if (n == 0) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_zero));
					}
					if (n == 1) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_one));
					}
					if (n == 2) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_two));
					}
					if (n == 3) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_three));
					}
					if (n == 4) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_four));
					}
					if (n == 5) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_five));
					}
					if (n == 6) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_six));
					}
					if (n == 7) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_sev));
					}
					if (n == 8) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_eight));
					}
					if (n == 9) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.green_nine));
					}
				} else {
					if (m == 1) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_one));
					}
					if (m == 2) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_two));
					}
					if (m == 3) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_three));
					}
					if (m == 4) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_four));
					}
					if (m == 5) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_five));
					}
					if (m == 6) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_six));
					}
					if (m == 7) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_sev));
					}
					if (m == 8) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_eight));
					}
					if (m == 9) {
						park_num_one.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_nine));
					}
					if (n == 1) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_one));
					}
					if (n == 2) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_two));
					}
					if (n == 3) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_three));
					}
					if (n == 4) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_four));
					}
					if (n == 5) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_five));
					}
					if (n == 6) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_six));
					}
					if (n == 7) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_sev));
					}
					if (n == 8) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_eight));
					}
					if (n == 9) {
						park_num_two.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.red_nine));
					}
				}

			}
			if (remain >= 100) {
				Bitmap bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.green_nine);
				park_num_one.setImageBitmap(bm);
				park_num_two.setImageBitmap(bm);
			}
		}

		slidingDrawer = (SlidingDrawer) findViewById(R.id.sliding_drawer);
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				sliding_icon.setImageResource(R.drawable.park_down);
			}
		});
		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				sliding_icon.setImageResource(R.drawable.btn_parckdetail);
			}
		});
		
		first_in=(ImageView) findViewById(R.id.parkdetail_first_in);
		isFirstImage=mPrefHelper.readBoolean(Constant.FIRST_INDETAIL);
		if(isFirstImage){
			first_in.setVisibility(View.VISIBLE);
		}

	}

	private void setListener() {
		
		first_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				first_in.setVisibility(View.GONE);
				mPrefHelper.save(Constant.FIRST_INDETAIL, false);
			}
		});
		
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (menu_display) {
					// 如果 Menu已经打开 ，先关闭Menu
					popupWindow.dismiss();
					menu_display = false;
				} else {
					ParkDetailActivity.this.finish();
				}
			}
		});
		change_large.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isfullscreen) {
					change_large.setBackgroundResource(R.drawable.fun);
					my_buttons.setVisibility(View.GONE);
					my_framelayout.setVisibility(View.GONE);
//					slidingDrawer.setVisibility(View.GONE);
					isfullscreen = false;
				} else {
					change_large.setBackgroundResource(R.drawable.funn_screen);
					my_buttons.setVisibility(View.VISIBLE);
					my_framelayout.setVisibility(View.VISIBLE);
//					slidingDrawer.setVisibility(View.VISIBLE);
					isfullscreen = true;
				}
			}
		});

		care.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				info = app.getUserInfoFromShared();
				if (info == null || "".equals(info)) {
					Intent intent = new Intent(ParkDetailActivity.this,
							LoginActivity.class);
					intent.putExtra("type", 1);
					startActivityForResult(intent, 10);
					overridePendingTransition(R.anim.roll_up, R.anim.roll);
				} else {
					if (flag) {
						Intent intent = new Intent(ParkDetailActivity.this,
								SetCareActivity.class);
						userid = info.getId();
						Bundle b = new Bundle();
						b.putString("userid", userid);
						b.putInt("pid", pid);
						b.putString("shortAddress", datas.getShortAddres());
						b.putString("shortname", datas.getShortName());
						intent.putExtras(b);
						startActivityForResult(intent, 7);
					} else {
						popupWindow = makeCarePopupWindow(ParkDetailActivity.this);
						popupWindow.showAtLocation(care,Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
								StringUtil.dip2px(context, 60));
						menu_display = true;
					}
				}
			}
		});
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null == mSynthesizerPlayer) {
					// 创建合成对象.
					mSynthesizerPlayer = SynthesizerPlayer
							.createSynthesizerPlayer(ParkDetailActivity.this,
									"appid=" + getString(R.string.app_id));
				}
				if (voice_play) {
					
					int voice_Type = mPrefHelper.readInt("vioce_type");
					if (voice_Type == 0){
						mSynthesizerPlayer.setVoiceName("xiaoyu");
					} else {
						mSynthesizerPlayer.setVoiceName("xiaoyan");
					}
					mSynthesizerPlayer.setVolume(50);
					mSynthesizerPlayer.setSpeed(50);
					mSynthesizerPlayer.playText(talk_content, null, new listener());
					play.setText("暂停");
					voice_play = false;
				} else {
					mSynthesizerPlayer.cancel();
					play.setText("播放");
					voice_play = true;
				}

			}
		});
		talk_error.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				PopupWindow popupWindow = makeErrorPopupWindow(ParkDetailActivity.this);
				talk_error.setText("取消报错");
				popupWindow.showAtLocation(talk_error, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, StringUtil.dip2px(context, 60));
				
				popupWindow.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						talk_error.setText("报错");
					}
				});
			}
		});
	}

	class sendVoice extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				return UploadUtil.uploadFile(Constant.UPLOAD_VOICE,
						getAmrPath(), 2 + "");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (mDialog != null) {
				mDialog.cancel();
				mDialog = null;
			}
			animationController.slideOut2(record_type2,1000, 0);
			record_type2.setVisibility(View.GONE);
			animationController.slideIn2(record_type1,1000, 0);
			record_type1.setVisibility(View.VISIBLE);
			
			if ("200".equals(result)) {
				Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			mDialog = DialogUtil.showProgress(ParkDetailActivity.this, "正在发送...", null);
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
//		aMap.animateCamera(CameraUpdateFactory.
//				newCameraPosition(SHANGHAI_STATUS));
		aMap.moveCamera(CameraUpdateFactory.
				newCameraPosition(SHANGHAI_STATUS));
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.charge));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		// myLocationStyle.radiusFillColor(color)//设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(5);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnMapClickListener(this);
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		addMarkersToMap();// 往地图上添加marker

	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		markerOption = new MarkerOptions();
		markerOption.position(new LatLng(datas.getLatitude(), datas
				.getLongitude()));
		markerOption.title(datas.getShortName());
		markerOption
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.free));
		markerOption.perspective(true);
		markerOption.draggable(true);
		marker = aMap.addMarker(markerOption);
		marker.setObject((AroundParkData)datas);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
	
	protected void onStop() {
		if (null != mSynthesizerPlayer) {
			mSynthesizerPlayer.cancel();
		}
        if(mapView!=null){
        	mapView.onPause();
        }
		super.onStop();
	}
	

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		if (first_in != null) {
            BitmapDrawable bmpDrawable = (BitmapDrawable) first_in.getBackground();
            if (bmpDrawable != null) {
                Bitmap bmp = bmpDrawable.getBitmap();
                first_in.setBackgroundDrawable(null);
                if (bmp != null && bmp.isRecycled() == false) {
                    try {
                        bmpDrawable.getBitmap().recycle(); // 这个生命周期会销毁View，因此在回收被View引用的控件时，也无需先对View引用图片的属性置为空
                    } catch (Exception e) {
                    }
                }
            }
        }
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			if (mAMapLocationManager != null) {
				mAMapLocationManager.removeUpdates(this);
				mAMapLocationManager.destory();
			}
			mAMapLocationManager = null;
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					aLocation.getLatitude(), aLocation.getLongitude()), 16));// 设置指定的可视区域地图
			if(polyline!=null){
				polyline.remove();
			}
			latitude=aLocation.getLatitude();
			longtitude=aLocation.getLongitude();
			// 绘制一条折线
			PolylineOptions options = new PolylineOptions();
			options.add(new LatLng(aLocation.getLatitude(), aLocation
					.getLongitude()));
			options.add(new LatLng(datas.getLatitude(), datas.getLongitude()));
			options.width(5);
			polyline=aMap.addPolyline(options);
		}

	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null){
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 5000, 10, this);
		}

	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.marker2, null);
		Button navigation = (Button) view.findViewById(R.id.navigations);
	    TextView map_name = (TextView) view.findViewById(R.id.map_name);
	    final AroundParkData data = (AroundParkData) marker.getObject();
	    if(mPrefHelper.readInt("navigation")>0){
	    	if (mPrefHelper.readInt("navigation") == 1){
	    		map_name.setText("使用百度地图导航");
	    	}if(mPrefHelper.readInt("navigation") == 2){
	    		map_name.setText("使用高德地图导航");
	    	}if(mPrefHelper.readInt("navigation") == 3){
	    		map_name.setText("使用谷歌地图导航");
	    	}
	    }else{
	    	map_name.setText("默认导航软件");
	    }
	    navigation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
							ParkDetailActivity.this.startActivity(intent); // 启动调用
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
							ParkDetailActivity.this.startActivity(intent);
						}else if(mPrefHelper.readInt("navigation") == 3){
							Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="+
						     app.aLocation.getLatitude()+","+app.aLocation.getLongitude()+"&daddr="+
						     data.getLatitude()+","+data.getLongitude()+"&hl=zh"));
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK   
							        & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);   
							i.setClassName("com.google.android.apps.maps",   
							        "com.google.android.maps.MapsActivity");  
							ParkDetailActivity.this.startActivity(i);
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
							ParkDetailActivity.this.startActivity(intent); // 启动调用
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
							ParkDetailActivity.this.startActivity(intent);
							return ;
						}else if(name.contains("com.google.android.apps.maps")){
							Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="+
								     app.aLocation.getLatitude()+","+app.aLocation.getLongitude()+"&daddr="+
								     data.getLatitude()+","+data.getLongitude()+"&hl=zh"));
									i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK   
									        & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);   
									i.setClassName("com.google.android.apps.maps",   
									        "com.google.android.maps.MapsActivity");  
									ParkDetailActivity.this.startActivity(i);
									return ;
						}else{
							Toast.makeText(context, "你没有安装导航软件，请在设置-导航软件中选择安装",
									Toast.LENGTH_LONG).show();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		return view;
	}

	@Override
	public void onInfoWindowClick(Marker arg0){
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 10) {
			if (data != null) {
				AroundParkListData aroundData = (AroundParkListData) data
						.getSerializableExtra("park_care_list");
				if (aroundData != null) {
					for (int i = 0; i < aroundData.datalist.size(); i++) {
						AroundParkData park = aroundData.datalist.get(i);
						if (park.getId() == datas.getId()) {
							care.setText("已关注");
							flag = false;
						}
					}
				}
			}
		}
		if (resultCode == 6) {
			care.setText("已关注");
			flag = false;
		}
	}

	// ----------------------录音----------------------------//

	// 删除老文件
	void scanOldFile() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"my/voice.amr");
		if (file.exists()) {
			file.delete();
		}
	}

	// 录音时显示Dialog
	void showVoiceDialog() {
		dialog = new Dialog(ParkDetailActivity.this, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}

	// 录音时间太短时Toast显示
	void showWarnToast() {
		Toast toast = new Toast(ParkDetailActivity.this);
		LinearLayout linearLayout = new LinearLayout(ParkDetailActivity.this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(20, 20, 20, 20);

		// 定义一个ImageView
		ImageView imageView = new ImageView(ParkDetailActivity.this);
		imageView.setImageResource(R.drawable.voice_to_short); // 图标

		TextView mTv = new TextView(ParkDetailActivity.this);
		mTv.setText("录音时间应大于3秒!");
		mTv.setTextSize(14);
		mTv.setTextColor(Color.WHITE);// 字体颜色
		// mTv.setPadding(0, 10, 0, 0);

		// 将ImageView和ToastView合并到Layout中
		linearLayout.addView(imageView);
		linearLayout.addView(mTv);
		linearLayout.setGravity(Gravity.CENTER);// 内容居中
		linearLayout.setBackgroundResource(R.drawable.record_bg);// 设置自定义toast的背景

		toast.setView(linearLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);// 起点位置为中间 100为向下移100dp
		toast.show();
	}

	// 获取文件手机路径
	private String getAmrPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"my/voice.amr");
		return file.getAbsolutePath();
	}

	// 录音计时线程
	void mythread() {
		recordThread = new Thread(ImgThread);
		recordThread.start();
	}

	// 录音Dialog图片随声音大小切换
	void setDialogImage() {
		if (voiceValue < 200.0) {
			dialog_img.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			dialog_img.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			dialog_img.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			dialog_img.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			dialog_img.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			dialog_img.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			dialog_img.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_14);
		}
	}

	// 录音线程
	private Runnable ImgThread = new Runnable() {

		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					imgHandle.sendEmptyMessage(0);
				} else {
					try {
						Thread.sleep(200);
						recodeTime += 0.2;
						if (RECODE_STATE == RECORD_ING) {
							voiceValue = mr.getAmplitude();
							imgHandle.sendEmptyMessage(1);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		Handler imgHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 0:
					// 录音超过15秒自动停止
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						try {
							mr.stop();
							voiceValue = 0.0;
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (recodeTime < 1.0) {
							showWarnToast();
							voiceWindow.btn_record.setText("按住录音");
							RECODE_STATE = RECORD_NO;
						} else {
							voiceWindow.btn_record.setText("录音完成");
							// luyin_txt.setText("录音时间："+((int)recodeTime));
							// luyin_path.setText("文件路径："+getAmrPath());
						}
					}
					break;
				case 1:
					setDialogImage();
					break;
				default:
					break;
				}

			}
		};
	};
	
	class listener implements SynthesizerPlayerListener{
		
		@Override
		public void onBufferPercent(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onEnd(SpeechError arg0) {
			Log.i("tag", "结束回调");
			play.setText("播放");
		}

		@Override
		public void onPlayBegin() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPlayPaused() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPlayPercent(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPlayResumed() {
			// TODO Auto-generated method stub

		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 获取 back键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (menu_display) {
				// 如果 Menu已经打开 ，先关闭Menu
				popupWindow.dismiss();
				menu_display = false;
			} else {
				ParkDetailActivity.this.finish();

			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onMapClick(LatLng arg0) {
		 if(marker.isInfoWindowShown()){
		 marker.hideInfoWindow();
		 }	
	}

	public PopupWindow makeCarePopupWindow(Context cx) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mMenuView = inflater.inflate(R.layout.alert_popwindow, null);
		btn_edit_care = (TextView) mMenuView.findViewById(R.id.btn_edit_care);
		btn_remove_care = (TextView) mMenuView.findViewById(R.id.btn_remove_care);
		btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 销毁弹出框
				// dismiss();
				popupWindow.dismiss();
			}
		});
		btn_edit_care.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ParkDetailActivity.this,
						SetCareActivity.class);
				userid = info.getId();
				Bundle b = new Bundle();
				b.putString("userid", userid);
				b.putInt("pid", pid);
				b.putString("shortAddress", datas.getShortAddres());
				b.putString("shortname", datas.getShortName());
				intent.putExtras(b);
				startActivityForResult(intent, 7);

			}
		});
		btn_remove_care.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userid = info.getId();
				removeAttentionPark(userid, pid);

			}
		});
		PopupWindow window = new PopupWindow(mMenuView,
				WindowManager.LayoutParams.FILL_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		window.setTouchable(true); // 设置PopupWindow可触摸
		window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		window.setFocusable(true); // 这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setAnimationStyle(R.style.AnimBottom);
		return window;
	}

	public PopupWindow makeErrorPopupWindow(Context cx) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mMenuView = inflater.inflate(R.layout.voice_popwindow, null);
		btn_record = (Button) mMenuView.findViewById(R.id.record_sounce);
		player = (Button) mMenuView.findViewById(R.id.player);
		send = (Button) mMenuView.findViewById(R.id.send);
		record_type1 = (LinearLayout) mMenuView.findViewById(R.id.record_type1);
		record_type2 = (LinearLayout) mMenuView.findViewById(R.id.record_type2);
		stop = (Button) mMenuView.findViewById(R.id.stop);

		btn_record.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (RECODE_STATE != RECORD_ING) {
						scanOldFile();
						mr = new AudioRecorder("voice");
						RECODE_STATE = RECORD_ING;
						showVoiceDialog();
						try {
							mr.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mythread();
					}

					break;
				case MotionEvent.ACTION_UP:
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						try {
							mr.stop();
							voiceValue = 0.0;
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (recodeTime < MIX_TIME) {
							showWarnToast();
							// voiceWindow.btn_record.setText("按住录音");
							RECODE_STATE = RECORD_NO;
						} else {
							// voiceWindow.btn_record.setText("录音完成");
							voice_finish = true;
							animationController.slideOut(
									record_type1, 1000, 0);
							record_type1.setVisibility(View.GONE);
							animationController.slideIn(record_type2, 1000, 0);
							record_type2.setVisibility(View.VISIBLE);
							// luyin_txt.setText("录音时间："+((int)recodeTime));
							// luyin_path.setText("文件路径："+getAmrPath());
						}
					}

					break;
				}
				return false;
			}
		});
		player.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!playState) {
					mediaPlayer = new MediaPlayer();
					String url = "file:///sdcard/my/voice.amr";
					try {
						// 模拟器里播放传url，真机播放传getAmrPath()
						// mediaPlayer.setDataSource(url);
						mediaPlayer.setDataSource(getAmrPath());
						mediaPlayer.prepare();
						mediaPlayer.start();
						player.setBackgroundResource(R.drawable.stop);
						// voiceWindow.player.setText("正在播放");
						playState = true;
						// 设置播放结束时监听
						mediaPlayer
								.setOnCompletionListener(new OnCompletionListener() {

									@Override
									public void onCompletion(
											MediaPlayer mp) {
										if (playState) {
											// voiceWindow.player.setText("听听看");
											playState = false;
											player.setBackgroundResource(R.drawable.listener);
										}
									}
								});
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.stop();
						playState = false;
						player.setBackgroundResource(R.drawable.listener);
					} else {
						playState = false;
						player.setBackgroundResource(R.drawable.listener);
					}
					// voiceWindow.player.setText("听听看");
				}
			}
		});
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (voice_finish) {
					sendVoice sVoice = new sendVoice();
					sVoice.execute();
				} else {
					Toast.makeText(context, "请按住录音", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animationController.slideOut2(record_type2,1000, 0);
				record_type2.setVisibility(View.GONE);
				animationController.slideIn2(record_type1,1000, 0);
				record_type1.setVisibility(View.VISIBLE);
			}
		});

		PopupWindow window = new PopupWindow(mMenuView,
				LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		window.setTouchable(true); // 设置PopupWindow可触摸
		window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		window.setFocusable(true); // 这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setAnimationStyle(R.style.AnimBottom);
		return window;
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
          return pName;
  } 
}
