package com.zerodo.activity;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.ConfigUtil;
import com.zerodo.base.util.CryptorFun;
import com.zerodo.base.util.RSAUtils;
import com.zerodo.base.util.StringUtil;

public class RegistActivity extends CommonActivity implements OnTouchListener {

	public static String sPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNDySvNmCaiyJTTQelrUylzNHzlX9VFGcU1lX0DP38dV6B7H/nrd6oqMpqlCyEs/kCn+6b1fHMXxp/GWmPVQq07qbUEE0vnORA/rfg9ljYjrK1/BQ42VKTaNWYXmVNd94zRkZ/HnkIENwgV6XxrW+Dn8bAALS/vN7pQEkVMe+CMwIDAQAB";
	private static String wsurl = "http://zerodo.cn/LicServ/LicServIntf.asmx";
	private String NameSpace = "http://tempuri.org/";
	private String MethodName = "RegisterSoft";
	private String soapAction = NameSpace + MethodName;
	private Button regist, trial,btnBack;
	private EditText sAccidText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_regist);
		initUI();
		initInfo(this);
	}

	private void initUI() {
		regist = (Button) findViewById(R.id.regist);
		regist.setOnTouchListener(this);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnTouchListener(this);
		sAccidText = (EditText) findViewById(R.id.sAccid);
	}

	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.regist:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				regist.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				regist.setBackgroundResource(R.drawable.add_button_normal);
				if (StringUtil.isNull(sAccidText.getText().toString())) {
					Toast.makeText(this, "授权码不能为空！请您输入正确的授权码。", Toast.LENGTH_SHORT).show();
					break;
				}
				try{
					JSONObject rtnObject =regclick();
					if(rtnObject!=null){
						String result=rtnObject.getString("result");
						if(StringUtil.isNotNull(result)&&"true".equals(result)){
							Toast.makeText(this, rtnObject.getString("sReturnMsg"), Toast.LENGTH_SHORT).show();
							ConfigUtil.setConfig(this, ConfigUtil.KEY_SREGKEY, rtnObject.getString("sRegKey"));
							ConfigUtil.setConfig(this, ConfigUtil.KEY_SACCID,sAccidText.getText().toString());
							HealthyApplication.getInstance().setVIP(true);
							this.finish();
							break;
						}else{
							Toast.makeText(this, rtnObject.getString("sReturnMsg"), Toast.LENGTH_SHORT).show();
						}
					}				
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
			}
			break;
//		case R.id.trial:
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				trial.setBackgroundResource(R.drawable.add_button_select);
//				break;
//
//			case MotionEvent.ACTION_UP:
//				trial.setBackgroundResource(R.drawable.add_button_normal);
//				Intent it = new Intent(this, MainActivity.class);
//				startActivity(it);
//				break;
//			}
//			break;
		case R.id.btnBack:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;

			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				this.finish();
				break;
			}
			break;
		}
		return false;
	}

	public JSONObject regclick() {
		try {
			JSONObject rtnObject = new JSONObject();
			String macAddress = null, iMei = null;
			WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (null != info) {
				if(info.getMacAddress()==null&& !wifiMgr.isWifiEnabled()){
					wifiMgr.setWifiEnabled(true);
					for (int i = 0; i < 10; i++) {
						WifiInfo _info = wifiMgr.getConnectionInfo();
						if ( _info.getMacAddress() != null) {
							macAddress = _info.getMacAddress().replaceAll(":", "");
							break;
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
					}
					wifiMgr.setWifiEnabled(false);
				}else{
					macAddress = info.getMacAddress().replaceAll(":", "");
				}
			}
			iMei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
					.getDeviceId();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sys", "0");
			jsonObject.put("mac", macAddress);
			jsonObject.put("imei", iMei);

			String sLIcInfo = null;

			RSAUtils.Cipher_ALGORITHM = RSAUtils.PKCS1Padding;
			sLIcInfo = Base64.encodeToString(RSAUtils.encryptByPublicKey(
					jsonObject.toString().getBytes(), sPubkey), Base64.DEFAULT);
			String sAccid = sAccidText.getText().toString();
			rtnObject=regSoft(wsurl, sLIcInfo, sAccid,iMei,macAddress);
			return rtnObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject regSoft(String url, String sLicInfo, String sAccid,String imei,String mac) throws JSONException {
		JSONObject rtnObject = new JSONObject();
		try {
			SoapObject request = new SoapObject(NameSpace, MethodName);
			request.addProperty("sAccid", sAccid);
			request.addProperty("sLicInfo", sLicInfo);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.bodyOut = request;
			HttpTransportSE ht = new HttpTransportSE(url);
			ht.call(soapAction, envelope);
			if (envelope.getResponse() != null) {
				Vector response = (Vector) envelope.getResponse();
				rtnObject.put("result", response.get(0).toString());
				rtnObject.put("sRegKey", response.get(1).toString());
				rtnObject.put("sReturnMsg", response.get(2).toString());
//
//				if (response.get(0).toString().equals("true")) {
//					String sRegKey = response.get(1).toString();
//					RSAUtils.Cipher_ALGORITHM = RSAUtils.NoPadding;
//					result += "realkey:"
//							+ new String(RSAUtils.decryptByPublicKey(
//									Base64.decode(sRegKey, Base64.DEFAULT),
//									sPubkey)) + "\n";
//					result += "oldkey :"
//							+ CryptorFun.Encrypt(GetSignID(imei, mac), "SHA");
//					// 软件启动时通过判断realkey是否和oldkey 一致，一致则表示软件的注册码有效。
//				}
			}
		} catch (Exception e) {
			rtnObject.put("result","false");
			rtnObject.put("sReturnMsg", "注册失败，请您检查网络或者下次再试。");
		}

		return rtnObject;
	}

	
	public void initInfo(Context context){
		TextView telView=(TextView)this.findViewById(R.id.registTel);
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
		
		TextView urlView=(TextView)this.findViewById(R.id.registUrl);
		String htmlUrlText = getString(R.string.regist_url);
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


}
