package com.zerodo.base.common;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zerodo.activity.R;

public class CommonListApapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<CommonModel> datas;

	public CommonListApapter(Context context,List datas){
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
		view = inflater.inflate(R.layout.commonlist_item, null);
		CommonModel dataModel=datas.get(index);
		((TextView)view.findViewById(R.id.commonText)).setText(dataModel.getFdName());
		return view;
	}
	public void delete(int index){
		datas.remove(index);
	}
	public void delete(CommonModel model){
		datas.remove(model);
	}
	public List<CommonModel> getDatas() {
		return datas;
	}
	public void setDatas(List<CommonModel> datas) {
		this.datas = datas;
	}

}
