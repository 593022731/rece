package com.wei.rece.inner.conf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.wei.rece.conf.BasicConfig;

/**
 * 配置文件
 * @author Jetory
 *
 */
public class Config extends BasicConfig{
	
	public static Map<String, Setting> settings = new HashMap<String, Setting>();
	
	public static Map<String, Setting> ftpSettings = new HashMap<String, Setting>();
	
	public static String[] names = null;
	
	public final static String SCAN_PREFIX_NAME = "scan.prefix";
	public final static String BACKUP_BASIC = "backup.basic";
	
	public final static String SENDFTP_FILEINFO = "sendftp.fileinfo";
	public final static String SENDFTP_RECEIPT = "sendftp.receipt";
	public final static String SENDFS_FILEINFO = "sendfs.fileinfo";
	public final static String SENDFS_RECEIPT = "sendfs.receipt";
	
	public static String DEF_SENDFTP_FILEINFO = null;
	public static String DEF_SENDFTP_RECEIPT = null;
	public static String DEF_SENDFS_FILEINFO = null;
	public static String DEF_SENDFS_RECEIPT = null;
	
	public static String DEF_BACKUP_BASIC = null;
	
	public Config(Properties p){
		super(p);
		names = p.getProperty("rece-inner", "").split(",");
		
		DEF_SCAN_CLASSNAME = p.getProperty(SCAN_CLASSNAME, "com.wei.rece.inner.scan.InnerScanPack");
		
		DEF_SCAN_BASIC = p.getProperty(SCAN_BASIC, "");
		DEF_SCAN_FOLDER = p.getProperty(SCAN_FOLDER, "");
		DEF_SCAN_RECEIPT = p.getProperty(SCAN_RECEIPT, "RECEIPT");
		DEF_SCAN_FILEINFO = p.getProperty(SCAN_FILEINFO, "FILE");
		DEF_SCAN_BAK = p.getProperty(SCAN_BAK, "");
		DEF_SCAN_SUCCESS =  p.getProperty(SCAN_SUCCESS, "SUCCESS");
		DEF_SCAN_FAIL =  p.getProperty(SCAN_FAIL, "FAILED");
		DEF_BACKUP_BASIC =  p.getProperty(BACKUP_BASIC, FileUtils.getUserDirectoryPath());
		
		DEF_SENDFTP_FILEINFO = p.getProperty(SENDFTP_FILEINFO, "FILE");
		DEF_SENDFTP_RECEIPT = p.getProperty(SENDFTP_RECEIPT, "RECEIPT");
		DEF_SENDFS_FILEINFO = p.getProperty(SENDFS_FILEINFO, "FILE");
		DEF_SENDFS_RECEIPT = p.getProperty(SENDFS_RECEIPT, "RECEIPT");
		
		String[] ftps = p.getProperty("inner.ftp", "").split(",");
		for(String ftp : ftps){
			Setting setting = new Setting();
			setting.setFs("ftp");
			setting.setRemote(p.getProperty(ftp + "." + FTP_REMOTE, DEF_FTP_REMOTE));
			setting.setPort(p.getProperty(ftp + "." + FTP_PORT, DEF_FTP_PORT));
			setting.setUser(p.getProperty(ftp + "." + FTP_USER, DEF_FTP_USER));
			setting.setPwd(p.getProperty(ftp + "." + FTP_PWD, DEF_FTP_PWD));
			setting.setDir(p.getProperty(ftp + "." + FTP_DIR, DEF_FTP_DIR));
			setting.setTimeout(Integer.valueOf(p.getProperty(ftp + "." + FTP_CONTIMEOUT, Integer.toString(DEF_FTP_CONTIMEOUT))));
			setting.setPoolSize(Integer.valueOf(p.getProperty(ftp + "." + FTPPOOL_SIZE, Integer.toString(DEF_FTPPOOL_SIZE))));
			
			setting.setFtpFileFolder(p.getProperty(ftp + "." + SENDFTP_FILEINFO, DEF_SENDFTP_FILEINFO));
			setting.setFtpReceiptFolder(p.getProperty(ftp + "." + SENDFTP_RECEIPT, DEF_SENDFTP_RECEIPT));
			
			ftpSettings.put(ftp, setting);
		}
		
		for(String s : names){
			Setting setting = new Setting();
			setting.setClassName(p.getProperty(s + "." + SCAN_CLASSNAME, DEF_SCAN_CLASSNAME));
			setting.setBasic(p.getProperty(s + "." + SCAN_BASIC, DEF_SCAN_BASIC));
			setting.setFolder(p.getProperty(s + "." + SCAN_FOLDER, DEF_SCAN_FOLDER));
			setting.setFileInfo(p.getProperty(s + "." + SCAN_FILEINFO, DEF_SCAN_FILEINFO));
			setting.setFileReceipt(p.getProperty(s + "." + SCAN_RECEIPT, DEF_SCAN_RECEIPT));
			setting.setFileSuccess(p.getProperty(s + "." + SCAN_SUCCESS, DEF_SCAN_SUCCESS));
			setting.setFileFail(p.getProperty(s + "." + SCAN_FAIL, DEF_SCAN_FAIL));
			setting.setFileBak(p.getProperty(s + "." + SCAN_BAK, DEF_SCAN_BAK));
			setting.setRecursive(Boolean.valueOf(p.getProperty(s + "." + "scan.recursive", "false")));
			String prefix = p.getProperty(s + "." + SCAN_PREFIX_NAME, "");
			if(prefix.length() > 0){
				setting.setPrefix(prefix.split(","));
			}
			setting.setBackupBasic(p.getProperty(s + "." + BACKUP_BASIC, DEF_BACKUP_BASIC));
			
			/**
			 * ftp配置
			 */
			setting.setName(s);
			String[] str = p.getProperty(s + "." + "transmit", "").split(",");
			List<Setting> ftpServer = new ArrayList<Setting>();
			List<Setting> fsServer = new ArrayList<Setting>();
			for(String st : str){
				if(st.startsWith("ftp_")){
					Setting set = ftpSettings.get(st.replace("ftp_", ""));
					if(set != null){
						Setting ftp = new Setting();
						ftp.setFs("ftp");
						ftp.setName(s);
						ftp.setRemote(set.getRemote());
						ftp.setPort(set.getPort());
						ftp.setUser(set.getUser());
						ftp.setPwd(set.getPwd());
						ftp.setDir(set.getDir());
						ftp.setTimeout(set.getTimeout());
						ftp.setPoolSize(set.getPoolSize());
						ftp.setFtpFileFolder(set.getFtpFileFolder());
						ftp.setFtpReceiptFolder(set.getFtpReceiptFolder());
						ftpServer.add(ftp);
						
						String info = set.getDir() + "/" /*+ setting.getFolder() + "/"*/ + /*setting.getFileInfo()*/set.getFtpFileFolder();
						String rece = set.getDir() + "/" /*+ setting.getFolder() + "/"*/ + /*setting.getFileReceipt()*/set.getFtpReceiptFolder();
						
						try {
							ftp.getFileSystem().createDirectory(info);
							ftp.getFileSystem().createDirectory(rece);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}else{
					Setting set = new Setting();
					set.setName(s);
					set.setFs("local");
					set.setDir(st);
					set.setFsFileFolder(DEF_SENDFS_FILEINFO);
					set.setFsReceiptFolder(DEF_SENDFS_RECEIPT);
					
					fsServer.add(set);
					
					String basic = st + File.separator;
					String info = basic + set.getFsFileFolder();//setting.getFileInfo() ;
					String rece = basic + set.getFsReceiptFolder();//setting.getFileReceipt() ;
					
					try {
						set.getFileSystem().createDirectory(info);
						set.getFileSystem().createDirectory(rece);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			setting.setFtpServer(ftpServer);
			setting.setFsServer(fsServer);
			settings.put(s, setting);
		}
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
