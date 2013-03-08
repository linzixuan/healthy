package com.zerodo.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.ExpandableAdapter;
import com.zerodo.adapter.MedicalListApapter;
import com.zerodo.base.common.CommonActivity;
import com.zerodo.base.util.SDFileUtil;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;

public class PatientActivity extends TabActivity implements OnTouchListener,OnClickListener,OnItemLongClickListener,OnChildClickListener {

	private ExpandableListView expandListView;
	private Button searchBtn;
	private ExpandableAdapter adapter;
	private PatientInfo patientSelected;
	private AutoCompleteTextView autoCompleteTextView;
	//���ࡢ��ҳ
	private View moreView;     // ListView�ײ�View 
    private Handler handler;    
    private ProgressBar pg;
    private Button morebutton;
    private final static int rowsize=20;
    private static int pageno=0;
    private boolean removeFoot=false; 
    private boolean search=false; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HealthyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_patient);
		initUI();
		initDataOnCreate();
		initListFoot();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initDataOnResume();
	}

	private void initUI(){
		expandListView=(ExpandableListView) findViewById(R.id.expandableListView);
		expandListView.setOnItemLongClickListener(this);
		expandListView.setOnChildClickListener(this);
		expandListView.setCacheColorHint(0);
		//patientAddBtn=(Button) findViewById(R.id.patientAddBtn);
		//patientAddBtn.setOnTouchListener(this);
		autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.patientSearchText);
		searchBtn=(Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);

	}
	
	private void initDataOnCreate(){
		String [] autoString=generateArray();
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				autoString);
		autoCompleteTextView.setAdapter(adapter);	
	}
	
	private void initDataOnResume(){
		pageno=0;
		PatientInfoDao dao=new PatientInfoDao(this);
		String limit=pageno*rowsize+","+rowsize;
		List datas=dao.find(null, null, null, null, null, "fd_name desc", limit);
		adapter = new ExpandableAdapter(this,datas);
		pageno++;
		if(datas.size()<rowsize){
			expandListView.setAdapter(adapter);
			if(expandListView.findViewById(R.id.morebutton)!=null&&expandListView.findViewById(R.id.morebutton).getVisibility()==View.VISIBLE){
				expandListView.removeFooterView(moreView);
			}
		}else{
			if(expandListView.findViewById(R.id.morebutton)==null){
				expandListView.addFooterView(moreView);
				morebutton.setVisibility(View.VISIBLE);
			}	
			expandListView.setAdapter(adapter);
		}
		
	}
	private void initListFoot(){
        // ʵ�����ײ����� 
        moreView = getLayoutInflater().inflate(R.layout.medicallist_foot, null);  
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        morebutton=(Button) moreView.findViewById(R.id.morebutton);
        expandListView.addFooterView(moreView); 
        handler = new Handler(); 
        pg.setVisibility(View.GONE);
        morebutton.setOnClickListener(this);
	}
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.searchBtn:
//			PatientInfoDao patientInfoDao =new PatientInfoDao(this);
//			String searchCondition=autoCompleteTextView.getText().toString();
//			String[] selectionArgs = {"%"+searchCondition+"%","%"+searchCondition+"%"};
//			List datas=patientInfoDao.find(null, "fd_name like ? or fd_phone like ? ",selectionArgs, null, null, "fd_id desc", null);
//			adapter=new ExpandableAdapter(this,datas);
//			expandListView.setAdapter(adapter);
			search();
			break;
		case R.id.morebutton:
			morebutton.setVisibility(View.GONE);
    		pg.setVisibility(View.VISIBLE); 
    		handler.postDelayed(new Runnable() { 
    			public void run() { 
    				int newDataNum=loadMoreDate(); 
    		        if(newDataNum==rowsize){
    		        	morebutton.setVisibility(View.VISIBLE);
    		        }
    		        pg.setVisibility(View.GONE); 
    		        	
    			} 
    		}, 1000); //�ӳ�1��ִ��runnable	
    		break;
		}
	}
	
	private void search(){
		//��ѯ
		pageno=0;
		PatientInfoDao patientInfoDao =new PatientInfoDao(this);
		String searchCondition=autoCompleteTextView.getText().toString();
		String[] selectionArgs = {"%"+searchCondition+"%","%"+searchCondition+"%"};
		String limit=pageno*rowsize+","+rowsize;
		List datas=patientInfoDao.find(null, "fd_name like ? or fd_phone like ? ",selectionArgs, null, null, "fd_name desc", limit);
		adapter=new ExpandableAdapter(this,datas);
		pageno++;
		if(expandListView.findViewById(R.id.morebutton)!=null){
			if(datas.size()<rowsize)
				expandListView.removeFooterView(moreView);
			
		}else{
			if(datas.size()==rowsize){
				expandListView.addFooterView(moreView);
				morebutton.setVisibility(View.VISIBLE);
			}
		}
		expandListView.setAdapter(adapter);
		search=true;	
	}
	private int loadMoreDate(){
		PatientInfoDao patientInfoDao =new PatientInfoDao(this);
		String limit=pageno*rowsize+","+rowsize;
		List moreDatas;
		if(!search){
			moreDatas=patientInfoDao.find(null, null, null, null, null, "fd_id desc", limit);
		}else{
			String searchCondition=autoCompleteTextView.getText().toString();
			String[] selectionArgs = {"%"+searchCondition+"%","%"+searchCondition+"%"};
			moreDatas=patientInfoDao.find(null, "fd_name like ? or fd_phone like ? ",selectionArgs, null, null, "fd_name desc", limit);
		}
		adapter.getDatas().addAll(moreDatas);
		adapter.notifyDataSetChanged();
		pageno++;
        if(moreDatas.size()<rowsize)
        	expandListView.removeFooterView(moreView);
		return moreDatas.size();
	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
//		case R.id.patientAddBtn:
//			switch (event.getAction()){
//			case MotionEvent.ACTION_DOWN:
//				patientAddBtn.setBackgroundResource(R.drawable.icon_patient_add_select);
//				break;
//			case MotionEvent.ACTION_UP:
//				patientAddBtn.setBackgroundResource(R.drawable.icon_patient_add_normal);
//				Intent patientAddActivity = new Intent(this, PatientAddActivity.class);
//				startActivity(patientAddActivity);
//				break;
//			}
//			break;	
		}
		
		return true;
	}

	public boolean onItemLongClick(AdapterView adapterView, final View view, final int index,
			long arg3) {
			expandListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {                                        
	        	public void onCreateContextMenu(ContextMenu menu, View v,                                                       
	        			ContextMenuInfo menuInfo) {  
	        		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
	                int type = ExpandableListView.getPackedPositionType(info.packedPosition); 
	                if(type == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
	                	patientSelected=(PatientInfo)adapter.getGroup((Integer) view.getTag(R.id.expand_view_tagId));
		        		menu.add(0, 0, 0, "�༭");                                               
		        		menu.add(0, 1, 0, "ɾ��");  
		        		menu.add(0, 2, 0, "��Ӳ���");
		        		menu.add(0, 3, 0, "����");  
		        		menu.add(0, 4, 0, "�ղ�"); 
		        		} 
	        		}
	        	});	
			return false;
	}
	 // �����˵���Ӧ����        
	public boolean onContextItemSelected(MenuItem item) { 
		PatientInfoDao dao=new PatientInfoDao(this);
		Bundle bundle =null;
		Intent it=null;
		switch (item.getItemId()) {               
			case 0:                       
				bundle = new Bundle();
				it=new Intent(this,PatientAddActivity.class);
				bundle.putSerializable("model",patientSelected);
				it.putExtras(bundle);
				startActivity(it);
			break;                 
			case 1:        
				dialog(patientSelected);
				break;
			case 2:  
				bundle = new Bundle();
				it=new Intent(this,MedicalCaseAddActivity.class);
				bundle.putSerializable("patientInfo",patientSelected);
				it.putExtras(bundle);
				startActivity(it);
				break; 
			case 3:                        
				// ����                     
				break; 
			case 4:                        
				patientSelected.setFdIsFavor(1);
				dao.update(patientSelected);
				Toast.makeText(this,"�ղسɹ�", Toast.LENGTH_SHORT).show();
				break;                
			}                 
		return super.onContextItemSelected(item); 
	}
	public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition,
			int childPosition, long id) {
		MedicalCase medicalCase=(MedicalCase) adapter.getChild(groupPosition, childPosition);
		Bundle bundle = new Bundle();
		Intent it=new Intent(this,MedicalCaseAddActivity.class);
		bundle.putSerializable("medicalCase",medicalCase);
		it.putExtras(bundle);
		startActivity(it);
		return false;
	}	
	private void dialog(final PatientInfo patientInfo) { 
		 final PatientInfoDao dao=new PatientInfoDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("��ȷ��ɾ���û�����Ϣ?"); 
		 builder.setTitle("��ʾ"); 
		 builder.setPositiveButton("ȷ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 		dao.execSQL("delete from table_medical_case where fd_patient_id='"+patientInfo.getFdId()+"'");
					dao.delete(patientInfo.getFdId()); 
					adapter.delete(patientInfo);
					adapter.notifyDataSetChanged();
					Toast.makeText(PatientActivity.this,"ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			 	} 
		 }); 
		 builder.setNegativeButton("ȡ��", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 	} 
		 }); 
		 builder.create().show(); 
	 }
	
	private String[] generateArray(){
		String []rtnArray=null;
		PatientInfoDao dao=new PatientInfoDao(this);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			String sql="select fd_name as name from table_patient_info union all select distinct fd_phone as name from table_patient_info";
			db =dao.getDbHelper().getWritableDatabase();
			cursor=(Cursor) dao.query(sql, null);
			rtnArray=new String[cursor.getCount()];
			int arrayIndex=0;
			while (cursor.moveToNext()) {
				rtnArray[arrayIndex]=cursor.getString(cursor.getColumnIndex("name"));
				arrayIndex++;
			}	
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return rtnArray;
	}
}
