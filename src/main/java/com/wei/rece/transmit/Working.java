package com.wei.rece.transmit;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.wei.rece.conf.BasicConfig;
import com.wei.rece.conf.BasicSetting;

public class Working extends WorkQueue implements Runnable {

	protected ExecutorService EXECUTOR = null;
	
	protected int maxQueue = BasicConfig.DEF_FTP_SENDER_MAXQUEUE;
	
	protected ArrayBlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<Runnable>(maxQueue);
	
	public Working(BasicSetting[] setting, int core) throws Exception{
		EXECUTOR =  new ThreadPoolExecutor(core, core, 0L, TimeUnit.MILLISECONDS, workingQueue);
	}
	
	@Override
	public void run() {
		while(true){
			if((queue.size() >= maxQueue)){
				try {
					Thread.sleep(1000 * 10);
				} catch (Exception e) {
					if(logger.isErrorEnabled()){
						logger.error("文件传输队列处理出错!", e);
					}
				}
			}else{
				try {
					StayFile stay =  this.getQueue();
					if(stay != null){
						stay.setWorker(this);
						EXECUTOR.execute(stay);
					}else{
						Thread.sleep(1000 * 10);
					}
				} catch (Exception e) {
					if(logger.isErrorEnabled()){
						logger.error("文件传输队列处理出错!", e);
					}
				}
			}
		}
	}
	
	@Override
	public void waitWorkerComplete() {
		if(logger.isDebugEnabled()){
			logger.debug("等待数据上传完成, 待发送文件：" + this.queue.size());
		}
		
		long times = System.currentTimeMillis();
		
		EXECUTOR.shutdown();
		
		if(logger.isDebugEnabled()){
			logger.debug("数据上传完成,系统停止,耗时：" + ((System.currentTimeMillis() - times) / 1000L) + " 秒.");
		}
	}

}
