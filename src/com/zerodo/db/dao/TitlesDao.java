package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.Titles;
													
public class TitlesDao extends TemplateDAO<Titles> {

	public TitlesDao(Context context) {
		super(new MyDBHelper(context));
	}

}
