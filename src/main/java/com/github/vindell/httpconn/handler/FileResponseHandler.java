package com.github.vindell.httpconn.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


import com.github.vindell.httpconn.HttpIOUtils;
import com.github.vindell.httpconn.HttpStatus;
import com.github.vindell.httpconn.exception.HttpResponseException;

public class FileResponseHandler implements ResponseHandler<File> {

	private File destFile;
	
	public FileResponseHandler( File destFile) {
		this.destFile = destFile;
	}

	@Override
	public void handleConn(HttpURLConnection httpConn) {
		
	}
	
	@Override
	public File handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			InputStream input = null;
			FileOutputStream output = null;
			// 先存为临时文件，等全部下完再改回原来的文件名
			File storeFile = new File(destFile.getParent() , destFile.getName()  + ".tmp"); 
			try {
				output = new FileOutputStream(storeFile);
				// 从request中取得输入流
				input = httpConn.getInputStream();
				HttpIOUtils.copy(input, output);
			} finally {
				// 释放资源
				HttpIOUtils.closeQuietly(input);
				HttpIOUtils.closeQuietly(output);
			}
			storeFile.renameTo(destFile);
			return destFile;
		} else {
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}

}