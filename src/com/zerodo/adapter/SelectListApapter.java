package com.zerodo.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zerodo.activity.R;
import com.zerodo.base.common.CommonModel;

public class SelectListApapter extends BaseAdapter {
	private Context context;
	private List<CommonModel> datas;
	private LayoutInflater inflater;
	private List<CommonModel> selectedList=new ArrayList();
	// 用来控制CheckBox的选中状况    
	private static HashMap<Integer,Boolean> isSelected;


	public SelectListApapter(Context context,List datas){
		this.context=context;
		this.datas=datas;
		this.inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}
	


	// 初始化isSelected的数据    
	private void initData(){        
		for(int i=0; i<datas.size();i++) {            
			getIsSelected().put(i,false);        
			}    
		}
	
    public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		SelectListApapter.isSelected = isSelected;
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

	private final class  ViewHolder{
		public TextView commonText;
		public CheckBox checkBox;
	}
	
	public View getView(int index, View view, ViewGroup parent) {
		ViewHolder viewHolder=null;
		CommonModel dataModel=datas.get(index);
		if(null==view){
			viewHolder=new ViewHolder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.selectlist_item, null);
			viewHolder.commonText=(TextView)view.findViewById(R.id.commonText);
			viewHolder.checkBox=(CheckBox) view.findViewById(R.id.commonCheckBox);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) view.getTag();
		}
		viewHolder.commonText.setText(dataModel.getFdName());	
		viewHolder.checkBox.setChecked(getIsSelected().get(index));
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
	public List getSelectedList() {
		return selectedList;
	}

}
