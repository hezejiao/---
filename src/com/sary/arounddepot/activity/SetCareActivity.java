package com.sary.arounddepot.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.wheel.AbWheelView;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.Result;
import com.sary.arounddepot.receiver.AlarmReceiver;
import com.sary.arounddepot.util.AlarmHelper;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DBHelper;
import com.sary.arounddepot.util.DialogUtil;
import com.sary.arounddepot.util.TimeSelectHelper;

public class SetCareActivity extends BaseActivity{
	private Context context;
    private boolean timeChanged = false;
	private boolean timeScrolled = false;
//	private String horur,minitus;
	private ProgressDialog mDialog;//搜索的进度条
	
	public static PendingIntent sender;
	//用户id，和停车场id
	private String userid,settime,shortname,shortName;
	private int pid;
	private ImageButton btn_back,btn_save;
	private RelativeLayout my_buttons;
//	private WheelView hours,mins;
	public AbWheelView hours,mins;
	private String repeatString1 = "无重复";
	private TextView repeatTextView,tip_time;
	private int[] repeatArray1 = {0,0,0,0,0,0,0};
    private boolean[] repeatArray2={false,false,false,false,false,false,false};
	
	public boolean repeat_1_1 = false;
	public boolean repeat_1_2 = false;
	public boolean repeat_1_3 = false;
	public boolean repeat_1_4 = false;
	public boolean repeat_1_5 = false;
	public boolean repeat_1_6 = false;
	public boolean repeat_1_7 = false;
	
	public DBHelper dbHelper;
	
	private String is_repeat,isopen,set_time;
	
	private String[] strArray=new String[4];
	
	private String repeats,set_mytime;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_care);
		context=getBaseContext();
		dbHelper = DBHelper.getInstance(context);
		Bundle b=getIntent().getExtras();
		userid=b.getString("userid");
		pid=b.getInt("pid");
		shortname=b.getString("shortAddress");
		shortName=b.getString("shortname");

		initView();
		setListener();
		Cursor cursor=dbHelper.getAlarmColock(userid, pid+"");
		if(cursor.moveToFirst()){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
			{
				set_time=cursor.getString(2);
				is_repeat=cursor.getString(3);
				isopen=cursor.getString(4);
			}
			if(set_time!=null){
				String []times=set_time.split(":");
				hours.setCurrentItem(Integer.parseInt(times[0]));
				mins.setCurrentItem(Integer.parseInt(times[1]));
			}
			if(is_repeat!=null){
				repeatTextView.setText(is_repeat);
			}else{
				repeatTextView.setText("无重复");
			}
			if(isopen!=null){
				String []rip=isopen.split("_");
				for(int i=0;i<rip.length;i++){
					if("true".equals(rip[i])){
						repeatArray2[i]=true;
					}else{
						repeatArray2[i]=false;
					}
				}
			}
		}
	}
	private void initView(){
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		btn_save=(ImageButton)findViewById(R.id.btn_save);
		my_buttons=(RelativeLayout)findViewById(R.id.my_buttons);
		repeatTextView=(TextView)findViewById(R.id.date_set);
		tip_time=(TextView)findViewById(R.id.tip_time);
//		String text= mPrefHelper.read("repeatString");
//		if(!"".equals(text)){
//			repeatTextView.setText(text);
//		}
		tip_time.setText("你希望我们在几点钟提醒您关于"+shortName+"的信息?");
		initWheelTime2();
	}
	
	public void initWheelTime2(){
		hours = (AbWheelView) findViewById(R.id.hour);
		mins = (AbWheelView) findViewById(R.id.mins);
		hours.setCenterSelectDrawable(new BitmapDrawable());
		mins.setCenterSelectDrawable(new BitmapDrawable());
		TimeSelectHelper.initWheelTimePicker2(hours, mins,1,1,true);
	}
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SetCareActivity.this.finish();
			}
		});
		
		btn_save.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				int hour = hours.getCurrentItem();
				int minute = mins.getCurrentItem();
				Intent intent =new Intent(SetCareActivity.this, AlarmReceiver.class);
				intent.putExtra("shortname", shortname);
				sender=PendingIntent.getBroadcast(SetCareActivity.this, pid, intent, 0);
				AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
				Calendar calendar=Calendar.getInstance();
