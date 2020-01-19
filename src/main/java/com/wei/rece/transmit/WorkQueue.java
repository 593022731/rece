package com.wei.rece.transmit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WorkQueue implements Worker{

	protected final static Logger logger = LoggerFactory.getLogger(WorkQueue.class);
	
	public final int maxQuque = 10000 * 50;
	
	/**
	 * 发送的文件队列
	 */
	protected Queue<StayFile> queue = new ArrayBlockingQueue<StayFile>(maxQuque);
	
	@Override
	public boolean addStayFile(StayFile stayFile) throws IllegalStateException{
		return queue.add(stayFile);
	}
	
	@Override
	public boolean addStayFile(Collection<StayFile> stayFile) throws IllegalStateException{
		return queue.addAll(stayFile);
	}

	@Override
	public StayFile getQueue(){
		return queue.poll();
	}
	
	@Override
	public boolean isFull(){
		return maxQuque <= queue.size() + 50;
	}
	
	@Override
	public Collection<StayFile> getQueue(int max){
		if(queue.isEmpty()){
			return null;
		}
		
		List<StayFile> list = new ArrayList<StayFile>(max);
		for(int i = 0; i < max; i++){
			StayFile stay = this.getQueue();
			if(stay == null){
				break;
			}else{
				list.add(stay);
			}
		}
		return list;
	}
	
	@Override
	public boolean isEmpty(){
		return queue.isEmpty();
	}
}
