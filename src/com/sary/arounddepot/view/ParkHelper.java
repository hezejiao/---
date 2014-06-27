package com.sary.arounddepot.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbStrUtil;
import com.ab.view.wheel.AbWheelView;
import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.LargePicActivity;
import com.sary.arounddepot.activity.MainActivity;
import com.sary.arounddepot.adapter.ImageShowAdapter;
import com.sary.arounddepot.receiver.ParkAlarmReceiver;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DBHelper;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.MyCountdownTimer;
import com.sary.arounddepot.util.PrefHelper;
import com.sary.arounddepot.util.TimeSelectHelper;
import com.sary.arounddepot.view.FlipperLayout.OnOpenListener;

public class ParkHelper {

	private Context mContext;

	private Activity mActivity;

	private View mParkHelper;

	private OnOpenListener mOnOpenListener;

	private MyApplication application;

	public RelativeLayout timing, photo, fixtime;

	public Button photos, begin_time, start_count;

	private ImageButton btn_back;

	public LinearLayout jishi, dingshi, mywheel;

	public LinearLayout paizhao;

	public GridView mGridView = null;
	private static final String TAG = "AddPhotoActivity";
	private static final boolean D = Constant.DEBUG;
	public ImageShowAdapter mImagePathAdapter = null;
	public ArrayList<String> mPhotoList = new ArrayList<String>();
	public int selectIndex = 0;
	public int camIndex = 0;
	/* 拍照的照片存储位置 */
	public File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	public File mCurrentPhotoFile;
	public String mFileName;

	// 时间选择器
	private boolean timeChanged = false;
	private boolean timeScrolled = false;
	// 时间选择器2
	private boolean timeChanged2 = false;
	private boolean timeScrolled2 = false;
	private String horur, minitus, time2;
	// private WheelView hours, mins;

	public AbWheelView mWheelViewMD, mWheelViewMM, mWheelViewHH, hours, mins;

	protected PrefHelper mPrefHelper;
	private TextView txt_time;// 显示剩余时间
	private MyCount count;// 定时类对象
	// private MyTimeCount timecount;
	private long current_time = 0;
	// private long hour = 0,count_hour=0;// 时间变量
	// private long minute = 0,count_minute=0;
	private long second = 0, count_second = 0;
	private long time = 0, count_time = 0;// 毫秒为单位
	public boolean flag = true, flag2 = true;
	public ImageView help_back;
	private LinearLayout mWheelView;
	private static final int SECONDS = 60; // 秒数
	private static final int MINUTES = 60 * 60; // 小时

	private long first = 0, twice = 0, third = 0;
	private long mtmp = 0, mtmp2 = 0;
	
	private NotificationManager nm;
	private Notification notification;

	public ParkHelper(Context context, MyApplication app, Activity activity) {
		mContext = context;
		this.application = app;
		this.mActivity = activity;
		mParkHelper = LayoutInflater.from(context).inflate(
				R.layout.park_helper, null);
		initView();
	}

	public void initView() {
		mWheelView = (LinearLayout) mParkHelper.findViewById(R.id.mWheelView);
		start_count = (Button) mParkHelper.findViewById(R.id.start_count);
		mPrefHelper = PrefHelper.getInstance(mContext);
		btn_back = (ImageButton) mParkHelper.findViewById(R.id.btn_back);
		photos = (Button) mParkHelper.findViewById(R.id.mphotos);
		begin_time = (Button) mParkHelper.findViewById(R.id.begin_time);
		timing = (RelativeLayout) mParkHelper.findViewById(R.id.timing);
		photo = (RelativeLayout) mParkHelper.findViewById(R.id.photo);
		fixtime = (RelativeLayout) mParkHelper.findViewById(R.id.fixtime);
		jishi = (LinearLayout) mParkHelper.findViewById(R.id.jishi);

		paizhao = (LinearLayout) mParkHelper.findViewById(R.id.paizhao);
		help_back = (ImageView) mParkHelper.findViewById(R.id.help_back);
		dingshi = (LinearLayout) mParkHelper.findViewById(R.id.dingshi);
		mywheel = (LinearLayout) mParkHelper.findViewById(R.id.mywheel);
		txt_time = (TextView) mParkHelper.findViewById(R.id.txt_time);
		mGridView = (GridView) mParkHelper.findViewById(R.id.mGrid);
		mPhotoList = getPictures(Environment.getExternalStorageDirectory()
				+ "/hzj" + "");
		if (mPhotoList == null) {
			mPhotoList = new ArrayList<String>();
		}
		mImagePathAdapter = new ImageShowAdapter(mContext, mPhotoList, 60, 60);
		mGridView.setAdapter(mImagePathAdapter);
		initWheelTime2(mParkHelper);
		initWheelTime(mParkHelper);
		setListener();
	}

