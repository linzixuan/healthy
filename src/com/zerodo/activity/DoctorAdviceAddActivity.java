package com.zerodo.activity;

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
import com.zerodo.db.dao.DoctorAdviceDao;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DiseaseFeatures;
import com.zerodo.db.model.DoctorAdvice;

public class DoctorAdviceAddActivity extends CommonActivity implements OnTouchListener,OnItemSelectedListener{
	
	private Button btnBack;
	private Button btnSave;
	private TextView textName;
	private DoctorAdvice model;
	private Spinner spinner;
	private ArrayAdapter arrayAdapter;
	private DiseaseCategory diseaseSelected;
	String[] datas;
	List diseasesList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_doctoradviceadd);
		initUI();
		initDatas();
	}
	private void initUI(){
		btnBack=(Button) findViewById(R.id.doctorAdviceAddBack);
		btnSave=(Button) findViewById(R.id.doctorAdviceSave);
		textName=(TextView) findViewById(R.id.textName);
		spinner=(Spinner) findViewById(R.id.doctorAdviceAddPinner);
		btnBack.setOnTouchListener(this);
		btnSave.setOnTouchListener(this);
	}
	
	private void initDatas(){
		Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        DiseaseCategory currentCateggory=null;
        if(null!=bundle){
            model= (DoctorAdvice)bundle.get("model");
            if(null!=model){
            	textName.setText(model.getFdName()); 
            	DiseaseCategoryDao diseaseDao=new DiseaseCategoryDao(this);
            	currentCateggory=diseaseDao.get(model.getFdDiseaseId());
            }
            		
        }
        int defaultPosition=-1;
        DiseaseCategoryDao dao=new DiseaseCategoryDao(this);
		diseasesList=dao.find();
		datas=new String[diseasesList.size()];
		for(int i=0;i<diseasesList.size();i++){
			DiseaseCategory disease=(DiseaseCategory)diseasesList.get(i);
			datas[i]=disease.getFdName();
			if(null!=currentCateggory&&disease.getFdId()==currentCateggory.getFdId())
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
		case R.id.doctorAdviceAddBack:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				Intent intent = new Intent(this, DoctorAdviceActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;
		case R.id.doctorAdviceSave:
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
		DoctorAdviceDao dao=new DoctorAdviceDao(this);
		if(null!=model){
			model.setFdName(textName.getText().toString());
			dao.update(model);
			model.setFdDiseaseId(diseaseSelected.getFdId());
			dao.update(model);
		}else{
			DoctorAdvice doctorAdvice=new DoctorAdvice();
			doctorAdvice.setFdName(textName.getText().toString());
			doctorAdvice.setFdDiseaseId(diseaseSelected.getFdId());
			dao.insert(doctorAdvice);		
		}
		Intent intent = new Intent(this, DoctorAdviceActivity.class);
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
