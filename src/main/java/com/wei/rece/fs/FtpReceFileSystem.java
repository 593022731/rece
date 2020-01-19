package com.wei.rece.fs;

import java.io.IOException;

import com.enterprisedt.net.ftp.FTPException;
import com.wei.rece.ftp.FtpConnectionException;
import com.wei.rece.ftp.FtpPool;
import com.wei.rece.ftp.FtpServer;

public class FtpReceFileSystem implements ReceFileSystem {
	
	private String dataSource;
	
	private FtpPool ftpPool;
		
	public FtpReceFileSystem(String dataSource, FtpPool ftpPool) throws FTPException, IOException {
		super();
		this.dataSource = dataSource;
		this.ftpPool = ftpPool;
	}

	public synchronized boolean copy(String src, String target) throws Exception {
		FtpServer ftpServer = null;
		try{
			ftpServer = this.ftpPool.getFtpService(this.dataSource);
			ftpServer.uploadFile(src, target);
			return true;
		}finally{
			if(ftpServer != null){
				this.ftpPool.reverseFtpServer(this.dataSource, ftpServer);
			}
		}
	}

	public synchronized boolean deleteFile(String path) throws Exception {
		return false;
	}

	public synchronized boolean rename(String path, String newName) throws Exception {
		return false;
	}

	public synchronized boolean createDirectory(String dir) throws Exception {
		FtpServer ftpServer = null;
		try{
			ftpServer = this.ftpPool.getFtpService(this.dataSource);
			ftpServer.createDirectory(dir);
			return true;
		}finally{
			if(ftpServer != null){
				this.ftpPool.reverseFtpServer(this.dataSource, ftpServer);
			}
		}
	}

	public void init() {
	}

	public void destory() {
		try {
			this.ftpPool.getFtpService(dataSource).disconnect();
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FtpConnectionException e) {
			e.printStackTrace();
		}
	}
    
}
