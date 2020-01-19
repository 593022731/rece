package com.wei.rece.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.wei.rece.ScanFiles;
import com.wei.rece.conf.BasicConfig;
import com.wei.rece.inner.conf.Config;
import com.wei.rece.inner.conf.Setting;
import com.wei.rece.thread.ScanFilesThread;
import com.wei.rece.transmit.Worker;
import com.wei.rece.transmit.Working;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wei.commons.properties.AppProperties;

/**
 * 系统初始化(加载配置文件，启动相关线程)
 * @author 		weih
 * @date   		2013-7-1下午03:26:29
 * @version     
 */
public class SystemInitServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Worker working;
	
	private List<ScanFiles> scan = new ArrayList<ScanFiles>();
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			AppProperties app = new AppProperties("police");
			String appConfig = app.getProperty("rece.appname");
			BasicConfig.AREACODE = app.getProperty("areaCode");
			
			if("rece-net".equals(appConfig)){
				startNet();
			}else if("rece-inner".equals(appConfig)){
				startInner();
			}else{
				throw new Error("启动项未配置! rece.appname: rece-net or rece-inner");
			}
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	
	/**
	 * 内网操作
	 */
	private void startInner(){
		try {
			Config config = new Config(new AppProperties(new String[]{"rece-inner", "police"}));
			Working w = new Working(config.getSettings(), BasicConfig.DEF_WORING_CORE);
			working = w;
			new Thread(w).start();
			logger.info("内网发送ftp线程开启");
			
			Setting[] settings  = config.getSettings();
			for(Setting s : settings){
				
				String className = s.getClassName();
				ScanFiles scanFiles = null;
				try {
					Class<?> clazz = Class.forName(className);
					if(ScanFiles.class.isAssignableFrom(clazz)){
						scanFiles = (ScanFiles) clazz.getConstructor(Setting.class, Worker.class).newInstance(s, working);
						scan.add(scanFiles);
					}else{
						logger.error("没有找到指定的扫描类"+className+"请检查配置项(scan.className)是否正确");
					}
					
					if(scanFiles!=null){
						new Thread(new ScanFilesThread(scanFiles,working)).start();
						logger.info("内网扫描打包线程"+className+"开启");
					}
					
				} catch (Exception e) {
					logger.error("",e);
				}
			}
			
			
		} catch (IOException e) {
			logger.error("",e);
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	/**
	 * 外网操作
	 */
	private void startNet(){
		try {
			com.wei.rece.net.conf.Config conf = new com.wei.rece.net.conf.Config(new AppProperties(new String[]{"rece-net", "police"}));;
			Working w = new Working(conf.getSettings(), BasicConfig.DEF_WORING_CORE);
			working = w;
			new Thread(w).start();
			logger.info("外网发送ftp线程开启");
			
			com.wei.rece.net.conf.Setting[] settings  = conf.getSettings();
			for(com.wei.rece.net.conf.Setting s : settings){
				String className = s.getClassName();
				ScanFiles scanFiles = null;
				try {
					Class<?> clazz = Class.forName(className);
					if(ScanFiles.class.isAssignableFrom(clazz)){
						scanFiles = (ScanFiles) clazz.getConstructor(com.wei.rece.net.conf.Setting.class, Worker.class).newInstance(s, working);
						scan.add(scanFiles);
					}else{
						logger.error("没有找到指定的扫描类"+className+"请检查配置项(scan.className)是否正确");
					}
					
					if(scanFiles!=null){
						new Thread(new ScanFilesThread(scanFiles,working),"Thread-"+s.getName()).start();
						logger.info("外网扫描打包线程"+className+"开启");
					}
				} catch (Exception e) {
					logger.error("",e);
				}
			}
			
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	@Override
	public void destroy() {
		working.waitWorkerComplete();
		for(ScanFiles s : scan){
			s.destory();
		}
	}
}
