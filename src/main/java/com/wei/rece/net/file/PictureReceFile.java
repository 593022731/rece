package com.wei.rece.net.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 照片数据传输文件对象类
 * @author 		weih
 * @date   		2013-7-3下午02:57:35
 * @version     
 */
public class PictureReceFile extends NetReceFile{

	private static final long serialVersionUID = 1L;
	
	protected List<PictureReceFile> infoFiles = new ArrayList<PictureReceFile>(); //照片源文件数据集

	protected String newName; //照片新名称
	
	protected boolean zipTxtFile; //是否需要压缩txt描述文件
	
	public List<PictureReceFile> getInfoFiles() {
		return infoFiles;
	}

	public void setInfoFiles(List<PictureReceFile> infoFiles) {
		this.infoFiles = infoFiles;
	}

	public File getTxtDescFile() {
		return txtDescFile;
	}

	public void setTxtDescFile(File txtDescFile) {
		this.txtDescFile = txtDescFile;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public boolean isZipTxtFile() {
		return zipTxtFile;
	}

	public void setZipTxtFile(boolean zipTxtFile) {
		this.zipTxtFile = zipTxtFile;
	}
	
}
