package com.sary.arounddepot.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class NativeHttpClient {
	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient customerHttpClient;

	private NativeHttpClient() {
	}

	public static synchronized HttpClient getHttpClient() {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置
			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, 1000 * 60 * 2);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params, 1000 * 60 * 2);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 4);

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}

	public static String post(String url, NameValuePair... params) {
		try {
			// 编码参数
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 璇锋眰鍙傛暟
			for (NameValuePair p : params) {
				formparams.add(p);
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
					CHARSET);
			// 创建post请求
			HttpPost request = new HttpPost(url);
			request.setEntity(entity);
			// 发送请求
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET);
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}

	}

	public static String postString(String url) {
		try {
			HttpPost request = new HttpPost(url);
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET);
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}

	}

	public static InputStream post(String url, List<BasicNameValuePair> list) {
		try {
			// 创建post请求
			HttpPost request = new HttpPost(url);
			if (list != null) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
						CHARSET);
				// 将请求中的参数信息放置到request中
				request.setEntity(entity);
				System.out.println("hehehehe");
			}
			// 发送请求
			HttpClient client = getHttpClient();
			System.out.println("ffffffff");
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}

			return response.getEntity().getContent();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}

	}

	public static InputStream post(String url) {
		try {
			// 创建POST请求
			HttpPost request = new HttpPost(url);
			// 发送请求
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}

			return response.getEntity().getContent();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}

	}
}
