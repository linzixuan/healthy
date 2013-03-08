package com.zerodo.db.model;

import java.io.Serializable;

import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;
/**
 * @author �����
 *  ����
 */
@Table(name="table_medical_case",init=false)
public class MedicalCase implements Serializable {
	@Id
	@Column(name = "fd_id", length = 0, type = "INTEGER")
	private int fdId;
	
	//����
	@Column(name = "fd_patient_id", length = 0, type = "INTEGER")
	private int fdPaitentId;

	//��������
	@Column(name = "fd_patient_name", length = 50, type = "TEXT")
	private String fdPaitentName;
	
	//����
	@Column(name = "fd_date", length = 0, type = "TEXT")	
	private String fdDate;
	
	//����
	@Column(name="fd_disease",length=50,type="TEXT")
	private String fdDisease;
	
	//���� id
	@Column(name = "fd_disease_id", length = 0, type = "INTEGER")
	private int fdDiseaseId;
	
	//����
	@Column(name="fd_features",length=500,type="TEXT")
	private String fdFeatures;
	
	//����
	@Column(name="fd_features_ids",length=500,type="TEXT")
	private String fdFeaturesIds;
	
	//ҽ��
	@Column(name="fd_advice",length=500,type="TEXT")
	private String fdAdvice;
	
	@Column(name="fd_advice_ids",length=500,type="TEXT")
	private String fdAdviceIds;
	
	//����
	@Column(name="fd_operation",length=50,type="TEXT")
	private  String fdOperation;
	
	//����
	@Column(name="fd_area",length=20,type="TEXT")
	private String fdArea;
	
	//����
	@Column(name="fd_ward",length=20,type="TEXT")
	private String fdWard;
	
	//����
	@Column(name="fd_bed",length=20,type="TEXT")
	private String fdBed;
	
	
	//����˵��
	@Column(name="fd_remark",length=500,type="TEXT")
	private String fdRemark;
	
	
	//�Ƿ��ղ�
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