	public void initWheelTime(View mTimeView) {
		mWheelViewMD = (AbWheelView) mTimeView.findViewById(R.id.wheelView1);
		mWheelViewMM = (AbWheelView) mTimeView.findViewById(R.id.wheelView2);
		mWheelViewHH = (AbWheelView) mTimeView.findViewById(R.id.wheelView3);
		mWheelViewMD.setCenterSelectDrawable(new BitmapDrawable());
		mWheelViewMM.setCenterSelectDrawable(new BitmapDrawable());
		mWheelViewHH.setCenterSelectDrawable(new BitmapDrawable());
		TimeSelectHelper.initWheelTimePicker(mWheelViewMD, mWheelViewMM,
				mWheelViewHH, 2013, 1, 1, 10, 0, true);
	}

	public void initWheelTime2(View mTimeView) {
		hours = (AbWheelView) mTimeView.findViewById(R.id.hour);
		mins = (AbWheelView) mTimeView.findViewById(R.id.mins);
		hours.setCenterSelectDrawable(new BitmapDrawable());
		mins.setCenterSelectDrawable(new BitmapDrawable());
		TimeSelectHelper.initWheelTimePicker3(hours, mins, 1, 1, true);
		hours.setCurrentItem(0);
		mins.setCurrentItem(0);
	}

