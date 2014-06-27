package com.sary.arounddepot.resolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.Result;
import com.sary.arounddepot.entity.UserInfo;
import com.sary.arounddepot.util.Constant;

public class RegistResolver implements BaseResolver{

	@Override
	public String getRequestURL() {
		// TODO Auto-generated method stub
		return Constant.USER_REGIST;
	}

	@Override
	public BaseData parseData(String content) throws JSONException {
		UserInfo info=new UserInfo();
		JSONObject obj = new JSONObject(content);
		Result result=new Result();
		result.setCode(obj.getString("code"));
		info.setResult(result);
		if("200".equals(obj.getString("code"))){
			JSONArray temp = obj.getJSONArray("list");
			for(int i=0;i<temp.length();i++){
				JSONObject tempObj = (JSONObject) temp.get(i);
				info.setUsername(tempObj.getString("userName"));
				info.setNickname(tempObj.getString("nickName"));
				info.setEmail(tempObj.getString("email"));
				info.setPhone(tempObj.getString("phone"));
				info.setSex(tempObj.getInt("sex"));
				info.setId(tempObj.getString("id"));
			}
		}
		return info;
	}

}
