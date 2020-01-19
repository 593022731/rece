package com.wei.rece.inner.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wei.rece.ReceFile;
import com.wei.rece.inner.conf.Setting;

public class InnerReceFile extends ReceFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File receiptFile;
	
	private File infoFile;
	
	private File bakFile;
	
	private List<Setting> ftpServer = new ArrayList<Setting>();
	
	private List<Setting> fsServer = new ArrayList<Setting>();
	
	public File getReceiptFile() {
		return receiptFile;
	}

	public void setReceiptFile(File receiptFile) {
		this.receiptFile = receiptFile;
	}

	public File getInfoFile() {
		return infoFile;
	}

	public void setInfoFile(File infoFile) {
		this.infoFile = infoFile;
	}

	public File getBakFile() {
		return bakFile;
	}

	public void setBakFile(File bakFile) {
		this.bakFile = bakFile;
	}

	public List<Setting> getFtpServer() {
		return ftpServer;
	}

	public void setFtpServer(List<Setting> ftpServer) {
		this.ftpServer = ftpServer;
	}

	public List<Setting> getFsServer() {
		return fsServer;
	}

	public void setFsServer(List<Setting> fsServer) {
		this.fsServer = fsServer;
	}
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("凭证文件: ").append(this.receiptFile.getPath()).append("\n");
		build.append("实体文件: ").append(this.infoFile.getPath()).append("\n");
		if(this.bakFile != null){
			build.append("备份文件: ").append(this.bakFile.getPath()).append("\n");
		}
		build.append("文件已等待: ").append((System.currentTimeMillis() - createTimes) / 1000L).append("秒");
		return build.toString();
	}
	
}
