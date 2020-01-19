package com.wei.rece.thread;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import com.wei.rece.ReceFile;
import com.wei.rece.ScanFiles;
import com.wei.rece.transmit.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用线程(扫描文件后处理)
 * @author 		weih
 * @date   		2013-7-2下午07:32:45
 * @version     1.0
 */
public class ScanFilesThread implements Runnable{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ScanFiles scanFiles;
	private Worker working;
	
	private boolean start = true;
	
	public ScanFilesThread(ScanFiles scanFiles,Worker working) {
		this.scanFiles = scanFiles;
		this.working = working;
	}
	
	@Override
	public void run() {
		execute();
	}
	
	/**
	 * 方法执行
	 * @throws IOException
	 */
	private synchronized void execute(){
		logger.debug("线程"+Thread.currentThread().getName()+"开始扫描文件");
		
		while(start){
			try{
				if(working.isFull()){
					if(logger.isWarnEnabled()){
						logger.warn("队列已满, 等待队列完成!");
					}
					try {
						Thread.sleep(1000 * 30);
					} catch (InterruptedException e) {
						logger.error("",e);
					}
				}else{
					Collection<ReceFile> infoFiles = scanFiles.getFiles();//获取数据文件
					if(infoFiles!=null && !infoFiles.isEmpty()){
						for(Iterator<ReceFile> it = infoFiles.iterator();it.hasNext();){
							if(working.isFull()){
								logger.warn("队列已满, 等待队列完成!");
								break;
							}
							scanFiles.handler(it.next());
						}
					}else{
						try {
							Thread.sleep(1000 * 30);
						} catch (InterruptedException e) {
							logger.error("",e);
						}
					}
				}
			}catch (Exception e) {
				logger.error("线程"+Thread.currentThread().getName()+"出现异常",e);
			}
		}
		
	}
	
	public synchronized void stop(){
		this.start = false;
	}
	
}
