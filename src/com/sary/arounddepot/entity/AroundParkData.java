package com.sary.arounddepot.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 周围停车场实体类
 * @author Administrator
 *
 */
public class AroundParkData implements BaseData{
	
	public int id;//唯一标识

	public String shortName;
	
	public String shortAddres;
	
	public int total;
	
	public int remainNum;
	
	public int online;
	
	public int vip1;
	
	public int vip2;
	
	public int vip3;
	
	public int vip4;
	
	public int confirm;
	
	public String price;
	
	public int isFree;
	
	public double sortPrice;
	
	public int distance;
	
	public double latitude;
	
	public double longitude;
	
	public String name;
	
	public String address;
	
	public String tel;
	
	public String businessTime;
	
//	private String []imageURL;
	
	public String imageURL;
	
	public int pid;
	
	public int turn;
	
	public int wc;
	
	public int lift;
	
	public int slopeCharge;
	
	public int twoFloor;
	
	public int cmsingal;
	
	public int cusingal;
	
	public int ctsingal;
	
	public int wheel;
	
	public int chargenot;
	
//	public List<NewsInfo> newsInfo=new ArrayList<NewsInfo>();
	public String newsInfo;
	
	public int addtime;
	
	public int update;
	
	public int push;
	
	public int stats;
	
	public String set_time;
	
	public String set_date;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the shortAddres
	 */
	public String getShortAddres() {
		return shortAddres;
	}

	/**
	 * @param shortAddres the shortAddres to set
	 */
	public void setShortAddres(String shortAddres) {
		this.shortAddres = shortAddres;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the remainNum
	 */
	public int getRemainNum() {
		return remainNum;
	}

	/**
	 * @param remainNum the remainNum to set
	 */
	public void setRemainNum(int remainNum) {
		this.remainNum = remainNum;
	}

	/**
	 * @return the online
	 */
	public int getOnline() {
		return online;
	}

	/**
	 * @param online the online to set
	 */
	public void setOnline(int online) {
		this.online = online;
	}

	/**
	 * @return the vip1
	 */
	public int getVip1() {
		return vip1;
	}

	/**
	 * @param vip1 the vip1 to set
	 */
	public void setVip1(int vip1) {
		this.vip1 = vip1;
	}

	/**
	 * @return the vip2
	 */
	public int getVip2() {
		return vip2;
	}

	/**
	 * @param vip2 the vip2 to set
	 */
	public void setVip2(int vip2) {
		this.vip2 = vip2;
	}

	/**
	 * @return the vip3
	 */
	public int getVip3() {
		return vip3;
	}

	/**
	 * @param vip3 the vip3 to set
	 */
	public void setVip3(int vip3) {
		this.vip3 = vip3;
	}

	/**
	 * @return the vip4
	 */
	public int getVip4() {
		return vip4;
	}

	/**
	 * @param vip4 the vip4 to set
	 */
	public void setVip4(int vip4) {
		this.vip4 = vip4;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the isFree
	 */
	public int getIsFree() {
		return isFree;
	}

	/**
	 * @param isFree the isFree to set
	 */
	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}

	/**
	 * @return the sortPrice
	 */
	public double getSortPrice() {
		return sortPrice;
	}

	/**
	 * @param sortPrice the sortPrice to set
	 */
	public void setSortPrice(double sortPrice) {
		this.sortPrice = sortPrice;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * @return the confirm
	 */
	public int getConfirm() {
		return confirm;
	}

	/**
	 * @param confirm the confirm to set
	 */
	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBusinessTime() {
		return businessTime;
	}

	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getWc() {
		return wc;
	}

	public void setWc(int wc) {
		this.wc = wc;
	}

	public int getLift() {
		return lift;
	}

	public void setLift(int lift) {
		this.lift = lift;
	}

	public int getSlopeCharge() {
		return slopeCharge;
	}

	public void setSlopeCharge(int slopeCharge) {
		this.slopeCharge = slopeCharge;
	}

	public int getTwoFloor() {
		return twoFloor;
	}

	public void setTwoFloor(int twoFloor) {
		this.twoFloor = twoFloor;
	}

	public int getCmsingal() {
		return cmsingal;
	}

	public void setCmsingal(int cmsingal) {
		this.cmsingal = cmsingal;
	}

	public int getCusingal() {
		return cusingal;
	}

	public void setCusingal(int cusingal) {
		this.cusingal = cusingal;
	}

	public int getCtsingal() {
		return ctsingal;
	}

	public void setCtsingal(int ctsingal) {
		this.ctsingal = ctsingal;
	}

	public int getWheel() {
		return wheel;
	}

	public void setWheel(int wheel) {
		this.wheel = wheel;
	}

	public int getChargenot() {
		return chargenot;
	}

	public void setChargenot(int chargenot) {
		this.chargenot = chargenot;
	}

//	public List<NewsInfo> getNewsInfo() {
//		return newsInfo;
//	}
//
//	public void setNewsInfo(List<NewsInfo> newsInfo) {
//		this.newsInfo = newsInfo;
//	}
	
	public int getAddtime() {
		return addtime;
	}

	public String getNewsInfo() {
		return newsInfo;
	}

	public void setNewsInfo(String newsInfo) {
		this.newsInfo = newsInfo;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}

	public int getPush() {
		return push;
	}

	public void setPush(int push) {
		this.push = push;
	}

	public int getStats() {
		return stats;
	}

	public void setStats(int stats) {
		this.stats = stats;
	}

	public String getSet_time() {
		return set_time;
	}

	public void setSet_time(String set_time) {
		this.set_time = set_time;
	}

	public String getSet_date() {
		return set_date;
	}

	public void setSet_date(String set_date) {
		this.set_date = set_date;
	}
	
	
}
