package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.DiseaseFeatures;
													
public class DiseaseFeaturesDao extends TemplateDAO<DiseaseFeatures> {

	public DiseaseFeaturesDao(Context context) {
		super(new MyDBHelper(context));
	}

}
