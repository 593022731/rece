package com.wei.rece.net.file.desc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.wei.commons.lang.StringUtils;
import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.inner.conf.Config;

/**
 * 图片描述对象
 * @author weih
 *
 */
public class PictureInfo extends DescFileInfo{
	
	/**
	 * PhotoType 类型说明 0--身份证照片 1--大头照 3--指纹照片 5--控制台摄像照片 6--客户机登陆摄像照片 7--客户机摄像照片
	 * 8--连续截屏 9--摄像头拍照 4--第二张指纹照
	 * 30：QQ空间相册照片；31：QQ聊天内容中的照片；32：QQ视频截屏照片；33：手机绑定上传抓拍大头照；34：手机绑定上传抓拍证件照)
	 * 注意：当PhotoType=5的时候，需要在police.properties文件中配置console_upload_ip
	 */
	private int photoType = -1;

	/**
	 * 手机绑定提交照片的手机号
	 */
	private String cardID;

	/**
	 * 网吧注册号
	 */
	private String pubCode;

	/**
	 * 操作类型(Read：读; Write：写; Read2：’’;Read3：读取截客户端照片;Read4:读取截屏照片)
	 */
	private String operation;

	/**
	 * 客户端时间
	 */
	private String clientDate;

	/**
	 * 分组编号
	 */
	private String groupId;

	/**
	 * 时间
	 */
	private String Date;

	public PictureInfo(File file) {
		BufferedReader filein = null;
		try {
			filein = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String info = filein.readLine();
			String picinfo[] = info.split(SPLIT_A);
			String pic[] = new String[20];
			for (int a = 0; a < pic.length; a++) {
				if (a < picinfo.length) {
					pic[a] = picinfo[a];
				} else {
					pic[a] = "";
				}
			}
			int i = 0;
			try{
				this.photoType = Integer.valueOf(pic[i++]);
			}catch (NumberFormatException e) {
				throw new RuntimeException("照片类型解析错误不是整数");
			}
			this.cardID = pic[i++];
			this.pubCode = pic[i++];
			this.serialID = pic[i++];
			this.operation = pic[i++];
			this.certID = pic[i++];
			this.recordID = pic[i++];
			this.clientDate = pic[i++];
			this.fileName = pic[i++];
			this.sessionID = pic[i++];
			this.groupId = pic[i++];
			this.Date = pic[i++];
			this.certType = pic[i++];
			this.qqCode = pic[i++];
			this.oppCode = pic[i++];
		} catch (Exception e) {
			logger.error("获取图片描述信息错误：",e);
		}finally{
			try {
				if(filein!=null){
					filein.close();
				}
			} catch (IOException e) {
				logger.error("",e);
			}
		}
	}
	
	public int getPhotoType() {
		return photoType;
	}

	public void setPhotoType(int photoType) {
		this.photoType = photoType;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public String getPubCode() {
		return pubCode;
	}

	public void setPubCode(String pubCode) {
		this.pubCode = pubCode;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getClientDate() {
		return clientDate;
	}

	public void setClientDate(String clientDate) {
		this.clientDate = clientDate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}
	
	/**
	 * 获取新名称(场所编码_照片类型_身份号码_编号_时间_sessionID_身份类型.jpg)
	 * @return
	 */
	@Override
	public String getFileNameTag() {
		/*3.4的接收分离应用已经为这3类照片打好包，外网RECE无须打包
		 * if (this.getPhotoType() == 30 || this.getPhotoType() == 31 || this.getPhotoType() == 32) {
			return this.getFileName();// 当PhotoType=30，31,32的时候，此值为文件的MD5值
		}*/
		if(StringUtils.isEmpty(certID)){
			return null;
		}
		StringBuffer b = new StringBuffer();
		b.append(StringUtils.isNotEmpty(pubCode) ? pubCode : "#").append(Config.UNDER_LINE);// 1
		b.append(StringUtils.isNotEmpty(String.valueOf(photoType)) ? photoType : "#").append(Config.UNDER_LINE);// 1
		b.append(StringUtils.isNotEmpty(certID) ? certID : "#").append(Config.UNDER_LINE);// 1
		b.append(StringUtils.isNotEmpty(recordID) ? recordID : "#").append(Config.UNDER_LINE);// 1
		String nowStr = DateFormatUtils.format(new Date(),"yyyyMMddHHmmss");// yyyyMMddHHmmss
		b.append(nowStr);// 5
		if (this.getPhotoType() == 1 || this.getPhotoType() == 2 || this.getPhotoType() == 6) {
			b.append(Config.UNDER_LINE).append(StringUtils.isNotEmpty(sessionID) ? sessionID : "#");// 1
			b.append(Config.UNDER_LINE).append(StringUtils.isNotEmpty(certType) ? certType : "#");// 1
		}
		b.append(".jpg");
		return b.toString();
	}
}
