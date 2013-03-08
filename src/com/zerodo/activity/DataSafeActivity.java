package com.zerodo.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.backup.BackupTask;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.model.MedicalCase;

public class DataSafeActivity extends CommonActivity implements OnTouchListener,OnClickListener {
	//���ݱ���
	private LinearLayout layoutDataBackup;
	//���ݻ�ԭ
	private LinearLayout layoutDataRecycle;	
	
	private Button btnBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_datasafe);
		initUI();
	}
	private void initUI(){
		btnBack=(Button) findViewById(R.id.btnBack);
		layoutDataBackup=(LinearLayout) findViewById(R.id.layoutDataBackup);
		layoutDataRecycle=(LinearLayout) findViewById(R.id.layoutDataRecycle);
		btnBack.setOnTouchListener(this);
		layoutDataBackup.setOnClickListener(this);
		layoutDataRecycle.setOnClickListener(this);
	}
	public void onClick(View view) {
		Intent it;
		switch (view.getId()) {
		case R.id.layoutDataBackup:
			dialog("backup");
			break;
		case R.id.layoutDataRecycle:
			it =new Intent(this, DataRecycleActivity.class);
			startActivity(it);
			break;
		}
	}
	private void dialog(final String param) { 
		 AlertDialog.Builder builder = new Builder(this); 
		 if("backup".equals(param))
			 builder.setMessage("��ȷ���������ݱ���?");
		 else
			 builder.setMessage("��ȷ���������ݻ�ԭ?");	
		 builder.setTitle("��ʾ"); 
		 builder.setPositiveButton("ȷ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		new BackupTask(DataSafeActivity.this,null).execute(param);
			 		dialog.dismiss(); 
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
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.btnBack:
			switch (event.getAction()){
			//���£��������á����ء��ı���ͼƬ
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			//�ɿ����������á����ء��ı���ͼƬ
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				//�������ʱ�رձ�ҳ��
				this.finish();
				break;
			}
			break;
		}
		return true;
	}
}
