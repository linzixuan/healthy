package com.zerodo.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.common.CommonListApapter;
import com.zerodo.db.dao.DepartmentDao;
import com.zerodo.db.dao.DiseaseCategoryDao;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.Department;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.PatientInfo;

public class DepartmentActivity extends CommonActivity implements OnTouchListener,OnCreateContextMenuListener,OnItemClickListener {
	private ListView departmentList;
	private CommonListApapter departmentApapter;
	private Button btnBack;
	private Button btnNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_department);
		initUI();
		
	}
	private void initUI(){
		departmentList=(ListView) findViewById(R.id.departmentlist);
		
		DepartmentDao dao=new DepartmentDao(this);
		List datas=dao.find();
		departmentApapter=new CommonListApapter(this,datas);
		departmentList.setAdapter(departmentApapter);
		departmentList.setOnCreateContextMenuListener(this);
		departmentList.setOnItemClickListener(this);
		btnBack=(Button) findViewById(R.id.departmentBack);
		btnNew=(Button) findViewById(R.id.departmentNew);
		btnBack.setOnTouchListener(this);
		btnNew.setOnTouchListener(this);
	}
	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v,   //���������˵�
	   ContextMenuInfo menuInfo) {
		menu.add(0, 0, 0, "�༭");
		menu.add(0, 1, 0, "ɾ��");
	  
	  super.onCreateContextMenu(menu, v, menuInfo);
	 } 

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo menuInfo = (ContextMenuInfo) item.getMenuInfo();   
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item .getMenuInfo();  
		Department model=(Department) departmentApapter.getItem(info.position);
		DepartmentDao dao=new DepartmentDao(this);
		switch(item.getItemId()){
			case 0:
				Bundle bundle = new Bundle();
				Intent it=new Intent(this,DepartmentAddActivity.class);
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
		Department model=(Department) departmentApapter.getItem(index);
		Bundle bundle = new Bundle();
		Intent it=new Intent(this,DepartmentAddActivity.class);
		bundle.putSerializable("model",model);
		it.putExtras(bundle);
		startActivity(it);
	}
	
	private void dialog(final Department department) { 
		 final DepartmentDao dao=new DepartmentDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("��ȷ��ɾ���ü�¼?"); 
		 builder.setTitle("��ʾ"); 
		 builder.setPositiveButton("ȷ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					dao.delete(department.getFdId());  
					departmentApapter.delete(department);
					departmentApapter.notifyDataSetChanged();
					Toast.makeText(DepartmentActivity.this,"ɾ���ɹ�", Toast.LENGTH_SHORT).show();
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
		case R.id.departmentBack:
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
		case R.id.departmentNew:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				Intent intent = new Intent(this, DepartmentAddActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;


		}
		return true;
	}
}
