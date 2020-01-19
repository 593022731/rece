package com.wei.rece.ftp;

public class FtpConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5920785804730211851L;

	public FtpConnectionException() {
		super();
	}
	
	public FtpConnectionException(String msg) {
		super(msg);
	}
	
	public FtpConnectionException(String msg, Exception e) {
		super(msg, e);
	}
	
}
