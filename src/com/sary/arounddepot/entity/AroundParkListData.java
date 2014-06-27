package com.sary.arounddepot.entity;

import java.util.ArrayList;
import java.util.List;

public class AroundParkListData implements BaseData{
	
	public List<AroundParkData> datalist;
	
	public String code;
	
	public int totalCount;
	
	public AroundParkListData(){
		datalist=new ArrayList<AroundParkData>();
	}

}
