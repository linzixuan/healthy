package com.zerodo.activity;

import java.io.Serializable;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.common.CommonListApapter;
import com.zerodo.db.dao.DepartmentDao;
import com.zerodo.db.dao.DiseaseCategoryDao;
import com.zerodo.db.model.Department;
import com.zerodo.db.model.DiseaseCategory;

public class DiseaseCategoryActivity extends CommonActivity implements OnTouchListener,OnCreateContextMenuListener,OnItemClickListener{

	private ListView diseaseList;
	private CommonListApapter diseaseApapter;
	private Button btnBack;
	private Button btnNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_diseasecategory);
		initUI();
		
	}
	private void initUI(){
		diseaseList=(ListView) findViewById(R.id.diseaselist);
		
		DiseaseCategoryDao dao=new DiseaseCategoryDao(this);
		List datas=dao.find();
		diseaseApapter=new CommonListApapter(this,datas);
		diseaseList.setAdapter(diseaseApapter);
		diseaseList.setOnCreateContextMenuListener(this);
		diseaseList.setOnItemClickListener(this);
		btnBack=(Button) findViewById(R.id.diseaseBack);
		btnNew=(Button) findViewById(R.id.diseaseNew);
		btnBack.setOnTouchListener(this);
		btnNew.setOnTouchListener(this);
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
		DiseaseCategory model=(DiseaseCategory) diseaseApapter.getItem(info.position);
		DiseaseCategoryDao dao=new DiseaseCategoryDao(this);
		switch(item.getItemId()){
			case 0:
				Bundle bundle = new Bundle();
				Intent it=new Intent(this, DiseaseCategoryAddActivity.class);
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
		DiseaseCategory model=(DiseaseCategory) diseaseApapter.getItem(index);
		Bundle bundle = new Bundle();
		Intent it=new Intent(this, DiseaseCategoryAddActivity.class);
		bundle.putSerializable("model",model);
		it.putExtras(bundle);
		startActivity(it);
		
	}
	private void dialog(final DiseaseCategory model) { 
		 final DiseaseCategoryDao dao=new DiseaseCategoryDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("你确定删除该记录?"); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss();
					dao.execSQL("delete table_disease_features where fd_disease_id='"+model.getFdId()+"'");
					dao.execSQL("delete table_doctor_advice where fd_disease_id='"+model.getFdId()+"'");
					dao.delete(model.getFdId());  
					diseaseApapter.delete(model);
					diseaseApapter.notifyDataSetChanged();
					Toast.makeText(DiseaseCategoryActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
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
		case R.id.diseaseBack:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				Intent intent = new Intent(this, BaseDataActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;
		case R.id.diseaseNew:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				Intent intent = new Intent(this, DiseaseCategoryAddActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;


		}
		return true;
	}
}
