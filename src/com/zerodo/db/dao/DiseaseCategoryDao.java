package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.DiseaseCategory;
													
public class DiseaseCategoryDao extends TemplateDAO<DiseaseCategory> {

	public DiseaseCategoryDao(Context context) {
		super(new MyDBHelper(context));
	}

}
