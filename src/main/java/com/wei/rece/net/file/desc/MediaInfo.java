package com.wei.rece.net.file.desc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.wei.commons.time.DateFormatUtils;
import com.wei.rece.inner.conf.Config;

/**
 * 音视频描述对象
 * @author 		weih
 * @date   		2013-7-5下午05:45:45
 * @version     
 */
public class MediaInfo extends DescFileInfo{
	
	public MediaInfo(File file) {
		BufferedReader filein = null;
		try {
			filein = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String info = filein.readLine();
			String mediainfo[] = info.split(SPLIT_A);
			String media[] = new String[20];
			for (int a = 0; a < media.length; a++) {
				if (a < mediainfo.length) {
					media[a] = mediainfo[a];
				} else {
					media[a] = "";
				}
			}
			int i = 0;
			this.action = media[i++];
			this.pubcode = media[i++];
			this.recordID = media[i++];
			this.certID = media[i++];
			this.certType = media[i++];
			this.sessionID = media[i++];
			this.serialID = media[i++];
			this.idCode = media[i++];
			this.idType = media[i++];
			try{
				this.type = Integer.valueOf(media[i++]);
			}catch (NumberFormatException e) {
				this.type = 0;
				logger.error("用于区分烽火布控和中心布控的type列描述不是数字错误：",e);
			}
			this.qqCode = media[i++];
			this.oppCode = media[i++];
			this.fileName = media[i++];
		} catch (Exception e) {
			logger.error("获取音视频描述信息错误：",e);
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
	
	/**
	 * 操作类型号2001为截取音频，2002为截取视频
	 */
	private String action;
	
	/**
	 * 网吧编号
	 */
	private String pubcode;
	
	/**
	 * 虚拟身份号码
	 */
	private String idCode;
	
	/**
	 * 虚拟身份类型
	 */
	private String idType;
	
	/**
	 * 语音类型 用于区分烽火布控和中心布控
	 */
	private int type;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPubcode() {
		return pubcode;
	}

	public void setPubcode(String pubcode) {
		this.pubcode = pubcode;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String mediaType(){
		if("2001".equals(this.action.toLowerCase())){
			return ".wav";
		}else if("2002".equals(this.action.toLowerCase())){
			return ".wma";
		}
		return this.action.toLowerCase();
	}
	/**
	 * 获取新名称(会话ID_记录ID_场所编码_身份类型_身份编码_时间戳_系列编号)
	 * @return
	 */
	@Override
	public String getFileNameTag(){
		StringBuffer b = new StringBuffer();
		b.append(this.sessionID).append(Config.UNDER_LINE);
		b.append(this.recordID).append(Config.UNDER_LINE);// 1
		b.append(this.pubcode).append(Config.UNDER_LINE);// 1
		b.append(this.certType).append(Config.UNDER_LINE);// 1
		b.append(this.certID).append(Config.UNDER_LINE);// 1
		b.append(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss")).append(Config.UNDER_LINE);// 5
		b.append(this.serialID).append(this.mediaType());// 1
		return b.toString();
	}
}
