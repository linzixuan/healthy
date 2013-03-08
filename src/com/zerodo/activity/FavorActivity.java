package com.zerodo.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.FavorMedicalListApapter;
import com.zerodo.adapter.FavorPatientListApapter;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;

public class FavorActivity extends TabActivity implements OnItemLongClickListener,OnItemClickListener{

	private FavorPatientListApapter favorPatientListApapter;
	private FavorMedicalListApapter favorMedicalListApapter;
	private ListView patientListView,medicalListView;
	private PatientInfo patientInfo;
	private MedicalCase medicalCase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_favor);
		initUI();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initUI(){
		patientListView=(ListView) findViewById(R.id.patientListView);
		patientListView.setOnItemLongClickListener(this);
		patientListView.setOnItemClickListener(this);
		medicalListView=(ListView) findViewById(R.id.medicalListView);
		medicalListView.setOnItemLongClickListener(this);
		medicalListView.setOnItemClickListener(this);
	}
	
	private void initData(){
		String[] selectionArgs = {"1"};
		PatientInfoDao patientInfoDao =new PatientInfoDao(this);
		List patientDatas=patientInfoDao.find(null, "fd_is_favor=?", selectionArgs, null, null, "fd_id desc", null);
		favorPatientListApapter=new FavorPatientListApapter(this,patientDatas);
		patientListView.setAdapter(favorPatientListApapter);		
		
		MedicalCaseDao medicalCaseDao =new MedicalCaseDao(this);
		List medicalDatas=medicalCaseDao.find(null, "fd_is_favor=?", selectionArgs, null, null, "fd_date desc", null);
		favorMedicalListApapter=new FavorMedicalListApapter(this,medicalDatas);
		medicalListView.setAdapter(favorMedicalListApapter);
	}
	
	public boolean onItemLongClick(AdapterView adapterView, View view, int index,
			long arg3) {
		if(adapterView.getAdapter() instanceof FavorPatientListApapter){
			patientInfo=(PatientInfo)favorPatientListApapter.getDatas().get(index);
			patientListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {                                        
	        	public void onCreateContextMenu(ContextMenu menu, View v,                                                       
	        			ContextMenuInfo menuInfo) {                                               
	        		menu.add(0, 0, 0, "编辑");                                               
	        		menu.add(0, 1, 0, "移除");                                                
	        		menu.add(0, 2, 0, "导出");  
	        		}  
	        	public boolean onContextItemSelected(MenuItem item) {
					return false; 
	        		
	        	}
	        	});	
		}else if(adapterView.getAdapter() instanceof FavorMedicalListApapter){
			medicalCase=(MedicalCase)favorMedicalListApapter.getDatas().get(index);
			medicalListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {                                        
	        	public void onCreateContextMenu(ContextMenu menu, View v,                                                       
	        			ContextMenuInfo menuInfo) {                                               
	        		menu.add(1, 0, 0, "编辑");                                               
	        		menu.add(1, 1, 0, "移除");                                                
	        		menu.add(1, 2, 0, "导出");  
	        		} 
	        	public boolean onContextItemSelected(MenuItem item) {
					return false; 
	        		
	        	}
	        	});
		}
			return false;
	}
	
	public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
		if(adapterView.getAdapter() instanceof FavorPatientListApapter){
			patientInfo=(PatientInfo)favorPatientListApapter.getDatas().get(index);
			Bundle bundle = new Bundle();
			Intent it=new Intent(this,PatientAddActivity.class);
			bundle.putSerializable("model",patientInfo);
			it.putExtras(bundle);
			startActivity(it);			
		}else if(adapterView.getAdapter() instanceof FavorMedicalListApapter){
			medicalCase=(MedicalCase)favorMedicalListApapter.getDatas().get(index);
			Bundle medicalBundle = new Bundle();
			Intent medicalIt=new Intent(this,MedicalCaseAddActivity.class);
			medicalBundle.putSerializable("medicalCase",medicalCase);
			medicalIt.putExtras(medicalBundle);
			startActivity(medicalIt);	
		}
	}
	// 长按菜单响应函数        
	public boolean onContextItemSelected(MenuItem item) { 
		PatientInfoDao patientInfoDao=new PatientInfoDao(this);
		MedicalCaseDao medicalCaseDao=new MedicalCaseDao(this);
		switch (item.getGroupId()) {               
			case 0:      
				switch (item.getItemId()) {               
				case 0:   
					Bundle bundle = new Bundle();
					Intent it=new Intent(this,PatientAddActivity.class);
					bundle.putSerializable("model",patientInfo);
					it.putExtras(bundle);
					startActivity(it);
					break;                 
				case 1:     
					patientInfo.setFdIsFavor(0);
					patientInfoDao.update(patientInfo);
					Toast.makeText(this,"移除成功", Toast.LENGTH_SHORT).show();
					favorPatientListApapter.delete(patientInfo);
					favorPatientListApapter.notifyDataSetChanged();
					break;                 
				case 2:                        
					// 导出                     
					break; 
				}
				break;
			case 1: 
				switch (item.getItemId()) { 
				case 0:                        
					Bundle medicalBundle = new Bundle();
					Intent medicalIt=new Intent(this,MedicalCaseAddActivity.class);
					medicalBundle.putSerializable("medicalCase",medicalCase);
					medicalIt.putExtras(medicalBundle);
					startActivity(medicalIt);
					break;
				case 1:     
					medicalCase.setFdIsFavor(0);
					medicalCaseDao.update(medicalCase);
					Toast.makeText(this,"移除成功", Toast.LENGTH_SHORT).show();
					favorMedicalListApapter.delete(medicalCase);
					favorMedicalListApapter.notifyDataSetChanged();
					break;                 
				case 2:                        
					// 导出                     
				break; 
			}
				break;
		}
		return super.onContextItemSelected(item); 
	}
}
