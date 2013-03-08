package com.zerodo.db.model;

import com.zerodo.base.common.CommonModel;
import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;
/**
 * @author �����
 * 	������Ϣ
 *
 */
@Table(name="table_patient_info",init=false)
public class PatientInfo extends CommonModel {

	@Id
	@Column(name = "fd_id", length = 0, type = "INTEGER")
	private int fdId;
	
	@Column(name = "fd_name", length = 50, type = "TEXT")
	private String fdName;
	
	//�Ա�
	@Column(name = "fd_sexy", length = 10, type = "INTEGER")
	private String fdSexy;
	
	//����
	@Column(name="fd_age",length=4,type="INTEGER")
	private int fdAge;
	
	//��������
	@Column(name="fd_birth_date",length=20,type="TEXT")
	private String fdBirthDay;
	
	//סַ
	@Column(name="fd_address",length=200,type="TEXT")
	private String fdAddress;
	
	//��ϵ�绰
	@Column(name="fd_phone",length=30,type="TEXT")
	private String fdPhone;
	
	//Ӧ����ϵ��
	@Column(name="fd_emergency_person",length=50,type="TEXT")
	private String fdEmergencyPerson;
	
	//Ӧ����ϵ�绰
	@Column(name="fd_emergency_phone",length=30,type="TEXT")
	private String fdEmergencyPhone;
	
	//������
	@Column(name="fd_healthy_no",length=250,type="TEXT")
	private String fdHealthyNo;
	
	//����˵��
	@Column(name="fd_remark",length=500,type="TEXT")
	private String fdRemark;
	
	//ͷ��
	@Column(name="fd_head_pic",length=0,type="BLOB")
	private byte[] fdHeadPic;
	
	//�Ƿ��ղ�
	@Column(name="fd_is_favor",length=0,type="INTEGER")
	private int fdIsFavor;
	
	//���֤����
	@Column(name="fd_identity_card",length=18,type="TEXT")
	private String fdIdentityCard;

	//����޸�ʱ��
	@Column(name="fd_update_time",length=20,type="TEXT")
	private String fdUpdateTime;
	
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

	public String getFdSexy() {
		return fdSexy;
	}

	public void setFdSexy(String fdSexy) {
		this.fdSexy = fdSexy;
	}

	public int getFdAge() {
		return fdAge;
	}

	public void setFdAge(int fdAge) {
		this.fdAge = fdAge;
	}

	public String getFdBirthDay() {
		return fdBirthDay;
	}

	public void setFdBirthDay(String fdBirthDay) {
		this.fdBirthDay = fdBirthDay;
	}

	public String getFdAddress() {
		return fdAddress;
	}

	public void setFdAddress(String fdAddress) {
		this.fdAddress = fdAddress;
	}

	public String getFdPhone() {
		return fdPhone;
	}

	public void setFdPhone(String fdPhone) {
		this.fdPhone = fdPhone;
	}

	public String getFdEmergencyPerson() {
		return fdEmergencyPerson;
	}

	public void setFdEmergencyPerson(String fdEmergencyPerson) {
		this.fdEmergencyPerson = fdEmergencyPerson;
	}

	public String getFdEmergencyPhone() {
		return fdEmergencyPhone;
	}

	public void setFdEmergencyPhone(String fdEmergencyPhone) {
		this.fdEmergencyPhone = fdEmergencyPhone;
	}

	public String getFdHealthyNo() {
		return fdHealthyNo;
	}

	public void setFdHealthyNo(String fdHealthyNo) {
		this.fdHealthyNo = fdHealthyNo;
	}

	public String getFdRemark() {
		return fdRemark;
	}

	public void setFdRemark(String fdRemark) {
		this.fdRemark = fdRemark;
	}

	public byte[] getFdHeadPic() {
		return fdHeadPic;
	}

	public void setFdHeadPic(byte[] fdHeadPic) {
		this.fdHeadPic = fdHeadPic;
	}

	public int getFdIsFavor() {
		return fdIsFavor;
	}

	public void setFdIsFavor(int fdIsFavor) {
		this.fdIsFavor = fdIsFavor;
	}

	public String getFdIdentityCard() {
		return fdIdentityCard;
	}

	public void setFdIdentityCard(String fdIdentityCard) {
		this.fdIdentityCard = fdIdentityCard;
	}

	public String getFdUpdateTime() {
		return fdUpdateTime;
	}

	public void setFdUpdateTime(String fdUpdateTime) {
		this.fdUpdateTime = fdUpdateTime;
	}
	
}
