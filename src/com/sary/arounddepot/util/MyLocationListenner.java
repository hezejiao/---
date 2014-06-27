package com.sary.arounddepot.util;

import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.sary.arounddepot.MyApplication;

public class MyLocationListenner implements AMapLocationListener{
	
	private MyApplication application=MyApplication._instance;

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation location){
	    if(location==null){
	    	return ;
	    }
	    application.setLatitude(location.getLatitude());
	    application.setLongitude(location.getLongitude());
	}

}
