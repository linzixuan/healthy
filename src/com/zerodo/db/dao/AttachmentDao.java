package com.zerodo.db.dao;

import android.content.Context;

import com.zerodo.base.db.dao.TemplateDAO;
import com.zerodo.db.MyDBHelper;
import com.zerodo.db.model.Attachment;
													
public class AttachmentDao extends TemplateDAO<Attachment> {

	public AttachmentDao(Context context) {
		super(new MyDBHelper(context));
	}

}
