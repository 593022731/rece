package com.wei.rece.net.scan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.wei.rece.net.conf.annotation.BackUpDirComment;
import com.wei.rece.net.conf.annotation.DirComment;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.ReceFile;
import com.wei.rece.ScanFiles;
import com.wei.rece.net.conf.Config;
import com.wei.rece.net.conf.Setting;
import com.wei.rece.net.file.NetReceFile;
import com.wei.rece.net.file.NetStayFile;
import com.wei.rece.net.file.PictureReceFile;
import com.wei.rece.transmit.Worker;

/**
 * 抽象类(扫描数据、初始化文件夹、tomcat初始化、重置目录、创建凭证文件、打包、添加队列、构建ftp路径等相关操作)
 * @author 		weih
 * @date   		2013-7-3下午02:06:45
 * @version     1.0.3.7
 */
public abstract class AbstractScanPackService implements ScanFiles {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Setting setting;
	
	protected Worker working;
	
	static boolean initFlag = true;//初始化扫描ftp待发送目录标识(只扫一次)
	
	protected StringBuilder buildPath = new StringBuilder(); //构建路径缓冲区
	
	public AbstractScanPackService(Setting setting, Worker working) {
		this.setting = setting;
		this.working = working;
		initDir();
		initBakDir();
		if(initFlag){
			init(); //初始化扫描ftp待发送目录(只扫一次)
			initFlag = false;
		}
	}
	
	public abstract Collection<ReceFile> getFiles() throws IOException;
	public abstract void handler(ReceFile receFile) throws IOException;
	
	private String nowTime = DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd"); //当前时间
	
	/*
	 * 重置操作
	 */
	public void reset(){
		nowTime = DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd");
		if(!nowTime.equals(setting.getTodayStr())){//一天创建一次备份文件夹
			logger.debug("日期："+nowTime+"开始重置操作");
			resetPath();
			initBakDir();
			setting.setTodayStr(nowTime);
		}
	}
	
	//目录重置(每天重置一次)
	private void resetPath(){
		String basicInfo = setting.getBasic() + File.separator + setting.getFolder() + File.separator;
		setting.setFileWanSuccessBakPath(basicInfo +setting.getFileSuccess() + File.separator + nowTime + File.separator + setting.getFileInfo());
		setting.setReceiptWanSuccessBakPath(basicInfo +setting.getFileSuccess() + File.separator + nowTime + File.separator + setting.getFileReceipt());
		setting.setFileWanFailBakPath(basicInfo +setting.getFileFail() + File.separator + nowTime + File.separator + setting.getFileInfo());
		setting.setReceiptWanFailBakPath(basicInfo +setting.getFileFail() + File.separator + nowTime + File.separator + setting.getFileReceipt());
		
		setting.setSenderWanSuccessBakFileInfo(setting.getSenderWan() + File.separator +setting.getFileSuccess() + File.separator + nowTime + File.separator + setting.getSenderFileInfo());
		setting.setSenderWanSuccessBakReceipt(setting.getSenderWan() + File.separator +setting.getFileSuccess() + File.separator + nowTime + File.separator + setting.getSenderFileReceipt());
		setting.setSenderWanFailBakFileInfo(setting.getSenderWan() + File.separator +setting.getFileFail() + File.separator + nowTime + File.separator + setting.getSenderFileInfo());
		setting.setSenderWanFailBakReceipt(setting.getSenderWan() + File.separator +setting.getFileFail() + File.separator + nowTime + File.separator + setting.getSenderFileReceipt());
	}
	
	/**
	 * 初始化备份文件夹
	 */
	private void initBakDir(){
		Field [] fields = setting.getClass().getDeclaredFields();
		for(Field f : fields){
			if(f.isAnnotationPresent(BackUpDirComment.class)){
				f.setAccessible(true);
				try{
					String path = (String)f.get(setting);
					File file = new File(path);
					if(!file.exists()){
						file.mkdirs();
					}
				}catch (Exception e) {
					logger.error("",e);
				}
			}
		}
	}
	
	/**
	 * 初始化系统文件夹
	 */
	private void initDir(){
		Field [] fields = setting.getClass().getDeclaredFields();
		for(Field f : fields){
			if(f.isAnnotationPresent(DirComment.class)){
				f.setAccessible(true);
				try{
					String path = (String)f.get(setting);
					
					File file = new File(path);
					if(!file.exists()){
						file.mkdirs();
					}
				}catch (Exception e) {
					logger.error("",e);
				}
			}
		}
		
		try {
			logger.debug("创建ftp文件夹"+setting.getFtpWanFileInfo());
			logger.debug("创建ftp文件夹"+setting.getFtpWanBak());
			logger.debug("创建ftp文件夹"+setting.getFtpWanReceipt());
			setting.getFileSystem().createDirectory(setting.getFtpWanFileInfo());
			setting.getFileSystem().createDirectory(setting.getFtpWanBak());
			setting.getFileSystem().createDirectory(setting.getFtpWanReceipt());
		} catch (Exception e) {
			logger.error("创建ftp文件夹错误:",e);
		}
	}
	
