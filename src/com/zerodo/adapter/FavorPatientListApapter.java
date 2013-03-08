package com.zerodo.adapter;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zerodo.activity.R;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;

public class FavorPatientListApapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<PatientInfo> datas;

	public FavorPatientListApapter(Context context,List datas){
		this.context=context;
		this.datas=datas;
		this.inflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return datas.size();
	}

	public Object getItem(int index) {
		return datas.get(index);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int index, View view, ViewGroup parent) {
		PatientInfo patientInfo=datas.get(index);
		ViewHolder viewHolder=null;
		if(view==null){
			viewHolder=new ViewHolder();
			view = inflater.inflate(R.layout.favorpatientlist_item, null);
			viewHolder.patientName=(TextView)view.findViewById(R.id.patientName);
			viewHolder.patientPhone=(TextView)view.findViewById(R.id.patientPhone);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) view.getTag();
		}
		viewHolder.patientName.setText(patientInfo.getFdName());
		String htmlTelText="<a style=\"color:blue;\" href=\"\">"+patientInfo.getFdPhone()+"</a>";
		viewHolder.patientPhone.setText(Html.fromHtml(htmlTelText));
		viewHolder.patientPhone.setClickable(true);
		viewHolder.patientPhone.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	TextView view=(TextView)v;
		    	String phoneNum=view.getText().toString();
				Intent dialIntent =new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNum));
				dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(dialIntent); 
		    }
		});
		
		return view;
	}
	public final class ViewHolder{
		public TextView patientName;
		public TextView patientPhone;
	}
	public void delete(int index){
		datas.remove(index);
	}
	public void delete(PatientInfo patientInfo){
		datas.remove(patientInfo);
	}
	
	public List<PatientInfo> getDatas() {
		return datas;
	}
	public void setDatas(List<PatientInfo> datas) {
		this.datas = datas;
	}

}
