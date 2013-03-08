package com.zerodo.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.common.CommonListApapter;
import com.zerodo.db.dao.DiseaseCategoryDao;
import com.zerodo.db.dao.DiseaseFeaturesDao;
import com.zerodo.db.dao.DoctorAdviceDao;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DiseaseFeatures;
import com.zerodo.db.model.DoctorAdvice;

public class DoctorAdviceActivity extends CommonActivity implements OnTouchListener,OnCreateContextMenuListener,OnItemSelectedListener,OnItemClickListener {
	private ListView doctorAdviceList;
	private CommonListApapter doctorAdviceApapter;
	private Button btnBack;
	private Button btnNew;
	private Spinner spinner;
	private ArrayAdapter arrayAdapter;
	String[] diseaseDatas;
	List diseasesList;
	private DiseaseCategory diseaseSelected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_doctoradvice);
		initUI();
		initDatas();
	}
	private void initUI(){
		doctorAdviceList=(ListView) findViewById(R.id.doctorAdviceList);
		doctorAdviceList.setOnCreateContextMenuListener(this);
		doctorAdviceList.setOnItemClickListener(this);
		btnBack=(Button) findViewById(R.id.doctorAdviceBack);
		btnNew=(Button) findViewById(R.id.doctorAdviceNew);
		spinner=(Spinner) findViewById(R.id.doctorAdviceSpinner);
		btnBack.setOnTouchListener(this);
		btnNew.setOnTouchListener(this);
	}
	private void initDatas(){
		DoctorAdviceDao dao=new DoctorAdviceDao(this);
		List datas=dao.find();
		doctorAdviceApapter=new CommonListApapter(this,datas);
		doctorAdviceList.setAdapter(doctorAdviceApapter);
		
        DiseaseCategoryDao DiseaseDao=new DiseaseCategoryDao(this);
		diseasesList=DiseaseDao.find();
		diseaseDatas=new String[diseasesList.size()];
		for(int i=0;i<diseasesList.size();i++){
			DiseaseCategory disease=(DiseaseCategory)diseasesList.get(i);
			diseaseDatas[i]=disease.getFdName();
		}
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, diseaseDatas); 
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		spinner.setAdapter(arrayAdapter);  
		spinner.setOnItemSelectedListener(this);  
		spinner.setVisibility(View.VISIBLE);  
	}
	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v,   //长按弹出菜单
	   ContextMenuInfo menuInfo) {
		 menu.add(0, 0, 0, "编辑");
		 menu.add(0, 1, 0, "删除");
	  super.onCreateContextMenu(menu, v, menuInfo);
	 } 

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo menuInfo = (ContextMenuInfo) item.getMenuInfo();   
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item .getMenuInfo();  
		DoctorAdvice model=(DoctorAdvice) doctorAdviceApapter.getItem(info.position);
		DoctorAdviceDao dao=new DoctorAdviceDao(this);
		switch(item.getItemId()){
			case 0:
				Bundle bundle = new Bundle();
				Intent it=new Intent(this,DoctorAdviceAddActivity.class);
				bundle.putSerializable("model",model);
				it.putExtras(bundle);
				startActivity(it);
				break;
			case 1:
				dialog(model);
				break;
		}
		return super.onContextItemSelected(item);
	}
	
	public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
		DoctorAdvice model=(DoctorAdvice) doctorAdviceApapter.getItem(index);
		Bundle bundle = new Bundle();
		Intent it=new Intent(this,DoctorAdviceAddActivity.class);
		bundle.putSerializable("model",model);
		it.putExtras(bundle);
		startActivity(it);
		
	}
	
	private void dialog(final DoctorAdvice model) { 
		 final DoctorAdviceDao dao=new DoctorAdviceDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("你确定删除该记录?"); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					dao.delete(model.getFdId());  
					doctorAdviceApapter.delete(model);
					doctorAdviceApapter.notifyDataSetChanged();
					Toast.makeText(DoctorAdviceActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
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
		case R.id.doctorAdviceBack:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				this.finish();
				break;
			}
			break;
		case R.id.doctorAdviceNew:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				Intent intent = new Intent(this,DoctorAdviceAddActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;


		}
		return true;
	}
	public void onItemSelected(AdapterView<?> adapter, View arg1, int arg2,
			long arg3) {
		int index = adapter.getSelectedItemPosition();
		diseaseSelected=(DiseaseCategory)diseasesList.get(index);
		DoctorAdviceDao dao=new DoctorAdviceDao(this);
		String[] selectionArgs = { Integer.toString(diseaseSelected.getFdId()) };
		List datas=dao.find(null, "fd_disease_id=?",selectionArgs, null, null, null, null);
		doctorAdviceApapter.setDatas(datas);
		doctorAdviceApapter.notifyDataSetChanged();
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
