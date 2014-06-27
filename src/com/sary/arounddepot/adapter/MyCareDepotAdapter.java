package com.sary.arounddepot.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.receiver.AlarmReceiver;
import com.sary.arounddepot.resolver.BaseResolver;
import com.sary.arounddepot.resolver.ResolverFactory;
import com.sary.arounddepot.util.Constant;
import com.sary.arounddepot.util.DBHelper;
import com.sary.arounddepot.util.PrefHelper;
import com.sary.arounddepot.util.StringUtil;

public class MyCareDepotAdapter extends BaseAdapter {

	private static AsyncHttpClient client = new AsyncHttpClient();// 异步线程访问网络

	public boolean mNetConnected;// 网络状态是否连接

	private List<AroundParkData> list;

	private String userId;
	
	private PrefHelper mPrefHelper;

	private Context context;

	private LayoutInflater inflater;

	private ViewHolder view;

	private boolean isEdit,flag=true;
	
//	private List<Boolean> flags=new ArrayList<Boolean>();

	private int ids = -1;
	
	private int currentIndex = -1;

	private boolean os = false;
	
	public DBHelper dbHelper;

	public MyCareDepotAdapter(Context context, String userid){
		this.context = context;
		this.userId = userid;
		mPrefHelper = PrefHelper.getInstance(context);
		dbHelper = DBHelper.getInstance(context);
	}

	public void setMyCareDepotList(List<AroundParkData> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
		ids = -1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Log.d("LXH","id = "+position );
		return position;
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.mycaredepot_item, null);
			view = new ViewHolder();
			view.compile = (ImageButton) convertView.findViewById(R.id.btn_compile);
			view.delete = (ImageButton) convertView.findViewById(R.id.btn_delete);
			view.name = (TextView) convertView.findViewById(R.id.care_depotname);
			view.care_time = (TextView) convertView.findViewById(R.id.care_time);
			view.btn_care_time = (ImageView) convertView.findViewById(R.id.btn_care_time);
			view.repeat= (TextView) convertView.findViewById(R.id.repeat);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		final AroundParkData data = list.get(position);
		Log.i("tag", "位置："+position);
//		if (data == null) {
//			return null;
//		}
		view.name.setText(data.getShortName());
		
		if(!"".equals(data.getSet_date())&& data.getSet_date()!=null){
			view.repeat.setText(data.getSet_date());
		}
		
		if(!"null".equals(data.getSet_time())&& !"".equals(data.getSet_time())){
			view.care_time.setText(data.getSet_time());
		}else{
			view.care_time.setText("--");
		}
		if("null".equals(data.getSet_time())&& "".equals(data.getSet_date())){
			view.btn_care_time.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.off));
			flag=false;
		}
		
