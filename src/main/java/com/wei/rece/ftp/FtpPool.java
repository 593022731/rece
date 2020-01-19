package com.wei.rece.ftp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.enterprisedt.net.ftp.FTPException;

public class FtpPool {

	private boolean active = true;
	
	public FtpPool(boolean active){
		this.active = active;
	}
	private static Map<String, BlockingQueue<FtpServer>> ftpServier = new HashMap<String, BlockingQueue<FtpServer>>();
	
	public synchronized boolean addFtpService(String name, FtpServer ftp){
		BlockingQueue<FtpServer> queue = ftpServier.get(name);
		if(queue == null){
			queue = new ArrayBlockingQueue<FtpServer>(1000);
			ftpServier.put(name, queue);
		}
		queue.add(ftp);
		return true;
	}
	
	public FtpServer getFtpService(String name) throws FtpConnectionException{
		
		FtpServer server = null;
		
		try {
			BlockingQueue<FtpServer> queue = ftpServier.get(name);
			if(queue != null){
				server = queue.poll(30, TimeUnit.SECONDS);
				if(server != null){
					server.resetCollection();
				}else{
					throw new FtpConnectionException("Ftp连接超过最大值, Ftp获取名: " + name);
				}
				return server;
			}else{
				throw new FtpConnectionException("没有找到Ftp连接池, Ftp获取名: " + name);
			}
		} catch (InterruptedException e) {
			if(server != null){
				this.reverseFtpServer(name, server);
			}
			throw new FtpConnectionException("Ftp连接超过最大值, Ftp获取名: " + name, e);
		} catch (FTPException e) {
			if(server != null){
				this.reverseFtpServer(name, server);
			}
			throw new FtpConnectionException("Ftp异常, Ftp获取名: " + name, e);
		} catch (Exception e) {
			if(server != null){
				this.reverseFtpServer(name, server);
			}
			throw new FtpConnectionException("Ftp异常, Ftp获取名: " + name, e);
		}
	}
	
	public void reverseFtpServer(String name, FtpServer ftp){
		try {
			this.addFtpService(name, ftp);
			if(!this.active){
				ftp.disconnect();
			}
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
