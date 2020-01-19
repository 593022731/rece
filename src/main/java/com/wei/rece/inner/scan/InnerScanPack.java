package com.wei.rece.inner.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.wei.rece.inner.conf.Config;
import com.wei.rece.inner.file.InnerStayFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.ReceFile;
import com.wei.rece.ScanFiles;
import com.wei.rece.conf.BasicConfig;
import com.wei.rece.inner.conf.Setting;
import com.wei.rece.inner.file.InnerReceFile;
import com.wei.rece.transmit.Worker;

/**
 * 内网分发
 * @author Jetory
 *
 */
public class InnerScanPack implements ScanFiles {

	private final static Logger logger = LoggerFactory.getLogger(InnerScanPack.class);

	protected Setting setting;

	protected Worker working;

	private String receiptPath;

	private String infoFilePath;
	
	private String backFilePath;

	/**
	 * 发送后的文件存放根路径
	 */
	private String backupBasicPath;
	
	/**
	 * 发送成功后存放文件目录
	 */
	private String successPathReceipt;
	private String successPathFileinfo;

	/**
	 * 发送失败后存放文件目录
	 */
	private String failedPathReceipt;
	private String failedPathFileinfo;

	private String backPathFileinfo;
	
	/**
	 * 待发送目录
	 */
	private String tempPathReceipt;
	
	private String todayStr;
	
