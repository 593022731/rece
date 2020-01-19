package com.wei.rece.fs;

import com.wei.rece.conf.BasicSetting;
import com.wei.rece.ftp.FtpPool;
import com.wei.rece.ftp.FtpServer;

public class FileSystemFactory {

	private static FtpPool ftpPool = null;
	
	/**
	 * @param set
	 * @param active 对于文件系统是否是活动的随时保持畅通的
	 * @return
	 * @throws Exception
	 */
	public static ReceFileSystem getFileSystem(BasicSetting set, boolean active) throws Exception{
		if(set.getFs().equals("ftp")){
			if(ftpPool == null){
				ftpPool = new FtpPool(active);
			}
			for(int i = 0; i < set.getPoolSize(); i ++){
				FtpServer ftp = new FtpServer(set.getRemote(), Integer.valueOf(set.getPort()), set.getUser(), set.getPwd(), set.getEncoding(), set.getTimeout());
				ftpPool.addFtpService(set.getName(), ftp);
			}
			return new FtpReceFileSystem(set.getName(), ftpPool);
		}else if(set.getFs().equals("local")){
			return new LocalReceFileSystem();
		}
		return null;
	}
	
}
