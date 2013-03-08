package com.zerodo;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

public class HealthyApplication extends Application {
	private List<Activity> activityList=new LinkedList<Activity>();
	private boolean isVIP=false;
	//全局变量 存储放大的附件图片
	private Bitmap bigBitmap=null; 
	
	private static HealthyApplication instance;
	
	private HealthyApplication(){}
	
	public static HealthyApplication getInstance(){
		if(null==instance){
			instance=new HealthyApplication();
		}
		return instance;
	}
	
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	public void exit(){
		for(Activity activity:activityList){
			activity.finish();
		}
		System.exit(0);
	}

	public boolean isVIP() {
		return isVIP;
	}

	public void setVIP(boolean isVIP) {
		this.isVIP = isVIP;
	}

	public Bitmap getBigBitmap() {
		return bigBitmap;
	}

	public void setBigBitmap(Bitmap bigBitmap) {
		this.bigBitmap = bigBitmap;
	}
	
}
