package com.wei.rece.net.conf;

import com.wei.rece.conf.BasicSetting;
import com.wei.rece.fs.FileSystemFactory;
import com.wei.rece.fs.ReceFileSystem;
import com.wei.rece.net.conf.annotation.BackUpDirComment;
import com.wei.rece.net.conf.annotation.DirComment;

public class Setting extends BasicSetting {

	private static final long serialVersionUID = -231411205327030806L;
	
	@Override
	public ReceFileSystem getFileSystem() throws Exception{
		if(fileSystem == null){
				fileSystem = FileSystemFactory.getFileSystem(this, false);
		}
		return fileSystem;
	}

	// /////////////////////通用配置start////////////////////////
	/**
	 * 当天时间格式化(yyyyMMdd)
	 */
	private String todayStr;
	
	/**
	 * 扫描打包文件功能类
	 */
	private String className;
	
	/**
	 * 扫描实体文件夹名
	 */
	private String fileInfo="";

	/**
	 * 扫描凭证文件夹名
	 */
	private String fileReceipt="";

	/**
	 * 成功文件夹名
	 */
	private String fileSuccess;

	/**
	 * 失败文件夹名
	 */
	private String fileFail;
	
	/**
	 * BAK文件夹名
	 */
	private String bak;

	/**
	 * ftp工作目录(扫描文件主目录,外网即3.4发送数据目录，内网即ftp已发送数据目录)
	 */
	private String basic;
	
	/**
	 * ftp分类文件夹名(扫描文件子文件夹名）
	 */
	private String folder;

	/**
	 * 是否备份
	 */
	private boolean backup;
	
	/**
	 * 是否发送BAK文件
	 */
	private boolean sendbak;
	
	/**
	 * 扫描目录中是否需要FILEINFO目录
	 */
	private boolean hasfileinfo;

	/**
	 * 是否递归扫描子目录
	 */
	private boolean recursive;
	// /////////////////////通用置end////////////////////////

	// /////////////////////外网 相关配置start////////////////////////
	
	/**
	 * 发送的实体文件夹名
	 */
	private String senderFileInfo;
	/**
	 * 发送的凭证文件夹名
	 */
	private String senderFileReceipt;
	
	/**
	 * 打包图片最大数量
	 */
	private int picCount;
	
	/**
	 * 扫描数据文件目录
	 */
	private String fileWanPath;

	/**
	 * 扫描凭证文件目录
	 */
	private String receiptWanPath;
	
	/**
	 * 扫描数据文件成功备份目录
	 */
	@BackUpDirComment
	private String fileWanSuccessBakPath;

	/**
	 * 扫描凭证文件成功备份目录
	 */
	@BackUpDirComment
	private String receiptWanSuccessBakPath;
	
	/**
	 * 扫描数据文件失败备份目录
	 */
	@BackUpDirComment
	private String fileWanFailBakPath;

	/**
	 * 扫描凭证文件失败备份目录
	 */
	@BackUpDirComment
	private String receiptWanFailBakPath;

	
	
	
	
	
	/**
	 * ftp待发送数据主目录
	 */
	@DirComment
	private String senderWan;

	/**
	 * ftp待发送数据文件目录
	 */
	@DirComment
	private String senderWanFileInfo;
	
	/**
	 * ftp待发送凭证文件目录
	 */
	@DirComment
	private String senderWanReceipt;
	
	/**
	 * ftp待发送数据文件目录发送成功备份目录
	 */
	@BackUpDirComment
	private String senderWanSuccessBakFileInfo;
	
	/**
	 * ftp待发送凭证文件目录发送成功备份目录
	 */
	@BackUpDirComment
	private String senderWanSuccessBakReceipt;
	
	/**
	 * ftp待发送数据文件目录发送失败备份目录
	 */
	@BackUpDirComment
	private String senderWanFailBakFileInfo;
	
	/**
	 * ftp待发送凭证文件目录发送失败备份目录
	 */
	@BackUpDirComment
	private String senderWanFailBakReceipt;
	
	
	
	
	
	
	
	
	
	/**
	 * ftp发送数据文件目录
	 */
	private String ftpWanFileInfo;
	
	/**
	 * ftp发送凭证文件目录
	 */
	private String ftpWanReceipt;
	
	/**
	 * ftp发送BAK文件目录
	 */
	private String ftpWanBak;
	
	// /////////////////////外网 相关配置end////////////////////////

