package com.wei.rece.net.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.wei.rece.ReceFile;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.transmit.Worker;
import org.apache.commons.io.FileUtils;

import com.wei.rece.net.file.NetReceFile;
import com.wei.rece.net.file.NetStayFile;

/**
 * 通用扫描类(移包、建立凭证文件、入队列相关操作)
 * @author weih
 * @date 2013-7-3上午09:59:02
 * @version
 */
public class CommonScanPack extends AbstractScanPackService {

	public CommonScanPack(Setting setting, Worker working) {
		super(setting, working);
	}
	
	@Override
	public void handler(ReceFile receFile) throws IOException {
		super.reset();
		NetReceFile netReceFile = (NetReceFile)receFile;
		
		File file = netReceFile.getInfoFile();
		FileUtils.moveFile(file, netReceFile.getSenderInfoFile());//第一步：移动文件到待发送目录
		createReceipt(netReceFile);// 第二步：创建凭证文件
		addQueue(new NetStayFile(netReceFile, setting));// 第三步：放入队列
	}
	
	@Override
	public Collection<ReceFile> getFiles() throws IOException {
		File fileInfo = new File(setting.getFileWanPath());
		Collection<File> infoFiles = FileUtils.listFiles(fileInfo, null, this.setting.isRecursive());;//获取所有源数据文件
		
		if(infoFiles!=null && !infoFiles.isEmpty()){
			List<ReceFile> receFiles = new ArrayList<ReceFile>();
			String fileName ;
			File file;
			for(Iterator<File> it = infoFiles.iterator();it.hasNext();){
				file = it.next();
				fileName = file.getName();
				NetReceFile netReceF = new NetReceFile();
				netReceF.setInfoFile(file);
				buildPath(netReceF, fileName, "");
				receFiles.add(netReceF);
			}
			return receFiles;
		}
		return null;
	}
}
