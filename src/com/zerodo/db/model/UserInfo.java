package com.zerodo.db.model;

import java.sql.Blob;

import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;

@Table(name="table_userinfo",init=false)
public class UserInfo {
	@Id
	@Column(name = "fd_id", length = 0, type = "INTEGER")
	private int fdId;
	
	@Column(name = "fd_username", length = 20, type = "TEXT")
	private String fdUserName;
	
	@Column(name = "fd_hospital", length = 50, type = "TEXT")
	private String fdHospital;
	
	@Column(name = "fd_department_id", length = 0, type = "INT")
	private int fdDepartmentId;
	
	@Column(name = "fd_titles_id", length = 0, type = "INT")
	private int fdTitlesId;
	
	@Column(name = "fd_password", length = 20, type = "TEXT")
	private String fdPasswor;
	
	@Column(name = "fd_login_name", length = 20, type = "TEXT")
	private String fdLoginName;
	
	@Column(name = "fd_nike_name", length = 50, type = "TEXT")
	private String fdNikeName;
	
	@Column(name = "fd_intorducation", length = 500, type = "TEXT")
	private String fdIntroduction;
	
	@Column(name = "fd_head_pic", length = 0, type = "BLOB")
	private byte[] fdHeadPic;
	
	
	@Column(name = "fd_educational_id", length = 0, type = "INT")
	private int fdEducationalId;
	
	@Column(name = "fd_specialty", length = 50, type = "TEXT")
	private String fdspecialty;
	
	public String getFdspecialty() {
		return fdspecialty;
	}
	public void setFdspecialty(String fdspecialty) {
		this.fdspecialty = fdspecialty;
	}
	public int getFdId() {
		return fdId;
	}
	public void setFdId(int fdId) {
		this.fdId = fdId;
	}
	public String getFdUserName() {
		return fdUserName;
	}
	public void setFdUserName(String fdUserName) {
		this.fdUserName = fdUserName;
	}
	public String getFdHospital() {
		return fdHospital;
	}
	public void setFdHospital(String fdHospital) {
		this.fdHospital = fdHospital;
	}

	public int getFdDepartmentId() {
		return fdDepartmentId;
	}
	public void setFdDepartmentId(int fdDepartmentId) {
		this.fdDepartmentId = fdDepartmentId;
	}
	public int getFdTitlesId() {
		return fdTitlesId;
	}
	public void setFdTitlesId(int fdTitlesId) {
		this.fdTitlesId = fdTitlesId;
	}
	public int getFdEducationalId() {
		return fdEducationalId;
	}
	public void setFdEducationalId(int fdEducationalId) {
		this.fdEducationalId = fdEducationalId;
	}
	public String getFdPasswor() {
		return fdPasswor;
	}
	public void setFdPasswor(String fdPasswor) {
		this.fdPasswor = fdPasswor;
	}
	public String getFdLoginName() {
		return fdLoginName;
	}
	public void setFdLoginName(String fdLoginName) {
		this.fdLoginName = fdLoginName;
	}
	public String getFdNikeName() {
		return fdNikeName;
	}
	public void setFdNikeName(String fdNikeName) {
		this.fdNikeName = fdNikeName;
	}
	public String getFdIntroduction() {
		return fdIntroduction;
	}
	public void setFdIntroduction(String fdIntroduction) {
		this.fdIntroduction = fdIntroduction;
	}
	public byte[] getFdHeadPic() {
		return fdHeadPic;
	}
	public void setFdHeadPic(byte[] fdHeadPic) {
		this.fdHeadPic = fdHeadPic;
	}
}
