package com.wei.rece;

import java.io.IOException;
import java.util.Collection;

/**
 * RECE功能接口定义
 * @author weih
 * 1.0.3
 */
public interface ScanFiles {
	
	/**
	 * Tomcat初始化
	 */
	void init();

	/**
	 * 获取一批待处理的数据文件
	 * @return
	 * @throws IOException
	 */
	Collection<ReceFile> getFiles() throws IOException;
	
	/**
	 * 对获取的文件进行处理
	 * @param receFile
	 * @throws IOException
	 */
	void handler(ReceFile receFile) throws IOException;
	
	/**
	 * Tomcat销毁
	 */
	void destory();
}
