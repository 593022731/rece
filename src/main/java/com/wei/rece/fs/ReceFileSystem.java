package com.wei.rece.fs;


public interface ReceFileSystem {

	public void init();
	
	public boolean copy(String src, String target) throws Exception;
	
	public boolean deleteFile(String path) throws Exception;
	
	public boolean rename(String path, String newName) throws Exception;
	
	public boolean createDirectory(String dir) throws Exception;
	
	public void destory();
	
}
