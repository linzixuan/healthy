package com.zerodo.base.common;

import java.io.Serializable;

public class CommonModel implements Serializable  {
	
	private int fdId;
	private String fdName;
	
	public int getFdId() {
		return fdId;
	}
	public void setFdId(int fdId) {
		this.fdId = fdId;
	}
	public String getFdName() {
		return fdName;
	}
	public void setFdName(String fdName) {
		this.fdName = fdName;
	}
}
