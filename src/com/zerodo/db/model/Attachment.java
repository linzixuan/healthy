package com.zerodo.db.model;

import com.zerodo.base.db.annotation.Column;
import com.zerodo.base.db.annotation.Id;
import com.zerodo.base.db.annotation.Table;

/**
 * @author �����
 *	ͼƬ����
 */
@Table(name="table_attachment",init=false)
public class Attachment {
	//����
	@Id
	@Column(name = "fd_id", length = 0, type = "INTEGER")
	private int fdId;
	
	//����
	@Column(name = "fd_name", length = 50, type = "TEXT")
	private String fdName;
	
	//���ĵ�ID
	@Column(name = "fd_main_id", length = 0, type = "INTEGER")
	private int fdMainId;
	
	//���ĵ���ģ��
	@Column(name = "fd_model_name", length = 100, type = "TEXT")
	private String fdModelName;
	
	//�ĵ�·��
	@Column(name = "fd_file_path", length = 100, type = "TEXT")
	private String fdFilePath;
	
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
	public int getFdMainId() {
		return fdMainId;
	}
	public void setFdMainId(int fdMainId) {
		this.fdMainId = fdMainId;
	}
	public String getFdModelName() {
		return fdModelName;
	}
	public void setFdModelName(String fdModelName) {
		this.fdModelName = fdModelName;
	}
	public String getFdFilePath() {
		return fdFilePath;
	}
	public void setFdFilePath(String fdFilePath) {
		this.fdFilePath = fdFilePath;
	}
}
