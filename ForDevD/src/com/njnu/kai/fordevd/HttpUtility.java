package com.njnu.kai.fordevd;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtility {

	static public String GetUseAutoEncoding(String url) {
		HttpClient client = new DefaultHttpClient();
		String ls_content = null;
	    try {
	    	HttpGet httpGet = new HttpGet(url);
//	    	System.out.println("GetUseAutoEncoding: " + httpGet.getURI());
	    	HttpResponse response = client.execute(httpGet);
	    	HttpEntity entity = response.getEntity();
//	    	System.out.println(response.getStatusLine());
	    	if (entity != null) {
	    		String charset = EntityUtils.getContentCharSet(entity);
	    		ls_content = EntityUtils.toString(entity, ((charset == null) ? "UTF-8" : charset));
	    	}
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    finally {
	    	client.getConnectionManager().shutdown();
	    }

	    return ls_content;
	}

}
