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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zerodo.HealthyApplication;
import com.zerodo.adapter.MedicalListApapter;
import com.zerodo.base.util.SDFileUtil;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.model.MedicalCase;

public class MedicalCaseActivity extends TabActivity implements OnTouchListener,OnClickListener,OnItemLongClickListener,OnScrollListener,OnItemClickListener{
	private Button medicalCaseAddBtn,searchBtn;
	private MedicalListApapter medicalListApapter;
	private ListView medicalListView;
	private MedicalCase medicalCase;
	private AutoCompleteTextView autoCompleteTextView;
	//更多、分页
	private View moreView;     // ListView底部View 
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
		setContentView(R.layout.activity_medicalcase);
		initUI();
		initDataOnCreate();
		initListFoot();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initDataonResume();
	}

	private void initUI(){
		//medicalCaseAddBtn=(Button) findViewById(R.id.medicalCaseAddBtn);
		//medicalCaseAddBtn.setOnTouchListener(this);
		medicalListView=(ListView) findViewById(R.id.medicalListView);
		medicalListView.setOnItemLongClickListener(this);
		medicalListView.setOnItemClickListener(this);
		medicalListView.setOnScrollListener(this);
		autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.caseSearchText);
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
	//数据初始化
	private void initDataonResume(){
		pageno=0;
		MedicalCaseDao medicalCaseDao =new MedicalCaseDao(this);
		String limit=pageno*rowsize+","+rowsize;
		List datas=medicalCaseDao.find(null, null, null, null, null, "fd_date desc", limit);
		pageno++;
		medicalListApapter=new MedicalListApapter(this,datas);
		
		if(datas.size()<rowsize){
			medicalListView.setAdapter(medicalListApapter);
			if(medicalListView.findViewById(R.id.morebutton)!=null&&medicalListView.findViewById(R.id.morebutton).getVisibility()==View.VISIBLE){
				medicalListView.removeFooterView(moreView);
			}
		}else{
			if(medicalListView.findViewById(R.id.morebutton)==null){
				medicalListView.addFooterView(moreView);
				morebutton.setVisibility(View.VISIBLE);
			}	
			medicalListView.setAdapter(medicalListApapter);	
		}
		
//		if(medicalListView.findViewById(R.id.morebutton)!=null){
//			if(datas.size()<rowsize)
				
			
//		}else{
//			if(datas.size()==rowsize){
//				medicalListView.addFooterView(moreView);
//				morebutton.setVisibility(View.VISIBLE);
//			}
//				
//		}
		
	}
	
	private void initListFoot(){
        // 实例化底部布局 
        moreView = getLayoutInflater().inflate(R.layout.medicallist_foot, null);  
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        morebutton=(Button) moreView.findViewById(R.id.morebutton);
        medicalListView.addFooterView(moreView); 
        handler = new Handler(); 
        pg.setVisibility(View.GONE);
        morebutton.setOnClickListener(this);
	}
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.searchBtn:
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
    		}, 1000); //延迟两1执行runnable	
    		break;
		}
	}
	private void search(){
		//查询
		pageno=0;
		MedicalCaseDao medicalCaseDao =new MedicalCaseDao(this);
		String searchCondition=autoCompleteTextView.getText().toString();
		String[] selectionArgs = {"%"+searchCondition+"%","%"+searchCondition+"%","%"+searchCondition+"%"};
		String limit=pageno*rowsize+","+rowsize;
		List datas=medicalCaseDao.find(null, "fd_patient_name like ? or fd_disease like ? or fd_date like ? ",selectionArgs, null, null, "fd_date desc", limit);
		medicalListApapter=new MedicalListApapter(this,datas);
		pageno++;
		if(medicalListView.findViewById(R.id.morebutton)!=null){
			if(datas.size()<rowsize)
				medicalListView.removeFooterView(moreView);
			
		}else{
			if(datas.size()==rowsize){
				medicalListView.addFooterView(moreView);
				morebutton.setVisibility(View.VISIBLE);
			}
		}
		medicalListView.setAdapter(medicalListApapter);
		search=true;	
	}
	
	private int loadMoreDate(){
		MedicalCaseDao medicalCaseDao =new MedicalCaseDao(this);
		String limit=pageno*rowsize+","+rowsize;
		List moreDatas;
		if(!search){
			moreDatas=medicalCaseDao.find(null, null, null, null, null, "fd_date desc", limit);
		}else{
			String searchCondition=autoCompleteTextView.getText().toString();
			String[] selectionArgs = {"%"+searchCondition+"%","%"+searchCondition+"%","%"+searchCondition+"%"};
			moreDatas=medicalCaseDao.find(null, "fd_patient_name like ? or fd_disease like ? or fd_date like ? ",selectionArgs, null, null, "fd_date desc", limit);
		}
		medicalListApapter.getDatas().addAll(moreDatas);
		medicalListApapter.notifyDataSetChanged();
		pageno++;
        if(moreDatas.size()<rowsize)
        	medicalListView.removeFooterView(moreView);
		return moreDatas.size();
	}
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
//		case R.id.medicalCaseAddBtn:
//			switch (event.getAction()){
//			case MotionEvent.ACTION_DOWN:
//				medicalCaseAddBtn.setBackgroundResource(R.drawable.icon_case_add_select);
//				break;
//			case MotionEvent.ACTION_UP:
//				medicalCaseAddBtn.setBackgroundResource(R.drawable.icon_case_add_normal);
//				Intent medicalCaseAddActivity = new Intent(this, MedicalCaseAddActivity.class);
//				startActivity(medicalCaseAddActivity);
//				break;
//			}
//			break;
		}
		return true;
	}
	public boolean onItemLongClick(AdapterView adapterView, View view, int index,
			long arg3) {
			medicalCase=(MedicalCase)medicalListApapter.getDatas().get(index);
			medicalListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {                                        
	        	public void onCreateContextMenu(ContextMenu menu, View v,                                                       
	        			ContextMenuInfo menuInfo) {                                               
	        		menu.add(0, 0, 0, "编辑");                                               
	        		menu.add(0, 1, 0, "删除");                                                
	        		menu.add(0, 2, 0, "导出");  
	        		menu.add(0, 3, 0, "收藏"); 
	        		}                               
	        	});	
			return false;
	}
	
	public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
		Bundle bundle = new Bundle();
		Intent it=new Intent(this,MedicalCaseAddActivity.class);
		bundle.putSerializable("medicalCase",medicalListApapter.getDatas().get(index));
		it.putExtras(bundle);
		startActivity(it);
		
	}
	 public void onScroll(AbsListView view, int firstVisiableItem,int visibleItemCount, int totalItemCount) {
	    	// 所有的条目已经和最大条数相等，则移除底部的View 
	    	if(removeFoot) { 
	    		removeFoot=false;
	    		//medicalListView.removeFooterView(moreView); 
	    	} 
	    } 
	// 长按菜单响应函数        
	public boolean onContextItemSelected(MenuItem item) { 
		MedicalCaseDao dao=new MedicalCaseDao(this);
		switch (item.getItemId()) {               
			case 0:                       
				Bundle bundle = new Bundle();
				Intent it=new Intent(this,MedicalCaseAddActivity.class);
				bundle.putSerializable("medicalCase",medicalCase);
				it.putExtras(bundle);
				startActivity(it);
			break;                 
			case 1:  
				dialog(medicalCase);
				break;                 
			case 2:                        
				// 导出                     
				break; 
			case 3:                        
				medicalCase.setFdIsFavor(1);
				dao.update(medicalCase);
				Toast.makeText(this,"收藏成功", Toast.LENGTH_SHORT).show();
				break;                
			}                 
		return super.onContextItemSelected(item); 
	}
	
	private void dialog(final MedicalCase medicalCase) { 
		 final MedicalCaseDao dao=new MedicalCaseDao(this);
		 AlertDialog.Builder builder = new Builder(this); 
		 builder.setMessage("你确定删除该病历?"); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
			 		dao.execSQL("delete from table_attachment where fd_model_name='com.zerodo.db.model.MedicalCase' and fd_main_id='"+medicalCase.getFdId()+"'");
					dao.delete(medicalCase.getFdId()); 
					medicalListApapter.delete(medicalCase);
					medicalListApapter.notifyDataSetChanged();
					Toast.makeText(MedicalCaseActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
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
	
	
	private String[] generateArray(){
		String []rtnArray=null;
		MedicalCaseDao dao=new MedicalCaseDao(this);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			String sql="select fd_patient_name as name from table_medical_case union all select distinct fd_disease as name from table_medical_case union all select distinct fd_date as name from table_medical_case";
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

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
