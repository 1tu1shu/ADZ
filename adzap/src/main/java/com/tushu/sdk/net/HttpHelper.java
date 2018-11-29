package com.tushu.sdk.net;

import com.tushu.sdk.utils.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpHelper {
	
	 public static String getToken(byte[] postData) {
        String md5 = "screen_" + Base64.MD5(postData) + "_lock";
        return Base64.MD5(md5.getBytes());
    }

	public static String doPost2(String path, String postData){
		Map<String,String> map = new HashMap<>();
		map.put("Content-Type","application/json; charset=utf-8");
		return doPost(path,map,postData.getBytes());
	}

	public static String doPost(String path, String postData){
		Map<String,String> map = new HashMap<String,String>();
		map.put("Content-Type","application/json; charset=utf-8");
		String data = doPost(path+="?token="+getToken(DESUtil.encodeDES2Btyes(postData)), map, DESUtil.encodeDES2Btyes(postData));
		if(null!=data){
			return DESUtil.Decrypt(data);
		}
		return null;
	}

	public static String doGet(String path)
	{
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			int code = conn.getResponseCode();
			if (code == 200) {
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				int len = 0;
				byte buffer[] = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return new String(baos.toByteArray(),"utf-8");
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null!=baos) baos.close();
				if(null!=is) is.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
    
    public static String doPost(String path, Map<String,String> map, byte[] postData){
      	OutputStream out = null;
    	InputStream is = null;
    	ByteArrayOutputStream baos = null;
    	try {
    		URL url = new URL(path);
    		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    		conn.setConnectTimeout(10 * 1000);
    		conn.setReadTimeout(10 * 1000);
    		conn.setDoOutput(true);
    		conn.setUseCaches(false);
    		conn.setRequestMethod("POST");
    		for(Map.Entry<String,String> entry:map.entrySet()){
				conn.setRequestProperty(entry.getKey(),entry.getValue());
    		}
    		out = conn.getOutputStream();  
    		out.write(postData);
    		out.flush();
    		if (conn.getResponseCode() == 200) {  
                is = conn.getInputStream();  
                baos = new ByteArrayOutputStream();
                int len = 0;  
                byte buffer[] = new byte[1024];  
                while ((len = is.read(buffer)) != -1) {  
                    baos.write(buffer, 0, len);  
                }  
                return new String(baos.toByteArray(),"utf-8");
    		}else{
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null!=baos) baos.close();
				if(null!=is) is.close();  
				if(null!=out) out.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
    	return null;
    }

}
