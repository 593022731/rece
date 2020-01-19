package com.wei.rece.transmit;

import java.util.Collection;

public interface Worker{

	/**
	 * 添加一个传输文件在后台运行
	 * @param StayFile
	 * @return
	 * @throws IllegalStateException
	 */
	public boolean addStayFile(StayFile stayFile) throws IllegalStateException;
	
	public boolean addStayFile(Collection<StayFile> stayFile) throws IllegalStateException;

	/**
	 * 获取单个发送文件,如果文件数量为零返回null
	 * @return
	 */
	public StayFile getQueue();
	
	/**
	 * 队列是否达到最大值
	 * @return
	 */
	public boolean isFull();
	
	public Collection<StayFile> getQueue(int max);
	
	/**
	 * 对列是否为空
	 * @return
	 */
	public boolean isEmpty();
	
	/**
	 * 命令队列停止接收文件,并等待队列所有文件发送完成
	 */
	public void waitWorkerComplete();
	
}
