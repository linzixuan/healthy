package com.zerodo.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.zerodo.adapter.ExpandableAdapter;
import com.zerodo.adapter.SelectListApapter;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.common.CommonModel;
import com.zerodo.base.util.PingyinComparator;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.PatientInfo;

public class ImportContactsActivity extends CommonActivity implements
		OnItemClickListener, OnTouchListener,OnClickListener {
	private final String TAG = this.getClass().getName();
	private ListView contactsList;
	private List datas=new ArrayList();
	private List<CommonModel> contactsSelectList=new ArrayList();
	private SelectListApapter contactApapter;
	private Button btnBack;
	private Button btnSave;
	private Button searchBtn;
	private Button btnAll;
	private AutoCompleteTextView autoCompleteTextView;
	private boolean selectAll=false;
	
	/**获取库Phon表字段**/  
    private static final String[] PHONES_PROJECTION = new String[] {  
        Phone.DISPLAY_NAME, Phone.NUMBER}; 
    /**联系人显示名称**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**电话号码**/  
    private static final int PHONES_NUMBER_INDEX = 1;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importcontacts);
		initUI();
		initData();
	}

	private void initUI() {
		contactsList = (ListView) findViewById(R.id.contactsList);
		contactsList.setOnItemClickListener(this);
		contactsList.setFastScrollEnabled(true);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnAll=(Button) findViewById(R.id.btnAll);
		btnBack.setOnTouchListener(this);
		btnSave.setOnTouchListener(this);
		btnAll.setOnTouchListener(this);
		autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.searchText);
		searchBtn=(Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);
	}

	private void initData() {
		// 得到ContentResolver对象
		ContentResolver cr = this.getContentResolver();
 
		// 取得电话本中开始一项的光标
		Cursor cursor = cr.query(Phone.CONTENT_URI,PHONES_PROJECTION,null, null, Phone.DISPLAY_NAME);
		// 向下移动光标
		while (cursor.moveToNext()) {
			
			//得到手机号码   
	        String phoneNumber = cursor.getString(PHONES_NUMBER_INDEX);  
	        //当手机号码为空的或者为空字段 跳过当前循环   
	        if (StringUtil.isNull(phoneNumber))  
	            continue;  
	        //得到联系人名称   
	        String contactName = cursor.getString(PHONES_DISPLAY_NAME_INDEX);  
			CommonModel model=new CommonModel();
			model.setFdName(contactName+"|"+phoneNumber);
			datas.add(model);
		}
		cursor.close();
		datas=sortContacts(datas);
		contactApapter=new SelectListApapter(this,datas);
		contactsList.setAdapter(contactApapter);
	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.btnBack:
			switch (event.getAction()) {
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
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnSave.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				dialog();
				break;
			}
			break;
		case R.id.btnAll:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				btnAll.setBackgroundResource(R.drawable.common_titlebar_common_btn_selected);
				break;
			case MotionEvent.ACTION_UP:
				btnAll.setBackgroundResource(R.drawable.common_titlebar_common_btn_normal);
				if(!selectAll){
					for(int i=0,size=datas.size();i<size;i++){
						if (!contactsSelectList.contains(datas.get(i))) {
							contactsSelectList.add((CommonModel) datas.get(i));
							SelectListApapter.getIsSelected().put(i,true);
						}	
					}	
					for(int index=0,size=contactsList.getChildCount();index<size;index++){
						LinearLayout layout=(LinearLayout) contactsList.getChildAt(index);
						CheckBox checkBox = (CheckBox) layout.findViewById(R.id.commonCheckBox);
						checkBox.setChecked(true);
					}
					selectAll=true;
					btnAll.setText("清空");
				}else{
					for(int i=0,size=datas.size();i<size;i++){
						SelectListApapter.getIsSelected().put(i,false);
					}
					contactsSelectList=new ArrayList<CommonModel>();
					for(int index=0,size=contactsList.getChildCount();index<size;index++){
						LinearLayout layout=(LinearLayout) contactsList.getChildAt(index);
						CheckBox checkBox = (CheckBox) layout.findViewById(R.id.commonCheckBox);
						checkBox.setChecked(false);
					}
					selectAll=false;
					btnAll.setText("全选");
				}

				break;
			}
			break;

		}
		return true;
	}
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.searchBtn:
			datas.removeAll(datas);
			ContentResolver cr = this.getContentResolver();
			String searchCondition=autoCompleteTextView.getText().toString();
			String whereBlock=null;
			if(StringUtil.isNotNull(searchCondition)){
				whereBlock=Phone.NUMBER+" LIKE"+" '%"+ searchCondition +"%'"+" OR "+Phone.DISPLAY_NAME+" LIKE"+" '%"+searchCondition+"%'";
			}
			Cursor cursor = cr.query(Phone.CONTENT_URI,PHONES_PROJECTION,whereBlock, null, Phone.DISPLAY_NAME);
			while (cursor.moveToNext()) {
		        String phoneNumber = cursor.getString(PHONES_NUMBER_INDEX);  
		        if (StringUtil.isNull(phoneNumber))  
		            continue;  
		        String contactName = cursor.getString(PHONES_DISPLAY_NAME_INDEX);  
				CommonModel model=new CommonModel();
				model.setFdName(contactName+"|"+phoneNumber);
				datas.add(model);
			}
			cursor.close();
			datas=sortContacts(datas);
			contactApapter.setDatas(datas);
			contactApapter.notifyDataSetChanged();
			break;
		}
	}
	
	public void onItemClick(AdapterView adapterView, View view, int index,
			long arg3) {
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.commonCheckBox);
		if (checkBox.isChecked()) {
			contactsSelectList.remove(datas.get(index));
		} else {
			if (!contactsSelectList.contains(datas.get(index))) {
				contactsSelectList.add((CommonModel) datas.get(index));
			}
		}
		checkBox.toggle();
		SelectListApapter.getIsSelected().put(index,checkBox.isChecked()); 
		
	}
	private void dialog() { 
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("您确定导入选择的联系人到患者吗？"); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		new SaveTask(ImportContactsActivity.this).execute("saveAdd");
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
	
	private List sortContacts(List datas){
		PingyinComparator comparator=new PingyinComparator();
		Collections.sort(datas,comparator);
		return datas;
	}
	
	class SaveTask extends AsyncTask<String, Void, Integer>{
    	private AlertDialog.Builder builder;
    	private Dialog dialog;
    	private Context myContext;
    	private int count=0;
    	private int existsCount=0;
    	
    	public SaveTask(Context myContext) {
    		this.myContext = myContext;
    		builder = new Builder(myContext);
    		builder.setMessage("导入中。。。请稍后");
    		dialog = builder.show();
		}
    	
		@Override
		protected Integer doInBackground(String... arg0) {
			try{
				for(CommonModel model:contactsSelectList){
					PatientInfo patient;
					PatientInfoDao dao=new PatientInfoDao(myContext);
					String[] array=model.getFdName().split("\\|");
					if(array.length>=2&&!findExistsPatient(array[0], array[1])){
						patient=new PatientInfo();
						patient.setFdName(array[0]);
						patient.setFdPhone(array[1]);
						dao.insert(patient);
						count++;
					}
				}
				return 1;				
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}

		}
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 1:
				if(existsCount>0){
					Toast.makeText(myContext,"成功导入了"+count+"个联系人,其中"+existsCount+"个已经存在，没有导入。", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(myContext,"成功导入了"+count+"个联系人", Toast.LENGTH_SHORT).show();	
				}
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
		
		private boolean findExistsPatient(String name,String phone){
			boolean rtnValue=false;
			PatientInfoDao patientInfoDao=new PatientInfoDao(myContext);
			String whereBlock="fd_name='"+name+"' and fd_phone='"+phone+"'";
			List existsPatient=patientInfoDao.find(null,whereBlock , null, null, null, null, null);
			if(!existsPatient.isEmpty()){
				existsCount++;
				rtnValue=true;
			}
			return rtnValue;
		}
	}
}
