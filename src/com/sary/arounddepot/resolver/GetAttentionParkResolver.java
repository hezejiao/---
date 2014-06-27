package com.sary.arounddepot.resolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.util.Constant;

public class GetAttentionParkResolver implements BaseResolver{

	@Override
	public String getRequestURL() {
		
		return Constant.GET_ATTENTION_PARK;
	}

	@Override
	public BaseData parseData(String content) throws JSONException {
		AroundParkListData data=new AroundParkListData();
		JSONObject obj = new JSONObject(content);
		data.code=obj.getString("code");
		data.totalCount=obj.getInt("totalCount");
		Log.e("tag", data.code+"====="+data.totalCount);
		if("200".equals(data.code)){
			JSONArray temp = obj.getJSONArray("list");
			if(temp.length()>0){
				for(int i=0;i<temp.length();i++){
					JSONObject tempObj = (JSONObject) temp.get(i);
					AroundParkData parkData=new AroundParkData();
					parkData.setId(tempObj.getInt("pid"));
					parkData.setShortName(tempObj.getString("shortName"));
					parkData.setName(tempObj.getString("name"));
					parkData.setAddtime(tempObj.getInt("addtime"));
					parkData.setUpdate(tempObj.getInt("update"));
					parkData.setPush(tempObj.getInt("push"));
					parkData.setStats(tempObj.getInt("stats"));
					parkData.setSet_time(tempObj.getString("set_time"));
					parkData.setSet_date(tempObj.getString("set_date"));
					data.datalist.add(parkData);
				}
			}
		}
		return data;
	}

}
