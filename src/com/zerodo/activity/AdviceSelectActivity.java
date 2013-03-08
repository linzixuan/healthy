package com.zerodo.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.SelectListApapter;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.db.dao.DiseaseCategoryDao;
import com.zerodo.db.dao.DoctorAdviceDao;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DoctorAdvice;

public class AdviceSelectActivity extends CommonActivity implements OnTouchListener,OnItemClickListener,OnItemSelectedListener {
	private ListView adviceList;
	private List datas,adviceSelectedList,diseasesList;;
	private SelectListApapter adviceApapter;
	private Button btnBack;
	private Button btnSave;
	private DiseaseCategory diseaseSelected;
	
	private Spinner diseaseSpinner;
	private ArrayAdapter arrayAdapter;
	String[] diseaseDatas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_adviceselect);
		initUI();
		initDatas();
	}
	private void initUI(){
		adviceList=(ListView) findViewById(R.id.adviceList);
		adviceList.setOnItemClickListener(this);
		//diseaseFeaturesList.setOnCreateContextMenuListener(this);
		btnBack=(Button) findViewById(R.id.adviceBack);
		btnSave=(Button) findViewById(R.id.adviceSave);
		btnBack.setOnTouchListener(this);
		btnSave.setOnTouchListener(this);
		diseaseSpinner=(Spinner) findViewById(R.id.diseaseSpinner);
	}
	private void initDatas(){
		Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        String fdDisease=bundle.getString("fdDisease");
        DiseaseCategoryDao diseaseDao=new DiseaseCategoryDao(this);
        List list=diseaseDao.find(null, "fd_name='"+fdDisease+"'", null, null, null, null, null);
        DiseaseCategory initDisease = null;
        if(!list.isEmpty()){
        	initDisease=(DiseaseCategory) list.get(0);
        }
		int defaultPosition=-1;
		diseasesList=diseaseDao.find();
		diseaseDatas=new String[diseasesList.size()];
		for(int i=0;i<diseasesList.size();i++){
			DiseaseCategory disease=(DiseaseCategory)diseasesList.get(i);
			diseaseDatas[i]=disease.getFdName();
			if(null!=initDisease&&disease.getFdId()==initDisease.getFdId())
				defaultPosition=i;
		}
		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, diseaseDatas); 
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		diseaseSpinner.setAdapter(arrayAdapter);  
		diseaseSpinner.setOnItemSelectedListener(this);  
		diseaseSpinner.setVisibility(View.VISIBLE);  
		if(defaultPosition!=-1)
			diseaseSpinner.setSelection(defaultPosition);


	}
	
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.adviceBack:
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
		case R.id.adviceSave:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				String advice="";
				for(int i=0;i<adviceSelectedList.size();i++){
					DoctorAdvice model=(DoctorAdvice) adviceSelectedList.get(i);
					advice+=model.getFdName();
					if(i<adviceSelectedList.size()-1){
						advice+=";";
					}
				}
				Intent intent=this.getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("advice", advice);
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
			adviceSelectedList.remove(datas.get(index));
		}else{
			if(!adviceSelectedList.contains(datas.get(index))){
				adviceSelectedList.add(datas.get(index));
			}
		}
		checkBox.toggle();
		SelectListApapter.getIsSelected().put(index,checkBox.isChecked()); 
	}
	public void onItemSelected(AdapterView<?> adapter, View view, int arg2,
			long arg3) {
		int index = adapter.getSelectedItemPosition();
		diseaseSelected=(DiseaseCategory)diseasesList.get(index);
		DoctorAdviceDao dao=new DoctorAdviceDao(this);
		datas=dao.find(null, "fd_disease_id='"+diseaseSelected.getFdId()+"'", null, null, null, null, null);
		adviceApapter=new SelectListApapter(this,datas);
		adviceList.setAdapter(adviceApapter);
		adviceSelectedList=adviceApapter.getSelectedList();
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
