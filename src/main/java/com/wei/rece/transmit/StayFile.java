package com.wei.rece.transmit;

import java.io.Serializable;

import com.wei.rece.ReceFile;
import com.wei.rece.fs.ReceFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wei.rece.conf.BasicSetting;

/**
 * 文件发送对象
 * 该对象决定文件如何发送，在发送成功或失败后会得到通知调用以下方法:
 * execute 发送方法
 * call 发送完成后
 * failed 发送失败后
 * @author Jetory
 *
 */
public abstract class StayFile implements Serializable, Runnable {

	private static final long serialVersionUID = 5517871356209252071L;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 具体的RECE文件对象
	 */
	protected ReceFile receFile = null;
	
	/**
	 * 针对该类型文件的一系列配置
	 */
	protected BasicSetting setting = null;
	
	/**
	 * 用来发送StayFile对象的工作队列
	 */
	protected Worker worker = null;

	/**
	 * 发送失败尝试的次数
	 */
	protected int retry = 0;
	
	public StayFile(ReceFile receFile, BasicSetting setting) {
		super();
		this.receFile = receFile;
		this.setting = setting;
	}

	@Override
	public void run() {
		boolean success = false;
		try{
			this.execute();
			success = true;
			if(logger.isDebugEnabled()){
				logger.debug("发送文件成功, 文件信息:\n" + this.receFile.toString());
			}
		}catch (Exception e) {
			if(this.retry > 3){
				success = false;
				if(logger.isErrorEnabled()){
					logger.error("发送文件失败 , 文件信息:\n" + receFile.toString(), e);
				}
				this.failed();
			}else{
				this.retry ++;
				worker.addStayFile(this);
				if(logger.isErrorEnabled()){
					logger.error("发送文件失败, 重试: " + this.retry + "次,文件信息:\n" + receFile.toString(), e);
				}
			}
		}
		
		if(success){
			this.call();
		}
	}
	
	public abstract void call();
	
	public abstract void execute() throws Exception;
	
	public abstract void failed();

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}
	
	public ReceFile getReceFile() {
		return receFile;
	}

	public void setReceFile(ReceFile receFile) {
		this.receFile = receFile;
	}

	public BasicSetting getSetting() {
		return setting;
	}

	public void setSetting(BasicSetting setting) {
		this.setting = setting;
	}

	public ReceFileSystem getFileSystem() throws Exception {
		return this.setting.getFileSystem();
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

}
