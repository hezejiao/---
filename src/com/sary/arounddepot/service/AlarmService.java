package com.sary.arounddepot.service;

import java.io.IOException;
import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.AboutDepotActivity;
import com.sary.arounddepot.activity.AboutUsActivity;
import com.sary.arounddepot.activity.LoginActivity;
import com.sary.arounddepot.activity.MainActivity;
import com.sary.arounddepot.view.ParkHelper;

public class AlarmService extends Service {

	private NotificationManager nm;
	private Notification notification;
	private RemoteViews views;
	
	public int NOT_FLAG = 27;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i("tag", "进入通知：");
		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
		if (taskInfos.size() > 0){
			Log.d("PackageName", taskInfos.get(0).topActivity.getPackageName());
			if (!getPackageName().equals(
					taskInfos.get(0).topActivity.getPackageName())){
				nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				notification = new Notification();
				notification.icon = R.drawable.icon1;
				notification.tickerText = getString(R.string.app_name);
				notification.when = System.currentTimeMillis();
				notification.defaults = Notification.DEFAULT_SOUND;
				Intent notice=new Intent(this,MainActivity.class);
				notice.setAction(Intent.ACTION_MAIN);
				notice.addCategory(Intent.CATEGORY_LAUNCHER);
				notice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				notice.putExtra("notice", 1);
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
						notice, 0);
				notification.setLatestEventInfo(this, "停哪儿", "您设置的停车助手定时已到点", contentIntent);
				notification.contentIntent=contentIntent;
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
				// 将任务添加到任务栏中
				nm.notify(0, notification);
			}
		}
		Intent mintent=new Intent();
		mintent.setAction("com.sary.park");
	    sendBroadcast(mintent);//发送广播
	    stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
}
