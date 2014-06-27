package com.sary.arounddepot.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbViewUtil;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.Update;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.service.UpdateService;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DBHelper;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.view.Desktop;
import com.sary.arounddepot.view.Desktop.onChangeViewListener;
import com.sary.arounddepot.view.FlipperLayout;
import com.sary.arounddepot.view.FlipperLayout.OnOpenListener;
import com.sary.arounddepot.view.MainView;
import com.sary.arounddepot.view.MyInfo;
import com.sary.arounddepot.view.MyNews;
import com.sary.arounddepot.view.ParkHelper;
import com.sary.arounddepot.view.Set;
import com.sary.arounddepot.view.SuggestionBack;

public class MainActivity extends BaseActivity implements OnOpenListener,
		SynthesizerPlayerListener {
	/**
	 * 当前显示内容的容器(继承于ViewGroup)
	 */
	public FlipperLayout mRoot;
	/**
	 * 菜单界面
	 */
	private Desktop mDesktop;
	/**
	 * 内容首页界面
	 */
	public MainView mView;
	/**
	 * 消息首页界面
	 */
	private MyNews mNews;
	/**
	 * 个人中心
	 */
	public MyInfo mInfo;
	/**
	 * 停车助手
	 */
	public ParkHelper mParkHelper;
	/**
	 * 意见反馈
	 */
	private SuggestionBack mSuBack;
	/**
	 * 设置
	 */
	private Set mSet;
	/**
	 * 当前显示的View的编号
	 */
	private int mViewPosition;
	/**
	 * 退出时间
	 */
	private long mExitTime;
	/**
	 * 退出间隔
	 */
	private static final int INTERVAL = 2000;

	public static Activity mInstance;

	private boolean isFirstIn = true;

	public static Context context;

	private UserInfo userInfo;

	private MyApplication app;

	public DBHelper dbHelper;

	Bundle savedInstanceState;

	public ProgressDialog mDialog;// 搜索的进度条

	// 拍照：
	private static final String TAG = "AddPhotoActivity";

	// 合成对象.
	public SynthesizerPlayer mSynthesizerPlayer;

	public Update update;

	// 通知对话框
	private Dialog noticeDialog;

	private MyBroadcastReceiver myBroadcastReceiver;

	/** {@link #mCenterView}的两侧阴影效果 */
	private RelativeLayout bgShade;

	private int mScreenWidth;
	private int mScreenHeight;

	private int count;

	private TextView refuse, dialog_cancel, give_high_opinion;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		((MyApplication) getApplication()).mMain = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.app = (MyApplication) getApplication();
		context = getBaseContext();
		count = mPrefHelper.readInt(Constant.APP_COUNT);
		mPrefHelper.save(Constant.APP_COUNT, ++count);
		Log.d("tag", "进入应用次数：" + count);
		dbHelper = DBHelper.getInstance(getApplicationContext());
		context = getBaseContext();
		bgShade = new RelativeLayout(context);
		WindowManager windowManager = getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		mScreenWidth = display.getWidth();
		mScreenHeight = display.getHeight();
		LayoutParams bgParams = new LayoutParams(mScreenWidth, mScreenHeight);
		bgShade.setLayoutParams(bgParams);

		View bgShadeContent = new View(context);
		bgShadeContent.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shadows_sidebar));
		bgShade.addView(bgShadeContent, bgParams);

		mRoot = new FlipperLayout(MainActivity.this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mRoot.setLayoutParams(params);
		mDesktop = new Desktop(this, this, app);
		mView = new MainView(app, this, this, savedInstanceState);
		mRoot.addView(mDesktop.getView(), 0, params);

		mRoot.addView(bgShade, 1, bgParams); // 阴影层
		mRoot.addView(mView.getView(), 2, params);
		setContentView(mRoot);
		checkUpdate(2, app.curVersionCode);
		setListener();
		mInstance = this;
		if (count % 3 == 0 && mPrefHelper.readBoolean("refuse_way")) {
			final MyDialog dialog = new MyDialog(this, 300, 300,
					R.layout.score_dialog, R.style.my_dialog);
			dialog.show();
			refuse = (TextView) dialog.findViewById(R.id.refuse);
			dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);
			give_high_opinion = (TextView) dialog
					.findViewById(R.id.give_high_opinion);
			refuse.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog_cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mPrefHelper.save("refuse_way", false);
					dialog.cancel();
				}
			});
			give_high_opinion.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mPrefHelper.save("refuse_way", false);
					dialog.cancel();
					try {
						Uri uri = Uri.parse("market://details?id="
								+ getPackageName());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(context, "未检测到市场", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
		}
	}

	private void checkUpdate(int type, int curVersionCode) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("type", type + ""));
		key.add(new BasicNameValuePair("version", curVersionCode + ""));
		boolean result = sendRequest(Constant.UPDATE, key);
	}

	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (data == null) {
			Toast.makeText(getApplicationContext(), "数据加载出错，请重试",
					Toast.LENGTH_SHORT).show();
			return;
		}
		update = (Update) data;
		Log.i("tag", "版本信息：" + app.curVersionCode + ", " + app.curVersionName
				+ update.getVersionCode() + "," + update.getVersionName());
		if (update.getForce() == 1) {
			showForceNoticeDialog();
		}
		if (update.getUpdate() == 1 && update.getForce() == 0) {
			showNoticeDialog();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (myBroadcastReceiver == null) {
			myBroadcastReceiver = new MyBroadcastReceiver();
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.sary.park");
		registerReceiver(myBroadcastReceiver, intentFilter);
	}

	@Override
	protected void onResume() {
		if (mView != null) {
			if (mView.mapView != null) {
				mView.mapView.onResume();
			}
		}
		super.onPause();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("tag", "shanchhul");
		if (mView != null) {
			if (mView.mapView != null) {
				mView.mapView.onPause();
			}
		}
		// if(mView.mSynthesizerPlayer!=null){
		// mView.mSynthesizerPlayer.cancel();
		// }
	}

	@Override
	protected void onStop() {
		Log.i("tag", "shanchhu3");
		super.onStop();
		if (mView != null) {
			if (mView.mSynthesizerPlayer != null) {
				mView.mSynthesizerPlayer.cancel();
			}
			if (mView.mapView != null) {
				mView.mapView.onPause();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// dbHelper=DBHelper.getInstance(getApplicationContext());
		// dbHelper.clearPark();
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
		Log.i("tag", "shanchhu2");
		if (mView != null) {
			if (mView.mapView != null) {
				mView.mapView.onDestroy();
			}
			if (mView.tip_image != null) {
				BitmapDrawable bmpDrawable = (BitmapDrawable) mView.tip_image
						.getBackground();
				if (bmpDrawable != null) {
					Bitmap bmp = bmpDrawable.getBitmap();
					mView.tip_image.setBackgroundDrawable(null);
					if (bmp != null && bmp.isRecycled() == false) {
						try {
							bmpDrawable.getBitmap().recycle(); // 这个生命周期会销毁View，因此在回收被View引用的控件时，也无需先对View引用图片的属性置为空
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	private void setListener() {
		mView.setOnOpenListener(this);
		/**
		 * 监听菜单界面切换显示内容(onChangeViewListener接口在Desktop中定义)
		 */
		mDesktop.setOnChangeViewListener(new onChangeViewListener() {

			public void onChangeView(int arg0) {
				mViewPosition = arg0;
				switch (arg0) {
				case Constant.MAIN:
					mView = new MainView(app, MainActivity.this,
							MainActivity.this, savedInstanceState);
					mView.setOnOpenListener(MainActivity.this);
					mRoot.close(mView.getView());
					break;
				case Constant.MY_INFO:
					if (mInfo == null) {
						mInfo = new MyInfo(MainActivity.this, app);
						mInfo.setOnOpenListener(MainActivity.this);
					} else {
						mInfo.initView();
					}
					mRoot.close(mInfo.getView());
					break;
				case Constant.MY_NEWS:
					if (mNews == null) {
						mNews = new MyNews(MainActivity.this,
								MainActivity.this, app);
						mNews.setOnOpenListener(MainActivity.this);
					} else {
						mNews.initView();
					}
					mRoot.close(mNews.getView());
					break;
				case Constant.PARK_HELPER:
					if (mParkHelper == null) {
						mParkHelper = new ParkHelper(MainActivity.this, app,
								MainActivity.this);
						mParkHelper.setOnOpenListener(MainActivity.this);
					}
					mRoot.close(mParkHelper.getView());
					break;
				case Constant.SUGGESTION_BACK:
					if (mSuBack == null) {
						mSuBack = new SuggestionBack(MainActivity.this);
						mSuBack.setOnOpenListener(MainActivity.this);
					}
					mRoot.close(mSuBack.getView());
					break;
				case Constant.SET:
					if (mSet == null) {
						mSet = new Set(MainActivity.this);
						mSet.setOnOpenListener(MainActivity.this);
					}
					mRoot.close(mSet.getView());
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 17) {
			AroundParkData aroundData = (AroundParkData) data.getExtras()
					.getSerializable("aroundpark");
			// app.screen = 1;
			app.listDatas = new AroundParkListData();
			dbHelper.clearPark();
			Log.e("tag",
					"指定地点的经纬度" + aroundData.getLongitude() + ","
							+ aroundData.getLatitude() + ","
							+ aroundData.getShortAddres());
			app.longitude = aroundData.getLongitude();
			app.latitude = aroundData.getLatitude();
			if (app.aLocation == null) {
				Toast.makeText(getApplicationContext(), "未检测到网络，请检测网络连接",
						Toast.LENGTH_SHORT).show();
				return;
			}
			app.aLocation.setLatitude(aroundData.getLatitude());
			app.aLocation.setLongitude(aroundData.getLongitude());
			app.address = aroundData.getShortAddres();
			mView = new MainView(app, MainActivity.this, MainActivity.this,
					savedInstanceState);
			mView.current_location.setText(app.address);
			mView.setOnOpenListener(MainActivity.this);
			mRoot.close(mView.getView());

			if (null == mSynthesizerPlayer) {
				// 创建合成对象.
				mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(
						context, "appid=" + context.getString(R.string.app_id));
			}
			int voice_Type = mPrefHelper.readInt("vioce_type");
			int have_voice = mPrefHelper.readInt("have_voice");
			if (voice_Type == 0) {
				mSynthesizerPlayer.setVoiceName("xiaoyu");
			} else {
				mSynthesizerPlayer.setVoiceName("xiaoyan");
			}
			if (have_voice == 0) {
				mSynthesizerPlayer.setVolume(50);
			} else {
				mSynthesizerPlayer.setVolume(0);
			}
			mSynthesizerPlayer.setSpeed(50);
			String content = "现在显示的是" + app.address + "附近的停车场";
			Log.i("tag", "朗读内容：" + content);
			mSynthesizerPlayer.playText(content, null, this);
		}
		if (resultCode == 31) {
			mInfo = new MyInfo(MainActivity.this, app);
			mInfo.setOnOpenListener(MainActivity.this);
			mRoot.close(mInfo.getView());
		}
		if (requestCode == Constant.CAMERA_WITH_DATA) {

			String sdStatus = Environment.getExternalStorageState();

			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Toast.makeText(context, "sd卡不可用", Toast.LENGTH_SHORT).show();
				return;
			}
			if (data == null) {
				return;
			}
			String name = new DateFormat().format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");
			FileOutputStream b = null;
			String fileName = Environment.getExternalStorageDirectory()
					+ "/hzj";
			File file1 = new File(fileName);
			if (!file1.exists())
				file1.mkdirs();
			String filenames = "";
			try {
				filenames = fileName + "/" + name;
				b = new FileOutputStream(filenames);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (b != null) {
						b.flush();
						b.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			mParkHelper.mImagePathAdapter.addItem(
					mParkHelper.mImagePathAdapter.getCount(), filenames);
			mParkHelper.camIndex++;
			AbViewUtil.setAbsListViewHeight(mParkHelper.mGridView, 2, 25);

		}
		if (requestCode == 25) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String path = bundle.getString("imagepath");
				File deleteFile = new File(path);
				deleteFile.delete();
				int position = bundle.getInt("position");
				mParkHelper.mImagePathAdapter.deleteItem(position);
				AbViewUtil.setAbsListViewHeight(mParkHelper.mGridView, 2, 25);
			}
		}
	}

	/**
	 * 返回键监听
	 */
	public void onBackPressed() {
		exit();
	}

	/**
	 * 判断两次返回时间间隔,小于两秒则退出程序
	 */
	private void exit() {
		if (System.currentTimeMillis() - mExitTime > INTERVAL) {
			Toast.makeText(this, "再按一次返回键,可直接退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			// 新增
			dbHelper.clearPark();
			mPrefHelper.save("sort_way", 0);
			mPrefHelper.save("first", 0);
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	public void open() {
		if (mRoot.getScreenState() == FlipperLayout.SCREEN_STATE_CLOSE) {
			mRoot.open();
		}
	}

	@Override
	public void onBufferPercent(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnd(SpeechError arg0) {
		// TODO Auto-generated method stub

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

	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("发现新版本");
		// builder.setMessage(updateMsg);
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent updateIntent = new Intent(MainActivity.this,
						UpdateService.class);
				updateIntent
						.putExtra("url",
								"http://www.591park.com/wordpress/download/tn_laster.apk");
				startService(updateIntent);
			}
		});
		builder.setNegativeButton("忽略", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 强制更新
	 */
	private void showForceNoticeDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("发现新版本");
		builder.setCancelable(false);
		// builder.setMessage(updateMsg);
		builder.setPositiveButton("更新", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent updateIntent = new Intent(MainActivity.this,
						UpdateService.class);
				updateIntent
						.putExtra("url",
								"http://www.591park.com/wordpress/download/tn_laster.apk");
				startService(updateIntent);
//				MainActivity.this.finish();
			}
		});
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				finish();
//			}
//		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	// 广播类
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if ("com.sary.park".equals(intent.getAction())) {
				if (mParkHelper != null && mParkHelper.start_count != null) {
					mParkHelper.start_count.setText("定时闹钟");
					mParkHelper.flag2 = true;
					mParkHelper.help_back.setVisibility(View.GONE);
					mParkHelper.setTouch(mParkHelper.mWheelViewMD,
							mParkHelper.mWheelViewMM, mParkHelper.mWheelViewHH);
					PendingIntent senders = PendingIntent.getBroadcast(context,
							1, intent, 0);
					AlarmManager am;
					am = (AlarmManager) getSystemService(ALARM_SERVICE);
					am.cancel(senders);
					Log.i("tag", "进来的次数i");
					DialogUtil.showAlert2(context, "停哪儿提醒您", "您设置地停车助手定时已到点");
					// context.stopService(intent);
				}
			}
		}
	}
}
