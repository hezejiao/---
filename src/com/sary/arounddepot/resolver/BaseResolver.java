package com.sary.arounddepot.resolver;

import org.json.JSONException;

import com.sary.arounddepot.entity.BaseData;



public interface BaseResolver {
	//请求的URL
	public String getRequestURL();

	public BaseData parseData(String content) throws JSONException;
}
