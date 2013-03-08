package com.zerodo.activity;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class BaseDataActivity extends CommonActivity implements OnTouchListener,OnClickListener {
	//����
	private LinearLayout layoutDiseaseCategory;
	//����
	private LinearLayout layoutDiseaseFeatures;
	//ҽ��
	private LinearLayout layoutDoctorAdvice;
	//����
	private LinearLayout layoutDepartment;
	//ѧ��
	private LinearLayout layoutEducation;
	//ְ��
	private LinearLayout layoutTitles;
	//����ͨѶ¼
	private LinearLayout layoutImport;
	
	private Button btnBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_basedata);
		initUI();
	}
	private void initUI(){
		btnBack=(Button) findViewById(R.id.baseBack);
		layoutDiseaseCategory=(LinearLayout) findViewById(R.id.layoutDiseaseCategory);
		layoutDiseaseFeatures=(LinearLayout) findViewById(R.id.layoutDiseaseFeatures);
		layoutDoctorAdvice=(LinearLayout) findViewById(R.id.layoutDoctorAdvice);
		layoutDepartment=(LinearLayout) findViewById(R.id.layoutDepartment);
		layoutEducation=(LinearLayout) findViewById(R.id.layoutEducation);
		layoutTitles=(LinearLayout) findViewById(R.id.layoutTitles);
		layoutImport=(LinearLayout) findViewById(R.id.layoutImport);
		btnBack.setOnTouchListener(this);
		layoutDiseaseCategory.setOnClickListener(this);
		layoutDiseaseFeatures.setOnClickListener(this);
		layoutDoctorAdvice.setOnClickListener(this);
		layoutDepartment.setOnClickListener(this);
		layoutEducation.setOnClickListener(this);
		layoutTitles.setOnClickListener(this);
		layoutImport.setOnClickListener(this);
	}
	public void onClick(View view) {
		Intent it;
		switch (view.getId()) {
		case R.id.layoutDiseaseCategory:
				layoutDiseaseCategory.setBackgroundResource(R.drawable.setting_top_normal);
				it=new Intent(this, DiseaseCategoryActivity.class);
				startActivity(it);
			break;
		case R.id.layoutDiseaseFeatures:
				layoutDiseaseFeatures.setBackgroundResource(R.drawable.setting_middle_normal);
				it=new Intent(this, DiseaseFeaturesActivity.class);
				startActivity(it);
			break;
		case R.id.layoutDoctorAdvice:
				layoutDoctorAdvice.setBackgroundResource(R.drawable.setting_bottom_normal);
				it=new Intent(this, DoctorAdviceActivity.class);
				startActivity(it);
			break;
		case R.id.layoutDepartment:
				layoutDepartment.setBackgroundResource(R.drawable.setting_top_normal);
				it=new Intent(this, DepartmentActivity.class);
				startActivity(it);
			break;
		case R.id.layoutEducation:
				layoutEducation.setBackgroundResource(R.drawable.setting_middle_normal);
				it=new Intent(this,EducationalActivity.class);
				startActivity(it);
			break;
		case R.id.layoutTitles:
				layoutTitles.setBackgroundResource(R.drawable.setting_bottom_normal);
				it=new Intent(this,TitlesActivity.class);
				startActivity(it);
			break;
		case R.id.layoutImport:
			layoutImport.setBackgroundResource(R.drawable.setting_bottom_normal);
			it=new Intent(this,ImportContactsActivity.class);
			startActivity(it);
		break;
		}
	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.baseBack:
			switch (event.getAction()){
			//���£��������á����ء��ı���ͼƬ
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			//�ɿ����������á����ء��ı���ͼƬ
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				//�������ʱ�رձ�ҳ��
				this.finish();
				break;
			}
			break;
		}
		return true;
	}
}
