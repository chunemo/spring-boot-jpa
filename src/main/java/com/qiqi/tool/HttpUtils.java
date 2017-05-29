package com.qiqi.tool;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * http接口工具类
 *
 */
@Slf4j
public class HttpUtils {
	private static final String APPLICATION_JSON = "application/json";
    
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	
	private RequestConfig requestConfig = RequestConfig.custom()	//
			.setSocketTimeout(60000)	//
			.setConnectTimeout(60000)	//
			.setConnectionRequestTimeout(60000).build();

	private static HttpUtils instance = null;

	private HttpUtils() {

	}

	public static HttpUtils getInstance() {
		if (instance == null) {
			synchronized (HttpUtils.class) {
				if (instance == null) {
					instance = new HttpUtils();
				}
			}
		}
		return instance;
	}

	/**
	 * 发送 post请求
	 * 
	 * @param url 地址
	 */
	public String sendHttpPost(String url) {
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求
	 *
	 * @author qc_zhong
	 * @param url 地址
	 * @param params 参数(格式:key1=value1&key2=value2)
	 * @return
	 */
	public String sendHttpPost(String url, String params) {
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		try {
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送post请求
	 *
	 * @author qc_zhong
	 * @param url 地址
	 * @param params 参数
	 * @param contentType 请求格式
	 * @return
	 */
	public String sendHttpPost(String url, String params, String contentType) {
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		try {
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType(contentType);
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param url 地址
	 * @param params 参数
	 */
	public String sendHttpPost(String url, Map<String, Object> params) {
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, (String)params.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送Post请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	
	/**
	 * 发送json post请求，并记录日志信息
	 *
	 * @author qc_zhong
	 * @param url
	 * @param json
	 * @return
	 */
	public String sendJsonPost(String url, String json) {
		// log
		try {
			log.info("-----http json post begin------");
			log.info(url);
			log.info(json);
		} catch (Exception e) {
		}
		// build http post
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		try {
			// 设置参数
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity stringEntity = new StringEntity(json, "UTF-8");
			stringEntity.setContentType(CONTENT_TYPE_TEXT_JSON);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// post
		String ret = sendHttpPost(httpPost);
		// log
		try {
			log.info("-----http json result------");
			log.info(String.valueOf((new Date()).getTime()));
			log.info(ret);
		} catch (Exception e) {
		}
		// return
		return ret;
	}

	/**
	 * 发送 get请求
	 * 
	 * @param url
	 * @throws Exception 
	 */
	public String sendHttpGet(String url) throws Exception {
		HttpGet httpGet = new HttpGet(url);// 创建get请求
		return sendHttpGet(httpGet);
	}

	/**
	 * 发送 get请求Https
	 * 
	 * @param url
	 */
	public String sendHttpsGet(String url) {
		HttpGet httpGet = new HttpGet(url);// 创建get请求
		return sendHttpsGet(httpGet);
	}

	/**
	 * 发送Get请求
	 * 
	 * @param httpPost
	 * @return
	 * @throws Exception 
	 */
	private String sendHttpGet(HttpGet httpGet) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	/**
	 * 发送Get请求Https
	 * 
	 * @param httpPost
	 * @return
	 */
	private String sendHttpsGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	
	/**
	 * 构建url参数
	 *
	 * @author qc_zhong
	 * @param params
	 * @return
	 */
	public String buildUrlParams(Map<String, String> params) {
		Set<String> keys = params.keySet();
		
		StringBuilder urlParams = new StringBuilder();
		String key = "";
		String value = "";
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			key = it.next();
			value = (String) params.get(key);
			if (value == null || "".equals(value)) {
				continue;
			}
			urlParams.append(key).append("=").append(value).append("&");
		}
		if (urlParams.length() > 0) {
			urlParams.deleteCharAt(urlParams.length() - 1);
		}
		return urlParams.toString();
	}
	
	/**
	 * 构建get请求url
	 *
	 * @author qc_zhong
	 * @param url
	 * @param params
	 * @return
	 */
	public String buildGetUrl(String url, Map<String, String> params) {
		String urlParams = buildUrlParams(params);
		StringBuilder sb = new StringBuilder();
		if (url.endsWith("?")) {
			sb.append(url).append(urlParams);
		} else {
			sb.append(url).append("?").append(urlParams);
		}
		return sb.toString();
	}
}
