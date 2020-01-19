package com.wei.rece.ftp;

import java.io.IOException;

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;

/**
 * ftp 客户端
 * @author 		weih
 * @date   		2013-7-1下午04:14:02
 * @version     
 */
public class FtpServer {
	
	private FileTransferClient client;
	
	/**
	 * 连接ftp
     * @param hostIP FTP服务器地址
     * @param hostPort FTP服务器端口号
     * @param userName   FTP账号
     * @param userPass    FTP密码
     * @param encoding    编码
     * @return
     * @throws FTPException
     * @throws IOException
     */
	public FtpServer(String hostIP,int hostPort,String userName,String userPass,String encoding, int timeout) throws FTPException, IOException {
		super();
		client = new FileTransferClient();
		client.setRemoteHost(hostIP);
		client.setRemotePort(hostPort);
		client.setUserName(userName);
		client.setPassword(userPass);
		
		 //解决中文文件名乱码
		client.getAdvancedSettings().setControlEncoding(encoding);
		client.setTimeout(timeout);
		
		//设置传输缓冲区
		client.getAdvancedSettings().setTransferBufferSize(1024 * 512);
		//设置传输通知的时间间隔
		//client.getAdvancedSettings().setTransferNotifyInterval(1000);
		
		 //被动模式，数据连接由客户端发起
		client.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.PASV);
		
		//用HTML 和文本编写的文件必须用ASCII模式上传,用BINARY模式上传会破坏文件,导致文件执行出错.
        //client.setContentType(FTPTransferType.ASCII);
		
	     //BINARY模式用来传送可执行文件,压缩文件,和图片文件.
		client.setContentType(FTPTransferType.BINARY);
		//client.connect();
	}
    
    /**
     * 上传ftp
     * @param localFileName 本地文件
     * @param remoteFileName ftp文件
     * @throws FTPException
     * @throws IOException
     */
    public void uploadFile(String localFileName,String remoteFileName) throws FTPException, IOException{
    	
    	/**
    	 * WriteMode 上传模式
    	 * RESUME 断点续传，即连接中断下次连接直接续传，前提是此ftp服务器支持断掉续传
    	 * OVERWRITE：覆盖上传，即上次如果没传完，这次删掉上次的重新传
    	 * APPEND：续写，即如果FTP服务器上存在同名的文件，就接着它后面续加
    	 */
    	client.uploadFile(localFileName, remoteFileName,WriteMode.OVERWRITE);
    	//client.getSize(remoteFileName);//获取上传成功后的文件大小
    }
    
	/**
     * 从FTP Server断开连接
     * @param ftp
     * @throws FTPException
     * @throws IOException
     */
    public void disconnect() throws FTPException, IOException {
        if(client != null && client.isConnected()) {
        	client.disconnect();
        }
    }
    
    /**
     * 如果FTP未连接则打开连接
     * @return
     * @throws FTPException
     * @throws IOException
     */
    public FtpServer resetCollection() throws FTPException, IOException{
    	if(!this.client.isConnected()){
    		this.client.connect();
    	}
    	return this;
    }
    
    /**
     * 创建指定的目录
     * @param dir
     * @throws FTPException
     * @throws IOException
     */
    public void createDirectory(String dir) throws FTPException, IOException{
    	
    	if(dir == null || dir.isEmpty()){
    		return;
    	}
    	
    	String[] directory = dir.replaceFirst("[(\\)]|[(/)]|[//]|[\\\\]", "").split("[(\\)]|[(/)]|[//]|[\\\\]");
    	
    	if(directory == null || directory.length == 0){
    		return;
    	}
    	
    	String str = "";
    	for(String d : directory){
    		str += "/" + d;
    		//String result = this.client.executeCommand("CWD " + str);
    		boolean result = this.client.getFTPClient().existsDirectory(str);
    		if(!result){
    			//this.client.executeCommand("MKD " + str);
    			this.client.getFTPClient().mkdir(str);
    		}
    	}
    	
    }
    
    public void testUploadFile() throws FTPException, IOException {
        String localFileName = "F:/中文.txt";
        String remoteFileName = "/pub/中文.txt";
        uploadFile(localFileName, remoteFileName);
        disconnect();
    }
    
    /**
     * 下载文件
     * @throws FTPException
     * @throws IOException
     */
    public void testDownloadFile() throws FTPException, IOException {
    	client.downloadFile("F:/中文.txt", "/pub/中文.txt");
    	disconnect();
    }
    
    public static void main(String[] args) {
    	FtpServer f = null;
		try {
			//for(int i = 0; i < 20; i ++){
				f = new FtpServer("192.168.2.100", 21, "ftpuser", "123QWE", "GBK", 500);
				f.testUploadFile();
			//}
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	/*try {
			//f.testUploadFile();
			//f.testDownloadFile();
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    	
	}
    
    
}
