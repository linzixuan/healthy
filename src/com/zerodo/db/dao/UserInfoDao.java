package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.UserInfo;
													
public class UserInfoDao extends TemplateDAO<UserInfo> {

	public UserInfoDao(Context context) {
		super(new MyDBHelper(context));
	}

}
