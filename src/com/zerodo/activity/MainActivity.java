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
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.util.ConfigUtil;
import com.zerodo.base.util.RSAUtils;
import com.zerodo.base.util.StringUtil;
import com.zerodo.update.DownLoadManager;

public class MainActivity extends TabActivity implements OnTabChangeListener {
	public static String TAB_TAG_HOME = "home";
	public static String TAB_TAG_PATIENT= "patient";
	public static String TAB_TAG_MEDICAL = "medical";
	public static String TAB_TAG_FAVOR = "favor";
	public static String TAB_TAB_SETTING = "setting";
	public static TabHost mTabHost;
	static final int COLOR1 = Color.parseColor("#787878");
	static final int COLOR2 = Color.parseColor("#ffffff");
	ImageView mBut1, mBut2, mBut3, mBut4, mBut5;
	TextView mCateText1, mCateText2, mCateText3, mCateText4, mCateText5;

	Intent mHomeItent, mPatientlIntent, mMedicalIntent, mFavorIntent,
			mSettingIntent;

	int mCurTabId = R.id.channel_home;

	// Animation
	private Animation left_in, left_out;
	private Animation right_in, right_out;


	//检测升级相关
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

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		prepareAnim();
		prepareIntent();
		setupIntent();
		prepareView();
		try {
			checkVersion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//监听广播
		registerBoradcastReceiver(); 
	}
    public void registerBoradcastReceiver(){ 
        IntentFilter myIntentFilter = new IntentFilter(); 
        myIntentFilter.addAction("medical"); 
        myIntentFilter.addAction("patient");
        //注册广播       
        registerReceiver(mBroadcastReceiver, myIntentFilter); 
    } 
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){ 
        @Override 
        public void onReceive(Context context, Intent intent) { 
            String action = intent.getAction(); 
            if(action.equals("medical")){ 
            	onClick(findViewById(R.id.channel_medical));
            } 
            if(action.equals("patient")){ 
            	onClick(findViewById(R.id.channel_patient));
            } 
        }

         
    };

	private void onClick(View v) {
		if (mCurTabId == v.getId()) {
			return;
		}
		int checkedId = v.getId();
		mBut1.setImageResource(R.drawable.icon_home_n);
		mBut2.setImageResource(R.drawable.icon_patient_n);
		mBut3.setImageResource(R.drawable.icon_medical_n);
		mBut4.setImageResource(R.drawable.icon_favor_n);
		mBut5.setImageResource(R.drawable.icon_setting_n);
		mCateText1.setTextColor(COLOR1);
		mCateText2.setTextColor(COLOR1);
		mCateText3.setTextColor(COLOR1);
		mCateText4.setTextColor(COLOR1);
		mCateText5.setTextColor(COLOR1);
		final boolean o;
		if (mCurTabId < checkedId)
			o = true;
		else
			o = false;
		if (o)
			mTabHost.getCurrentView().startAnimation(left_out);
		else
			mTabHost.getCurrentView().startAnimation(right_out);
		switch (checkedId) {
		case R.id.channel_home:
			mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
			mBut1.setImageResource(R.drawable.icon_home_c);
			mCateText1.setTextColor(COLOR2);
			break;
		case R.id.channel_patient:
			mTabHost.setCurrentTabByTag(TAB_TAG_PATIENT);
			mBut2.setImageResource(R.drawable.icon_patient_c);
			mCateText2.setTextColor(COLOR2);
			break;
		case R.id.channel_medical:
			mTabHost.setCurrentTabByTag(TAB_TAG_MEDICAL);
			mBut3.setImageResource(R.drawable.icon_medical_c);
			mCateText3.setTextColor(COLOR2);
			break;
		case R.id.channel_favor:
			mTabHost.setCurrentTabByTag(TAB_TAG_FAVOR);
			mBut4.setImageResource(R.drawable.icon_favor_c);
			mCateText4.setTextColor(COLOR2);
			break;
		case R.id.channel_setting:
			mTabHost.setCurrentTabByTag(TAB_TAB_SETTING);
			mBut5.setImageResource(R.drawable.icon_setting_c);
			mCateText5.setTextColor(COLOR2);
//			Intent it=new Intent(this, SettingActivity.class);
//			startActivity(it);
			break;
		default:
			break;
		}

		if (o)
			mTabHost.getCurrentView().startAnimation(left_in);
		else
			mTabHost.getCurrentView().startAnimation(right_in);			


		mCurTabId = checkedId;
		
	} 
	private void prepareAnim() {
		left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);
		left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);

		right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
		right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
	}

	private void prepareView() {
		mBut1 = (ImageView) findViewById(R.id.imageView1);
		mBut2 = (ImageView) findViewById(R.id.imageView2);
		mBut3 = (ImageView) findViewById(R.id.imageView3);
		mBut4 = (ImageView) findViewById(R.id.imageView4);
		mBut5 = (ImageView) findViewById(R.id.imageView5);
		findViewById(R.id.channel_home).setOnClickListener(new MyOnClickListener());
		findViewById(R.id.channel_patient).setOnClickListener(new MyOnClickListener());
		findViewById(R.id.channel_medical).setOnClickListener(new MyOnClickListener());
		findViewById(R.id.channel_favor).setOnClickListener(new MyOnClickListener());
		findViewById(R.id.channel_setting).setOnClickListener(new MyOnClickListener());
		mCateText1 = (TextView) findViewById(R.id.textView1);
		mCateText2 = (TextView) findViewById(R.id.textView2);
		mCateText3 = (TextView) findViewById(R.id.textView3);
		mCateText4 = (TextView) findViewById(R.id.textView4);
		mCateText5 = (TextView) findViewById(R.id.textView5);
	}

	private void prepareIntent() {
		mHomeItent = new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		mPatientlIntent = new Intent(this, PatientActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		mMedicalIntent = new Intent(this, MedicalCaseActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		mFavorIntent = new Intent(this, FavorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		mSettingIntent = new Intent(this, SettingActivity.class);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			mBut1.performClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setupIntent() {
		mTabHost = getTabHost();
		mTabHost.addTab(buildTabSpec(TAB_TAG_HOME, R.string.main_home,
				R.drawable.icon_home_n, mHomeItent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_PATIENT,
				R.string.main_patient, R.drawable.icon_patient_n, mPatientlIntent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_MEDICAL, R.string.main_medical,
				R.drawable.icon_medical_n, mMedicalIntent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_FAVOR,
				R.string.main_favor, R.drawable.icon_favor_n, mFavorIntent));
		mTabHost.addTab(buildTabSpec(TAB_TAB_SETTING, R.string.main_setting,
				R.drawable.icon_setting_n, mSettingIntent));
		mTabHost.setOnTabChangedListener(this);
	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return mTabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	public static void setCurrentTabByTag(String tab) {
		mTabHost.setCurrentTabByTag(tab);
	}
	class MyOnClickListener implements android.view.View.OnClickListener{
		public void onClick(View v) {
			if (mCurTabId == v.getId()) {
				return;
			}
			int checkedId = v.getId();
			mBut1.setImageResource(R.drawable.icon_home_n);
			mBut2.setImageResource(R.drawable.icon_patient_n);
			mBut3.setImageResource(R.drawable.icon_medical_n);
			mBut4.setImageResource(R.drawable.icon_favor_n);
			mBut5.setImageResource(R.drawable.icon_setting_n);
			mCateText1.setTextColor(COLOR1);
			mCateText2.setTextColor(COLOR1);
			mCateText3.setTextColor(COLOR1);
			mCateText4.setTextColor(COLOR1);
			mCateText5.setTextColor(COLOR1);
			final boolean o;
			if (mCurTabId < checkedId)
				o = true;
			else
				o = false;
			if (o)
				mTabHost.getCurrentView().startAnimation(left_out);
			else
				mTabHost.getCurrentView().startAnimation(right_out);
			switch (checkedId) {
			case R.id.channel_home:
				mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
				mBut1.setImageResource(R.drawable.icon_home_c);
				mCateText1.setTextColor(COLOR2);
				break;
			case R.id.channel_patient:
				mTabHost.setCurrentTabByTag(TAB_TAG_PATIENT);
				mBut2.setImageResource(R.drawable.icon_patient_c);
				mCateText2.setTextColor(COLOR2);
				break;
			case R.id.channel_medical:
				mTabHost.setCurrentTabByTag(TAB_TAG_MEDICAL);
				mBut3.setImageResource(R.drawable.icon_medical_c);
				mCateText3.setTextColor(COLOR2);
				break;
			case R.id.channel_favor:
				mTabHost.setCurrentTabByTag(TAB_TAG_FAVOR);
				mBut4.setImageResource(R.drawable.icon_favor_c);
				mCateText4.setTextColor(COLOR2);
				break;
			case R.id.channel_setting:
				mTabHost.setCurrentTabByTag(TAB_TAB_SETTING);
				mBut5.setImageResource(R.drawable.icon_setting_c);
				mCateText5.setTextColor(COLOR2);
//				Intent it=new Intent(this, SettingActivity.class);
//				startActivity(it);
				break;
			default:
				break;
			}

			if (o)
				mTabHost.getCurrentView().startAnimation(left_in);
			else
				mTabHost.getCurrentView().startAnimation(right_in);			


			mCurTabId = checkedId;
		}
	}
	

	public void onTabChanged(String tabId) {
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
				//是否第一次提交特征码 是的话记录提交返回信息
				String infoDone=ConfigUtil.getConfig(MainActivity.this, ConfigUtil.KEY_INFODONE);
				if(StringUtil.isNull(infoDone)&&StringUtil.isNotNull(versionObject.getString("sAppInfo"))){
					RSAUtils.Cipher_ALGORITHM = RSAUtils.NoPadding;
					String sAppInfo=new String(RSAUtils.decryptByPublicKey(Base64.decode(versionObject.getString("sAppInfo"), Base64.DEFAULT),sPubkey));
					JSONObject sAppInfoObject=new JSONObject(sAppInfo);
					infoDone=sAppInfoObject.get("done").toString();
					if("true".equals(infoDone)){
						ConfigUtil.setConfig(MainActivity.this, ConfigUtil.KEY_INFODONE, infoDone);
					}
				}
				if(versionObject.get("result").toString().equals("false")){
					Log.i(TAG,"版本号相同无需升级");
//					LoginMain();
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
		String infoDone=ConfigUtil.getConfig(MainActivity.this, ConfigUtil.KEY_INFODONE);
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
			switch (msg.what) {
			case UPDATA_CLIENT:
				//对话框通知用户升级程序 
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//服务器超时 
				//Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show();
//				LoginMain();
				break;	
			case DOWN_ERROR:
				//下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败，请您登录www.zerodo.cn下载最新版本。", 1).show();
//				LoginMain();
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
					//LoginMain();
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
	
	/*
	 * 进入程序的主界面
	 */
	private void LoginMain(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		//结束掉当前的activity 
		this.finish();
	}
}