package com.zerodo.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zerodo.activity.R;
import com.zerodo.adapter.FavorMedicalListApapter.ViewHolder;
import com.zerodo.db.dao.PatientInfoDao;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;

public class PatientMedicalListApapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<MedicalCase> datas;

	public PatientMedicalListApapter(Context context,List datas){
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
		MedicalCase medicalCase=datas.get(index);
		ViewHolder viewHolder=null;
		if(view==null){
			viewHolder=new ViewHolder();
			view = inflater.inflate(R.layout.patientmedicallist_item, null);
			viewHolder.disease=(TextView)view.findViewById(R.id.disease);
			viewHolder.date=(TextView)view.findViewById(R.id.date);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) view.getTag();
		}
		viewHolder.disease.setText(medicalCase.getFdDisease());
		viewHolder.date.setText(medicalCase.getFdDate());
		return view;
	}

	public final class ViewHolder{
		public TextView disease;
		public TextView date;
	}
	public void delete(int index){
		datas.remove(index);
	}
	public void delete(MedicalCase medicalCase){
		datas.remove(medicalCase);
	}
	
	public List<MedicalCase> getDatas() {
		return datas;
	}
	public void setDatas(List<MedicalCase> datas) {
		this.datas = datas;
	}

}
