package com.zerodo.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.DiseaseCategoryDao;
import com.zerodo.db.dao.DiseaseFeaturesDao;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DiseaseFeatures;

public class DiseaseFeaturesAddActivity extends CommonActivity implements OnTouchListener,OnItemSelectedListener{
	
	private Button btnBack;
	private Button btnSave;
	private TextView textName;
	private DiseaseFeatures model;
	private Spinner spinner;
	private ArrayAdapter arrayAdapter;
	private DiseaseCategory diseaseSelected;
	String[] datas;
	List diseasesList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_diseasefeaturesadd);
		initUI();
		initDatas();
	}
	private void initUI(){
		btnBack=(Button) findViewById(R.id.diseaseFeaturesAddBack);
		btnSave=(Button) findViewById(R.id.diseaseFeaturesSave);
		textName=(TextView) findViewById(R.id.textName);
		spinner=(Spinner) findViewById(R.id.diseaseFeaturesAddSpinner);
		btnBack.setOnTouchListener(this);
		btnSave.setOnTouchListener(this);
	}
	
	private void initDatas(){
		Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(null!=bundle){
            model= (DiseaseFeatures)bundle.get("model");
            if(null!=model){
            	textName.setText(model.getFdName()); 
            }
            		
        }
        int defaultPosition=-1;
        DiseaseCategoryDao dao=new DiseaseCategoryDao(this);
		diseasesList=dao.find();
		datas=new String[diseasesList.size()];
		for(int i=0;i<diseasesList.size();i++){
			DiseaseCategory disease=(DiseaseCategory)diseasesList.get(i);
			datas[i]=disease.getFdName();
			if(null!=model&&disease.getFdId()==model.getFdDiseaseId())
				defaultPosition=i;
		}
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datas); 
		//设置下拉列表的风格   
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		//将adapter2 添加到spinner中  
		spinner.setAdapter(arrayAdapter);  
		//添加事件Spinner事件监听    
		spinner.setOnItemSelectedListener(this);  
		//设置默认值  
		spinner.setVisibility(View.VISIBLE);  
		if(defaultPosition!=-1)
			spinner.setSelection(defaultPosition);

	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.diseaseFeaturesAddBack:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				Intent intent = new Intent(this, DiseaseFeaturesActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;
		case R.id.diseaseFeaturesSave:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				addOrUpdate();
				break;
			}
			break;
		}
		return true;
	}
	private void addOrUpdate(){
		if(!checkForm())return;
		DiseaseFeaturesDao dao=new DiseaseFeaturesDao(this);
		if(null!=model){
			model.setFdName(textName.getText().toString());
			dao.update(model);
			model.setFdDiseaseId(diseaseSelected.getFdId());
			dao.update(model);
		}else{
			DiseaseFeatures diseaseFeatures=new DiseaseFeatures();
			diseaseFeatures.setFdName(textName.getText().toString());
			diseaseFeatures.setFdDiseaseId(diseaseSelected.getFdId());
			dao.insert(diseaseFeatures);		
		}
		Intent intent = new Intent(this, DiseaseFeaturesActivity.class);
		startActivity(intent);
		this.finish();	
	}
	private boolean checkForm(){
		boolean rtnValue=true;
		if(null==diseaseSelected){
			Toast.makeText(this,"病种不能为空！请您选择录入病种信息。", Toast.LENGTH_SHORT).show();
			rtnValue=false;
		}
		if(StringUtil.isNull(textName.getText().toString())){
			Toast.makeText(this,"名称不能为空！请您重新输入名称。", Toast.LENGTH_SHORT).show();
			rtnValue=false;
		}
		return rtnValue;
	}
	public void onItemSelected(AdapterView<?> adapter, View view, int arg2,
			long arg3) {
		int index = adapter.getSelectedItemPosition();
		diseaseSelected=(DiseaseCategory)diseasesList.get(index);
		
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
