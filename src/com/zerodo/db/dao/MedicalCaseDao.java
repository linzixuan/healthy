package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.MedicalCase;
													
public class MedicalCaseDao extends TemplateDAO<MedicalCase> {

	public MedicalCaseDao(Context context) {
		super(new MyDBHelper(context));
	}

}
