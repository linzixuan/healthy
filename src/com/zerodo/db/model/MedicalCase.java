package com.zerodo.db.model;

import java.io.Serializable;

import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;
/**
 * @author 林梓璇
 *  病历
 */
@Table(name="table_medical_case",init=false)
public class MedicalCase implements Serializable {
	@Id
	@Column(name = "fd_id", length = 0, type = "INTEGER")
	private int fdId;
	
	//患者
	@Column(name = "fd_patient_id", length = 0, type = "INTEGER")
	private int fdPaitentId;

	//患者姓名
	@Column(name = "fd_patient_name", length = 50, type = "TEXT")
	private String fdPaitentName;
	
	//日期
	@Column(name = "fd_date", length = 0, type = "TEXT")	
	private String fdDate;
	
	//病种
	@Column(name="fd_disease",length=50,type="TEXT")
	private String fdDisease;
	
	//病种 id
	@Column(name = "fd_disease_id", length = 0, type = "INTEGER")
	private int fdDiseaseId;
	
	//病征
	@Column(name="fd_features",length=500,type="TEXT")
	private String fdFeatures;
	
	//病征
	@Column(name="fd_features_ids",length=500,type="TEXT")
	private String fdFeaturesIds;
	
	//医嘱
	@Column(name="fd_advice",length=500,type="TEXT")
	private String fdAdvice;
	
	@Column(name="fd_advice_ids",length=500,type="TEXT")
	private String fdAdviceIds;
	
	//手术
	@Column(name="fd_operation",length=50,type="TEXT")
	private  String fdOperation;
	
	//病区
	@Column(name="fd_area",length=20,type="TEXT")
	private String fdArea;
	
	//病室
	@Column(name="fd_ward",length=20,type="TEXT")
	private String fdWard;
	
	//床号
	@Column(name="fd_bed",length=20,type="TEXT")
	private String fdBed;
	
	
	//其他说明
	@Column(name="fd_remark",length=500,type="TEXT")
	private String fdRemark;
	
	
	//是否收藏
	@Column(name="fd_is_favor",length=0,type="INTEGER")
	private int fdIsFavor;
	
	
	public int getFdId() {
		return fdId;
	}


	public void setFdId(int fdId) {
		this.fdId = fdId;
	}


	public int getFdPaitentId() {
		return fdPaitentId;
	}


	public void setFdPaitentId(int fdPaitentId) {
		this.fdPaitentId = fdPaitentId;
	}

	public String getFdPaitentName() {
		return fdPaitentName;
	}


	public void setFdPaitentName(String fdPaitentName) {
		this.fdPaitentName = fdPaitentName;
	}


	public String getFdDate() {
		return fdDate;
	}


	public void setFdDate(String fdDate) {
		this.fdDate = fdDate;
	}


	


	public String getFdDisease() {
		return fdDisease;
	}


	public void setFdDisease(String fdDisease) {
		this.fdDisease = fdDisease;
	}


	public int getFdDiseaseId() {
		return fdDiseaseId;
	}


	public void setFdDiseaseId(int fdDiseaseId) {
		this.fdDiseaseId = fdDiseaseId;
	}


	public String getFdFeatures() {
		return fdFeatures;
	}


	public void setFdFeatures(String fdFeatures) {
		this.fdFeatures = fdFeatures;
	}


	public String getFdAdvice() {
		return fdAdvice;
	}


	public void setFdAdvice(String fdAdvice) {
		this.fdAdvice = fdAdvice;
	}


	public String getFdOperation() {
		return fdOperation;
	}


	public void setFdOperation(String fdOperation) {
		this.fdOperation = fdOperation;
	}


	public String getFdArea() {
		return fdArea;
	}


	public void setFdArea(String fdArea) {
		this.fdArea = fdArea;
	}


	public String getFdWard() {
		return fdWard;
	}


	public void setFdWard(String fdWard) {
		this.fdWard = fdWard;
	}


	public String getFdBed() {
		return fdBed;
	}


	public void setFdBed(String fdBed) {
		this.fdBed = fdBed;
	}


	public String getFdRemark() {
		return fdRemark;
	}


	public void setFdRemark(String fdRemark) {
		this.fdRemark = fdRemark;
	}


	public int getFdIsFavor() {
		return fdIsFavor;
	}


	public void setFdIsFavor(int fdIsFavor) {
		this.fdIsFavor = fdIsFavor;
	}


	public String getFdFeaturesIds() {
		return fdFeaturesIds;
	}


	public void setFdFeaturesIds(String fdFeaturesIds) {
		this.fdFeaturesIds = fdFeaturesIds;
	}


	public String getFdAdviceIds() {
		return fdAdviceIds;
	}


	public void setFdAdviceIds(String fdAdviceIds) {
		this.fdAdviceIds = fdAdviceIds;
	}

}
