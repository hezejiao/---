package com.sary.arounddepot.entity;

import java.util.ArrayList;
import java.util.List;

public class MyNewsListData implements BaseData{
	
	public List<MyNewsInfo> datalist;
	
	public String code;
	
	public MyNewsListData(){
		datalist=new ArrayList<MyNewsInfo>();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
