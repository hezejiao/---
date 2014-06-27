package com.sary.arounddepot.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.AroundParkData;

public class SearchAdapter extends BaseAdapter{
	
    private List<AroundParkData> list;
	
	private Context context;
	
	private Holder holder;
	
	
   public SearchAdapter(Context context,List<AroundParkData> list){
		
		this.context=context;
		this.list=list;
	}
	
	public void setAroundParkList(List<AroundParkData> list) {
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
		AroundParkData data=list.get(position);
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context)
					.inflate(R.layout.activity_search_item, null);
			holder.park_title=(TextView)convertView.findViewById(R.id.park_names);
			holder.park_address=(TextView)convertView.findViewById(R.id.park_addres);
			convertView.setTag(this.holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		
		if(data==null){
			return null;
		}
		holder.park_title.setText(data.getShortName());
		holder.park_address.setText(data.getShortAddres());
		return convertView;
	}
	
	class Holder{
		TextView park_title,park_address;
	}
}
