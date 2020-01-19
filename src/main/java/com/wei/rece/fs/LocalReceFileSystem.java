package com.wei.rece.fs;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class LocalReceFileSystem implements ReceFileSystem {

	public boolean copy(String src, String target) throws Exception {
		try{
			File dest = new File(target);
			if(dest.exists()){
				dest.delete();
			}
			FileUtils.copyFile(new File(src), dest);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteFile(String path) throws Exception {
		return new File(path).delete();
	}

	public boolean rename(String path, String newName) throws Exception {
		File target = new File(newName);
		if(target.exists()){
			return false;
		}
		File srcFile = new File(path);
		if(!srcFile.exists()){
			return false;
		}
		FileUtils.moveFile(srcFile, target);
		return true;
	}

	public boolean createDirectory(String dir) throws Exception {
		File file = new File(dir);
		if(!file.exists()){
			return file.mkdirs();
		}
		return false;
	}

	public void init() {
	}

	public void destory() {
	}


}
