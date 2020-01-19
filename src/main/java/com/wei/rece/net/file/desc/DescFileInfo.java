package com.wei.rece.net.file.desc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * txt数据描述对象
 * @author 		weih
 * @date   		2013-7-5下午05:50:31
 * @version     
 */
public abstract class DescFileInfo {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 分隔符
	 */
	protected static final String SPLIT_A = "\u0001";
	
	/**
	 * 身份证号
	 */
	protected String certID;

	/**
	 * 证件类型
	 */
	protected String certType;
	
	/**
	 * 文件名称
	 */
	protected String fileName;
	protected String sessionID;
	protected String serialID;
	/**
	 * 自己QQ
	 */
	protected String qqCode;

	/**
	 * 对方qq
	 */
	protected String oppCode;
	/**
	 * 记录编号
	 */
	protected String recordID;
	
	/**
	 * 获取新名称
	 * @return
	 */
	public abstract String getFileNameTag();
	
	public String getCertID() {
		return certID;
	}
	public void setCertID(String certID) {
		this.certID = certID;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getSerialID() {
		return serialID;
	}
	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}
	public String getQqCode() {
		return qqCode;
	}
	public void setQqCode(String qqCode) {
		this.qqCode = qqCode;
	}
	public String getOppCode() {
		return oppCode;
	}
	public void setOppCode(String oppCode) {
		this.oppCode = oppCode;
	}
	public String getRecordID() {
		return recordID;
	}
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}
}