	public String getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(String fileInfo) {
		this.fileInfo = fileInfo;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
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

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public boolean isBackup() {
		return backup;
	}

	public void setBackup(boolean backup) {
		this.backup = backup;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public String getFileWanPath() {
		return fileWanPath;
	}

	public void setFileWanPath(String fileWanPath) {
		this.fileWanPath = fileWanPath;
	}

	public String getReceiptWanPath() {
		return receiptWanPath;
	}

	public void setReceiptWanPath(String receiptWanPath) {
		this.receiptWanPath = receiptWanPath;
	}

	public String getFileWanSuccessBakPath() {
		return fileWanSuccessBakPath;
	}

	public void setFileWanSuccessBakPath(String fileWanSuccessBakPath) {
		this.fileWanSuccessBakPath = fileWanSuccessBakPath;
	}

	public String getReceiptWanSuccessBakPath() {
		return receiptWanSuccessBakPath;
	}

	public void setReceiptWanSuccessBakPath(String receiptWanSuccessBakPath) {
		this.receiptWanSuccessBakPath = receiptWanSuccessBakPath;
	}

	public String getFileWanFailBakPath() {
		return fileWanFailBakPath;
	}

	public void setFileWanFailBakPath(String fileWanFailBakPath) {
		this.fileWanFailBakPath = fileWanFailBakPath;
	}

	public String getReceiptWanFailBakPath() {
		return receiptWanFailBakPath;
	}

	public void setReceiptWanFailBakPath(String receiptWanFailBakPath) {
		this.receiptWanFailBakPath = receiptWanFailBakPath;
	}

	public String getSenderWan() {
		return senderWan;
	}

	public void setSenderWan(String senderWan) {
		this.senderWan = senderWan;
	}

	public String getSenderWanFileInfo() {
		return senderWanFileInfo;
	}

	public void setSenderWanFileInfo(String senderWanFileInfo) {
		this.senderWanFileInfo = senderWanFileInfo;
	}

	public String getSenderWanReceipt() {
		return senderWanReceipt;
	}

	public void setSenderWanReceipt(String senderWanReceipt) {
		this.senderWanReceipt = senderWanReceipt;
	}

	public String getSenderWanSuccessBakFileInfo() {
		return senderWanSuccessBakFileInfo;
	}

	public void setSenderWanSuccessBakFileInfo(String senderWanSuccessBakFileInfo) {
		this.senderWanSuccessBakFileInfo = senderWanSuccessBakFileInfo;
	}

	public String getSenderWanSuccessBakReceipt() {
		return senderWanSuccessBakReceipt;
	}

	public void setSenderWanSuccessBakReceipt(String senderWanSuccessBakReceipt) {
		this.senderWanSuccessBakReceipt = senderWanSuccessBakReceipt;
	}

	public String getSenderWanFailBakFileInfo() {
		return senderWanFailBakFileInfo;
	}

	public void setSenderWanFailBakFileInfo(String senderWanFailBakFileInfo) {
		this.senderWanFailBakFileInfo = senderWanFailBakFileInfo;
	}

	public String getSenderWanFailBakReceipt() {
		return senderWanFailBakReceipt;
	}

	public void setSenderWanFailBakReceipt(String senderWanFailBakReceipt) {
		this.senderWanFailBakReceipt = senderWanFailBakReceipt;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isSendbak() {
		return sendbak;
	}

	public void setSendbak(boolean sendbak) {
		this.sendbak = sendbak;
	}

	public String getFtpWanFileInfo() {
		return ftpWanFileInfo;
	}

	public void setFtpWanFileInfo(String ftpWanFileInfo) {
		this.ftpWanFileInfo = ftpWanFileInfo;
	}

	public String getFtpWanReceipt() {
		return ftpWanReceipt;
	}

	public void setFtpWanReceipt(String ftpWanReceipt) {
		this.ftpWanReceipt = ftpWanReceipt;
	}

	public String getFtpWanBak() {
		return ftpWanBak;
	}

	public void setFtpWanBak(String ftpWanBak) {
		this.ftpWanBak = ftpWanBak;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public int getPicCount() {
		return picCount;
	}

	public void setPicCount(int picCount) {
		this.picCount = picCount;
	}
	
	public String getSenderFileInfo() {
		return senderFileInfo;
	}

	public void setSenderFileInfo(String senderFileInfo) {
		this.senderFileInfo = senderFileInfo;
	}

	public String getSenderFileReceipt() {
		return senderFileReceipt;
	}

	public void setSenderFileReceipt(String senderFileReceipt) {
		this.senderFileReceipt = senderFileReceipt;
	}

	public String getTodayStr() {
		return todayStr;
	}

	public void setTodayStr(String todayStr) {
		this.todayStr = todayStr;
	}

	public boolean isHasfileinfo() {
		return hasfileinfo;
	}

	public void setHasfileinfo(boolean hasfileinfo) {
		this.hasfileinfo = hasfileinfo;
	}
	
}
