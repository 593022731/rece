package com.wei.rece.conf;

import java.util.Properties;

public class BasicConfig {

	/**
	 * 文件名分隔符
	 */
	public static final String UNDER_LINE = "_";
	/**
	 * 区域编码
	 */
	public static String AREACODE = "";
	
	/**
	 * 文件后缀配置
	 */
	public final static String ZIP = ".zip";
	public final static String READY = ".ready";
	public final static String BAK = ".bak";
	
	/*
	 * 传输打包文件配置
	 */
	public final static String SCAN_BASIC = "scan.basic";
	
	public final static String SCAN_FOLDER = "scan.folder";
	
	public static String SCAN_CLASSNAME = "scan.className";
	
	public final static String SCAN_RECEIPT = "scan.receipt";
	
	public final static String SCAN_FILEINFO = "scan.fileinfo";
	
	public final static String SCAN_BAK = "scan.bak";
	
	public final static String SCAN_SENDER = "scan.sender";
	
	public final static String SCAN_SUCCESS = "backup.success";
	
	public final static String SCAN_FAIL = "backup.fail";
	
	public static String SCAN_RECURSIVE = "scan.recursive";
	
	public static String SCAN_PICCOUNT = "scan.piccount";
	
	/*
	 * FTP配置名称
	 */
	public final static String FTP_REMOTE = "ftp.remote";
	
	public final static String FTP_PORT = "ftp.port";
	
	public final static String FTP_USER = "ftp.user";
	
	public final static String FTP_PWD = "ftp.password";
	
	public final static String FTP_DIR = "ftp.directory";
	
	public final static String WORING_CORE = "working.core";
	
	public final static String FTPPOOL_SIZE = "ftp.pool";
	
	public final static String FTP_CONTIMEOUT = "ftp.timeout";
	
	/**
	 * 外网发送ftp目录配置或内网扫描接收数据目录配置
	 */
	public final static String FTP_FILE_FOLDER = "ftp.fileinfo";
	public final static String FTP_RECEIPT_FOLDER = "ftp.receipt";
	public final static String FTP_BAK_FOLDER = "ftp.bak";
	
	public final static String FTP_SENDER_MAXQUEUE = "ftp.sender.maxQueue";
	
	/*
	 * 默认值配置
	 */
	public static String DEF_FTP_REMOTE = null;
	
	public static String DEF_FTP_PORT = null;
	
	public static String DEF_FTP_USER = null;
	
	public static String DEF_FTP_PWD = null;
	
	public static String DEF_FTP_DIR = null;
	
	public static Integer DEF_WORING_CORE = null;
	
	public static Integer DEF_FTPPOOL_SIZE = null;
	
	public static Integer DEF_FTP_CONTIMEOUT = null;
	
	public static String DEF_FTP_FILE_FOLDER = null;
	
	public static String DEF_FTP_RECEIPT_FOLDER = null;
	
	public static String DEF_FTP_BAK_FOLDER = null;
	
	/*
	 * 默认配置
	 */
	public static String DEF_SCAN_CLASSNAME = null;
	
	public static String DEF_SCAN_BASIC = null;
	
	public static String DEF_SCAN_FOLDER = null;
	
	public static String DEF_SCAN_RECEIPT = null;
	
	public static String DEF_SCAN_FILEINFO = null;
	
	public static String DEF_SCAN_BAK = null;
	
	public static String DEF_SCAN_SENDER = null;
	
	public static String DEF_SCAN_SUCCESS = null;
	
	public static String DEF_SCAN_FAIL = null;
	
	public static String DEF_SCAN_PICCOUNT = null;
	
	/**
	 * ftp发送队列最大数量
	 */
	public static int DEF_FTP_SENDER_MAXQUEUE;
	
	public BasicConfig(Properties p){
		DEF_FTP_REMOTE = p.getProperty(FTP_REMOTE);
		DEF_FTP_PORT = p.getProperty(FTP_PORT, "21");
		DEF_FTP_USER = p.getProperty(FTP_USER, "");
		DEF_FTP_PWD = p.getProperty(FTP_PWD, "");
		DEF_FTP_DIR = p.getProperty(FTP_DIR, "");
		DEF_WORING_CORE = Integer.valueOf(p.getProperty(WORING_CORE, "5"));
		DEF_FTPPOOL_SIZE = Integer.valueOf(p.getProperty(FTPPOOL_SIZE, Integer.toString(DEF_WORING_CORE)));
		DEF_FTP_CONTIMEOUT = Integer.valueOf(p.getProperty(FTP_CONTIMEOUT, Integer.toString(1000 * 60)));
		
		DEF_FTP_FILE_FOLDER = p.getProperty(FTP_FILE_FOLDER, "FILEINFO");
		DEF_FTP_RECEIPT_FOLDER = p.getProperty(FTP_RECEIPT_FOLDER, "RECEIPT");
		DEF_FTP_BAK_FOLDER = p.getProperty(FTP_BAK_FOLDER, "BAK");
		
		try{
			DEF_FTP_SENDER_MAXQUEUE = Integer.valueOf(p.getProperty(FTP_SENDER_MAXQUEUE, "10000"));
		}catch (Exception e) {
			DEF_FTP_SENDER_MAXQUEUE = 10000;
			e.printStackTrace();
		}
	}
}
