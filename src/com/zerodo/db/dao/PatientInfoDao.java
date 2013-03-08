package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.PatientInfo;
													
public class PatientInfoDao extends TemplateDAO<PatientInfo> {

	public PatientInfoDao(Context context) {
		super(new MyDBHelper(context));
	}

}
