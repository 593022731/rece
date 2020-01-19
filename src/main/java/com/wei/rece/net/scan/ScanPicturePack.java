package com.wei.rece.net.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wei.rece.ReceFile;
import com.wei.rece.net.conf.Config;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.net.file.NetStayFile;
import com.wei.rece.net.file.desc.PictureInfo;
import com.wei.rece.transmit.Worker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.net.file.PictureReceFile;

/**
 * 照片文件(打包、建立凭证文件、移包、入队列相关操作)
 * 打包方式为多个文件一个包
 * 实体源文件名为:BAR_Picture_20130711173248260NO.10011.jpg
 * 描述文件名为:BAR_Picture_20130711173248260NO.10011.txt
 * 凭证文件名为:BAR_Picture_20130711173248260NO.10011.jpg.ready
 * zip包命名为：BAR_Picture_20130711173248260NO.10011.zip
 * @author weih
 * @date 2013-7-3上午09:59:02
 * @version 1.0.3
 */
public class ScanPicturePack extends AbstractScanPackService {

	public ScanPicturePack(Setting setting, Worker working) {
		super(setting, working);
	}
	
	int zipMaxSize  = setting.getPicCount();//打包图片最大数量
	
	public int count = 0; 

	/**
	 * 序号(每9999清零)
	 * @return
	 */
	public synchronized String getCount(){
		count++;
		return String.format("%04d", count); 
	}
	
	@Override
	public void handler(ReceFile receFile) throws IOException {
		super.reset();
		if(count==9999){
			count = 0;
		}
		zipFile(receFile);
	}
	
	private void zipFile(ReceFile receFile){
		List<PictureReceFile> picFiles =  ((PictureReceFile)receFile).getInfoFiles();
		
		if(picFiles!=null && !picFiles.isEmpty()){
			List<PictureReceFile> picFhFiles = new ArrayList<PictureReceFile>();//烽火照片集
			List<PictureReceFile> picCommonFiles = new ArrayList<PictureReceFile>();//其他照片集
			getZipPicList(picFiles, picFhFiles, picCommonFiles);
			
			if(!picFhFiles.isEmpty()){// 发送给接口系统的烽火数据
				buildPath.delete(0, buildPath.length());
				buildPath.append("FengHuo_Media_").append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS")).append("NO.").append(getCount());
				transact(buildPath.toString(), picFhFiles);
			}
			if(!picCommonFiles.isEmpty()){
				buildPath.delete(0, buildPath.length());
				buildPath.append("BAR_Picture").append(Config.UNDER_LINE).append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS")).append("NO.").append(getCount());
				transact(buildPath.toString(), picCommonFiles);
			}
		}
	}
	
	/**
	 * 处理获取到照片集后的操作
	 * @param fileName 打包名称
	 * @param picFiles	照片集
	 */
	private void transact(String fileName,List<PictureReceFile> picFiles){
		PictureReceFile picFile = new PictureReceFile();
		buildPath(picFile, fileName, Config.ZIP);
		
		boolean isSuccess = zipFile(picFiles, picFile.getSenderInfoFile());// 第一步：文件打包
		
		if(isSuccess){
			logger.debug("照片文件"+picFile.getSenderInfoFile().getPath()+"打包成功,照片数量为："+picFiles.size());
			
			createReceipt(picFile);// 第二步：创建凭证文件
			
			addQueue(new NetStayFile(picFile, setting));// 第三步：放入队列
		}else{
			logger.debug("照片文件"+picFile.getSenderInfoFile().getPath()+ "打包失败");
		}
		
		for(PictureReceFile pic : picFiles){// 第四步：文件备份
			NetStayFile netStayFile	 = new NetStayFile(pic, setting);
			netStayFile.backScanFile(isSuccess);
		}
	}
	
	/**
	 * 获取照片打包集合
	 * @param picFiles 源所有照片集
	 * @param picFhFiles 烽火照片集
	 * @param picCommonFiles 其他照片集
	 */
	private void getZipPicList(List<PictureReceFile> picFiles,List<PictureReceFile> picFhFiles,List<PictureReceFile> picCommonFiles){
		Map<String,String> map = new HashMap<String, String>();//用于判断其他照片集合中是否有重名照片
		for(PictureReceFile pic : picFiles){
			File file = pic.getInfoFile();
			
			NetStayFile netStayFile	 = new NetStayFile(pic, setting);
			if (!file.exists()) {
				logger.debug("照片实体文件" + file.getPath() + "不存在！");
				netStayFile.backScanFile(false);//备份到失败目录
				continue;
			}
			
			PictureInfo picInfo = new PictureInfo(pic.getTxtDescFile());
			
			if (picInfo.getPhotoType() == 35) {// 烽火照片压缩
				pic.setZipTxtFile(true); //需要压缩txt描述文件（接口系统需要）
				picFhFiles.add(pic);
			}else{//其他照片压缩
				String newName = picInfo.getFileNameTag();
				if(newName==null){
					netStayFile.backScanFile(false);//备份到失败目录
					logger.debug("照片描述信息中，没有身份号码，此照片"+pic.getInfoFile()+"被移除到失败目录！");
					continue;
				}
				if(map.containsKey(newName.trim())){
					logger.debug("此照片"+pic.getInfoFile()+"与打包文件列表中的文件名重复，等下次打包！");
					continue;
				}
				map.put(newName, "");
				pic.setNewName(newName);
				
				picCommonFiles.add(pic);
			}
		}
	}
	
	@Override
	public Collection<ReceFile> getFiles() throws IOException {
		List<File> receiptFiles = (List<File>) getReceiptFiles();
		if(receiptFiles.size() < zipMaxSize){
			return null;
		}
		List<ReceFile> receFiles = new ArrayList<ReceFile>();
		for(int i = zipMaxSize; i < receiptFiles.size() ; i += zipMaxSize){
			File file;//凭证文件
			String fileName ;//凭证文件名
			String infoFileName;//实体文件名
			String txtFileName;//描述文件名
			PictureReceFile picFile = new PictureReceFile();
			for(int j = i - zipMaxSize; j < i; j ++){
				file = receiptFiles.get(j);
				fileName = file.getName();
				PictureReceFile picF = new PictureReceFile();
				picF.setReceiptFile(file);
				
				infoFileName = fileName.substring(0,fileName.length()-Config.READY.length());
				buildPath.delete(0, buildPath.length());
				buildPath.append(setting.getFileWanPath()).append(File.separator).append(infoFileName);
				picF.setInfoFile(new File(buildPath.toString()));
				
				txtFileName = infoFileName.substring(0,infoFileName.length()-".jpg".length());
				buildPath.delete(0, buildPath.length());
				buildPath.append(setting.getFileWanPath()).append(File.separator).append(txtFileName).append(".txt");
				picF.setTxtDescFile(new File(buildPath.toString()));
				
				picFile.getInfoFiles().add(picF);
			}
			receFiles.add(picFile);
		}	
		return receFiles;
	}

	private Collection<File> getReceiptFiles() throws IOException {
		File file = new File(setting.getReceiptWanPath());
		return FileUtils.listFiles(file, new SuffixFileFilter(".jpg.ready"),
	            (setting.isRecursive() ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE));
	}
}
