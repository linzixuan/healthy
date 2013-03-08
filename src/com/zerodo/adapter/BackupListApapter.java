package com.zerodo.adapter;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zerodo.activity.R;
import com.zerodo.db.model.MedicalCase;

public class BackupListApapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<File> datas;

	public BackupListApapter(Context context,List datas){
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
		File file=datas.get(index);
		ViewHolder viewHolder=null;
		if(view==null){
			viewHolder=new ViewHolder();
			view = inflater.inflate(R.layout.backup_list_item, null);
			viewHolder.fileName=(TextView)view.findViewById(R.id.fileName);
			viewHolder.fileTime=(TextView)view.findViewById(R.id.fileTime);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) view.getTag();
		}
		viewHolder.fileName.setText(file.getName());
		Date modify=new Date(file.lastModified());
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		viewHolder.fileTime.setText(df.format(modify));
		return view;
	}
	
	public final class ViewHolder{
		public TextView fileName;
		public TextView fileTime;
	}
	public void delete(int index){
		datas.remove(index);
	}
	public void delete(File file){
		datas.remove(file);
	}
	
	public List<File> getDatas() {
		return datas;
	}
	public void setDatas(List<File> datas) {
		this.datas = datas;
	}

}
