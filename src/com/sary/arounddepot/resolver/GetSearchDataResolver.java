package com.sary.arounddepot.resolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.util.Constant;



public class GetSearchDataResolver implements BaseResolver{

	@Override
	public String getRequestURL() {
		// TODO Auto-generated method stub
		return Constant.GET_SEARCH_DATA;
	}

	@Override
	public BaseData parseData(String content) throws JSONException {
		AroundParkListData data=new AroundParkListData();
		JSONObject obj = new JSONObject(content);
		data.code=obj.getString("code");
		data.totalCount=obj.getInt("totalCount");
		if("200".equals(data.code)){
			JSONArray temp = obj.getJSONArray("list");
			for(int i=0;i<temp.length();i++){
				JSONObject tempObj = (JSONObject) temp.get(i);
				AroundParkData parkData=new AroundParkData();
				parkData.setShortName(tempObj.getString("shortName"));
				parkData.setShortAddres(tempObj.getString("shortAddress"));
				parkData.setLatitude(tempObj.getDouble("lat"));
				parkData.setLongitude(tempObj.getDouble("lng"));
				data.datalist.add(parkData);
			}
		}
		return data;
	}

}
