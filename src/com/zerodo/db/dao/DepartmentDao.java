package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.Department;
													
public class DepartmentDao extends TemplateDAO<Department> {

	public DepartmentDao(Context context) {
		super(new MyDBHelper(context));
	}

}
