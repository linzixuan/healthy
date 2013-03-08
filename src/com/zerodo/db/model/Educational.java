package com.zerodo.db.model;

import com.zerodo.base.common.CommonModel;
import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;
/**
 * @author ÁÖè÷è¯
 *	Ñ§Àú
 */
@Table(name = "table_educational",init=true)
public class Educational extends CommonModel{

	@Id
	@Column(name = "fd_id", length = 0, type = "INTEGER")
	private int fdId;
	@Column(name = "fd_name", length = 50, type = "TEXT")
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
