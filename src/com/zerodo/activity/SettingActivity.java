package com.zerodo.activity;

import java.io.File;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.ConfigUtil;
import com.zerodo.base.util.RSAUtils;
import com.zerodo.base.util.StringUtil;
import com.zerodo.update.DownLoadManager;


public class SettingActivity extends TabActivity implements OnTouchListener {
	private final String TAG = this.getClass().getName();
	private static String wsurl = "http://zerodo.cn/LicServ/LicServIntf.asmx";
	private String NameSpace = "http://tempuri.org/";
	private String MethodName = "CheckUpdate";
	private String soapAction = NameSpace + MethodName;
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	private JSONObject versionObject;
	private String localVersion;
	public static String sPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNDySvNmCaiyJTTQelrUylzNHzlX9VFGcU1lX0DP38dV6B7H/nrd6oqMpqlCyEs/kCn+6b1fHMXxp/GWmPVQq07qbUEE0vnORA/rfg9ljYjrK1/BQ42VKTaNWYXmVNd94zRkZ/HnkIENwgV6XxrW+Dn8bAALS/vN7pQEkVMe+CMwIDAQAB";

	
	//返回按钮
	private Button btnBack;
	//我的资料
	private LinearLayout layoutUserInfo;
	//自定义设置
	private LinearLayout layoutCustom;
	//数据安全
	private LinearLayout layoutData;
	//注册
	private LinearLayout layoutRegist;
	//软件升级
	private LinearLayout layoutUpdate;
	//关于
	private LinearLayout layoutAbout;
	
