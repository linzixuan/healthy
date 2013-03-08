package com.zerodo.base.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface Table {
	//���ݿ�����
	public abstract String name();
	//�Ƿ��г�ʼ��sql
	public abstract boolean init();
}
