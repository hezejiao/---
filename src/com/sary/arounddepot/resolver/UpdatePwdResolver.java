package com.sary.arounddepot.resolver;

import org.json.JSONException;
import org.json.JSONObject;

import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.Result;
import com.sary.arounddepot.util.Constant;

public class UpdatePwdResolver implements BaseResolver{

	@Override
	public String getRequestURL() {
		// TODO Auto-generated method stub
		return Constant.UPDATE_PWD;
	}

	@Override
	public BaseData parseData(String content) throws JSONException {
		Result result=new Result();
		JSONObject obj = new JSONObject(content);
		String code=obj.getString("code");
		if("200".equals(code)){
			result.setCode(code);
		}
		return result;
	}

}
