package com.github.vindell.httpconn.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.github.vindell.httpconn.HttpIOUtils;
import com.github.vindell.httpconn.HttpStatus;
import com.github.vindell.httpconn.exception.HttpResponseException;

/**
 * 
 * @className	： StreamResponseHandler
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月13日 下午9:20:09
 * @version 	V1.0
 */
public class StreamResponseHandler implements ResponseHandler<ByteArrayInputStream> {

	@Override
	public void preHandle(HttpURLConnection httpConn) {
		
	}

	@Override
	public ByteArrayInputStream handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			InputStream input = null;
			try {
				// 从request中取得输入流
				input = httpConn.getInputStream(); 
				// 响应内容
				return new ByteArrayInputStream(HttpIOUtils.toByteArray(input));
	        } finally{
				// 释放资源
				HttpIOUtils.closeQuietly(input);
			}
		} else {
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}

}