//		if(mPrefHelper.readInt("care")==1){
//			view.btn_care_time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.on));
//		}else{
//			view.btn_care_time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.off));
//		}
		if (!isEdit) {
			view.compile.setBackgroundResource(R.drawable.icon15);
			view.compile.setVisibility(View.GONE);
			view.delete.setVisibility(View.GONE);
			view.btn_care_time.setVisibility(View.VISIBLE);
		} else {
			view.compile.setVisibility(View.VISIBLE);
		}
		if (ids == position){
			if (!os) {
				view.compile.setBackgroundResource(R.drawable.icon14);
				view.delete.setVisibility(View.VISIBLE);
				view.btn_care_time.setVisibility(View.GONE);
			} else {
				view.compile.setBackgroundResource(R.drawable.icon15);
				view.delete.setVisibility(View.GONE);
				view.btn_care_time.setVisibility(View.VISIBLE);
			}
		} else {
			view.compile.setBackgroundResource(R.drawable.icon15);
			view.delete.setVisibility(View.GONE);
			view.btn_care_time.setVisibility(View.VISIBLE);
		}
		final int id = position;
		view.compile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (-1 != ids) {
					if (ids == id) {
						System.out.println("相同");
						if (!os) {
							os = true;
						} else {
							os = false;
						}
					} else {
						os = false;
						ids = id;
					}
				} else {
					ids = id;
					os = false;
				}
				notifyDataSetChanged();
			}
		});
		final int index = position;
		view.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View vie = (View) v.getParent();
				ViewHolder holder = (ViewHolder) vie.getTag();
				// holder.compile.setBackgroundResource(R.drawable.icon15);
				// holder.delete.setVisibility(View.GONE);
				ids = -1;
				AroundParkData data = list.get(index);
				list.remove(index);
				removeAttentionPark(userId,data.getId());
				dbHelper.deleteAlarmColock(userId, data.getId()+"");
			    notifyDataSetChanged();
			}
		});
		
		view.btn_care_time.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				ImageView view = (ImageView) v;
				if(flag==true){
					view.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.off));
					flag=false;
					mPrefHelper.save("care", 0);
					Intent intent = new Intent(context, AlarmReceiver.class);
					PendingIntent pi = PendingIntent.getBroadcast(context, data.getId(), intent, 0);
					AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
					am.cancel(pi);
				}else{
					view.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.on));
					flag=true;
					mPrefHelper.save("care", 1);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageButton compile, delete;
		TextView name, care_time,repeat;
		ImageView btn_care_time;
	}

	// 移除用户已关注的某个停车场
	private void removeAttentionPark(String userid, int pid) {
		ArrayList<NameValuePair> key = new ArrayList<NameValuePair>();
		key.add(new BasicNameValuePair("userId", userid));
		key.add(new BasicNameValuePair("pid", "" + pid));
		boolean result = sendRequest(Constant.REMOVE, key);
	}
	/*
	 * 以下是异步网络请求的数据操作（发送-接收-数据解析）
	 */
	protected void handleNearByMsg(int msg, BaseData data) {
	}

	protected boolean sendRequest(int msg, ArrayList<NameValuePair> key) {
		return sendRequest(msg, key, null);
	}

	// 通过此方法获得后台的数据
	protected boolean sendRequest(int msg, ArrayList<NameValuePair> getParam,
			ArrayList<NameValuePair> postParam) {

		NetworkInfo network = ((ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if ((network == null) || (!network.isAvailable())) {
			mNetConnected = false;
		} else {
			mNetConnected = true;
		}
		System.out.println("yyyyyyyyyyyy");
		if (!mNetConnected) {
			Toast.makeText(context, "未检测到网络，请检测网络连接", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		Log.e("tag", "msg" + msg);
		BaseResolver resolver = ResolverFactory.getResolver(msg);
		Log.e("tag", "resolver" + resolver);
		String url = resolver.getRequestURL() + "?";// 通过解析工厂得到实例后然后拿到访问的URL
		String strHash = "";//annel的值为test2,后台判断用哪个秘钥。也参与加密
		for(int i =0;i<getParam.size();i++){
			NameValuePair param = (NameValuePair) getParam.get(i);
			if(i==0){
				url += param.getName();
			}else{
				url += "&"+param.getName();
			}
		    url += "="+param.getValue();
		    strHash += param.getValue();
		}
		strHash +=Constant.CHANNEL;
		Log.e("tag", strHash);
//		strHash = StringUtil.toUTF8(strHash);//先UTF-8编码
		strHash = StringUtil.urlEncode(strHash);
		Log.e("tag", strHash);
		strHash += "@"+Constant.BASIC_KEY;//加入秘钥
		
		Log.e("tag", strHash);
		
		strHash = StringUtil.encrypt(strHash, "SHA");//SHA1加密
		strHash = StringUtil.encrypt(strHash, "MD5");//MD5加密
		strHash = strHash.toUpperCase();//变为大写		
		url += "&channel="+Constant.CHANNEL;
		url += "&sign="+strHash;
		Log.e("base", "=====" + url);
		// url =
		// "http://192.168.50.251/phone/parklist?location=121.437441,31.184826";
		if (postParam == null) {
			client.get(url, new RequestHandler(msg));
		} else {
			RequestParams req = new RequestParams();
			for (int i = 0; i < postParam.size(); i++) {
				NameValuePair param = postParam.get(i);
				req.put(param.getName(), param.getValue());
			}
			client.post(url, req, new RequestHandler(msg));
		}
		return true;
	}

	// 请求的处理
	public class RequestHandler extends AsyncHttpResponseHandler {

		private int mMessage;

		RequestHandler(int msg) {
			mMessage = msg;
		}

		@Override
		public void onSuccess(String response) {
			Log.e("tag", "onSuccess");
			BaseData data = null;
			try {
				BaseResolver resolver = ResolverFactory.getResolver(mMessage);
				data = resolver.parseData(response);// 解析数据
				System.out.println("zzzzzzzzzzzzzzzz");
			} catch (JSONException e) {
				Log.e("tag", e.toString());
				Log.e("tag", "content:" + response);
			}

			handleNearByMsg(mMessage, data);
		}

		@Override
		public void onFailure(Throwable e, String response) {
			Log.e("tag", "onFailure");
			Log.e("tag", "response" + response);
			e.printStackTrace();
			mNetConnected = false;
			Toast.makeText(context, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT).show();
			handleNearByMsg(mMessage, null);
		}
	}
	public void setSeclection(int position){
		currentIndex = position;
		}
}
