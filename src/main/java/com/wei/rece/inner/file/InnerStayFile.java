package com.wei.rece.inner.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wei.rece.ReceFile;
import com.wei.rece.inner.conf.Setting;
import com.wei.rece.transmit.StayFile;

public class InnerStayFile extends StayFile {

	private static final long serialVersionUID = 5429141773586344671L;

	private static Logger logger = LoggerFactory.getLogger(InnerStayFile.class);
	
	private Setting setting;
	
	private InnerReceFile innerFile;
	
	private String backPathFileinfo;
	
	private String failedPathReceipt;
	
	private String failedPathFileinfo;
	
	private String successPathReceipt;
	
	private String successPathFileinfo;
	
	public InnerStayFile(ReceFile receFile, Setting setting) {
		super(receFile, setting);
		this.setting = (Setting)setting;
		innerFile = (InnerReceFile) receFile;
	}

	@Override
	public void call() {
		//移到success目录
		try {
			
			if(innerFile.getInfoFile().exists()){
				FileUtils.moveFile(innerFile.getInfoFile(), new File(this.getSuccessPathFileinfo() + innerFile.getInfoFile().getName()));
			}else{
				if(logger.isErrorEnabled()){
					logger.error("发送成功后文件丢失! Path:" + innerFile.getInfoFile().getPath());
				}
			}
			
			if(innerFile.getReceiptFile().exists()){
				FileUtils.moveFile(innerFile.getReceiptFile(), new File(this.getSuccessPathReceipt() + innerFile.getReceiptFile().getName()));
			}else{
				if(logger.isErrorEnabled()){
					logger.error("发送成功后凭证文件丢失! Path:" + innerFile.getReceiptFile().getPath());
				}
			}
			
			if("yes".equals(this.setting.getFileBak()) && innerFile.getBakFile() != null){
				if(innerFile.getBakFile().exists()){
					File bakFile = new File(this.getBackPathFileinfo() + File.separator + innerFile.getBakFile().getName());
					if(bakFile.exists()){
						bakFile.delete();
					}
					FileUtils.moveFile(innerFile.getBakFile(), bakFile);
				}
			}
		} catch (IOException e) {
			if(logger.isErrorEnabled()){
				logger.error("移动完成发送成功文件失败! 文件信息:\n" + innerFile.toString(), e);
			}
			this.failed();
		}
	}

	@Override
	public void execute() throws Exception {
		
		//发送多份
		boolean success = true;
		
		//发送FTP
		if(this.setting.getFtpServer() != null){
			
			List<Setting> ftpServer = innerFile.getFtpServer();
			List<Setting> successSetting = new ArrayList<Setting>(ftpServer.size());
			
			for(Setting s : ftpServer){
				String basic = s.getDir() + "/" /*+ this.setting.getFolder() + "/"*/;
				String info = basic + s.getFtpFileFolder();//this.setting.getFileInfo();
				String rece = basic + s.getFtpReceiptFolder();//this.setting.getFileReceipt();
				if(logger.isDebugEnabled()){
					logger.debug("将文件发送至FTP, Ftp服务器名: " + s.getName() + ", 文件" + innerFile.getInfoFile().getPath() + "发送到" + info + "/" + innerFile.getInfoFile().getName());
				}
				try{
					s.getFileSystem().copy(innerFile.getReceiptFile().getPath(), rece + "/" + innerFile.getReceiptFile().getName());
					s.getFileSystem().copy(innerFile.getInfoFile().getPath(), info + "/" + innerFile.getInfoFile().getName());
					successSetting.add(s);
				}catch (Exception e) {
					success = false;
					if(logger.isErrorEnabled()){
						logger.error("将文件发送至FTP出现错误! Ftp服务器名: " + s.getName() + ", 文件" + innerFile.getInfoFile().getPath() + "发送到" + info + "/" + innerFile.getInfoFile().getName());
						logger.error("文件信息:\n " + innerFile.getReceiptFile().toString());
						logger.error("错误信息: ", e);
					}
				}
			}
			ftpServer.removeAll(successSetting);
			
		}
		
		//发送FS
		if(this.setting.getFsServer() != null){
			
			List<Setting> fsServer = innerFile.getFsServer();
			List<Setting> successSetting = new ArrayList<Setting>(fsServer.size());
			
			for(Setting s : fsServer){
				String basic = s.getDir() + File.separator /*+ this.setting.getFolder()*/ + File.separator;
				String info = basic + s.getFsFileFolder();//this.setting.getFileInfo();
				String rece = basic + s.getFsReceiptFolder();//this.setting.getFileReceipt() ;
				if(logger.isDebugEnabled()){
					logger.debug("将文件发送至文件系统, 目录名: " + this.setting.getFolder() + ", 文件" + innerFile.getInfoFile().getPath() + "发送到" + info + "/" + innerFile.getInfoFile().getName());
				}
				try {
					s.getFileSystem().copy(innerFile.getReceiptFile().getPath(), rece + File.separator + innerFile.getReceiptFile().getName());
					s.getFileSystem().copy(innerFile.getInfoFile().getPath(), info + File.separator + innerFile.getInfoFile().getName());
					successSetting.add(s);
				} catch (Exception e) {
					success = false;
					if(logger.isErrorEnabled()){
						logger.error("将文件发送至文件系统出错, 目录名: " + this.setting.getFolder() + ", 文件" + innerFile.getInfoFile().getPath() + "发送到" + info + "/" + innerFile.getInfoFile().getName());
						logger.error("文件信息:\n" + innerFile.getReceiptFile().toString());
						logger.error("错误信息: ", e);
					}
				}
				
			}
			
			fsServer.removeAll(successSetting);
			
		}
		
		if(!success){
			throw new Exception("分发文件部分失败!");
		}
		
	}
	
