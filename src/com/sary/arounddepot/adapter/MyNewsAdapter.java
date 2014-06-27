package com.sary.arounddepot.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sary.arounddepot.R;
import com.sary.arounddepot.activity.MyNewsWebViewActivity;
import com.sary.arounddepot.entity.MyNewsInfo;

public class MyNewsAdapter extends BaseAdapter{
	
	private List<MyNewsInfo> list;
	
	private Context context;
	
	private Holder holder;
	
	private WebView webview;
	
	public MyNewsAdapter(Context context){
		this.context=context;
	}
	
	public void setNewsInfoList(List<MyNewsInfo> list){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyNewsInfo info=list.get(position);
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context)
					.inflate(R.layout.my_news_item, null);
			holder.news_content=(TextView)convertView.findViewById(R.id.news_content);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		if(info==null){
			return null;
		}
		holder.news_content.setText(info.getContent());
		if(!"".equals(info.getUrl())){
			holder.news_content.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i("tag", "进入");
					Bundle b=new Bundle();
					b.putString("url", info.getUrl());
					Intent intent=new Intent(context,MyNewsWebViewActivity.class);
					intent.putExtras(b);
					context.startActivity(intent);
				}
			});
		}
		return convertView;
	}
	
	class Holder{
		TextView news_content;
	}

}
