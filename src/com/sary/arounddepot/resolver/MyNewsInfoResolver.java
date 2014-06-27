package com.sary.arounddepot.resolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.MyNewsInfo;
import com.sary.arounddepot.entity.MyNewsListData;
import com.sary.arounddepot.util.Constant;

public class MyNewsInfoResolver implements BaseResolver{

	@Override
	public String getRequestURL() {
		// TODO Auto-generated method stub
		return Constant.SYS_NEWS;
	}

	@Override
	public BaseData parseData(String content) throws JSONException{
		MyNewsListData data=new MyNewsListData();
		JSONObject obj = new JSONObject(content);
		data.code=obj.getString("code");
		if("200".equals(data.code)){
			JSONArray temp = obj.getJSONArray("list");
			if(temp.length()>0){
				for(int i=0;i<temp.length();i++){
					JSONObject tempObj = (JSONObject) temp.get(i);
					MyNewsInfo info=new MyNewsInfo();
					info.setId(tempObj.getInt("id"));
					info.setMsg(tempObj.getString("msg"));
					info.setContent(tempObj.getString("content"));
					info.setUrl(tempObj.getString("url"));
					info.setAddtime(tempObj.getInt("addtime"));
					info.setTime(tempObj.getString("time"));
					data.datalist.add(info);
				}
			}
		}
		return data;
	}

}
