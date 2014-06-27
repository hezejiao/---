package com.sary.arounddepot.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sary.arounddepot.MyApplication;
import com.sary.arounddepot.R;
import com.sary.arounddepot.adapter.HotShopAdapter;

public class SearchFragment extends Fragment{
	
	private MyApplication application;
	private Activity mActivity = null;
	public ListView listview;
	public HotShopAdapter searchAdapter;
	public ArrayAdapter<String> adapter_history;
	public String[] hisArrays;
	public LinearLayout clean_hostorys;
	private List<String> data=new ArrayList<String>();
	public String text="";
	public TextView my_search_txt;
	private TitlesListFragmentCallBack mTitlesListFragmentCallBack; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		// TODO Auto-generated method stub
		 mActivity = this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		 View view=inflater.inflate(R.layout.search_fragment, container, false);
		 listview=(ListView)view.findViewById(R.id.mysearch_list);
		 listview.setDivider(mActivity.getResources().getDrawable(R.drawable.line));
		 my_search_txt=(TextView)view.findViewById(R.id.my_search_txt);
		 clean_hostorys=(LinearLayout)view.findViewById(R.id.clean_hostorys);
		 SharedPreferences sp =mActivity.getSharedPreferences("history_strs", 0);
		 String save_history = sp.getString("history", "");
		 if(!"".equals(save_history)){
			 String[] hisArrays = save_history.split(",");
				for (String string : hisArrays) {
					data.add(string);
				}
			 clean_hostorys.setVisibility(View.VISIBLE);
		 }else{
			 clean_hostorys.setVisibility(View.GONE);
		 }
		 if(searchAdapter==null){
			 searchAdapter=new HotShopAdapter(mActivity, data);
		 }else{
			 searchAdapter.setHotShopList(data);
		 }
		 System.out.println("listview:"+listview+"adapter:"+searchAdapter);
		 
		 listview.setAdapter(searchAdapter);
		 listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				text=data.get(position);
//				Toast.makeText(mActivity, "点击值："+text, Toast.LENGTH_SHORT).show();
				mTitlesListFragmentCallBack.onItemSelected(text);
			}
		});
		 clean_hostorys.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 SharedPreferences sm =mActivity.getSharedPreferences("history_strs", 0);
				 String save_historys = sm.getString("history", "");
				 if(save_historys!=null){
					 SharedPreferences.Editor edit = sm.edit();
					 edit.putString("history", null).commit();
					 edit.clear();
					 edit.commit();
					 data.clear();
					 searchAdapter.setHotShopList(data);
					 clean_hostorys.setVisibility(View.GONE);
				 }
			}
		});
		return view;
	}
	
	//当该Fragment被添加,显示到Activity时调用该方法  
    //在此判断显示到的Activity是否已经实现了接口  
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof TitlesListFragmentCallBack)) {  
            throw new IllegalStateException("TitlesListFragment所在的Activity必须实现TitlesListFragmentCallBack接口");  
        }  
        mTitlesListFragmentCallBack=(TitlesListFragmentCallBack) activity;  
	}
	
	//定义一个业务接口  
    //该Fragment所在Activity需要实现该接口  
    //该Fragment将通过此接口与它所在的Activity交互  
    public interface TitlesListFragmentCallBack{  
        public void onItemSelected(String index);  
    }  

}
