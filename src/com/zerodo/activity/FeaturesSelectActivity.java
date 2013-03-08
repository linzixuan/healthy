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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.SelectListApapter;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.db.dao.DiseaseCategoryDao;
import com.zerodo.db.dao.DiseaseFeaturesDao;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DiseaseFeatures;

public class FeaturesSelectActivity extends CommonActivity implements OnTouchListener,OnCreateContextMenuListener,OnItemClickListener,OnItemSelectedListener {
	private ListView diseaseFeaturesList;
	private List datas,featuresSelectedList,diseasesList;
	private SelectListApapter diseaseFeaturesApapter;
	private Button btnBack;
	private Button btnNew;
	private DiseaseCategory diseaseSelected;
	
	private Spinner diseaseSpinner;
	private ArrayAdapter arrayAdapter;
	String[] diseaseDatas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_featuresselect);
		initUI();
		initDatas();
	}
	private void initUI(){
		diseaseFeaturesList=(ListView) findViewById(R.id.diseaseFeaturesList);
		diseaseFeaturesList.setOnItemClickListener(this);
		//diseaseFeaturesList.setOnCreateContextMenuListener(this);
		btnBack=(Button) findViewById(R.id.diseaseFeaturesBack);
		btnNew=(Button) findViewById(R.id.diseaseFeaturesNew);
		btnBack.setOnTouchListener(this);
		btnNew.setOnTouchListener(this);
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
	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v,   //长按弹出菜单
	   ContextMenuInfo menuInfo) {
	  menu.add(0, 0, 0, "删除");
	  menu.add(0, 1, 0, "编辑");
	  super.onCreateContextMenu(menu, v, menuInfo);
	 } 

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo menuInfo = (ContextMenuInfo) item.getMenuInfo();   
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item .getMenuInfo();  
		DiseaseFeatures model=(DiseaseFeatures) diseaseFeaturesApapter.getItem(info.position);
		DiseaseFeaturesDao dao=new DiseaseFeaturesDao(this);
		switch(item.getItemId()){
			case 0:
				dialog(model);
				break;
			case 1:
				Bundle bundle = new Bundle();
				Intent it=new Intent(this,DiseaseFeaturesAddActivity.class);
				bundle.putSerializable("model",model);
				it.putExtras(bundle);
				startActivity(it);
				break;

		}
		return super.onContextItemSelected(item);
	}
	private void dialog(final DiseaseFeatures model) { 
		 final DiseaseFeaturesDao dao=new DiseaseFeaturesDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("你确定删除该记录?"); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					dao.delete(model.getFdId());  
					diseaseFeaturesApapter.delete(model);
					diseaseFeaturesApapter.notifyDataSetChanged();
					Toast.makeText(FeaturesSelectActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
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
		case R.id.diseaseFeaturesBack:
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
		case R.id.diseaseFeaturesNew:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnNew.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				String features="";
				for(int i=0;i<featuresSelectedList.size();i++){
					DiseaseFeatures model=(DiseaseFeatures) featuresSelectedList.get(i);
					features+=model.getFdName();
					if(i<featuresSelectedList.size()-1){
						features+=";";
					}
				}
				Intent intent=this.getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("features", features);
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
			featuresSelectedList.remove(datas.get(index));
		}else{
			if(!featuresSelectedList.contains(datas.get(index))){
				featuresSelectedList.add(datas.get(index));
			}

		}
		checkBox.toggle();
		SelectListApapter.getIsSelected().put(index,checkBox.isChecked()); 
	}
	public void onItemSelected(AdapterView<?> adapter, View view, int arg2,
			long arg3) {
		int index = adapter.getSelectedItemPosition();
		diseaseSelected=(DiseaseCategory)diseasesList.get(index);
		DiseaseFeaturesDao dao=new DiseaseFeaturesDao(this);
		datas=dao.find(null, "fd_disease_id='"+diseaseSelected.getFdId()+"'", null, null, null, null, null);
		diseaseFeaturesApapter=new SelectListApapter(this,datas);
		diseaseFeaturesList.setAdapter(diseaseFeaturesApapter);
		featuresSelectedList=diseaseFeaturesApapter.getSelectedList();
		
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
