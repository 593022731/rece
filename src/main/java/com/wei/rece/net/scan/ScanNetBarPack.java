package com.wei.rece.net.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.wei.rece.ReceFile;
import com.wei.rece.net.conf.Config;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.net.file.NetReceFile;
import com.wei.rece.net.file.NetStayFile;
import com.wei.rece.transmit.Worker;

/**
 * 网吧数据流文件(打包、建立凭证文件、移包、入队列相关操作)
 * 打包方式为一个文件一个包
 * 实体源文件名为：NETBAR_Chat_20130710224939648NO.10012498
 * 凭证文件名为：(1000)NETBAR_Chat_20130710224939648NO.10012498，其中1000为数据流文件中的个数
 * zip包命名为：NETBAR_Chat_20130710224939648NO.10012498.zip
 * @author weih
 * @date 2013-7-3上午09:59:02
 * @version
 */
public class ScanNetBarPack extends AbstractScanPackService {

	public ScanNetBarPack(Setting setting, Worker working) {
		super(setting, working);
	}
	
	@Override
	public void handler(ReceFile receFile) throws IOException {
		super.reset();
		this.zipFile(receFile);
	}

	/**
	 * 文件打包
	 * @param receFile
	 */
	public void zipFile(ReceFile receFile) {
		NetReceFile traceFile = (NetReceFile)receFile;
		
		File file = traceFile.getInfoFile();
		NetStayFile netStayFile	 = new NetStayFile(traceFile, setting);
		if (!file.exists()) {
			logger.debug("NETBAR物理文件" + file.getPath() + "不存在！");
			netStayFile.backScanFile(false);//备份凭证文件到失败目录
			return;
		}
		
		boolean isSuccess = zipFile(file, traceFile.getSenderInfoFile(),null,null); // 第一步：文件打包
		if (isSuccess) {
			buildPath.delete(0, buildPath.length());
			buildPath.append("NETBAR文件").append(traceFile.getSenderInfoFile().getPath()).append("打包成功");
			logger.debug(buildPath.toString());
			
			createReceipt(traceFile);// 第二步：创建凭证文件
			addQueue(netStayFile);// 第三步：放入队列
		} else {
			buildPath.delete(0, buildPath.length());
			buildPath.append("NETBAR文件").append(traceFile.getSenderInfoFile().getPath()).append("打包失败");
			logger.debug(buildPath.toString());
		}
		netStayFile.backScanFile(isSuccess);//第四步：文件备份
	}
	
	/**
	 * 获取文件
	 * @return
	 */
	@Override
	public Collection<ReceFile> getFiles() throws IOException {
		Collection<File> receiptFiles = getReceiptFiles();//获取所有凭证文件
		if(receiptFiles!=null && !receiptFiles.isEmpty()){
			List<ReceFile> receFiles = new ArrayList<ReceFile>();
			String fileName ;
			File file;
			for(Iterator<File> it = receiptFiles.iterator();it.hasNext();){
				file = it.next();
				fileName = file.getName();
				
				NetReceFile traceF = new NetReceFile();
				traceF.setReceiptFile(file);
				//int count = Integer.valueOf(fileName.substring(fileName.indexOf("(")+1,fileName.indexOf(")"))).intValue();//数据流文件中的个数
				fileName = fileName.substring(fileName.indexOf(")")+1);
				fileName = fileName.substring(0, fileName.length() - Config.READY.length());
				
				buildPath.delete(0, buildPath.length());
				buildPath.append(setting.getFileWanPath()).append(File.separator).append(fileName);
				traceF.setInfoFile(new File(buildPath.toString()));
				
				buildPath(traceF, fileName,Config.ZIP);
				
				receFiles.add(traceF);
			}
			return receFiles;
		}
		return null;
	}
	
	/**
	 * 获取凭证文件
	 * @return
	 */
	private Collection<File> getReceiptFiles() throws IOException {
		File file = new File(setting.getReceiptWanPath());
		return FileUtils.listFiles(file, null, this.setting.isRecursive());
	}
}
