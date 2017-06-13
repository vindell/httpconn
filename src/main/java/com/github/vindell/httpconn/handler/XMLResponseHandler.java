 package com.github.vindell.httpconn.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.vindell.httpconn.HttpIOUtils;
import com.github.vindell.httpconn.HttpStatus;
import com.github.vindell.httpconn.exception.HttpResponseException;

/**
 * 
 * @className: XMLResponseHandler
 * @description: http请求响应处理：返回org.w3c.dom.Document对象
 * @author : vindell
 * @date : 下午01:43:19 2015-7-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class XMLResponseHandler implements ResponseHandler<Document> {

	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	@Override
	public void handleConn(HttpURLConnection httpConn) {
		
	}
	
	@Override
    public Document handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			InputStream input = null;
			InputStreamReader reader = null;
			try {
	            DocumentBuilder docBuilder = factory.newDocumentBuilder();
				// 从request中取得输入流
				input = httpConn.getInputStream(); 
				reader = new InputStreamReader(input, charset);
				// 响应内容
				return docBuilder.parse(HttpIOUtils.toString(reader));
	        } catch (ParserConfigurationException ex) {
	            throw new IllegalStateException(ex);
	        } catch (SAXException ex) {
	            throw new HttpResponseException("Malformed XML document", ex);
	        } finally{
				// 释放资源
				HttpIOUtils.closeQuietly(input);
				HttpIOUtils.closeQuietly(reader);
			}
		} else {
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
    }
}

 