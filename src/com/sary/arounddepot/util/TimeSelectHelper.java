package com.sary.arounddepot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.util.AbDateUtil;
import com.ab.util.AbStrUtil;
import com.ab.view.wheel.AbNumericWheelAdapter;
import com.ab.view.wheel.AbStringWheelAdapter;
import com.ab.view.wheel.AbWheelView;

public class TimeSelectHelper {
	
	/**
	 * 描述：默认的月日时分的时间选择器.
	 *
	 * @param activity     AbActivity对象
	 * @param mText the m text
	 * @param mWheelViewMD  选择月日的轮子
	 * @param mWheelViewHH the m wheel view hh
	 * @param mWheelViewMM  选择分的轮子
	 * @param okBtn 确定按钮
	 * @param cancelBtn 取消按钮
	 * @param defaultYear the default year
	 * @param defaultMonth the default month
	 * @param defaultDay the default day
	 * @param defaultHour the default hour
	 * @param defaultMinute the default minute
	 * @param initStart the init start
	 * @date：2013-7-16 上午10:19:01
	 * @version v1.0
	 */
	public static void initWheelTimePicker(AbWheelView mWheelViewMD,final AbWheelView mWheelViewHH,final AbWheelView mWheelViewMM,
			 int defaultYear,int defaultMonth,int defaultDay,int defaultHour,int defaultMinute,boolean initStart){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		if(initStart){
			defaultYear = year;
			defaultMonth = month;
			defaultDay = day;
			defaultHour = hour;
			defaultMinute = minute;
		}
		String val = AbStrUtil.dateTimeFormat(defaultYear+"-"+defaultMonth+"-"+defaultDay+" "+defaultHour+":"+defaultMinute+":"+second) ;
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		//
		final List<String> textDMList = new ArrayList<String>();
		final List<String> textDMDateList = new ArrayList<String>();
		for(int i=1;i<13;i++){
			if(list_big.contains(String.valueOf(i))){
				for(int j=1;j<32;j++){
					textDMList.add(i+"月"+" "+j+"日");
					textDMDateList.add(defaultYear+"-"+i+"-"+j);
				}
			}else{
				if(i==2){
					if(AbDateUtil.isLeapYear(defaultYear)){
						for(int j=1;j<28;j++){
							textDMList.add(i+"月"+" "+j+"日");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}else{
						for(int j=1;j<29;j++){
							textDMList.add(i+"月"+" "+j+"日");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}
				}else{
					for(int j=1;j<29;j++){
						textDMList.add(i+"月"+" "+j+"日");
						textDMDateList.add(defaultYear+"-"+i+"-"+j);
					}
				}
			}
			
		}
		String currentDay = defaultMonth+"月"+" "+defaultDay+"日";
		int currentDayIndex = textDMList.indexOf(currentDay);
		
		// 月日
		mWheelViewMD.setAdapter(new AbStringWheelAdapter(textDMList,AbStrUtil.strLength("12月"+" "+"12日")));
		mWheelViewMD.setCyclic(true);
		mWheelViewMD.setLabel(""); 
		mWheelViewMD.setCurrentItem(currentDayIndex);
		mWheelViewMD.setValueTextSize(32);
		mWheelViewMD.setLabelTextSize(38);
//		mWheelViewMD.setLabelTextColor(0x80000000);
		//mWheelViewMD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("点");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(32);
		mWheelViewHH.setLabelTextSize(30);
//		mWheelViewHH.setLabelTextColor(0x80000000);
		//mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("分");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(32);
		mWheelViewMM.setLabelTextSize(30);
//		mWheelViewMM.setLabelTextColor(0x80000000);
		//mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
    }
	
	/**
	 * 描述：默认的时分的时间选择器.
	 *
	 * @param activity     AbActivity对象
	 * @param mText the m text
	 * @param mWheelViewHH the m wheel view hh
	 * @param mWheelViewMM  选择分的轮子
	 * @param okBtn 确定按钮
	 * @param cancelBtn 取消按钮
	 * @param defaultHour the default hour
	 * @param defaultMinute the default minute
	 * @param initStart the init start
	 */
	public static void initWheelTimePicker2(final AbWheelView mWheelViewHH,final AbWheelView mWheelViewMM,
			 int defaultHour,int defaultMinute,boolean initStart){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		if(initStart){
			defaultHour = hour;
			defaultMinute = minute;
		}
		String val = AbStrUtil.dateTimeFormat(defaultHour+":"+defaultMinute) ;
		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("点");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(28);
		mWheelViewHH.setLabelTextSize(25);
//		mWheelViewHH.setLabelTextColor(0x80000000);
		//mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("分");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(28);
		mWheelViewMM.setLabelTextSize(25);
//		mWheelViewMM.setLabelTextColor(0x80000000);
    }
	
	public static void initWheelTimePicker3(final AbWheelView mWheelViewHH,final AbWheelView mWheelViewMM,
			 int defaultHour,int defaultMinute,boolean initStart){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		if(initStart){
			defaultHour = hour;
			defaultMinute = minute;
		}
		String val = AbStrUtil.dateTimeFormat(defaultHour+":"+defaultMinute) ;
		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 7));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("小时");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(28);
		mWheelViewHH.setLabelTextSize(25);
//		mWheelViewHH.setLabelTextColor(0x80000000);
		//mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("分钟");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(28);
		mWheelViewMM.setLabelTextSize(25);
//		mWheelViewMM.setLabelTextColor(0x80000000);
   }

	public static List<String>  getList(){
		Calendar calendar = Calendar.getInstance();
		int defaultYear = calendar.get(Calendar.YEAR);
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		//
		final List<String> textDMList = new ArrayList<String>();
		final List<String> textDMDateList = new ArrayList<String>();
		for(int i=1;i<13;i++){
			if(list_big.contains(String.valueOf(i))){
				for(int j=1;j<32;j++){
					textDMList.add(i+"月"+" "+j+"日");
					textDMDateList.add(defaultYear+"-"+i+"-"+j);
				}
			}else{
				if(i==2){
					if(AbDateUtil.isLeapYear(defaultYear)){
						for(int j=1;j<28;j++){
							textDMList.add(i+"月"+" "+j+"日");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}else{
						for(int j=1;j<29;j++){
							textDMList.add(i+"月"+" "+j+"日");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}
				}else{
					for(int j=1;j<29;j++){
						textDMList.add(i+"月"+" "+j+"日");
						textDMDateList.add(defaultYear+"-"+i+"-"+j);
					}
				}
			}
			
		}
		return textDMDateList;
	}
}
