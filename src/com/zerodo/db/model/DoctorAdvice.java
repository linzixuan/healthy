package com.zerodo.db.model;

import com.zerodo.base.common.CommonModel;
import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;
/**
 * @author ÁÖè÷è¯
 *	Ò½Öö¸À
 */
@Table(name = "table_doctor_advice",init=false)
public class DoctorAdvice extends CommonModel{

	@Id
	@Column(name = "fd_id", length = 0, type = "INTEGER")
	private int fdId;
	@Column(name = "fd_name", length = 500, type = "TEXT")
	private String fdName;

	@Column(name="fd_disease_id",length=0,type = "INTEGER")
	private int fdDiseaseId;
	
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

	public int getFdDiseaseId() {
		return fdDiseaseId;
	}

	public void setFdDiseaseId(int fdDiseaseId) {
		this.fdDiseaseId = fdDiseaseId;
	}
}
