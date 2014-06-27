/**
 * 
 */
package com.sary.arounddepot.receiver;

import java.util.List;

import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.MainActivity;
import com.sary.arounddepot.service.AlarmService;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ParkAlarmReceiver extends BroadcastReceiver {
	
	private NotificationManager nm;
	private Notification notification;
	
	@Override
	public void onReceive(Context context, Intent intent){
		Log.i("tag", "闹钟响了");
//	    Toast.makeText(context, "您设置的计时时间已到", Toast.LENGTH_SHORT).show();
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
		if (taskInfos.size() > 0){
			Log.d("PackageName", taskInfos.get(0).topActivity.getPackageName());
			if (!context.getPackageName().equals(
					taskInfos.get(0).topActivity.getPackageName())){
				nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
				notification = new Notification();
				notification.icon = R.drawable.icon1;
				notification.tickerText =context.getString(R.string.app_name);
				notification.when = System.currentTimeMillis();
				notification.defaults = Notification.DEFAULT_SOUND;
				Intent notice=new Intent(context,MainActivity.class);
				notice.setAction(Intent.ACTION_MAIN);
				notice.addCategory(Intent.CATEGORY_LAUNCHER);
				//设置启动模式
				notice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				notice.putExtra("notice", 1);
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
						notice, 0);
				notification.setLatestEventInfo(context, "停哪儿", "您设置的停车助手定时已到点", contentIntent);
				notification.contentIntent=contentIntent;
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
				// 将任务添加到任务栏中，不同通知id应该不同，不然会覆盖
				nm.notify(0, notification);
			}
		}
		Intent mintent=new Intent();
		mintent.setAction("com.sary.park");
	    context.sendBroadcast(mintent);//发送广播
	    
	    
//	    Intent i = new Intent(context, AlarmService.class);
//	    context.startService(i); 
	}
}
