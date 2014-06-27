package com.sary.arounddepot.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sary.arounddepot.R;
import com.sary.arounddepot.adapter.HotShopAdapter;
import com.sary.arounddepot.entity.AroundParkData;

public class HotShopFragment extends Fragment{
	public ListView hotList;
	public BaseAdapter hotAdapter;
	private Activity mActivity = null;
	public TextView my_search_txt;
	private String []mName={"徐家汇商圈","五角场商圈","静安寺商圈","新天地商圈","中山公园龙之梦商圈"
			,"虹口龙之梦商圈","长寿路亚新商圈","八佰伴商圈","陆家嘴商圈"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActivity = this.getActivity();
		View view = inflater.inflate(R.layout.hotshop_fragment, container, false);
		my_search_txt=(TextView)view.findViewById(R.id.my_search_txt);
		hotList = (ListView)view.findViewById(R.id.hot_list);
		hotList.setDivider(mActivity.getResources().getDrawable(R.drawable.line));
		List<String> data = new ArrayList<String>();
		for (String string : mName){
			data.add(string);
		}
		hotAdapter = new HotShopAdapter(this.getActivity(), data);
		hotList.setAdapter(hotAdapter);
		hotList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Log.e("tag", "xxxxxxx");
				switch(position){
				case 0:
					AroundParkData data=new AroundParkData();
					data.setShortAddres("徐家汇商圈");
					data.setLatitude(31.194880);
					data.setLongitude(121.437721);
					Intent intent=new Intent();
					Bundle b=new Bundle();
					b.putSerializable("aroundpark", data);
					intent.putExtras(b);
					getActivity().setResult(17, intent);
					getActivity().finish();
					break;
				case 1:
					AroundParkData data2=new AroundParkData();
					data2.setShortAddres("五角场商圈");
					data2.setLatitude(31.300326);
					data2.setLongitude(121.513901);
					Intent intent2=new Intent();
					Bundle b2=new Bundle();
					b2.putSerializable("aroundpark", data2);
					intent2.putExtras(b2);
					getActivity().setResult(17, intent2);
					getActivity().finish();
					break;
                case 2:
                	AroundParkData data3=new AroundParkData();
					data3.setShortAddres("静安寺商圈");
					data3.setLatitude(31.224049);
					data3.setLongitude(121.446054);
					Intent intent3=new Intent();
					Bundle b3=new Bundle();
					b3.putSerializable("aroundpark", data3);
					intent3.putExtras(b3);
					getActivity().setResult(17, intent3);
					getActivity().finish();
					break;
				case 3:
					AroundParkData data4=new AroundParkData();
					data4.setShortAddres("新天地商圈");
					data4.setLatitude(31.219646);
					data4.setLongitude(121.475187);
					Intent intent4=new Intent();
					Bundle b4=new Bundle();
					b4.putSerializable("aroundpark", data4);
					intent4.putExtras(b4);
					getActivity().setResult(17, intent4);
					getActivity().finish();
					break;
				case 4:
					AroundParkData data5=new AroundParkData();
					data5.setShortAddres("中山公园龙之梦商圈");
					data5.setLatitude(31.219047);
					data5.setLongitude(121.416596);
					Intent intent5=new Intent();
					Bundle b5=new Bundle();
					b5.putSerializable("aroundpark", data5);
					intent5.putExtras(b5);
					getActivity().setResult(17, intent5);
					getActivity().finish();
					break;
				case 5:
					AroundParkData data6=new AroundParkData();
					data6.setShortAddres("虹口龙之梦商圈");
					data6.setLatitude(31.270595);
					data6.setLongitude(121.478497);
					Intent intent6=new Intent();
					Bundle b6=new Bundle();
					b6.putSerializable("aroundpark", data6);
					intent6.putExtras(b6);
					getActivity().setResult(17, intent6);
					getActivity().finish();
					break;
				case 6:
					AroundParkData data7=new AroundParkData();
					data7.setShortAddres("常寿路亚新商圈");
					data7.setLatitude(31.239271);
					data7.setLongitude(121.437259);
					Intent intent7=new Intent();
					Bundle b7=new Bundle();
					b7.putSerializable("aroundpark", data7);
					intent7.putExtras(b7);
					getActivity().setResult(17, intent7);
					getActivity().finish();
					break;
				case 7:
					AroundParkData data8=new AroundParkData();
					data8.setShortAddres("八佰伴商圈");
					data8.setLatitude(31.228317);
					data8.setLongitude(121.516918);
					Intent intent8=new Intent();
					Bundle b8=new Bundle();
					b8.putSerializable("aroundpark", data8);
					intent8.putExtras(b8);
					getActivity().setResult(17, intent8);
					getActivity().finish();
					break;
				case 8:
					AroundParkData data9=new AroundParkData();
					data9.setShortAddres("陆家嘴商圈");
					data9.setLatitude(31.234689);
					data9.setLongitude(121.507895);
					Intent intent9=new Intent();
					Bundle b9=new Bundle();
					b9.putSerializable("aroundpark", data9);
					intent9.putExtras(b9);
					getActivity().setResult(17, intent9);
					getActivity().finish();
					break;
				}
			}
		});
		return view;
		

	}

}
