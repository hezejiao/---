package com.sary.arounddepot.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sary.arounddepot.R;
import com.sary.arounddepot.entity.AroundParkData;


public class AroundParkAdapter extends BaseAdapter{
	
	private List<AroundParkData> list;
	
	private Context context;
	
	private Holder holder;
	
	
	public AroundParkAdapter(Context context){
		
		this.context=context;
	}
	
	public void setAroundParkList(List<AroundParkData> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.e("tag", "==="+list.size());
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
					.inflate(R.layout.activity_park_list_item, null);
			holder.park_title=(TextView)convertView.findViewById(R.id.park_title);
			holder.park_distance=(TextView)convertView.findViewById(R.id.park_distance);
			holder.park_price=(TextView)convertView.findViewById(R.id.park_price);
			holder.park_address=(TextView)convertView.findViewById(R.id.park_address);
			holder.park_num_one=(ImageView)convertView.findViewById(R.id.park_num_one);
			holder.park_num_two=(ImageView)convertView.findViewById(R.id.park_num_two);
			holder.car_userone=(ImageView)convertView.findViewById(R.id.car_userone);
			holder.car_usertwo=(ImageView)convertView.findViewById(R.id.car_usertwo);
			holder.car_userthree=(ImageView)convertView.findViewById(R.id.car_userthree);
			holder.youhui=(ImageView)convertView.findViewById(R.id.youhui);
			holder.vs=(ImageView)convertView.findViewById(R.id.vs);
			convertView.setTag(this.holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		
		if(data==null){
			return null;
		}
		holder.park_title.setText(data.getShortName());
		holder.park_distance.setText(data.getDistance()+"m");
		holder.park_price.setText(data.getPrice());
		holder.park_address.setText(data.getShortAddres());
		if(data.getConfirm()==1){
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.v);
			holder.vs.setVisibility(View.VISIBLE);
			holder.vs.setImageBitmap(bm);
		}else{
			holder.vs.setVisibility(View.INVISIBLE);
		}
		if(data.getVip1()==0){
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.car_notuser_one);
			holder.car_userone.setImageBitmap(bm);
		}else{
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.caruser_one);
			holder.car_userone.setImageBitmap(bm);
		}
		if(data.getVip2()==0){
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.car_notuser_two);
			holder.car_usertwo.setImageBitmap(bm);
		}else{
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.caruser_two);
			holder.car_usertwo.setImageBitmap(bm);
		}
		if(data.getVip3()==0){
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.car_notuser_three);
			holder.car_userthree.setImageBitmap(bm);
		}else{
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.caruser_three);
			holder.car_userthree.setImageBitmap(bm);
		}
		if(data.getVip4()==0){
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.have_no_youhui);
			holder.youhui.setImageBitmap(bm);
		}else{
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.have_youhui);
			holder.youhui.setImageBitmap(bm);
		}
		int online=data.getOnline();
		int total=data.getTotal();
		int remain=data.getRemainNum();
		if(online==0){
			Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.gray_zero);
			holder.park_num_one.setImageBitmap(bm);
			holder.park_num_two.setImageBitmap(bm);
		}else{
			if(remain==0){
				Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.red_zero);
				holder.park_num_one.setImageBitmap(bm);
				holder.park_num_two.setImageBitmap(bm);
			}if(remain>0 && remain<10){
				if((remain/(double)total)<0.1){
					holder.park_num_one.setImageBitmap(BitmapFactory.
							decodeResource(context.getResources(), R.drawable.red_zero));
					if(remain==1){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_one));
					}else if(remain==2){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_two));
					}else if(remain==3){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_three));
					}else if(remain==4){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_four));
					}else if(remain==5){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_five));
					}else if(remain==6){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_six));
					}else if(remain==7){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_sev));
					}else if(remain==8){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_eight));
					}else{
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_nine));
					}
				}else{
					holder.park_num_one.setImageBitmap(BitmapFactory.
							decodeResource(context.getResources(), R.drawable.green_zero));
					if(remain==1){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_one));
					}else if(remain==2){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_two));
					}else if(remain==3){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_three));
					}else if(remain==4){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_four));
					}else if(remain==5){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_five));
					}else if(remain==6){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_six));
					}else if(remain==7){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_sev));
					}else if(remain==8){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_eight));
					}else{
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_nine));
					}
				}
			}
			if(remain>=10 && remain<100){
				int m=remain/10;
				int n=remain%10;
				if((remain/(double)total)>0.1){
					if(m==1){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_one));
					}if(m==2){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_two));
					}if(m==3){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_three));
					}if(m==4){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_four));
					}if(m==5){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_five));
					}if(m==6){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_six));
					}if(m==7){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_sev));
					}if(m==8){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_eight));
					}if(m==9){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_nine));
					}if(n==0){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_zero));
					}if(n==1){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_one));
					}if(n==2){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_two));
					}
					if(n==3){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_three));
					}if(n==4){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_four));
					}if(n==5){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_five));
					}if(n==6){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_six));
					}if(n==7){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_sev));
					}if(n==8){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_eight));
					}if(n==9){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.green_nine));
					}
				}else{
					if(m==1){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_one));
					}if(m==2){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_two));
					}if(m==3){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_three));
					}if(m==4){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_four));
					}if(m==5){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_five));
					}if(m==6){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_six));
					}if(m==7){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_sev));
					}if(m==8){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_eight));
					}if(m==9){
						holder.park_num_one.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_nine));
					}if(n==1){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_one));
					}if(n==2){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_two));
					}
					if(n==3){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_three));
					}if(n==4){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_four));
					}if(n==5){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_five));
					}if(n==6){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_six));
					}if(n==7){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_sev));
					}if(n==8){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_eight));
					}if(n==9){
						holder.park_num_two.setImageBitmap(BitmapFactory.
								decodeResource(context.getResources(), R.drawable.red_nine));
					}
				}
				
			}if(remain>=100){
				Bitmap bm =BitmapFactory.decodeResource(context.getResources(), R.drawable.green_nine);
				holder.park_num_one.setImageBitmap(bm);
				holder.park_num_two.setImageBitmap(bm);
			}
		}
		return convertView;
	}
	class Holder{
		TextView park_title,park_distance,park_price,park_address;
		ImageView park_num_one,park_num_two,car_userone,car_usertwo,
		car_userthree,youhui,vs;
	}
	

}
