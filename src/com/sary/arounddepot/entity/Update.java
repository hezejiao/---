package com.sary.arounddepot.entity;

import java.io.Serializable;

/**
 * 应用程序更新实体类
 * @version 1.0
 */
public class Update implements BaseData{
	
	private int versionCode;
	private String versionName;//有更新时，新的版本号
	private String downloadUrl;
	private String updateLog;
	private int force;//是否强制更新
	private int update;//是否有更新
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	public int getForce() {
		return force;
	}
	public void setForce(int force) {
		this.force = force;
	}
	public int getUpdate() {
		return update;
	}
	public void setUpdate(int update) {
		this.update = update;
	}
}
