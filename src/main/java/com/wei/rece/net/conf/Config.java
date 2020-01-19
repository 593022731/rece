package com.wei.rece.net.conf;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.wei.commons.lang.StringUtils;
import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.conf.BasicConfig;

/**
 * 配置文件
 * @author Jetory
 *
 */
public class Config extends BasicConfig{
	
	/*
	 * 备份配置
	 */
	public final static String ISBACKUP = "isbackup";
	
	public final static String ISSENDBAK = "issendbak";
	
	/*
	 * 文件相关配置
	 */
	
	public final static String SENDER_FILEINFO = "sender.fileinfo";
	
	public final static String SENDER_RECEIPT = "sender.receipt";
	
	public final static String SCAN_HASFILEINFO = "scan.hasfileinfo";

	/*
	 * 默认值配置
	 */
	public static String DEF_SCAN_SENDER = null;
	
	public static String DEF_ISBACKUP = null;
	
	public static String DEF_ISSENDBAK = null;
	
	public static String DEF_SCAN_RECURSIVE = null;
	
	public static String DEF_SENDER_FILEINFO = null;
	
	public static String DEF_SENDER_RECEIPT = null;
	
	public static String DEF_SCAN_HASFILEINFO = null;
	
	
	public static Map<String, Setting> settings = new HashMap<String, Setting>();
	
	public static String[] names = null;
	
