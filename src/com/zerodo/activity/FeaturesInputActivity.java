package com.zerodo.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.zerodo.base.common.CommonActivity;

public class FeaturesInputActivity extends CommonActivity implements OnTouchListener {
	private Button btnBack,btnSave,btnSelect;
	private EditText fdContent;
	String fdDisease;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_featuresinput);
		initUI();
		initDatas();
	}
	
	private void initUI(){
		btnBack=(Button) findViewById(R.id.btnBack);
		btnBack.setOnTouchListener(this);
		btnSave=(Button) findViewById(R.id.btnSave);
		btnSave.setOnTouchListener(this);
		btnSelect=(Button) findViewById(R.id.btnSelect);
		btnSelect.setOnTouchListener(this);
		fdContent=(EditText) findViewById(R.id.fdContent);
	}
	private void initDatas(){
		Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        fdDisease=bundle.getString("fdDisease");
        String fdFeatures=bundle.getString("fdFeatures");
        fdContent.setText(fdFeatures);
        CharSequence text = fdContent.getText();
        if (text instanceof Spannable) {
        	Spannable spanText = (Spannable)text;
        	Selection.setSelection(spanText, text.length());
        }

	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.btnBack:
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
		case R.id.btnSave:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				String features=fdContent.getText().toString();
				Intent intent=this.getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("features", features);
				intent.putExtras(bundle);
				this.setResult(0, intent);
				this.finish();
				break;
			}
			break;
		case R.id.btnSelect:
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				btnSelect.setBackgroundResource(R.drawable.add_button_select);
				break;
			case MotionEvent.ACTION_UP:
				btnSelect.setBackgroundResource(R.drawable.add_button_normal);
				Intent it=new Intent(this, FeaturesSelectActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("fdDisease", fdDisease);
				it.putExtras(bundle);
				startActivityForResult(it, 0);
				break;
			}
			break;

		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		if(null==data)
			return;
		if (requestCode == 0) {
			try {
				Bundle bundle=data.getExtras();
				if(null!=bundle){
					int index=fdContent.getSelectionStart();
					fdContent.getText().insert(index, bundle.getString("features"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
	
}
