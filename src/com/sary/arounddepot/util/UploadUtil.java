package com.sary.arounddepot.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sary.arounddepot.entity.Result;

/**
 * 
 * 上传工具类
 */
public class UploadUtil {

	public static String uploadFile(String actionUrl, String uploadFile,
			String type){
		String code = "";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			FileBody fileBody = new FileBody(new File(uploadFile));
			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.STRICT);
			multipartEntity.addPart("msg", fileBody);
			
			StringBody a = new StringBody(type);
			multipartEntity.addPart("type", a);
			
			StringBody c = new StringBody(Constant.CHANNEL);
			multipartEntity.addPart("channel", c);
			
			String strHash = type+Constant.CHANNEL;
			strHash = StringUtil.urlEncode(strHash);
			strHash += "@"+Constant.BASIC_KEY;//加入秘钥
			strHash = StringUtil.encrypt(strHash, "SHA");//SHA1加密
			strHash = StringUtil.encrypt(strHash, "MD5");//MD5加密
			strHash = strHash.toUpperCase();//变为大写
			
			StringBody d = new StringBody(strHash);
			multipartEntity.addPart("sign", d);
			Log.e("tag", "value:"+uploadFile);
			connection.setRequestProperty("Content-Type", multipartEntity
					.getContentType().getValue());
			OutputStream out = connection.getOutputStream();
			multipartEntity.writeTo(out);
			
			InputStream is = connection.getInputStream();
			JSONObject json = getJson(is);
			code = json.getString("code");
			Result result = new Result();
			result.setCode(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	public static JSONObject getJson(InputStream is) throws IOException,
			JSONException {
		StringBuilder builder = new StringBuilder();
		BufferedReader bufferread = new BufferedReader(
				new InputStreamReader(is));
		for (String s = bufferread.readLine(); s != null; s = bufferread
				.readLine()) {
			builder.append(s);
		}
		Log.e("tag", "mmmmmmmmmmmmmm"+builder.toString());
		JSONObject json = new JSONObject(builder.toString());
		return json;
	}
}