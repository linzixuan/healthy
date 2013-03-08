package com.zerodo.activity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.ConfigUtil;
import com.zerodo.base.util.CryptorFun;
import com.zerodo.base.util.RSAUtils;
import com.zerodo.base.util.StringUtil;

public class PhoneInfoActivity extends CommonActivity {

	private TextView mac,imei,accid,vip,reg;
	public static String sPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNDySvNmCaiyJTTQelrUylzNHzlX9VFGcU1lX0DP38dV6B7H/nrd6oqMpqlCyEs/kCn+6b1fHMXxp/GWmPVQq07qbUEE0vnORA/rfg9ljYjrK1/BQ42VKTaNWYXmVNd94zRkZ/HnkIENwgV6XxrW+Dn8bAALS/vN7pQEkVMe+CMwIDAQAB";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phoneinfo);
		mac=(TextView) findViewById(R.id.mac);
		imei=(TextView) findViewById(R.id.imei);
		accid=(TextView) findViewById(R.id.accid);
		vip=(TextView) findViewById(R.id.vip);
		reg=(TextView) findViewById(R.id.reg);
		checkIsVIP();
	}
	
	
	 private void checkIsVIP(){
			String macV = null, imeiV = null;
			WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (null != info&&null!= info.getMacAddress()) {
				macV = info.getMacAddress().replaceAll(":", "");
			}
			imeiV = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
					.getDeviceId();
			mac.setText(macV);
			imei.setText(imeiV);	
		 
		 try{
			 	String accidV=ConfigUtil.getConfig(this,ConfigUtil.KEY_SACCID);
	     		accid.setText(accidV);	     		
			 	String sRegKey=ConfigUtil.getConfig(this,ConfigUtil.KEY_SREGKEY);
	     		reg.setText(sRegKey);
	     		if(StringUtil.isNotNull(sRegKey)){
	     			RSAUtils.Cipher_ALGORITHM = RSAUtils.NoPadding;
	     			String realKey=new String(RSAUtils.decryptByPublicKey(Base64.decode(sRegKey, Base64.DEFAULT),sPubkey));
	     			if(realKey.equals(CryptorFun.Encrypt(GetSignID(macV,imeiV), "SHA"))){
	     				vip.setText("ÊÇ");
	     			}else{
	     				vip.setText("ÊÇ");
	     			}
	     		}    		
	    	}catch(Exception e){
	    		reg.setText(e.getStackTrace().toString());
	    	}

	    }

	    private String GetSignID(String macV,String imeiV) {

			String sign1 = "";
			String sign2 = "";
			if (StringUtil.isNotNull(imeiV))
				sign1 = imeiV.substring(5);
			if (StringUtil.isNotNull(macV))
				sign2 = macV.substring(0, macV.length() - 3);
			if (sign1 != "" && sign2 != "")
				return sign1 + sign2;
			else if (!"".equals(sign1))
				return sign1 + imeiV;
			else if (!"".equals(sign2))
				return sign2 + macV;
			else
				return "";
		}
}
