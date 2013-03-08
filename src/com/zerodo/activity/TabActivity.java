package com.zerodo.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;

public class TabActivity extends CommonActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	protected void dialog() { 
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("ȷ��Ҫ�˳���"); 
		 builder.setTitle("��ʾ"); 
		 builder.setPositiveButton("ȷ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 		HealthyApplication.getInstance().exit();
			 	} 
		 }); 
		 builder.setNegativeButton("ȡ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 	} 
		 }); 
		 builder.create().show(); 
	 } 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        dialog(); 
	        return false; 
		}
		return false;
	}
}