//				int second = calendar.get(Calendar.SECOND);
//				int msecond=calendar.get(Calendar.MILLISECOND);
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hour);
				calendar.set(Calendar.MINUTE, minute);
				//将秒和毫秒设置为0
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				Log.i("tag", "时间："+calendar.getTimeInMillis()+" ,"+Calendar.getInstance().getTimeInMillis());
				if(calendar.getTimeInMillis()<Calendar.getInstance().getTimeInMillis() && repeatString1.equals("无重复")){
//					 Toast.makeText(context, "您在"+shortname+"的停车时间已到", Toast.LENGTH_SHORT).show();
//					calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1);
				}
				if(calendar.getTimeInMillis()>Calendar.getInstance().getTimeInMillis()
						 ||!repeatString1.equals("无重复")){
//					String time=hour+":"+minute;
//					mPrefHelper.save("time", time);
					set_time=hour+":"+minute;
				}
				int nowDay = AlarmHelper.getNowWeek();
				int setDay = 0;
				Log.i("tag", "今天："+nowDay);
				
				if(repeatString1.equals("无重复")&& calendar.getTimeInMillis()>Calendar.getInstance().getTimeInMillis()){
					alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), sender);
				}
				if(!(repeatString1.equals("无重复"))){
					String[] setStr = repeatString1.split(",");
					int[] dayOfNum = AlarmHelper.getDayOfNum(setStr);
            		setDay = AlarmHelper.getResultDifferDay(dayOfNum, nowDay);
            		int differDay = AlarmHelper.compareDayNowToNext(nowDay, setDay);
            		if(differDay == 0){
            			if(AlarmHelper.differSetTimeAndNowTime(calendar.getTimeInMillis())){
            				alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
            			}else{
            				alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() + AlarmHelper.getDifferMillis(7),sender);
            			}
            		}else{
            			alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() + AlarmHelper.getDifferMillis(differDay),sender);  
            		}
				}
				
				if(repeatString1.equals("无重复")){
					repeats="";
				}else{
					repeats=repeatString1;
				}
				caredeport(userid, pid,repeats);
				is_repeat=repeatString1;
				Cursor cursor= dbHelper.getAlarmColock(userid, pid+"");
				String[] strArray={pid+"",userid,set_time,is_repeat,isopen};
				if(cursor.moveToFirst()){
					dbHelper.updateAlarmColock(userid,pid+"", strArray);
				}else{
					dbHelper.insertAlarmColock(strArray);
				}
			}
		});
		my_buttons.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v){
				
				repeatString1 = "";
				new AlertDialog.Builder(SetCareActivity.this)
	            .setTitle(R.string.alert_dialog_multi_choice)
	            .setMultiChoiceItems(R.array.select_dialog_items,
	                    new boolean[]{repeatArray2[0], repeatArray2[1], repeatArray2[2], 
	            		repeatArray2[3], repeatArray2[4], repeatArray2[5], repeatArray2[6]},
	                    new DialogInterface.OnMultiChoiceClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton,
	                                boolean isChecked) {
	                        	/* User clicked on a check box do some stuff */
		                        switch(whichButton){
		                     	   case 0: if(isChecked){
		                     		          repeatArray1[0] = 1;
		                     	           }else{
		                     	        	  repeatArray1[0] = 0;
		                     	           }
		                     	           break;
		                     	   case 1:if(isChecked){
		              		                 repeatArray1[1] = 1;
		             	                  }else{
		             	        	         repeatArray1[1] = 0;
		             	                  }
		                     	           break;
		                     	   case 2:if(isChecked){
		                     		         repeatArray1[2] = 1;
		                     	          }else{
		                     	        	 repeatArray1[2] = 0;
		                     	          }
		             	                   break;
		                     	   case 3:if(isChecked){
		                     		         repeatArray1[3] = 1;
		                     	          }else{
		                     	        	 repeatArray1[3] = 0;
		                     	          }
		             	                   break;
		                     	   case 4:if(isChecked){
		                     		         repeatArray1[4] = 1;
		                     	          }else{
		                     	        	 repeatArray1[4] = 0;
		                     	          }
		             	                   break;
		                     	   case 5:if(isChecked){
		                     		         repeatArray1[5] = 1;
		                     	          }else{
		                     	        	 repeatArray1[5] = 0;
		                     	          }
		             	                   break;
		                     	   case 6:if(isChecked){
		                     		         repeatArray1[6] = 1;
		                     	          }else{
		                     	        	 repeatArray1[6] = 0;
		                     	          }
		             	                   break;
		                     	   default: break;
		                     	}                        	
	                        }
	                    })
	            .setPositiveButton(R.string.exit_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	
	                    if(repeatArray1[0] == 1){
	                    	repeatString1 += "周一"+",";
	                    	repeat_1_1 = true;
	                    }else{
	                    	repeat_1_1 = false;
	                    }
	                    
	                    if(repeatArray1[1] == 1){
	                    	repeatString1 += "周二"+",";
	                    	repeat_1_2 = true;
	                    }else{
	                    	repeat_1_2 = false;
	                    }
	                    
	                    if(repeatArray1[2] == 1){
	                    	repeatString1 += "周三"+",";
	                    	repeat_1_3 = true;
	                    }else{
	                    	repeat_1_3 = false;
	                    }
	                  
	                    if(repeatArray1[3] == 1){
	                    	repeatString1 += "周四"+",";
	                    	repeat_1_4 = true;
	                    }else{
	                    	repeat_1_4 = false;
	                    }
	                    
	                    if(repeatArray1[4] == 1){
	                    	repeatString1 += "周五"+",";
	                    	repeat_1_5 = true;
	                    }else{
	                    	repeat_1_5 = false;
	                    }
	                    
	                    if(repeatArray1[5] == 1){
	                    	repeatString1 += "周六"+",";
	                    	repeat_1_6 = true;
	                    }else{
	                    	repeat_1_6 = false;
	                    }
	                    
	                    if(repeatArray1[6] == 1){
	                    	repeatString1 += "周日";
	                    	repeat_1_7 = true;
	                    }else{
	                    	repeat_1_7 = false;
	                    }
	                    
	                    if(!(repeat_1_1 || repeat_1_2 || repeat_1_3 || repeat_1_4 ||
	                    		repeat_1_5 || repeat_1_6 || repeat_1_7)){
	                    	repeatString1 = "无重复";
	                    }
	                    
	                    repeatTextView.setText(repeatString1);
//                        mPrefHelper.save("repeatString", repeatString1);
	                    StringBuilder buffer=new StringBuilder();
	                    for(int i=0;i<repeatArray1.length;i++){
	                    	if(repeatArray1[i]==0){
	                    		buffer.append(false);
	                    	}else{
	                    		buffer.append(true);
	                    	}
	                    	buffer.append("_");
	                    	
	                    }
	                    isopen=buffer.toString();
	                }
	            })
	            .setNegativeButton(R.string.exit_cancle, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	
	                }
	            })
	           .show();
//				mPrefHelper.save("repeat", value);
			}
		});
		
	}
	
	private void caredeport(String userid,int pid,String repeat){
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId",userid));
		key.add(new BasicNameValuePair("pid",""+pid));
		key.add(new BasicNameValuePair("set_time",set_time));
		key.add(new BasicNameValuePair("set_date",repeat));
		boolean result = sendRequest(Constant.CARE, key);
		if(result){
			mDialog = DialogUtil.showProgress(SetCareActivity.this,"正在添加关注...", null);
		}
	}
	@Override
	protected void handleNearByMsg(int msg, BaseData data) {
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		if(data==null){
			return;
		}
		Result result=(Result)data;
		if("200".equals(result.getCode())){
			Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
			SetCareActivity.this.setResult(6);
			SetCareActivity.this.finish();
		}
	}	
	private String format(int x)
    {
	    String s=""+x;
	    if(s.length()==1) s="0"+s;
	    return s;
	}
	
}
