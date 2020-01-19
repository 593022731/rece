package com.wei.rece;

import java.io.Serializable;

/**
 * 传输文件对象类(封装文件属性对象)
 * @author 		weih
 * @date   		2013-7-4上午11:20:36
 * @version     
 */
public class ReceFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
	 * 最初创建时间(最初扫描到文件的时间)
	 */
	protected long createTimes = System.currentTimeMillis();

}
