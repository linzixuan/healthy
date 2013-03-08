package com.zerodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.DepartmentDao;
import com.zerodo.db.dao.EducationalDao;
import com.zerodo.db.model.Department;
import com.zerodo.db.model.DiseaseFeatures;
import com.zerodo.db.model.Educational;

public class EducationalAddActivity extends CommonActivity implements OnTouchListener{
	
	private Button btnBack;
	private Button btnSave;
	private TextView textName;
	private Educational model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_basedataadd);
		initUI();
	}
	public void initUI(){
		btnBack=(Button) findViewById(R.id.baseAddBack);
		btnSave=(Button) findViewById(R.id.baseSave);
		textName=(TextView) findViewById(R.id.textName);
		Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(null!=bundle){
            model= (Educational)bundle.get("model");
            if(null!=model)
            	textName.setText(model.getFdName());      	
        }
		btnBack.setOnTouchListener(this);
		btnSave.setOnTouchListener(this);
	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.baseAddBack:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnBack.setBackgroundResource(R.drawable.common_titlebar_back_btn_normal);
				Intent intent = new Intent(this, EducationalActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}
			break;
		case R.id.baseSave:
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
		EducationalDao dao=new EducationalDao(this);
		if(null!=model){
			model.setFdName(textName.getText().toString());
			dao.update(model);
		}else{
			Educational educational=new Educational();
			educational.setFdName(textName.getText().toString());
			dao.insert(educational);		
		}
		Intent intent = new Intent(this, EducationalActivity.class);
		startActivity(intent);
		this.finish();	
	}
	private boolean checkForm(){
		boolean rtnValue=true;
		if(StringUtil.isNull(textName.getText().toString())){
			Toast.makeText(this,"名称不能为空！请您重新输入名称。", Toast.LENGTH_SHORT).show();
			rtnValue=false;
		}
		return rtnValue;
	}
}
