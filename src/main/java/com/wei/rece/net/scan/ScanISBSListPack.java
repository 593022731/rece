package com.wei.rece.net.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.wei.rece.ReceFile;
import com.wei.rece.inner.conf.Config;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.net.file.NetReceFile;
import com.wei.rece.net.file.NetStayFile;
import com.wei.rece.transmit.Worker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.wei.commons.time.DateFormatUtils;

/**
 * 非经网吧场所文件(打包、建立凭证文件、移包、入队列相关操作)
 * 打包方式为一个文件一个包
 * 实体源文件名为：ISBSList_20130717011024538.xml
 * zip包文件名为：ISBSList_LOCATIONSYNC_NETBAR_20130717011024567NO.1001.zip
 * @author weih
 * @date 2013-7-17 下午19:40:02
 * @version
 */
public class ScanISBSListPack extends AbstractScanPackService {

	public ScanISBSListPack(Setting setting, Worker working) {
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
		NetReceFile traceFile = (NetReceFile)receFile;
		
		File file = traceFile.getInfoFile();
		
		boolean isSuccess = zipFile(file, traceFile.getSenderInfoFile(),null,null); // 第一步：文件打包
		NetStayFile netStayFile	 = new NetStayFile(receFile, setting);
		if (isSuccess) {
			buildPath.delete(0, buildPath.length());
			buildPath.append("非经场所文件").append(traceFile.getSenderInfoFile().getPath()).append("打包成功");
			logger.debug(buildPath.toString());
			
			createReceipt(traceFile);// 第二步：创建凭证文件
			addQueue(netStayFile);// 第三步：放入队列
		} else {
			buildPath.delete(0, buildPath.length());
			buildPath.append("非经场所件").append(traceFile.getSenderInfoFile().getPath()).append("打包失败");
			logger.debug(buildPath.toString());
		}
		netStayFile.backScanFile(isSuccess);// 第四步：文件备份
	}
	
	private String [] prefix = {"ISBSList_","PoliceStation_"};
	
	@Override
	public Collection<ReceFile> getFiles() throws IOException {
		Collection<File> infoFiles = FileUtils.listFiles(new File(setting.getFileWanPath()), FileFilterUtils.and(new SuffixFileFilter(".xml"), new PrefixFileFilter(prefix)),
	            (setting.isRecursive() ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE));
		
		if(infoFiles!=null && !infoFiles.isEmpty()){
			List<ReceFile> receFiles = new ArrayList<ReceFile>();
			File file;
			for(Iterator<File> it = infoFiles.iterator();it.hasNext();){
				file = it.next();
				NetReceFile netReceF = new NetReceFile();
				netReceF.setInfoFile(file);
				
				buildPath.delete(0, buildPath.length());
				buildPath.append("ISBSList_LOCATIONSYNC_NETBAR_").append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS")).append("NO.").append(getCount());
				
				buildPath(netReceF, buildPath.toString(), Config.ZIP);
				receFiles.add(netReceF);
			}
			return receFiles;
		}
		return null;
	}
}
