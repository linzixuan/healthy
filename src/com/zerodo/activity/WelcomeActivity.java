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
         //����ͼƬ����Ч��        
         AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);         
         aa.setDuration(1500);         
         iv.startAnimation(aa); 
         //�ڲ�������ʵ�ֶ�����������д�����¼������ǹ��ĵ�ʱ���һ��         
         aa.setAnimationListener(new AnimationListener(){             
         	public void onAnimationStart(Animation animation){}            
         	public void onAnimationRepeat(Animation animation){}            
         	//������������ת����¼����           
         	public void onAnimationEnd(Animation animation){ 
         		Log.i(TAG,"����û��Ƿ�ע�� ");
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
     				Log.i(TAG,"�Ѿ���ע���û� ");
     			}else{
     				Log.i(TAG,"��ע���û� ");
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
			if(info.getMacAddress()==null&& !wifiMgr.isWifiEnabled()){
				wifiMgr.setWifiEnabled(true);
				for (int i = 0; i < 10; i++) {
					WifiInfo _info = wifiMgr.getConnectionInfo();
					if ( _info.getMacAddress() != null) {
						mac = _info.getMacAddress().replaceAll(":", "");
						break;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
				wifiMgr.setWifiEnabled(false);
			}else{
				mac = info.getMacAddress().replaceAll(":", "");
			}
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
	 * ��������������
	 */
	private void LoginMain(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		//��������ǰ��activity 
		this.finish();
	}
}