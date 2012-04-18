package com.njnu.kai;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class JavaHttp {

	static public void exe(String urlAsString) throws IOException{
	     URL url = new URL(urlAsString);
	     HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	     int size = conn.getHeaderFields().size();
	     for(int i=0; i< size; i++){
	         System.out.print(conn.getHeaderFieldKey(i)+":\t");
	         System.out.println(conn.getHeaderField(i));
	     }
	     System.out.println("Request Method: "+conn.getRequestMethod());
	     System.out.println("Response Code: "+conn.getResponseCode());
	     System.out.println("Response Message: " +conn.getResponseMessage());
	     System.out.println("Last Modified: " +conn.getLastModified());
	     System.out.println("ContentHandler: " +conn.getContent().getClass().getName());
	     InputStream in = (InputStream)conn.getContent();
//	     FileOutputStream out = new FileOutputStream(new File("index.html"));
//	     copyStream(in, out);

	 }

	public static void exeHttpClient(String urlAsString) throws IOException{
	    HttpClient client = new DefaultHttpClient();
	    try {
	    	HttpGet httpGet = new HttpGet(urlAsString);
	    	System.out.println("execting request: " + httpGet.getURI());
	    	HttpResponse response = client.execute(httpGet);
	    	HttpEntity entity = response.getEntity();
	    	System.out.println("------------------");
	    	System.out.println(response.getStatusLine());
	    	if (entity != null) {
	    		String charset = EntityUtils.getContentCharSet(entity);
	    		System.out.println("Response content length: " + entity.getContentLength() + " CharSet:" + charset);
//	    		String ls_content = EntityUtils.toString(entity);
	    		String ls_content = EntityUtils.toString(entity, ((charset == null) ? "UTF-8" : charset));
//	    		String ls_content = EntityUtils.toString(entity, "UTF-8");
	    		System.out.println("Response content: " + ((ls_content.length() > 1000) ? ls_content.substring(0, 1000) : ls_content));
	    	}
	    	System.out.println("--------------------");
	    }
	    finally {
	    	client.getConnectionManager().shutdown();
	    }
	}

	public static void main(String[] args) throws IOException {
		System.out.println("hello, java http");
//		exeHttpClient("http://wap.baidu.com");
//		exeHttpClient("http://wap.google.com");
//		exeHttpClient("http://apt.ttpod.com/ttpod.html");
//		exeHttpClient("https://github.com/qhkyzf/FeiAndroid");
		exeHttpClient("http://whatsmyua.com/");
//		exeHttpClient("http://www.devdiv.com/%E7%81%8C%E6%B0%B4%E5%8C%BA-forum-14-1.html");
	}

}