	public Config(Properties p){
		super(p);
		String dataType = p.getProperty("rece-net");
		if(StringUtils.isEmpty(dataType)){
			throw new RuntimeException("发送数据类型未配置，请检查配置项(rece-net)是否正确");
		}
		names = dataType.split(",");;
		DEF_SCAN_CLASSNAME = p.getProperty(SCAN_CLASSNAME, "");
		
		DEF_SCAN_BASIC = p.getProperty(SCAN_BASIC, "");
		DEF_SCAN_FOLDER = p.getProperty(SCAN_FOLDER, "");
		
		DEF_SCAN_PICCOUNT = p.getProperty(SCAN_PICCOUNT, "30");
		
		DEF_SCAN_FILEINFO = p.getProperty(SCAN_FILEINFO, "FILEINFO");
		DEF_SCAN_RECEIPT = p.getProperty(SCAN_RECEIPT, "RECEIPT");
		DEF_SENDER_FILEINFO = p.getProperty(SENDER_FILEINFO, "FILEINFO");
		DEF_SENDER_RECEIPT = p.getProperty(SENDER_RECEIPT, "RECEIPT");
		
		DEF_SCAN_BAK = p.getProperty(SCAN_BAK, "BAK");
		DEF_SCAN_SENDER =  p.getProperty(SCAN_SENDER, "");
		DEF_SCAN_SUCCESS =  p.getProperty(SCAN_SUCCESS, "SUCCESS");
		DEF_SCAN_FAIL =  p.getProperty(SCAN_FAIL, "FAILED");
		DEF_ISBACKUP =  p.getProperty(ISBACKUP, "true");
		DEF_ISSENDBAK =  p.getProperty(ISSENDBAK, "true");
		DEF_SCAN_RECURSIVE =  p.getProperty(SCAN_RECURSIVE, "false");
		DEF_SCAN_HASFILEINFO =  p.getProperty(SCAN_HASFILEINFO, "false");
		
		for(String s : names){
			Setting setting = new Setting();
			/**
			 * ftp配置
			 */
			setting.setName(s);
			setting.setRemote(p.getProperty(s + "." + FTP_REMOTE, DEF_FTP_REMOTE));
			if(StringUtils.isEmpty(setting.getRemote())){
				throw new RuntimeException("ftp IP地址未配置，请检查配置项(ftp.remote)是否正确");
			}
			setting.setPort(p.getProperty(s + "." + FTP_PORT, DEF_FTP_PORT));
			setting.setUser(p.getProperty(s + "." + FTP_USER, DEF_FTP_USER));
			setting.setPwd(p.getProperty(s + "." + FTP_PWD, DEF_FTP_PWD));
			setting.setDir(p.getProperty(s + "." + FTP_DIR, DEF_FTP_DIR));
			setting.setTimeout(Integer.valueOf(p.getProperty(s + "." + FTP_CONTIMEOUT, Integer.toString(DEF_FTP_CONTIMEOUT))));
			setting.setPoolSize(Integer.valueOf(p.getProperty(s + "." + FTPPOOL_SIZE, Integer.toString(DEF_FTPPOOL_SIZE))));
			
			/**
			 * 通用配置
			 */
			setting.setTodayStr(DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd"));
			
			setting.setBasic(p.getProperty(s + "." + SCAN_BASIC, DEF_SCAN_BASIC));
			if(StringUtils.isEmpty(setting.getBasic())){
				throw new RuntimeException("3.4发送目录未配置，请检查配置项(scan.basic)是否正确");
			}
			setting.setFolder(p.getProperty(s + "." + SCAN_FOLDER, DEF_SCAN_FOLDER));
			
			setting.setClassName(p.getProperty(s + "." + SCAN_CLASSNAME, DEF_SCAN_CLASSNAME));
			
			setting.setHasfileinfo(Boolean.valueOf(p.getProperty(s + "." + SCAN_HASFILEINFO,DEF_SCAN_HASFILEINFO)));
			
			if(setting.isHasfileinfo()){
				setting.setFileInfo(p.getProperty(s + "." + SCAN_FILEINFO,DEF_SCAN_FILEINFO));
				setting.setFileReceipt(p.getProperty(s + "." + SCAN_RECEIPT,DEF_SCAN_RECEIPT));
			}
			
			setting.setSenderFileInfo(p.getProperty(s + "." + SENDER_FILEINFO,DEF_SENDER_FILEINFO));
			setting.setSenderFileReceipt(p.getProperty(s + "." + SENDER_RECEIPT,DEF_SENDER_RECEIPT));
			
			setting.setBak(p.getProperty(s + "." + SCAN_BAK, DEF_SCAN_BAK));
			
			setting.setPicCount(Integer.valueOf(p.getProperty(s + "." + SCAN_PICCOUNT, DEF_SCAN_PICCOUNT)));
			
			setting.setFileSuccess(p.getProperty(s + "." + SCAN_SUCCESS, DEF_SCAN_SUCCESS));
			setting.setFileFail(p.getProperty(s + "." + SCAN_FAIL, DEF_SCAN_FAIL));
			
			setting.setRecursive(Boolean.valueOf(p.getProperty(s + "." + SCAN_RECURSIVE, DEF_SCAN_RECURSIVE)));
			setting.setBackup(Boolean.valueOf(p.getProperty(s + "." + ISBACKUP, DEF_ISBACKUP)));
			setting.setSendbak(Boolean.valueOf(p.getProperty(s + "." + ISSENDBAK, DEF_ISSENDBAK)));
			
			initNetConfig(setting,p,s);
			
			settings.put(s, setting);
		}
	}
	
	/**
	 * 外网配置
	 */
	private void initNetConfig(Setting setting,Properties p,String s){
		/**
		 * 3.4发送文件目录配置
		 */
		String basicInfo = setting.getBasic() + File.separator + setting.getFolder() + File.separator;
		setting.setFileWanPath(basicInfo + setting.getFileInfo());
		setting.setReceiptWanPath(basicInfo + setting.getFileReceipt());
		
		setting.setFileWanSuccessBakPath(basicInfo +setting.getFileSuccess() + File.separator + setting.getTodayStr() + File.separator + setting.getFileInfo());
		setting.setReceiptWanSuccessBakPath(basicInfo +setting.getFileSuccess() + File.separator + setting.getTodayStr() + File.separator + setting.getFileReceipt());
		setting.setFileWanFailBakPath(basicInfo +setting.getFileFail() + File.separator + setting.getTodayStr() + File.separator + setting.getFileInfo());
		setting.setReceiptWanFailBakPath(basicInfo +setting.getFileFail() + File.separator + setting.getTodayStr() + File.separator + setting.getFileReceipt());
		
		/**
		 * ftp待发送目录配置
		 */
		setting.setSenderWan(p.getProperty(s + "." + SCAN_SENDER, DEF_SCAN_SENDER));
		if(StringUtils.isEmpty(setting.getSenderWan())){
			throw new RuntimeException("ftp待发送目录未配置，请检查配置项(scan.sender)是否正确");
		}
		setting.setSenderWanFileInfo(setting.getSenderWan() + File.separator + setting.getSenderFileInfo());
		setting.setSenderWanReceipt(setting.getSenderWan() + File.separator + setting.getSenderFileReceipt());
		
		setting.setSenderWanSuccessBakFileInfo(setting.getSenderWan() + File.separator +setting.getFileSuccess() + File.separator + setting.getTodayStr() + File.separator + setting.getSenderFileInfo());
		setting.setSenderWanSuccessBakReceipt(setting.getSenderWan() + File.separator +setting.getFileSuccess() + File.separator + setting.getTodayStr() + File.separator + setting.getSenderFileReceipt());
		setting.setSenderWanFailBakFileInfo(setting.getSenderWan() + File.separator +setting.getFileFail() + File.separator + setting.getTodayStr() + File.separator + setting.getSenderFileInfo());
		setting.setSenderWanFailBakReceipt(setting.getSenderWan() + File.separator +setting.getFileFail() + File.separator + setting.getTodayStr() + File.separator + setting.getSenderFileReceipt());
		
		/**
		 * ftp目录配置
		 */
		setting.setFtpWanFileInfo(setting.getDir() + File.separator + DEF_FTP_FILE_FOLDER);
		setting.setFtpWanReceipt(setting.getDir() + File.separator + DEF_FTP_RECEIPT_FOLDER);
		setting.setFtpWanBak(setting.getDir() + File.separator + DEF_FTP_BAK_FOLDER);
	}
	
	public String[] getNames(){
		return names;
	}
	
	public Setting[] getSettings(){
		Setting[] settings = new Setting[names.length];
		for (int i = 0; i < settings.length; i++) {
			settings[i] = this.getSetting(names[i]);
		}
		return settings;
	}
	
	public Setting getSetting(String name){
		return settings.get(name);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer()
		
		.append("Config: ")
		.append(Arrays.toString(names))
		.append("\n");
		
		for(Setting set : this.getSettings()){
			buffer.append("Name: ").append(set.getName());
			if(set != null){
				buffer.append("\n").append(set.toString());
			}
		}
		return buffer.toString();
	}
	
}
