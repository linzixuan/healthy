package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.Educational;
													
public class EducationalDao extends TemplateDAO<Educational> {

	public EducationalDao(Context context) {
		super(new MyDBHelper(context));
	}

}