	private void searchFiles(File f) {
		if (f.isDirectory()) {
			File files[] = f.listFiles();
			if (files == null)
				return;
			for (int i = 0; i < files.length; i++) {
				searchFiles(files[i]);
			}
		} else {
			String path = f.getPath();
			mPhotoList.add(path);
		}
	}

	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		timing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jishi.setVisibility(View.VISIBLE);
				paizhao.setVisibility(View.GONE);
				dingshi.setVisibility(View.GONE);
				timing.setBackgroundResource(R.drawable.park_helpdown);
				photo.setBackgroundDrawable(null);
				fixtime.setBackgroundDrawable(null);

			}
		});
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jishi.setVisibility(View.GONE);
				paizhao.setVisibility(View.VISIBLE);
				dingshi.setVisibility(View.GONE);

				photo.setBackgroundResource(R.drawable.park_helpdown);
				timing.setBackgroundDrawable(null);
				fixtime.setBackgroundDrawable(null);
			}
		});
		photos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doPickPhotoAction();
			}
		});
		fixtime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jishi.setVisibility(View.GONE);
				paizhao.setVisibility(View.GONE);
				dingshi.setVisibility(View.VISIBLE);
				fixtime.setBackgroundResource(R.drawable.park_helpdown);
				timing.setBackgroundDrawable(null);
				photo.setBackgroundDrawable(null);
			}
		});
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("tag", "num:" + mImagePathAdapter.getCount());
				String imagePath = mPhotoList.get(position);
				Log.i("tag", "url:" + imagePath);
				Intent intent = new Intent(mContext, LargePicActivity.class);
				Bundle b = new Bundle();
				b.putString("imagePath", imagePath);
				b.putInt("position", position);
				intent.putExtras(b);
				mActivity.startActivityForResult(intent, 25);
			}
		});
		begin_time.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0){
				Log.i("tag", "进入计时。。。。。。。。");
				int hour = hours.getCurrentItem();
				int minute = mins.getCurrentItem();
				second = Long.parseLong("0");
				time = (hour * 3600 + minute * 60 + second) * 1000;
				if(time==0){
					DialogUtil.showAlert(mContext, "您设置的计时时间已到！");
					return ;
				}
				if (flag == true){
					begin_time.setText("停止倒计时");
					mywheel.setVisibility(View.GONE);
					txt_time.setVisibility(View.VISIBLE);
					flag = false;
					Log.i("tag", "设置的时间：" + time);
					count = new MyCount(time, 1000);
					count.start();// 开始计时
				} else {
					begin_time.setText("开始倒计时");
					mywheel.setVisibility(View.VISIBLE);
					txt_time.setVisibility(View.GONE);
					count.cancel();// 取消计时，重置界面状态
					txt_time.setText("");
					flag = true;
				}
			}
		});
		start_count.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("tag", "进入定时。。。。。。。。");
				int index1 = mWheelViewMD.getCurrentItem();
				int index2 = mWheelViewMM.getCurrentItem();
				int index3 = mWheelViewHH.getCurrentItem();
				List<String> textDMDateList = TimeSelectHelper.getList();
				String dmStr = textDMDateList.get(index1);
				String str[] = dmStr.split("-");
				String month = str[1];
				String day = str[2];
				String val = AbStrUtil.dateTimeFormat(dmStr + " " + index2
						+ ":" + index3);
				String vals = AbStrUtil.dateTimeFormat(dmStr);
				Log.i("tag", "dmStr:" + dmStr);
				Intent intent = new Intent(mContext, ParkAlarmReceiver.class);
				PendingIntent sender = PendingIntent.getBroadcast(mContext, 1,
						intent, 0);
				AlarmManager alarm = (AlarmManager) mContext
						.getSystemService(mContext.ALARM_SERVICE);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());

				calendar.set(Calendar.MONTH, (Integer.parseInt(month)) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
				calendar.set(Calendar.HOUR_OF_DAY, index2);
				calendar.set(Calendar.MINUTE, index3);
				// 将秒和毫秒设置为0
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				Log.i("tag", "时间比较：" + calendar.getTimeInMillis() + ":"
						+ Calendar.getInstance().getTimeInMillis());

				if (calendar.getTimeInMillis() < Calendar.getInstance()
						.getTimeInMillis()) {
					DialogUtil.showAlert(mContext, "您设置的定时时间已到！");
					return ;
				}
				if (flag2 == true){
					alarm.set(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(), sender);
					Toast.makeText(mContext, "定时闹钟设置成功", Toast.LENGTH_LONG)
							.show();
					start_count.setText("停止");
					help_back.setVisibility(View.VISIBLE);
					setTouchNot(mWheelViewMD, mWheelViewMM, mWheelViewHH);
					flag2 = false;
				} else {
					start_count.setText("定时闹钟");
					flag2 = true;
					PendingIntent senders = PendingIntent.getBroadcast(
							mContext, 1, intent, 0);
					AlarmManager am;
					am = (AlarmManager) mContext
							.getSystemService(mContext.ALARM_SERVICE);
					am.cancel(senders);
					help_back.setVisibility(View.GONE);
					setTouch(mWheelViewMD, mWheelViewMM, mWheelViewHH);
				}
			}
		});
	}

	public void setTouchNot(AbWheelView mWheelViewMD, AbWheelView mWheelViewMM,
			AbWheelView mWheelViewHH) {

		mWheelViewMD.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		mWheelViewMM.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		mWheelViewHH.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	public void setTouch(AbWheelView mWheelViewMD, AbWheelView mWheelViewMM,
			AbWheelView mWheelViewHH) {

		mWheelViewMD.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		mWheelViewMM.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		mWheelViewHH.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	/**
	 * 描述：从照相机获取
	 */
	private void doPickPhotoAction() {
		String imageFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/WordFoto/shiyan.jpg";// 设置图片的保存路径
		File imageFile = new File(imageFilePath);// 通过路径创建保存文件
		Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mActivity.startActivityForResult(intent, Constant.CAMERA_WITH_DATA);
	}

	public View getView() {
		return mParkHelper;
	}

	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

	// 实现计时功能的类
	
	class MyCount extends MyCountdownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish(){
			txt_time.setText("");
			begin_time.setText("开始倒计时");
			mywheel.setVisibility(View.VISIBLE);
			hours.setCurrentItem(0);
			mins.setCurrentItem(0);
			txt_time.setVisibility(View.GONE);
			// 对话框对象
			DialogUtil.showAlert(mContext, "停哪儿提醒您：倒计时已经结束！");
			
			ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
			if (taskInfos.size() > 0){
				Log.d("PackageName", taskInfos.get(0).topActivity.getPackageName());
				if (!mContext.getPackageName().equals(
						taskInfos.get(0).topActivity.getPackageName())){
					nm = (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
					notification = new Notification();
					notification.icon = R.drawable.icon1;
					notification.tickerText =mContext.getString(R.string.app_name);
					notification.when = System.currentTimeMillis();
					notification.defaults = Notification.DEFAULT_SOUND;
					Intent notice=new Intent(mContext,MainActivity.class);
					notice.setAction(Intent.ACTION_MAIN);
					notice.addCategory(Intent.CATEGORY_LAUNCHER);
					notice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					PendingIntent contentIntent = PendingIntent.getActivity(mContext, 1,
							notice, 0);
					notification.setLatestEventInfo(mContext, "停哪儿", "倒计时已经结束！", contentIntent);
					notification.contentIntent=contentIntent;
					notification.flags=Notification.FLAG_AUTO_CANCEL;
					notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
					// 将任务添加到任务栏中
					nm.notify(1, notification);
				}
			}
		}

		// 更新剩余时间
		@Override
		public void onTick(long millisUntilFinished, int percent) {
			// current_time = millisUntilFinished;
			// 获取当前时间总秒数
			first = millisUntilFinished / 1000;
			if (first < SECONDS) { // 小于一分钟 只显示秒
				txt_time.setText("00:00:" + (first < 10 ? "0" + first : first));
			} else if (first < MINUTES) { // 大于或等于一分钟，但小于一小时，显示分钟
				twice = first % 60; // 将秒转为分钟取余，余数为秒
				mtmp = first / 60;
				if (twice == 0) {
					txt_time.setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp)
							+ ":00"); // 只显示分钟
				} else {
					txt_time.setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp)
							+ ":" + (twice < 10 ? "0" + twice : twice)); // 显示分钟和秒
				}
			} else {
				twice = first % 3600; // twice为余数 如果为0则小时为整数
				mtmp = first / 3600;
				if (twice == 0) {
					// 只剩下小时
					txt_time.setText("0" + first / 3600 + ":00:00");
				} else {
					if (twice < SECONDS) { // twice小于60 为秒
						txt_time.setText((mtmp < 10 ? "0" + mtmp : mtmp)
								+ ":00:" + (twice < 10 ? "0" + twice : twice)); // 显示小时和秒
					} else {
						third = twice % 60; // third为0则剩下分钟 否则还有秒
						mtmp2 = twice / 60;
						if (third == 0) {
							txt_time.setText((mtmp < 10 ? "0" + mtmp : mtmp)
									+ ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2)
									+ ":00");
						} else {
							txt_time.setText((mtmp < 10 ? "0" + mtmp : mtmp)
									+ ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2)
									+ ":" + third); // 还有秒
						}
					}
				}
				long myhour = (millisUntilFinished / 1000) / 3600;

				long myminute = ((millisUntilFinished / 1000) - myhour * 3600) / 60;
				long mysecond = millisUntilFinished / 1000 - myhour * 3600
						- myminute * 60;
				if (myhour < 10 && myminute < 10) {
					txt_time.setText("0" + myhour + ":" + "0" + myminute + ":"
							+ mysecond);
				} else if (myhour < 10 && myminute > 10) {
					txt_time.setText("0" + myhour + ":" + myminute + ":"
							+ mysecond);
				} else if (myhour > 10 && myminute < 10) {
					txt_time.setText(myhour + ":" + "0" + myminute + ":"
							+ mysecond);
				} else {
					txt_time.setText(myhour + ":" + myminute + ":" + mysecond);
				}
			}
		}
	}

	public ArrayList<String> getPictures(final String strPath) {

		ArrayList<String> list = new ArrayList<String>();
		File file = new File(strPath);
		File[] files = file.listFiles();

		if (files == null) {
			return null;
		}

		for (int i = 0; i < files.length; i++) {
			final File f = files[i];
			if (f.isFile()) {
				try {
					int idx = f.getPath().lastIndexOf(".");
					if (idx <= 0) {
						continue;
					}
					String suffix = f.getPath().substring(idx);
					if (suffix.toLowerCase().equals(".jpg")
							|| suffix.toLowerCase().equals(".jpeg")
							|| suffix.toLowerCase().equals(".bmp")
							|| suffix.toLowerCase().equals(".png")
							|| suffix.toLowerCase().equals(".gif")) {
						list.add(f.getPath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}
}
