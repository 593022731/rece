package com.wei.rece.net.file;

import java.io.File;
import java.io.IOException;

import com.wei.rece.ReceFile;
import com.wei.rece.inner.conf.Config;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.transmit.StayFile;
import org.apache.commons.io.FileUtils;

/**
 * 外网ftp发送zip包类
 * @author 		weih
 * @date   		2013-7-3下午02:00:18
 * @version  1.0.3   
 */
public class NetStayFile extends StayFile {
	
	private static final long serialVersionUID = 1L;

	NetReceFile netReceFile = null;
	
	Setting setting;
	
	public NetStayFile(ReceFile receFile, Setting setting){
		super(receFile, setting);
		netReceFile = (NetReceFile)receFile;
		this.setting = setting;
	}
	
	protected StringBuilder buildPath = new StringBuilder(); //构建路径 缓冲区
	
	@Override
	public void execute() throws Exception {
		super.getFileSystem().copy(netReceFile.getSenderInfoFile().getPath(),netReceFile.getFtpInfoFile().getPath());
		if(setting.isSendbak()){
			super.getFileSystem().copy(netReceFile.getSenderInfoFile().getPath(), setting.getFtpWanBak()+File.separator+netReceFile.getFtpInfoFile().getName()+ Config.BAK);
		}
		super.getFileSystem().copy(netReceFile.getSenderReceiptFile().getPath(), netReceFile.getFtpReceiptFile().getPath());
	}
	
	@Override
	public void call() {
		backFile(true);//备份成功目录
	}
	
	@Override
	public void failed() {
		backFile(false);//备份失败目录
	}
	
	/**
	 * 备份扫描文件
	 * @param isSuccess
	 */
	public void backScanFile(boolean isSuccess){
		try{
			if (setting.isBackup()) { // 是否备份
				if(netReceFile.getInfoFile()!=null && netReceFile.getInfoFile().exists()){
					buildPath.delete(0, buildPath.length());
					buildPath.append(isSuccess ? setting.getFileWanSuccessBakPath() : setting.getFileWanFailBakPath()).append(File.separator).append(netReceFile.getInfoFile().getName());
					backFile(netReceFile.getInfoFile(),new File(buildPath.toString()));
				}
				if(netReceFile.getReceiptFile()!=null && netReceFile.getReceiptFile().exists()){
					buildPath.delete(0, buildPath.length());
					buildPath.append(isSuccess ? setting.getReceiptWanSuccessBakPath() : setting.getReceiptWanFailBakPath()).append(File.separator).append(netReceFile.getReceiptFile().getName());
					backFile(netReceFile.getReceiptFile(),new File(buildPath.toString()));
				}
				if(netReceFile.getTxtDescFile()!=null && netReceFile.getTxtDescFile().exists()){ //存在描述文件
					buildPath.delete(0, buildPath.length());
					buildPath.append(isSuccess ? setting.getFileWanSuccessBakPath() : setting.getFileWanFailBakPath()).append(File.separator).append(netReceFile.getTxtDescFile().getName());
					backFile(netReceFile.getTxtDescFile(),new File(buildPath.toString()));
				}
			} else {
				if(netReceFile.getInfoFile()!=null && netReceFile.getInfoFile().exists()){
					netReceFile.getInfoFile().delete(); // 删除源实体文件
				}
				if (netReceFile.getReceiptFile()!=null && netReceFile.getReceiptFile().exists()) {
					netReceFile.getReceiptFile().delete();// 删除凭证文件
				}
				if(netReceFile.getTxtDescFile()!=null && netReceFile.getTxtDescFile().exists()){
					netReceFile.getTxtDescFile().delete(); //删除描述文件
				}
			}
			
		}catch (Exception e) {
			logger.error("",e);
		}
	}
	
	/**
	 * 备份待发送文件
	 * @param isSuccess
	 */
	private void backFile(boolean isSuccess){
		try{
			if (setting.isBackup()) { // 是否备份
				if(netReceFile.getSenderInfoFile()!=null && netReceFile.getSenderInfoFile().exists()){
					buildPath.delete(0, buildPath.length());
					buildPath.append(isSuccess ? setting.getSenderWanSuccessBakFileInfo() : setting.getSenderWanFailBakFileInfo()).append(File.separator).append(netReceFile.getSenderInfoFile().getName());
					backFile(netReceFile.getSenderInfoFile(),new File(buildPath.toString()));
				}
				if(netReceFile.getSenderReceiptFile()!=null && netReceFile.getSenderReceiptFile().exists()){
					buildPath.delete(0, buildPath.length());
					buildPath.append(isSuccess ? setting.getSenderWanSuccessBakReceipt() : setting.getSenderWanFailBakReceipt()).append(File.separator).append(netReceFile.getSenderReceiptFile().getName());
					backFile(netReceFile.getSenderReceiptFile(),new File(buildPath.toString()));
				}
			} else {
				if(netReceFile.getSenderInfoFile()!=null && netReceFile.getSenderInfoFile().exists()){
					netReceFile.getSenderInfoFile().delete(); // 删除待发送实体文件
				}
				if(netReceFile.getSenderReceiptFile()!=null && netReceFile.getSenderReceiptFile().exists()){
					netReceFile.getSenderReceiptFile().delete(); // 删除待发送凭证文件
				}
			}
			
		}catch (Exception e) {
			logger.error("",e);
		}
	}
	

	/**
	 * 文件备份
	 * @param srcFile 源文件
	 * @param backFile 备份文件
	 */
	private void backFile(File srcFile,File backFile){
		if(!srcFile.exists()){
			logger.debug("文件"+srcFile.getPath()+"不存在，无法备份！");
			return;
		}
		try {
			if (backFile.exists()) {
				backFile.delete();
			}
			FileUtils.moveFile(srcFile, backFile);
		} catch (IOException e) {
			logger.error("备份文件"+srcFile.getPath()+"到"+backFile.getPath()+"失败", e);
		}
	}

}
