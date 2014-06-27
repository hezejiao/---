package com.sary.arounddepot.resolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.MyNewsInfo;
import com.sary.arounddepot.entity.Update;
import com.sary.arounddepot.util.Constant;

public class CheckUpdateResolver implements BaseResolver{

	@Override
	public String getRequestURL() {
		// TODO Auto-generated method stub
		return Constant.CHECK_UPDATE;
	}

	@Override
	public BaseData parseData(String content) throws JSONException {
		JSONObject obj = new JSONObject(content);
		Update update=new Update();
		String code=obj.getString("code");
		if("200".equals(code)){
			JSONArray temp=obj.getJSONArray("list");
			if(temp.length()>0){
				for(int i=0;i<temp.length();i++){
					JSONObject tempObj = (JSONObject) temp.get(i);
//					update.setVersionName(tempObj.getString("version"));
					update.setForce(tempObj.getInt("force"));
					update.setUpdate(tempObj.getInt("update"));
				}
			}
		}
		return update;
	}

}
