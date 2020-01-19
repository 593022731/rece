package com.wei.rece.net.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.wei.rece.ReceFile;
import com.wei.rece.net.conf.Config;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.transmit.Worker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.net.file.NetReceFile;
import com.wei.rece.net.file.NetStayFile;


/**
 * MAC场所音视频(AV0002)文件(打包、建立凭证文件、移包、入队列相关操作)
 * 打包方式为一个文件一个包
 * 实体源文件名为：MemInfo_20130608_152000.txt 或Trace_20130620_163007.txt
 * zip包命名为：AV0002_MemInfo_20130710224939648NO.1001.zip 或AV0002_Trace_20130710224939648NO.1001.zip
 * @author weih
 * @date 2013-7-26 下午14:37:02
 * @version 1.0
 */
public class ScanAV0002Pack extends AbstractScanPackService {

	public ScanAV0002Pack(Setting setting, Worker working) {
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
	 * 文件打包
	 * @param receFile
	 */
	public void zipFile(ReceFile receFile) {
		NetReceFile macFile = (NetReceFile)receFile;
		
		File file = macFile.getInfoFile();
		NetStayFile netStayFile	 = new NetStayFile(macFile, setting);
		if (!file.exists()) {
			logger.debug("Mac(音视频AV0002)物理文件" + file.getPath() + "不存在！");
			netStayFile.backScanFile(false);//备份凭证文件到失败目录
			return;
		}
		
		boolean isSuccess = zipFile(file, macFile.getSenderInfoFile(),null,null); // 第一步：文件打包
		if (isSuccess) {
			buildPath.delete(0, buildPath.length());
			buildPath.append("Mac(音视频AV0002)文件").append(macFile.getSenderInfoFile().getPath()).append("打包成功");
			logger.debug(buildPath.toString());
			
			createReceipt(macFile);// 第二步：创建凭证文件
			addQueue(netStayFile);// 第三步：放入队列
		} else {
			buildPath.delete(0, buildPath.length());
			buildPath.append("Mac(音视频AV0002)文件").append(macFile.getSenderInfoFile().getPath()).append("打包失败");
			logger.debug(buildPath.toString());
		}
		netStayFile.backScanFile(isSuccess);//第四步：文件备份
	}
	
	private String [] prefix = {"MemInfo_","Trace_"};
	
	@Override
	public Collection<ReceFile> getFiles() throws IOException {
		Collection<File> infoFiles = FileUtils.listFiles(new File(setting.getFileWanPath()), FileFilterUtils.and(new SuffixFileFilter(".txt"), new PrefixFileFilter(prefix)),
	            (setting.isRecursive() ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE));
		
		if(infoFiles!=null && !infoFiles.isEmpty()){
			List<ReceFile> receFiles = new ArrayList<ReceFile>();
			File file;
			String fileName;
			for(Iterator<File> it = infoFiles.iterator();it.hasNext();){
				file = it.next();
				NetReceFile netReceF = new NetReceFile();
				netReceF.setInfoFile(file);
				fileName = file.getName();
				buildPath.delete(0, buildPath.length());
				buildPath.append("AV0002_").append(fileName.substring(0,fileName.indexOf("_")+1)).append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS")).append("NO.").append(getCount());
				
				buildPath(netReceF, buildPath.toString(), Config.ZIP);
				receFiles.add(netReceF);
			}
			return receFiles;
		}
		return null;
	}
}
