package com.wei.rece.net.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.ReceFile;
import com.wei.rece.net.conf.Config;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.net.file.NetReceFile;
import com.wei.rece.net.file.NetStayFile;
import com.wei.rece.net.file.desc.MediaInfo;
import com.wei.rece.transmit.Worker;

/**
 * 音视频文件(打包、建立凭证文件、移包、入队列相关操作)
 * 打包方式为一个文件一个包
 * 实体源文件名为:NETBAR_Media_20130705035141734NO.10011.wav或NETBAR_Media_20130705035141734NO.10011.wma
 * 描述文件名为:NETBAR_Media_20130705035141734NO.10011.txt
 * 凭证文件名为:NETBAR_Media_20130705035114932NO.10011.wav.ready或NETBAR_Media_20130705035114932NO.10011.wma.ready
 * zip包命名为：NETBAR_Media_20130705035141734NO.10011.wma.zip
 * @author weih
 * @date 2013-7-16上午09:59:02
 * @version 1.0.3
 */
public class ScanMediaPack extends AbstractScanPackService {

	public ScanMediaPack(Setting setting, Worker working) {
		super(setting, working);
	}
	
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
		this.zipFile(receFile);
	}
	
	/**
	 * 音视频文件打包
	 * @param receFile
	 */
	private void zipFile(ReceFile receFile) {
		NetReceFile mediaFile = (NetReceFile)receFile;
		
		File file = mediaFile.getInfoFile();
		
		NetStayFile netStayFile	 = new NetStayFile(mediaFile, setting);
		if (!file.exists()) {
			logger.debug("音视频实体文件" + file.getPath() + "不存在！");
			netStayFile.failed();//文件备份到失败目录
			return;
		}
		MediaInfo mediaInfo = new MediaInfo(mediaFile.getTxtDescFile());
		String fileName = file.getName();
		String newFileName = null;
		File txtFile = null;
		 // 第一步：文件打包
		if (mediaInfo.getType() == 1) {	//发送给接口系统的烽火数据
			buildPath.delete(0, buildPath.length());
			buildPath.append("FengHuo_Media_").append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS")).append("NO.").append(getCount());
			fileName = buildPath.toString();
			txtFile = mediaFile.getTxtDescFile();
		} else{	//普通的打包方式(zip包名无须改变，zip包中的文件名按规范命名)
			newFileName = mediaInfo.getFileNameTag();
		}
		
		buildPath(mediaFile, fileName,Config.ZIP);
		
		boolean isSuccess = zipFile(file,mediaFile.getSenderInfoFile(),newFileName,txtFile);
		
		if (isSuccess) {
			logger.debug("音视频文件"+mediaFile.getSenderInfoFile().getPath()+"打包成功");
			
			createReceipt(mediaFile);// 第二步：创建凭证文件
			addQueue(netStayFile);// 第三步：放入队列
		} else {
			logger.debug("音视频文件"+mediaFile.getSenderInfoFile().getPath()+"打包失败");
		}
		netStayFile.backScanFile(isSuccess);// 第四步：文件备份
	}
	
	@Override
	public Collection<ReceFile> getFiles() throws IOException {
		Collection<File> receiptFiles = getReceiptFiles();//获取所有凭证文件
		if(receiptFiles!=null && !receiptFiles.isEmpty()){
			List<ReceFile> receFiles = new ArrayList<ReceFile>();
			
			File file;//凭证文件
			String fileName ;//凭证文件名
			String infoFileName;//实体文件名
			String txtFileName;//描述文件名
			String scanType;//文件类型
			for(Iterator<File> it = receiptFiles.iterator();it.hasNext();){
				file = it.next();
				fileName = file.getName();
				NetReceFile mediaF = new NetReceFile();
				mediaF.setReceiptFile(file);
				
				infoFileName = fileName.substring(0, fileName.length()-Config.READY.length());
				buildPath.delete(0, buildPath.length());
				buildPath.append(setting.getFileWanPath()).append(File.separator).append(infoFileName);
				mediaF.setInfoFile(new File(buildPath.toString()));//实体源文件
				
				scanType = infoFileName.substring(infoFileName.lastIndexOf("."));//音视频后缀类型
				
				txtFileName = infoFileName.substring(0, infoFileName.length()-scanType.length());
				buildPath.delete(0, buildPath.length());
				buildPath.append(setting.getFileWanPath()).append(File.separator).append(txtFileName).append(".txt");
				mediaF.setTxtDescFile(new File(buildPath.toString()));//描述文件
				
				receFiles.add(mediaF);
			}
			return receFiles;
		}
		return null;
	}
	
	/**
	 * 音视频的凭证文件后缀名
	 */
	String [] suffix = new String []{".wma"+Config.READY,".wav"+Config.READY};
	
	private Collection<File> getReceiptFiles() throws IOException {
		File file = new File(setting.getReceiptWanPath());
		
		return FileUtils.listFiles(file, new SuffixFileFilter(suffix),
	            (setting.isRecursive() ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE));
	}
}
