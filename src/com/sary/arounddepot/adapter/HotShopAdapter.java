package com.sary.arounddepot.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sary.arounddepot.R;

public class HotShopAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private List<String> mName=new ArrayList<String>();
	
	private int mChoose = 0;
	
	public HotShopAdapter(Context context,List<String> mname) {
		mContext = context;
		mName=mname;
	}
	
	public void setHotShopList(List<String> mName){
		this.mName=mName;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mName.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mName.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void setChoose(int choose) {
		mChoose = choose;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.hot_shop_item, null);
			holder = new ViewHolder();
			holder.shop_name=(TextView)convertView.findViewById(R.id.hot_shop_id);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.shop_name.setText(mName.get(position));
		return convertView;
	}
	
class ViewHolder{
	TextView shop_name;
}

}
