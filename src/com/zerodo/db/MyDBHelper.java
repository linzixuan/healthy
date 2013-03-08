package com.zerodo.db;

import android.content.Context;

import com.zerodo.base.db.helper.DBHelper;
import com.zerodo.db.dao.DoctorAdviceDao;
import com.zerodo.db.model.Attachment;
import com.zerodo.db.model.Department;
import com.zerodo.db.model.DiseaseCategory;
import com.zerodo.db.model.DiseaseFeatures;
import com.zerodo.db.model.DoctorAdvice;
import com.zerodo.db.model.Educational;
import com.zerodo.db.model.MedicalCase;
import com.zerodo.db.model.PatientInfo;
import com.zerodo.db.model.Titles;
import com.zerodo.db.model.UserInfo;

public class MyDBHelper extends DBHelper {

	private final static String DATABASE_NAME = "healthy.db";
	private final static int DATABASE_VERSION = 8;

	private final static Class<?>[] modelClasses = { DiseaseCategory.class,
			Department.class, Educational.class, Titles.class, UserInfo.class,
			DiseaseFeatures.class, DoctorAdvice.class,PatientInfo.class,
			MedicalCase.class,Attachment.class};

	public MyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, modelClasses);
	}

}
