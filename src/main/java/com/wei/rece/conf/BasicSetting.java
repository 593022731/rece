package com.wei.rece.conf;

import java.io.Serializable;

import com.wei.rece.fs.FileSystemFactory;
import com.wei.rece.fs.ReceFileSystem;

public class BasicSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 需要发送ftp的数据(多数据已逗号隔开)
	 */
	private String name;
	
	/**
	 * ftp ip
	 */
	private String remote;
	
	/**
	 * ftp 端口
	 */
	private String port;
	
	/**
	 * ftp 用户名
	 */
	private String user;
	
	/**
	 * ftp 密码
	 */
	private String pwd;
	
	/**
	 * 存放文件ftp工作目录或者文件系统 
	 */
	private String dir;

	/**
	 * 使用的文件系统
	 * ftp
	 * local
	 */
	private String fs = "ftp";
	
	private int poolSize;
	
	private String encoding = "GBK";
	
	private int timeout;
	
	private String ftpFileFolder;
	
	private String ftpReceiptFolder;
	
	protected ReceFileSystem fileSystem = null;
	
	public ReceFileSystem getFileSystem() throws Exception{
		if(fileSystem == null){
				fileSystem = FileSystemFactory.getFileSystem(this, true);
		}
		return fileSystem;
	}

	public String getFs() {
		return fs;
	}

	public void setFs(String fs) {
		this.fs = fs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setFileSystem(ReceFileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}
	
	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getFtpFileFolder() {
		return ftpFileFolder;
	}

	public void setFtpFileFolder(String ftpFileFolder) {
		this.ftpFileFolder = ftpFileFolder;
	}

	public String getFtpReceiptFolder() {
		return ftpReceiptFolder;
	}

	public void setFtpReceiptFolder(String ftpReceiptFolder) {
		this.ftpReceiptFolder = ftpReceiptFolder;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("\t remote: ").append(remote)
				.append("\n").append("\t port  : ").append(port).append("\n")
				.append("\t user  : ").append(user).append("\n")
				.append("\t dir   : ").append(dir).append("\n")
				.toString();
	}

}