	/**
	 * 构建待发送目录和ftp目录
	 * @param netReceF  外网公用数据传输文件对象
	 * @param fileName 源文件名
	 * @param zip zip文件名(可以为空"")
	 */
	protected void buildPath(NetReceFile netReceF,String fileName,String zip){
		buildPath.delete(0, buildPath.length());
		buildPath.append(setting.getSenderWanFileInfo()).append(File.separator).append(fileName).append(zip);
		netReceF.setSenderInfoFile(new File(buildPath.toString()));
		
		buildPath.delete(0, buildPath.length());
		buildPath.append(setting.getSenderWanReceipt()).append(File.separator).append(fileName).append(zip).append(Config.READY);
		netReceF.setSenderReceiptFile(new File(buildPath.toString()));
		
		buildPath.delete(0, buildPath.length());
		buildPath.append(setting.getFtpWanFileInfo()).append(File.separator).append(fileName).append(zip);
		netReceF.setFtpInfoFile(new File(buildPath.toString()));
		
		buildPath.delete(0, buildPath.length());
		buildPath.append(setting.getFtpWanReceipt()).append(File.separator).append(fileName).append(zip).append(Config.READY);
		netReceF.setFtpReceiptFile(new File(buildPath.toString()));
	}
	
	/**
	 * 创建凭证文件
	 * 
	 * @param receFile
	 *            外网公用数据传输文件对象类
	 */
	protected void createReceipt(NetReceFile receFile) {
		try {
			if(!receFile.getSenderReceiptFile().exists()){
				receFile.getSenderReceiptFile().createNewFile();
				logger.debug("凭证文件" + receFile.getSenderReceiptFile().getPath() + "创建成功！");
			}
		} catch (IOException e) {
			logger.error("凭证文件" + receFile.getSenderReceiptFile().getPath() + "创建失败！",e);
		}
	}

	/**
	 * 放入队列
	 * 
	 * @param netStayFile
	 *            外网ftp发送zip包类
	 */
	protected void addQueue(NetStayFile netStayFile) {
		working.addStayFile(netStayFile);
	}
	
	/**
	 * 文件打成zip包
	 * @param srcFile 源文件
	 * @param zipFile zip文件
	 * @param newName zip包新名称
	 * @param txtFile txt描述文件
	 * @return
	 */
	protected boolean zipFile(File srcFile, File zipFile,String newName,File txtFile) {
		try {
			FileOutputStream fileOut = new FileOutputStream(zipFile);
			ZipOutputStream outputStream = new ZipOutputStream(fileOut);
			outputStream.setLevel(ZipOutputStream.DEFLATED);
			zipFile(outputStream, srcFile, newName);
			if(txtFile!=null){//需要压缩txt文件
				zipFile(outputStream, txtFile, newName);
			}
			if (outputStream != null) {
				outputStream.close();
			}
			return true;
		} catch (Exception e) {
			logger.error("打包异常：",e);
		}
		return false;
	}
	
	/**
	 * 照片文件列表打成zip包
	 * @param srcFiles 源文件列表
	 * @param zipFile zip文件
	 * @return
	 */
	protected boolean zipFile(List<PictureReceFile> srcFiles, File zipFile) {
		try {
			FileOutputStream fileOut = new FileOutputStream(zipFile);
			ZipOutputStream outputStream = new ZipOutputStream(fileOut);
			outputStream.setLevel(ZipOutputStream.DEFLATED);
			for (PictureReceFile picFile : srcFiles) {
				zipFile(outputStream, picFile.getInfoFile(), picFile.getNewName());
				
				if(picFile.isZipTxtFile()){ //需要压缩txt文件
					zipFile(outputStream, picFile.getTxtDescFile(), null);
				}
			}
			if (outputStream != null) {
				outputStream.close();
			}
			return true;
		} catch (Exception e) {
			logger.error("打包异常：",e);
		}
		return false;
	}
	
	/**
	 * 文件压缩
	 * @param outputStream zip流
	 * @param srcFile 源文件
	 * @param newName 压缩新文件名
	 * @throws Exception
	 */
	private void zipFile(ZipOutputStream outputStream,File srcFile,String newName) throws Exception {
		outputStream.putNextEntry(new ZipEntry(newName==null ? srcFile.getName() : newName));
		FileInputStream fileIn = new FileInputStream(srcFile.getAbsoluteFile());
		byte[] buffer = new byte[4096];
		int len;
		while ((len = fileIn.read(buffer)) > 0) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.closeEntry();
		if (fileIn != null) {
			fileIn.close();
		}
	}
	
	/**
	 * tomcat 启动后外网初始化操作(处理待发送目录异常未发送的数据)
	 */
	@Override
	public void init(){
		try {
			File receiptFile = new File(setting.getSenderWanReceipt());
			Collection<File> receiptFiles =  FileUtils.listFiles(receiptFile, null, this.setting.isRecursive());//获取所有凭证文件
			
			if(receiptFiles!=null && !receiptFiles.isEmpty()){
				String fileName ;
				for(Iterator<File> it = receiptFiles.iterator();it.hasNext();){
					fileName = it.next().getName();
					fileName = fileName.substring(0, fileName.length() - Config.READY.length());
					
					NetReceFile netReceF = new NetReceFile();
					buildPath(netReceF, fileName,"");
					
					addQueue(new NetStayFile(netReceF, setting)); //入队列发送ftp
				}
			}
		} catch (Exception e) {
			logger.error("外网初始化操作(处理待发送目录异常未发送的数据)错误：",e);
		}
	}
	
	/**
	 * tomcat 关闭前销毁方法
	 */
	@Override
	public void destory() {
		try {
			this.setting.getFileSystem().destory();
		} catch (Exception e) {
			logger.error("",e);
		}
	}
}
