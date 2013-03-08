package com.zerodo.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.SelectListApapter;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.db.dao.DiseaseCategoryDao;
import com.zerodo.db.dao.DoctorAdviceDao;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DoctorAdvice;

public class DiseaseSelectActivity extends CommonActivity implements OnTouchListener,OnItemClickListener {
	private ListView diseaseList;
	private List datas;
	private SelectListApapter diseaseApapter;
	private Button btnBack;
	private Button btnSave;
	private DiseaseCategory diseaseSelected;
	private CheckBox selectedCheckBox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_diseaseselect);
		initUI();
		initDatas();
	}
	private void initUI(){
		diseaseList=(ListView) findViewById(R.id.diseaseList);
		diseaseList.setOnItemClickListener(this);
		btnBack=(Button) findViewById(R.id.diseaseBack);
		btnSave=(Button) findViewById(R.id.diseaseSave);
		btnBack.setOnTouchListener(this);
		btnSave.setOnTouchListener(this);
	}
	private void initDatas(){
		DiseaseCategoryDao dao=new DiseaseCategoryDao(this);
		datas=dao.find();
		diseaseApapter=new SelectListApapter(this,datas);
		diseaseList.setAdapter(diseaseApapter);
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
				this.finish();
				break;
			}
			break;
		case R.id.diseaseSave:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);

				Intent intent=this.getIntent();
				Bundle bundle=new Bundle();
				if(null!=diseaseSelected){
					bundle.putString("fdDidease", diseaseSelected.getFdName());
				}else{
					bundle.putString("fdDidease", "");
				}				
				intent.putExtras(bundle);
				this.setResult(0, intent);
				this.finish();
				break;
			}
			break;


		}
		return true;
	}
	public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
		CheckBox checkBox=(CheckBox) view.findViewById(R.id.commonCheckBox);
		if(checkBox.isChecked()){
			selectedCheckBox=null;
			diseaseSelected=null;
			checkBox.setChecked(false);
		}else{
			if(null!=selectedCheckBox){
				selectedCheckBox.setChecked(false);
			}
			selectedCheckBox=checkBox;
			checkBox.setChecked(true);
			diseaseSelected=(DiseaseCategory) datas.get(index);

		}
	}
}
