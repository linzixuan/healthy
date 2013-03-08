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
import com.zerodo.db.dao.DoctorAdviceDao;
import com.zerodo.db.dao.EducationalDao;
import com.zerodo.db.model.Department;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DoctorAdvice;
import com.zerodo.db.model.Educational;

public class EducationalActivity extends CommonActivity implements OnTouchListener,OnCreateContextMenuListener,OnItemClickListener{
	private ListView educationalList;
	private CommonListApapter educationalApapter;
	private Button btnBack;
	private Button btnNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_educational);
		initUI();
		
	}
	private void initUI(){
		educationalList=(ListView) findViewById(R.id.educationallist);
		educationalList.setOnCreateContextMenuListener(this);
		educationalList.setOnItemClickListener(this);
		EducationalDao dao=new EducationalDao(this);
		List datas=dao.find();
		educationalApapter=new CommonListApapter(this,datas);
		educationalList.setAdapter(educationalApapter);
		btnBack=(Button) findViewById(R.id.educationalBack);
		btnNew=(Button) findViewById(R.id.educationalNew);
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
		Educational model=(Educational) educationalApapter.getItem(info.position);
		EducationalDao dao=new EducationalDao(this);
		switch(item.getItemId()){
			case 0:
				Bundle bundle = new Bundle();
				Intent it=new Intent(this,EducationalAddActivity.class);
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
		Educational model=(Educational) educationalApapter.getItem(index);
		Bundle bundle = new Bundle();
		Intent it=new Intent(this,EducationalAddActivity.class);
		bundle.putSerializable("model",model);
		it.putExtras(bundle);
		startActivity(it);
	}
	
	private void dialog(final Educational model) { 
		 final EducationalDao dao=new EducationalDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("你确定删除该记录?"); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					dao.delete(model.getFdId());  
					educationalApapter.delete(model);
					educationalApapter.notifyDataSetChanged();
					Toast.makeText(EducationalActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
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
		case R.id.educationalBack:
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
		case R.id.educationalNew:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				Intent intent = new Intent(this, EducationalAddActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;


		}
		return true;
	}
}
