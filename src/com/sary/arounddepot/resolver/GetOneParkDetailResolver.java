package com.sary.arounddepot.resolver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sary.arounddepot.entity.AroundParkData;
import com.sary.arounddepot.entity.AroundParkListData;
import com.sary.arounddepot.entity.BaseData;
import com.sary.arounddepot.entity.NewsInfo;
import com.sary.arounddepot.util.Constant;

public class GetOneParkDetailResolver implements BaseResolver{

	@Override
	public String getRequestURL() {
		// TODO Auto-generated method stub
		return Constant.GETPARK_DATAIL;
	}

	@Override
	public BaseData parseData(String content) throws JSONException {
		AroundParkListData data=new AroundParkListData();
//		List<NewsInfo> lists=null;
		System.out.println("heheheeheh");
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
					parkData.setId(tempObj.getInt("id"));
					parkData.setShortName(tempObj.getString("shortName"));
					parkData.setName(tempObj.getString("name"));
					parkData.setAddress(tempObj.getString("address"));
					parkData.setShortAddres(tempObj.getString("shortAddress"));
					parkData.setTotal(tempObj.getInt("total"));
					parkData.setRemainNum(tempObj.getInt("remainNum"));
					parkData.setOnline(tempObj.getInt("online"));
					parkData.setVip1(tempObj.getInt("vip1"));
					parkData.setVip2(tempObj.getInt("vip2"));
					parkData.setVip3(tempObj.getInt("vip3"));
					parkData.setVip4(tempObj.getInt("vip4"));
					parkData.setPrice(tempObj.getString("price"));
//					parkData.setIsFree(tempObj.getInt("isFree"));
//					parkData.setSortPrice(tempObj.getDouble("sortPrice"));
					parkData.setDistance(tempObj.getInt("distance"));
					parkData.setConfirm(tempObj.getInt("confirm"));
					parkData.setLatitude(tempObj.getDouble("lat"));
					parkData.setLongitude(tempObj.getDouble("lng"));
					parkData.setTel(tempObj.getString("tel"));
					parkData.setBusinessTime(tempObj.getString("BusinessTime"));
				    JSONArray array = tempObj.getJSONArray("img_list");
					if(array.length()>0){
						StringBuilder builder=new StringBuilder();
//						String[] imageURL = new String[array.length()];
						for (int j= 0; j< array.length(); j++) {
							String url = array.optString(j);
							builder.append(url);
							builder.append("_");
//							imageURL[j] = url;
						}
						parkData.setImageURL(builder.toString());
					}
					if(tempObj.has("mark_list")){
						JSONArray arrs = tempObj.getJSONArray("mark_list");
						System.out.println("hhhhhhh"+arrs.length());
						if(arrs.length()>0){
							for (int m = 0; m < arrs.length(); m++) {
								JSONObject ms = (JSONObject) arrs.get(m);
								parkData.setPid(ms.getInt("id"));
								parkData.setTurn(ms.getInt("turn"));
								parkData.setWc(ms.getInt("wc"));
								parkData.setLift(ms.getInt("lift"));
								parkData.setSlopeCharge(ms.getInt("slopeCharge"));
								parkData.setTwoFloor(ms.getInt("twoFloor"));
								parkData.setCmsingal(ms.getInt("CmSingal"));
								parkData.setCusingal(ms.getInt("CuSingal"));
								parkData.setCtsingal(ms.getInt("CtSingal"));
		                        parkData.setWheel(ms.getInt("wheelChannel"));
		                        parkData.setChargenot(ms.getInt("ChargeNotExit"));
							}
						}
					}
					Log.e("tag:","xxxxxxx"+parkData.getChargenot());
					JSONArray mp = tempObj.getJSONArray("news_list");
					if(mp.length()>0){
//						lists=new ArrayList<NewsInfo>();
//						for (int n = 0; n < mp.length(); n++) {
//							NewsInfo info=new NewsInfo();
//							JSONObject ps = (JSONObject) mp.get(n);
//							info.setId(ps.getInt("id"));
//							info.setTitle(ps.getString("title"));
//							info.setType(ps.getInt("type"));
//							info.setUrl(ps.getString("url"));
//							lists.add(info);
//						}
//						parkData.setNewsInfo(lists);
						parkData.setNewsInfo(mp.toString());
					}
					data.datalist.add(parkData);
				}
			}
			
		}
		Log.e("tag","data:"+data.datalist.size());
		System.out.println("data:"+data.datalist.size());
		return data;
	}

}
