package com.wei.rece.inner.conf;

import java.util.List;

import com.wei.rece.conf.BasicSetting;

public class Setting extends BasicSetting {

	private static final long serialVersionUID = -231411205327030806L;

	/**
	 * 扫描打包文件功能类
	 */
	private String className;
	
	/**
	 * 是否递归扫描目录
	 */
	private boolean recursive;

	private String basic;
	
	private String folder;
	
	private String fileInfo;
	
	private String fileReceipt;
	
	private String fileSuccess;
	
	private String fileFail;
	
	private String fileBak;
	
	private String[] prefix;
	
	private String backupBasic;
	
	private List<Setting> fsServer;
	
	private List<Setting> ftpServer;

	private String tempPathReceipt;
	
	private String backupBasicPath;
	
	private String fsFileFolder;
	
	private String fsReceiptFolder;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(String fileInfo) {
		this.fileInfo = fileInfo;
	}

	public String getFileReceipt() {
		return fileReceipt;
	}

	public void setFileReceipt(String fileReceipt) {
		this.fileReceipt = fileReceipt;
	}

	public String getFileSuccess() {
		return fileSuccess;
	}

	public void setFileSuccess(String fileSuccess) {
		this.fileSuccess = fileSuccess;
	}

	public String getFileFail() {
		return fileFail;
	}

	public void setFileFail(String fileFail) {
		this.fileFail = fileFail;
	}

	public List<Setting> getFsServer() {
		return fsServer;
	}

	public void setFsServer(List<Setting> fsServer) {
		this.fsServer = fsServer;
	}

	public List<Setting> getFtpServer() {
		return ftpServer;
	}

	public void setFtpServer(List<Setting> ftpServer) {
		this.ftpServer = ftpServer;
	}

	public String getFileBak() {
		return fileBak;
	}

	public void setFileBak(String fileBak) {
		this.fileBak = fileBak;
	}

	public String[] getPrefix() {
		return prefix;
	}

	public void setPrefix(String[] prefix) {
		this.prefix = prefix;
	}

	public String getBackupBasic() {
		return backupBasic;
	}

	public void setBackupBasic(String backupBasic) {
		this.backupBasic = backupBasic;
	}

	public String getTempPathReceipt() {
		return tempPathReceipt;
	}

	public void setTempPathReceipt(String tempPathReceipt) {
		this.tempPathReceipt = tempPathReceipt;
	}

	public String getBackupBasicPath() {
		return backupBasicPath;
	}

	public void setBackupBasicPath(String backupBasicPath) {
		this.backupBasicPath = backupBasicPath;
	}

	public String getFsFileFolder() {
		return fsFileFolder;
	}

	public void setFsFileFolder(String fsFileFolder) {
		this.fsFileFolder = fsFileFolder;
	}

	public String getFsReceiptFolder() {
		return fsReceiptFolder;
	}

	public void setFsReceiptFolder(String fsReceiptFolder) {
		this.fsReceiptFolder = fsReceiptFolder;
	}
	
}
