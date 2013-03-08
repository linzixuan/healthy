package com.zerodo.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zerodo.activity.R;
import com.zerodo.base.util.StringUtil;
import com.zerodo.db.dao.MedicalCaseDao;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	List<Map<String, String>> groups;
	List datas;
	Map childsMap=new HashMap();

	/*
	 * 构造函数: 参数1:context对象 参数2:一级列表数据源 参数3:二级列表数据源
	 */
	public ExpandableAdapter(Context context, List datas) {
		this.datas = datas;
		this.context = context;
	}

	public Object getChild(int groupPosition, int childPosition) {
		List childs=(List) childsMap.get(groupPosition);
		return childs.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		if(null!=childsMap.get(groupPosition))
			return ((List) childsMap.get(groupPosition)).size(); 
		MedicalCaseDao dao=new MedicalCaseDao(context);
		int patientId=((PatientInfo)datas.get(groupPosition)).getFdId();
		String[] selectionArgs={String.valueOf(patientId)};
		List childs=dao.find(null, "fd_patient_id=?", selectionArgs, null, null, "fd_date desc", null);
		childsMap.put(groupPosition, childs);
		return childs.size();
	}

	public Object getGroup(int groupPosition) {
		return datas.get(groupPosition);
	}

	public int getGroupCount() {
		return datas.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 获取一级列表View对象
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		PatientInfo patientInfo = (PatientInfo) datas.get(groupPosition);
		GroupHolder groupHolder=null;
		if(convertView==null){
			groupHolder=new GroupHolder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (LinearLayout) layoutInflater.inflate(R.layout.home_list_item, null);
			groupHolder.patientName = (TextView) convertView.findViewById(R.id.patientName);
			groupHolder.patientPhone = (TextView) convertView.findViewById(R.id.patientPhone);
			groupHolder.patientAge=(TextView) convertView.findViewById(R.id.patientAge);
			groupHolder.medicalCount=(TextView) convertView.findViewById(R.id.medicalCount);
			groupHolder.haveRemark=(TextView) convertView.findViewById(R.id.haveRemark);
			groupHolder.imageView = (ImageView) convertView.findViewById(R.id.expandImage);
			convertView.setTag(groupHolder);
		}else{
			groupHolder=(GroupHolder) convertView.getTag();
		}
		groupHolder.patientName.setText(patientInfo.getFdName());
		groupHolder.patientAge.setText(String.valueOf(patientInfo.getFdAge()));
		if(StringUtil.isNotNull(patientInfo.getFdRemark())){
			groupHolder.haveRemark.setText("有特殊说明");
			groupHolder.haveRemark.setTextColor(context.getResources().getColor(R.color.red));
		}else{
			groupHolder.haveRemark.setText("无特殊说明");
			groupHolder.haveRemark.setTextColor(context.getResources().getColor(R.color.black));
		}
			
		if(isExpanded)
			groupHolder.imageView.setImageResource(R.drawable.patient_detail_select);
		else
			groupHolder.imageView.setImageResource(R.drawable.patient_detail_normal);
		
		String htmlTelText="<a style=\"color:blue;\" href=\"\">"+patientInfo.getFdPhone()+"</a>";
		groupHolder.patientPhone.setText(Html.fromHtml(htmlTelText));
		groupHolder.patientPhone.setClickable(true);
		groupHolder.patientPhone.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	TextView view=(TextView)v;
		    	String phoneNum=view.getText().toString();
		    	dialog(phoneNum); 
		    }
		});
		SQLiteDatabase db = null;
		Cursor cursor = null;
		MedicalCaseDao dao=new MedicalCaseDao(context);
		try{
			String sql="select * from table_medical_case where fd_patient_id='"+patientInfo.getFdId()+"'";
			db =dao.getDbHelper().getWritableDatabase();
			cursor=(Cursor) dao.query(sql, null);
			groupHolder.medicalCount.setText(String.valueOf(cursor.getCount()));
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
		convertView.setTag(R.id.expand_view_tagId,groupPosition);
		return convertView;
	}
	
	private void dialog(final String phoneNum) { 
		 AlertDialog.Builder builder = new Builder(context); 
		 builder.setMessage("你确定拨打:"+phoneNum); 
		 builder.setTitle("提示"); 
		 builder.setPositiveButton("确定", 
				 new android.content.DialogInterface.OnClickListener() { 
			 	public void onClick(DialogInterface dialog, int which) { 
			 		dialog.dismiss(); 
					Intent dialIntent =new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNum));
					dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(dialIntent);
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
	public final class GroupHolder{
		public TextView patientName;
		public TextView patientPhone;
		public TextView patientAge;
		public TextView medicalCount;
		public TextView haveRemark;
		public ImageView imageView;
	}
	
	// 获取二级列表的View对象
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		@SuppressWarnings("unchecked")
		MedicalCase medicalCase = (MedicalCase) getChild(groupPosition, childPosition);
		ChildHolder childHolder=null;
		if(convertView==null){
			childHolder=new ChildHolder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=(LinearLayout) layoutInflater.inflate(R.layout.home_list_child, null);
			childHolder.disease=(TextView) convertView.findViewById(R.id.disease);
			childHolder.medicalDate=(TextView) convertView.findViewById(R.id.medicalDate);
			childHolder.advice=(TextView) convertView.findViewById(R.id.advice);
			convertView.setTag(childHolder);
		}else{
			childHolder=(ChildHolder) convertView.getTag();
		}
		childHolder.disease.setText(medicalCase.getFdDisease());
		childHolder.medicalDate.setText(medicalCase.getFdDate());
		childHolder.advice.setText(medicalCase.getFdAdvice());
		return convertView;
	}

	public final class ChildHolder{
		public TextView disease;
		public TextView medicalDate;
		public TextView advice;
	}
	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void delete(PatientInfo patient) {
		datas.remove(patient);
	}
	public List getDatas() {
		return datas;
	}

}
