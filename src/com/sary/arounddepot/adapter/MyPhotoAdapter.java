package com.sary.arounddepot.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sary.arounddepot.R;
import com.sary.arounddepot.util.BitMapUtil;

public class MyPhotoAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private List<String> mImagePaths = null;
	
	private Holder holder;
	
	public MyPhotoAdapter(Context context,List<String> imagePaths){
		mContext = context;
		this.mImagePaths = imagePaths;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImagePaths.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mImagePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(mContext)
					.inflate(R.layout.activity_park_list_item, null);
			holder.abum=(ImageView) convertView.findViewById(R.id.abum);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		String imagePath= mImagePaths.get(position);
		Bitmap mBitmap =BitMapUtil.originalImg(new File(imagePath));
		if(mBitmap!=null){
			holder.abum.setImageBitmap(mBitmap);
		}
		return convertView;
	}
	

	/**
	 * 增加并改变视图.
	 * @param position the position
	 * @param imagePaths the image paths
	 */
	public void addItem(int position,String imagePaths) {
		mImagePaths.add(position,imagePaths);
		notifyDataSetChanged();
	}
	
	/**
	 * 增加多条并改变视图.
	 * @param imagePaths the image paths
	 */
	public void addItems(List<String> imagePaths) {
		mImagePaths.addAll(imagePaths);
		notifyDataSetChanged();
	}
	
	/**
	 * 增加多条并改变视图.
	 */
	public void clearItems() {
		mImagePaths.clear();
		notifyDataSetChanged();
	}
	
	class Holder{
		ImageView abum;
	}

}
