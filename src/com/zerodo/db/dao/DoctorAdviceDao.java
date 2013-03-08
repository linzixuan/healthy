package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.DoctorAdvice;
													
public class DoctorAdviceDao extends TemplateDAO<DoctorAdvice> {

	public DoctorAdviceDao(Context context) {
		super(new MyDBHelper(context));
	}

}