	//手机信息
//	private LinearLayout layoutPhoneInfo;
    private ProgressBar pg;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_setting);
		initUI();
	}
	private void initUI(){
		layoutUserInfo=(LinearLayout) findViewById(R.id.layoutUserInfo);
		layoutCustom=(LinearLayout) findViewById(R.id.layoutCustom);
		layoutData=(LinearLayout) findViewById(R.id.layoutData);
		layoutRegist=(LinearLayout) findViewById(R.id.layoutRegist);
		layoutUpdate=(LinearLayout) findViewById(R.id.layoutUpdate);
		layoutAbout=(LinearLayout) findViewById(R.id.layoutAbout);
//		layoutPhoneInfo=(LinearLayout) findViewById(R.id.layoutPhoneInfo);
		layoutUserInfo.setOnClickListener(new MyOnClickListener());
		layoutCustom.setOnClickListener(new MyOnClickListener());
		layoutData.setOnClickListener(new MyOnClickListener());
		layoutRegist.setOnClickListener(new MyOnClickListener());
		layoutUpdate.setOnClickListener(new MyOnClickListener());
		layoutAbout.setOnClickListener(new MyOnClickListener());
//		layoutPhoneInfo.setOnClickListener(new MyOnClickListener());
		pg = (ProgressBar)findViewById(R.id.pg);
	}
	
	class MyOnClickListener implements android.view.View.OnClickListener{
		public void onClick(View view) {
			Intent it;
			switch (view.getId()) {
			case R.id.layoutUserInfo:
				it =new Intent(SettingActivity.this, UserInfoActivity.class);
				startActivity(it);
				break;
			case R.id.layoutCustom:
				it=new Intent(SettingActivity.this, BaseDataActivity.class);
				startActivity(it);
				break;
			case R.id.layoutData:
				it=new Intent(SettingActivity.this, DataSafeActivity.class);
				startActivity(it);
				break;
			case R.id.layoutRegist:
				if(HealthyApplication.getInstance().isVIP())
					it=new Intent(SettingActivity.this, RegistedActivity.class);
				else	
					it=new Intent(SettingActivity.this, RegistActivity.class);
				startActivity(it);
				break;
//			case R.id.layoutPhoneInfo:
//				it=new Intent(SettingActivity.this, PhoneInfoActivity.class);
//				startActivity(it);
//				break;
			case R.id.layoutUpdate:
	 			Log.i(TAG,"开始检测版本");
	 			try {
	 				 pg.setVisibility(View.VISIBLE);
					checkVersion();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.layoutAbout:
				it=new Intent(SettingActivity.this, AboutActivity.class);
				startActivity(it);
				break;
			}	
			
		}
	}
	
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
//		case R.id.settingBack:
//			switch (event.getAction()){
//			//按下，重新设置“返回”的背景图片
//			case MotionEvent.ACTION_DOWN:
//				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
//				break;
//			//松开，重新设置“返回”的背景图片
//			case MotionEvent.ACTION_UP:
//				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
//				//点击返回时关闭本页面
//				Intent intent = new Intent(this, MainActivity.class);
//				startActivity(intent);
//				this.finish();
//				break;
//			}
//			break;
		}
		return true;
	}
    private void checkVersion() throws Exception{
		localVersion = getVersionName();
		CheckVersionTask cv = new CheckVersionTask();
		new Thread(cv).start();
    }
	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}
	
	/*
	 * 从服务器获取xml解析并进行比对版本号 
	 */
	public class CheckVersionTask implements Runnable{
		public void run() {
			try {
				versionObject=getRemoteVerson();
				if(versionObject.get("result").toString().equals("false")){
					Log.i(TAG,"版本号相同无需升级");
					//Toast.makeText(getApplicationContext(), "版本号相同无需升级", 1).show();
					Message msg=new Message();
					msg.what=UPDATA_NONEED;
					handler.sendMessage(msg);
				}else{
					Log.i(TAG,"版本号不同 ,提示用户升级 ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// 待处理 
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} 
		}
	}
	
	private JSONObject getRemoteVerson() throws Exception{
		String macAddress = null, iMei = null;
		WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			macAddress = info.getMacAddress().replaceAll(":", "");
		}
		iMei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		String sAccid=ConfigUtil.getConfig(this, ConfigUtil.KEY_SACCID);
		String sVerInfo=null;
		JSONObject jsonObject=new JSONObject();
		String infoDone=ConfigUtil.getConfig(SettingActivity.this, ConfigUtil.KEY_INFODONE);
		if(StringUtil.isNotNull(infoDone)&&"true".equals(infoDone)){
			jsonObject.put("ver", localVersion);
		}else{
			jsonObject.put("ver", localVersion);
			jsonObject.put("imei", iMei);
			jsonObject.put("mac", macAddress);
			jsonObject.put("sys", "0");
		}
		RSAUtils.Cipher_ALGORITHM = RSAUtils.PKCS1Padding;
		sVerInfo = Base64.encodeToString(RSAUtils.encryptByPublicKey(
				jsonObject.toString().getBytes(), sPubkey), Base64.DEFAULT);
		return versionJSONObjecct(wsurl,sAccid, sVerInfo);
	}
	private JSONObject versionJSONObjecct(String url,String sAccid,String sVerInfo) throws JSONException {
		JSONObject rtnObject = new JSONObject();
		try {
			SoapObject request = new SoapObject(NameSpace, MethodName);
			request.addProperty("sAccid", sAccid);
			request.addProperty("sVerInfo", sVerInfo);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.bodyOut = request;
			HttpTransportSE ht = new HttpTransportSE(wsurl);
			ht.call(soapAction, envelope);
			if (envelope.getResponse() != null) {
				Vector response = (Vector) envelope.getResponse();
				rtnObject.put("result", response.get(0).toString());
				rtnObject.put("sNewVer", response.get(1).toString());
				rtnObject.put("sNewVerInfo", response.get(2).toString());
				rtnObject.put("sNewVerAddr", response.get(3).toString());
				rtnObject.put("sAppInfo", response.get(4).toString());
			}
		} catch (Exception e) {
			Log.i(TAG,"网络不可用，版本校验失败 ");
			return null;
		}

		return rtnObject;
	}
	
	Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pg.setVisibility(View.GONE);
			switch (msg.what) {
			case UPDATA_NONEED:
				Toast.makeText(getApplicationContext(), "您的健康簿已经是最新版本，感谢您的使用！", 1).show();
				break;
			case UPDATA_CLIENT:
				//对话框通知用户升级程序 
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//服务器超时 
				Toast.makeText(getApplicationContext(), "获取版本信息失败，请您检查网络或者下次再试。", 1).show();
				//LoginMain();
				break;	
			case DOWN_ERROR:
				//下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败，请您登录www.zerodo.cn下载最新版本。", 1).show();
				//LoginMain();
				break;	
			}
		}
	};
	
	/*
	 * 
	 * 弹出对话框通知用户更新程序 
	 * 
	 * 弹出对话框的步骤：
	 * 	1.创建alertDialog的builder.  
	 *	2.要给builder设置属性, 对话框的内容,样式,按钮
	 *	3.通过builder 创建一个对话框
	 *	4.对话框show()出来  
	 */
	protected void showUpdataDialog() {
		try{
			AlertDialog.Builder builer = new Builder(this) ; 
			builer.setTitle("版本升级");
			builer.setMessage(versionObject.getString("sNewVerInfo").toString());
			//当点确定按钮时从服务器上下载 新的apk 然后安装 
			builer.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
					Log.i(TAG,"下载apk,更新");
					downLoadApk();
				}   
			});
			//当点取消按钮时进行登录
			builer.setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			AlertDialog dialog = builer.create();
			dialog.show();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		try{
			final ProgressDialog pd;	//进度条对话框
			pd = new  ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("正在下载更新");
			pd.show();
			new Thread(){
				@Override
				public void run() {
					try {
						File file = DownLoadManager.getFileFromServer(versionObject.getString("sNewVerAddr").toString(), pd);
						sleep(3000);
						installApk(file);
						pd.dismiss(); //结束掉进度条对话框
					} catch (Exception e) {
						Message msg = new Message();
						msg.what = DOWN_ERROR;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}}.start();		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//安装apk 
	protected void installApk(File file) {
		Intent intent = new Intent();
		//执行动作
		intent.setAction(Intent.ACTION_VIEW);
		//执行的数据类型
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
}
