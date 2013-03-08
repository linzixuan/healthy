package com.zerodo.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;

public class AboutActivity extends CommonActivity implements OnTouchListener {

	private Button aboutBack;
	private TextView softVersion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_about);
		initUI();
		initInfo(this);
	}
	
	private void initUI(){
		aboutBack=(Button) findViewById(R.id.aboutBack);
		aboutBack.setOnTouchListener(this);
		softVersion=(TextView) findViewById(R.id.softVersion);
		softVersion.setText(softVersion.getText().toString()+"V"+getVersionName());
	}
	
	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionName() {
		String version="";
		try{
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
					0);
			version= packInfo.versionName;			
		}catch(Exception e){
			e.printStackTrace();
		}
		return version;
	}
	
	public void initInfo(Context context){
		TextView telView=(TextView)this.findViewById(R.id.aboutTel);
		String htmlTelText = getString(R.string.about_tel);
		telView.setText(Html.fromHtml(htmlTelText));
		telView.setClickable(true);
		telView.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	TextView view=(TextView)v;
		    	String phoneNum=view.getText().toString();
		        String []phoneNumArray=phoneNum.split("-");
		        phoneNum=phoneNumArray[0]+phoneNumArray[1];
		        dialog(phoneNum);
		    }
		});
		
		TextView urlView=(TextView)this.findViewById(R.id.aboutUrl);
		String htmlUrlText = getString(R.string.about_url);
		urlView.setText(Html.fromHtml(htmlUrlText));
		urlView.setClickable(true);
		urlView.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	TextView view=(TextView)v;
		    	String url=view.getText().toString();
		    	url=url.substring(url.lastIndexOf(":"),url.length());
				Intent it = new Intent(Intent.ACTION_VIEW,
				Uri.parse(url));
				it .setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				startActivity(it );

		    }
		});
	}
	

	private void dialog(final String phoneNum) { 
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("你确定拨打:"+phoneNum); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					Intent dialIntent =new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNum));
					dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(dialIntent);
			 	} 
		 }); 
		 builder.setNegativeButton("取消", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 	} 
		 }); 
		 builder.create().show(); 
	 } 

	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.aboutBack:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				aboutBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				aboutBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				this.finish();
				break;
			}
			break;
		}
		return true;
	}
}