	/**
	 * @param setting
	 * @param working
	 */
	public InnerScanPack(Setting setting, Worker working) {
		super();
		this.setting = setting;
		this.working = working;
		this.todayStr = DateFormatUtils.format(new Date(), "yyyyMMdd");
	
		//备份文件目录
		this.backFilePath = this.setting.getBasic() + File.separator + setting.getFolder() + File.separator + BasicConfig.DEF_FTP_BAK_FOLDER + File.separator;
		
		//凭证文件目录
		this.receiptPath = this.setting.getBasic() + File.separator
				+ setting.getFolder() + File.separator
				+ /*setting.getFileReceipt()*/ BasicConfig.DEF_FTP_RECEIPT_FOLDER;

		//实体文件目录
		this.infoFilePath = this.setting.getBasic() + File.separator
				+ setting.getFolder() + File.separator + /*setting.getFileInfo()*/ BasicConfig.DEF_FTP_FILE_FOLDER
				+ File.separator;

		logger.info("扫描目录: " + this.receiptPath);

		this.backupBasicPath = this.setting.getBackupBasic() + File.separator + setting.getFolder() + File.separator;
		
		//发送目录
		this.tempPathReceipt = this.backupBasicPath + File.separator + "TEMP" + File.separator + setting.getFileReceipt() + File.separator;
		
		File file = new File(this.backupBasicPath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(this.tempPathReceipt);
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(this.receiptPath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(this.infoFilePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(this.backFilePath);
		if(!file.exists()){
			file.mkdirs();
		}
				
		this.setting.setTempPathReceipt(this.tempPathReceipt);
		this.setting.setBackupBasicPath(this.backupBasicPath);
		
		this.setTodayDir(todayStr);
		this.init();
		
	}

	@Override
	public Collection<ReceFile> getFiles() throws IOException {
		return this.getFiles(this.receiptPath, this.infoFilePath, this.backFilePath);
	}

	/**
	 * 获取文件
	 * @param receiptPath
	 * @param infoFilePath
	 * @param backFilePath
	 * @return
	 * @throws IOException
	 */
	private Collection<ReceFile> getFiles(String receiptPath, String infoFilePath, String backFilePath) throws IOException {

		Collection<File> receiptFiles = this.getReceiptFiles(receiptPath);

		if (receiptFiles == null || receiptFiles.isEmpty()) {
			return null;
		}

		List<ReceFile> receFiles = new ArrayList<ReceFile>(receiptFiles.size());

		for (Iterator<File> it = receiptFiles.iterator(); it.hasNext();) {

			InnerReceFile innerFile = new InnerReceFile();
			innerFile.setReceiptFile(it.next());

			String name = innerFile.getReceiptFile().getName();
			innerFile
					.setInfoFile(new File(infoFilePath
							+ name.substring(0,
									name.length() - Config.READY.length())/* + BasicConfig.ZIP*/));

			if("yes".equals(this.setting.getFileBak())){
				innerFile.setBakFile(new File(backFilePath + innerFile.getInfoFile().getName() + BasicConfig.BAK));
			}
			
			receFiles.add(innerFile);

		}

		return receFiles;
	}
	
	private Collection<File> getReceiptFiles(String receiptPath) throws IOException {
		File file = new File(receiptPath);
		return FileUtils.listFiles(file, FileFilterUtils.and(new SuffixFileFilter(BasicConfig.READY), new PrefixFileFilter(setting.getPrefix())),
	            (setting.isRecursive() ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE));
	}

	@Override
	public void handler(ReceFile receFile) throws IOException {

		String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
		if(!today.equals(todayStr)){
			todayStr = today;
			this.setTodayDir(todayStr);
		}
		
		InnerReceFile innerFile = (InnerReceFile) receFile;
		
		//copy
		innerFile.getFsServer().addAll(this.setting.getFsServer());
		innerFile.getFtpServer().addAll(this.setting.getFtpServer());
		
		InnerStayFile stay = new InnerStayFile(innerFile, this.setting);
		
		stay.setFailedPathFileinfo(new String(failedPathFileinfo));
		stay.setFailedPathReceipt(new String(failedPathReceipt));
		stay.setSuccessPathReceipt(new String(successPathReceipt));
		stay.setSuccessPathFileinfo(new String(successPathFileinfo));
		stay.setBackPathFileinfo(new String(backPathFileinfo));
		
		try{
			
			File receipt = new File(tempPathReceipt
					+ innerFile.getReceiptFile().getName());
			
			if(receipt.exists()){
				if(logger.isErrorEnabled()){
					logger.error("数据文件重复: " + innerFile.getReceiptFile().getPath());
				}
				receipt.delete();
			}
			
			FileUtils.moveFile(innerFile.getReceiptFile(), receipt);
			innerFile.setReceiptFile(receipt);
			
			//读取备份文件
			if (!innerFile.getInfoFile().exists()) {
				//是否有备份
				if(innerFile.getBakFile() != null 
						&& innerFile.getBakFile().exists()){
					if(logger.isErrorEnabled()){
						logger.error("源数据文件丢失, 读取备份文件: " + innerFile.getBakFile().getPath());
					}
					FileUtils.copyFile(innerFile.getBakFile(), innerFile.getInfoFile());
				}else{
					if(logger.isErrorEnabled()){
						logger.error("在未读取到源文件情况下, 读取备份文件丢失: " + innerFile.getBakFile().getPath());
					}
					stay.failed();
					return;
				}
			}
			
			if(logger.isDebugEnabled()){
				logger.debug("将文件添加到发送队列, Receipt: " + innerFile.getReceiptFile().getPath());
			}
			
			// 发送文件
			this.working.addStayFile(stay);
			
		}catch (Exception e) {
			stay.failed();
		}
	}

	@Override
	public void init() {
		try {
			Collection<ReceFile> tempFiles = this.getFiles(this.tempPathReceipt, this.infoFilePath, this.backFilePath);
			if(tempFiles != null){
				for(ReceFile receFile : tempFiles){
					
					InnerReceFile innerFile = (InnerReceFile) receFile;
					InnerStayFile stay = new InnerStayFile(receFile, this.setting);
					stay.setFailedPathFileinfo(new String(failedPathFileinfo));
					stay.setFailedPathReceipt(new String(failedPathReceipt));
					stay.setSuccessPathReceipt(new String(successPathReceipt));
					stay.setSuccessPathFileinfo(new String(successPathFileinfo));
					stay.setBackPathFileinfo(new String(backPathFileinfo));
					
					if(logger.isDebugEnabled()){
						logger.debug("将文件添加到发送队列, Receipt: " + innerFile.getReceiptFile().getPath());
					}
					// 发送文件
					this.working.addStayFile(stay);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destory() {
		List<Setting> set = this.setting.getFtpServer();
		if(set != null){
			try {
				for(Setting s : set){
					s.getFileSystem().destory();
				}
			} catch (Exception e) {
				if(logger.isErrorEnabled()){
					logger.error("扫描系统销毁出错!", e);
				}
			}
		}
	}
	
	/**
	 * 重置备份目录
	 * @param today
	 */
	private synchronized void setTodayDir(String today){
		
		//发送成功凭证文件目录
		this.successPathReceipt = this.backupBasicPath + today + File.separator + "SUCCESS" + File.separator + setting.getFileReceipt() + File.separator;
		
		//发送成功目录
		this.successPathFileinfo = this.backupBasicPath + today + File.separator + "SUCCESS" + File.separator + setting.getFileInfo() + File.separator;

		//发送备份目录
		this.backPathFileinfo = this.backupBasicPath + today + File.separator + "BAK" + File.separator;
	
		//发送失败目录
		this.failedPathReceipt = this.backupBasicPath + today + File.separator + "FAILED" + File.separator + setting.getFileReceipt() + File.separator;

		this.failedPathFileinfo = this.backupBasicPath + today + File.separator + "FAILED" + File.separator + setting.getFileInfo() + File.separator;
		
		File file = new File(this.successPathReceipt);
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(this.successPathFileinfo);
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(this.backPathFileinfo);
		if(!file.exists()){
			file.mkdirs();
		}
			
		file = new File(this.failedPathReceipt);
		if(!file.exists()){
			file.mkdirs();
		}

		file = new File(this.failedPathFileinfo);
		if(!file.exists()){
			file.mkdirs();
		}
		
	}
	
}