	@Override
	public void failed() {
		
		try {
			
			File receipt = new File(this.getFailedPathReceipt()
					+ innerFile.getReceiptFile().getName());
			
			File fileinfo = new File(this.getFailedPathFileinfo()
					+ innerFile.getInfoFile().getName());
			
			
			if(logger.isErrorEnabled()){
				logger.error("文件发送失败, Path: " + innerFile.getReceiptFile().getPath() + "尝试次数: " + this.retry);
			}
			
			if(fileinfo.exists()){
				if(logger.isErrorEnabled()){
					logger.error("数据文件在系统中已存在, Path: " + fileinfo.getPath());
				}
				fileinfo.delete();
			}
			
			if(receipt.exists()){
				if(logger.isErrorEnabled()){
					logger.error("数据文件在系统中已存在, Path: " + receipt.getPath());
				}
				receipt.delete();
			}
			
			if(innerFile.getInfoFile().exists()){
				FileUtils.moveFile(innerFile.getInfoFile(), fileinfo);
			}
			
			if(innerFile.getReceiptFile().exists()){
				FileUtils.moveFile(innerFile.getReceiptFile(), receipt);
			}
			
			if(innerFile.getBakFile() != null){
				File fileBak = new File(this.getBackPathFileinfo()
						+ innerFile.getBakFile().getName());
				
				if(fileBak.exists()){
					fileBak.delete();
					if(logger.isErrorEnabled()){
						logger.error("数据文件在系统中已存在, Path: " + fileBak.getPath());
					}
				}
				if(innerFile.getBakFile().exists()){
					FileUtils.moveFile(innerFile.getBakFile(), fileBak);
				}
			}
			
		} catch (IOException e) {
			if(logger.isErrorEnabled()){
				logger.error("文件发送失败后,删除文件失败! 文件信息:\n" + innerFile.toString(), e);
			}
		}
	}
	
	@Override
	public String toString() {
		return innerFile.getInfoFile().getPath();
	}

	public String getBackPathFileinfo() {
		return backPathFileinfo;
	}

	public void setBackPathFileinfo(String backPathFileinfo) {
		this.backPathFileinfo = backPathFileinfo;
	}

	public String getFailedPathReceipt() {
		return failedPathReceipt;
	}

	public void setFailedPathReceipt(String failedPathReceipt) {
		this.failedPathReceipt = failedPathReceipt;
	}

	public String getFailedPathFileinfo() {
		return failedPathFileinfo;
	}

	public void setFailedPathFileinfo(String failedPathFileinfo) {
		this.failedPathFileinfo = failedPathFileinfo;
	}

	public String getSuccessPathReceipt() {
		return successPathReceipt;
	}

	public void setSuccessPathReceipt(String successPathReceipt) {
		this.successPathReceipt = successPathReceipt;
	}

	public String getSuccessPathFileinfo() {
		return successPathFileinfo;
	}

	public void setSuccessPathFileinfo(String successPathFileinfo) {
		this.successPathFileinfo = successPathFileinfo;
	}

}
