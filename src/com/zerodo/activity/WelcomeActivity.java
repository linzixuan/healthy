package com.zerodo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.ConfigUtil;
import com.zerodo.base.util.CryptorFun;
import com.zerodo.base.util.RSAUtils;
import com.zerodo.base.util.StringUtil;

public class WelcomeActivity extends CommonActivity {
	
	private final String TAG = this.getClass().getName();
	
	public static String sPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNDySvNmCaiyJTTQelrUylzNHzlX9VFGcU1lX0DP38dV6B7H/nrd6oqMpqlCyEs/kCn+6b1fHMXxp/GWmPVQq07qbUEE0vnORA/rfg9ljYjrK1/BQ42VKTaNWYXmVNd94zRkZ/HnkIENwgV6XxrW+Dn8bAALS/vN7pQEkVMe+CMwIDAQAB";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        HealthyApplication.getInstance().addActivity(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_welcome); 
        initWelcomeView();
    }
    
    private void initWelcomeView(){
    	 ImageView iv = (ImageView) this.findViewById(R.id.loader); 
         //设置图片动画效果        
         AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);         
         aa.setDuration(1500);         
         iv.startAnimation(aa); 
         //内部匿名类实现动画监听，重写三个事件，我们关心的时最后一个         
         aa.setAnimationListener(new AnimationListener(){             
         	public void onAnimationStart(Animation animation){}            
         	public void onAnimationRepeat(Animation animation){}            
         	//动画结束后，跳转到登录界面           
         	public void onAnimationEnd(Animation animation){ 
         		Log.i(TAG,"检测用户是否注册 ");
         		checkIsVIP();
         		LoginMain();
         	}         
         }); 
    }
    
    private void checkIsVIP(){
    	try{
     		String sRegKey=ConfigUtil.getConfig(WelcomeActivity.this,ConfigUtil.KEY_SREGKEY);
     		if(StringUtil.isNotNull(sRegKey)){
     			RSAUtils.Cipher_ALGORITHM = RSAUtils.NoPadding;
     			String realKey=new String(RSAUtils.decryptByPublicKey(Base64.decode(sRegKey, Base64.DEFAULT),sPubkey));
     			if(realKey.equals(CryptorFun.Encrypt(GetSignID(), "SHA"))){
     				HealthyApplication.getInstance().setVIP(true);
     				Log.i(TAG,"已经是注册用户 ");
     			}else{
     				Log.i(TAG,"非注册用户 ");
     			}
     		}    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    }

    private String GetSignID() {
		String mac = null, imei = null;
		WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr
				.getConnectionInfo());
		if (null != info) {
			mac = info.getMacAddress().replaceAll(":", "");
		}
		imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();
		String sign1 = "";
		String sign2 = "";
		if (StringUtil.isNotNull(imei))
			sign1 = imei.substring(5);
		if (StringUtil.isNotNull(mac))
			sign2 = mac.substring(0, mac.length() - 3);
		if (sign1 != "" && sign2 != "")
			return sign1 + sign2;
		else if (!"".equals(sign1))
			return sign1 + imei;
		else if (!"".equals(sign2))
			return sign2 + mac;
		else
			return "";
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