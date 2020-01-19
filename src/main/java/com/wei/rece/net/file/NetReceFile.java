package com.wei.rece.net.file;

import com.wei.rece.ReceFile;

import java.io.File;

/**
 * 外网公用数据传输文件对象类
 * @author 		weih
 * @date   		2013-7-5上午11:38:15
 * @version     
 */
public class NetReceFile extends ReceFile {
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("上传实体文件: ").append(this.senderInfoFile.getPath()).append("到").append(this.ftpInfoFile.getPath()).append("\n");
		build.append("上传凭证文件: ").append(this.senderReceiptFile.getPath()).append("到").append(this.ftpReceiptFile.getPath()).append("\n");
		build.append("文件已等待: ").append((System.currentTimeMillis() - createTimes) / 1000L).append("秒");
		return build.toString();
	}
	
	private static final long serialVersionUID = 1L;

	protected File txtDescFile; //txt 数据描述文件
	
	protected File ftpInfoFile; //ftp 数据文件
	protected File ftpReceiptFile;//ftp 凭证文件

	protected File infoFile;//3.4发送数据源文件
	protected File receiptFile;//3.4发送凭证源文件
	
	protected File senderInfoFile;//ftp 待发送数据文件
	protected File senderReceiptFile;//ftp 待发送凭证文件
	public File getFtpInfoFile() {
		return ftpInfoFile;
	}
	public void setFtpInfoFile(File ftpInfoFile) {
		this.ftpInfoFile = ftpInfoFile;
	}
	public File getFtpReceiptFile() {
		return ftpReceiptFile;
	}
	public void setFtpReceiptFile(File ftpReceiptFile) {
		this.ftpReceiptFile = ftpReceiptFile;
	}
	public File getInfoFile() {
		return infoFile;
	}
	public void setInfoFile(File infoFile) {
		this.infoFile = infoFile;
	}
	public File getReceiptFile() {
		return receiptFile;
	}
	public void setReceiptFile(File receiptFile) {
		this.receiptFile = receiptFile;
	}
	public File getSenderInfoFile() {
		return senderInfoFile;
	}
	public void setSenderInfoFile(File senderInfoFile) {
		this.senderInfoFile = senderInfoFile;
	}
	public File getSenderReceiptFile() {
		return senderReceiptFile;
	}
	public void setSenderReceiptFile(File senderReceiptFile) {
		this.senderReceiptFile = senderReceiptFile;
	}
	public File getTxtDescFile() {
		return txtDescFile;
	}
	public void setTxtDescFile(File txtDescFile) {
		this.txtDescFile = txtDescFile;
	}
}
