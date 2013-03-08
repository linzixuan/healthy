package com.zerodo.base.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface Table {
	//数据库名称
	public abstract String name();
	//是否有初始化sql
	public abstract boolean init();
}
