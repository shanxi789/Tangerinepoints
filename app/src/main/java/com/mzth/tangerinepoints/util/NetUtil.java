package com.mzth.tangerinepoints.util;

import android.content.Context;
import android.os.Handler;

import com.mzth.tangerinepoints.common.Constans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Describe 
 * @Author leeandy007
 * @Date 2016-9-5 下午4:53:45
 */
public class NetUtil {

	/**
	 * 编码格式
	 * */
	private static final String UTF_8 = "utf-8";

	/**
	 * 创建固定数量的线程池来管理线程
	 */
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

	/**
	 * 向UI线程发送消息
	 * */
	private static Handler handler = new Handler();

	/**
	 * 请求回调接口
	 * */
	public interface RequestCallBack {

		/**
		 * 请求成功
		 * @param statusCode 状态码
		 * @param json 返回JSON数据
		 * */
		public void onSuccess(int statusCode, String json);

		/**
		 * 接口异常
		 * @param statusCode 状态码
		 * @param errorMsg 异常提示信息
		 * */
		public void onFailure(int statusCode, String errorMsg);

		/**
		 * 请求失败
		 * @param e 异常
		 * @param errorMsg 异常提示信息
		 * */
		public void onFailure(Exception e, String errorMsg);

	}

	private static String getBaseUrl(){
		String baseUrl ="https://tangerinepoints.com/dev/api/customer/";
		return baseUrl;
	}

	public enum RequestMethod{
		GET,
		POST
	}

	/**
	 * Request
	 * @param mRequestMethod 请求方法 GET或POST
	 * @param action 接口名
	 * @param params 请求参数
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	public static void Request(RequestMethod mRequestMethod, String action, Map<String, Object> params,
							   String Authorization,String Instance,final RequestCallBack mRequestCallBack){
		switch (mRequestMethod) {
			case GET:
				get(action, params, Authorization,Instance,mRequestCallBack);
				break;
			case POST:
				post(action, params,Authorization,Instance, mRequestCallBack);
				break;
		}
	}

	/**
	 * Get
	 * @param action 接口名
	 * @param params 请求参数
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	private static void get(String action, Map<String, Object> params,final String Authorization,final String Instance, final RequestCallBack mRequestCallBack) {
        String requestUrl = null;
        if(action.equals(Constans.REGISTER_PHONE)||action.equals(Constans.REGISTER_CODE)){
			StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
			try {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					stringBuffer.append(URLEncoder.encode(StringUtil.isEmptyToString(entry.getValue()),UTF_8)).append("/");
				}
				stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"/"
			} catch (Exception e) {
				e.printStackTrace();
			}
            requestUrl = getBaseUrl() + action + stringBuffer.toString();
        }else{
            requestUrl = getBaseUrl() + action + "?" + getParamsUrl(params);
        }
        final String finalRequestUrl = requestUrl;
        executorService.submit(new Runnable() {
			@Override
			public void run() {
				RequestGetUrl(finalRequestUrl, Authorization,Instance,mRequestCallBack);
			}
		});
	}

	/**
	 * Post 可多文件上传
	 * @param action 接口名
	 * @param params 请求参数，上传文件格式params.put(file.getName(), file);
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	private static void post(final String action, final Map<String, Object> params,final String Authorization,final String Instance,final RequestCallBack mRequestCallBack) {
		final Map<String, Object> map = new HashMap<String, Object>();
		final Map<String, File> files = new HashMap<String, File>();
		for(Map.Entry<String, Object> entry : params.entrySet()){
			if(entry.getValue() instanceof File){
				files.put(entry.getKey(), (File)entry.getValue());
			} else {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				if(action.equals(Constans.SYNC_PURCHASES)){
				RequestPostUpLoadFile("https://tangerinepoints.com/dev/api/merchant/" + action, map,files,Authorization,Instance, mRequestCallBack);
				}else{
				RequestPostUpLoadFile(getBaseUrl() + action, map,files,Authorization,Instance, mRequestCallBack);
			}
			}
		});
	}


	/**
	 * Get请求
	 * */
	private static boolean RequestGetUrl(String requestUrl, final String Authorization,final String Instance,final RequestCallBack mRequestCallBack) {
		HttpURLConnection conn = null;
		try {
            URL url = new URL(requestUrl);
			//}

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(30 * 1000);// 设置连接主机超时（单位：毫秒）
			conn.setReadTimeout(30 * 1000);// 设置从主机读取数据超时（单位：毫秒）
			//判断如果Authorization和Instance不为空就添加到消息头
			if(!StringUtil.isEmpty(Authorization)||!StringUtil.isEmpty(Instance)) {
				conn.setRequestProperty("Authorization","Bearer "+Authorization);
				conn.setRequestProperty("X-App-Instance",Instance);
			}
			final int responseCode = conn.getResponseCode();
			final String message=conn.getResponseMessage();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 发送请求，获取服务器数据
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				final String json = buffer.toString();
				//在子线程中更新主线程（UI线程）
				handler.post(new Runnable() {
					@Override
					public void run() {
						mRequestCallBack.onSuccess(responseCode, json);
					}
				});
				return true;
			} else {
				String error = null;
				if(responseCode != 401){
					error = readErrorJsonString(conn);
				}

				final String finalError = error;
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 4XX 状态码表示请求可能出错
							if(!StringUtil.isEmpty(finalError)){
								String errors = GsonUtil.getJsonFromKey(finalError,"error");
								mRequestCallBack.onFailure(responseCode, errors);
							}else{
								mRequestCallBack.onFailure(responseCode, message);
							}
						} else if(responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 5XX 状态码表示服务器可能出错
							if(responseCode==500){
								mRequestCallBack.onFailure(responseCode, "The server encountered a temporary error and could not complete the request.");
							}else if(responseCode==503||responseCode==504){
								mRequestCallBack.onFailure(responseCode, "Service is temporarily unavailable. Please try again later.");
							}else{
								mRequestCallBack.onFailure(responseCode, "Server exception");
							}


						}
					}
				});
				return false;
			}
		} catch (final SocketTimeoutException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "Network Timeout");
				}
			});
			return false;
		} catch (final ConnectException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "Please check your network");
				}
			});
			return false;
		} catch (final IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "Parse data exception");
				}
			});
			return false;
		} finally {
			conn.disconnect();
		}
	}

	/**
	 * Post请求上传文件
	 */
	private static boolean RequestPostUpLoadFile(String RequestURL, Map<String, Object> params, Map<String, File> files,String Authorization,String Instance, final RequestCallBack mRequestCallBack){
		HttpURLConnection conn = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(100 * 1000);
			conn.setConnectTimeout(30 * 1000);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", UTF_8); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
			//判断如果Authorization和Instance不为空就添加到消息头
			if(!StringUtil.isEmpty(Authorization)||!StringUtil.isEmpty(Instance)) {
				conn.setRequestProperty("Authorization","Bearer "+Authorization);
				conn.setRequestProperty("X-App-Instance",Instance);
			}
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			if(params != null) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
					sb.append("Content-Type: text/plain; charset=" + UTF_8 + LINE_END);
					sb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
					sb.append(LINE_END);
					sb.append(StringUtil.isEmptyToString(entry.getValue()));
					sb.append(LINE_END);
				}
				dos.write(sb.toString().getBytes());
			}
			// 发送文件数据
			if (files != null){
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINE_END);
					sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINE_END);
					sb1.append("Content-Type: application/octet-stream; charset=" + UTF_8 + LINE_END);
					sb1.append(LINE_END);
					dos.write(sb1.toString().getBytes());
					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[4096];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						dos.write(buffer, 0, len);
					}
					is.close();
					dos.write(LINE_END.getBytes());
				}
			}
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			dos.write(end_data);
			dos.flush();

			final int responseCode = conn.getResponseCode();
			final String message = conn.getResponseMessage();
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			if(responseCode == HttpURLConnection.HTTP_OK){
				InputStream input = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				final String json = buffer.toString();
				handler.post(new Runnable() {
					@Override
					public void run() {
						mRequestCallBack.onSuccess(responseCode, json);
					}
				});
				return true;
			}else {
				String error = null;
				if(responseCode != 401){
					error = readErrorJsonString(conn);
				}

				final String finalError = error;
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 4XX 状态码表示请求可能出错
							if(!StringUtil.isEmpty(finalError)){
								String errors = GsonUtil.getJsonFromKey(finalError,"error");
								mRequestCallBack.onFailure(responseCode, errors);
							}else{
								mRequestCallBack.onFailure(responseCode, message);
							}

						} else if(responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 5XX 状态码表示服务器可能出错
							if(responseCode==500){
								mRequestCallBack.onFailure(responseCode, "The server encountered a temporary error and could not complete the request.");
							}else if(responseCode==503||responseCode==504){
								mRequestCallBack.onFailure(responseCode, "Service is temporarily unavailable. Please try again later.");
							}else{
								mRequestCallBack.onFailure(responseCode, "Server exception");
							}
						}
					}
				});
				return false;
			}
		} catch (final SocketTimeoutException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "Network Timeout");
				}
			});
			return false;
		} catch (final ConnectException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "Please check your network");
				}
			});
			return false;
		} catch (final IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "Parse data exception");
				}
			});
			return false;
		} finally {
			conn.disconnect();
		}
	}

	private static String readErrorJsonString(HttpURLConnection conn){
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
		StringBuilder sb = new StringBuilder();
		String output = null;
		try {
			while ((output = br.readLine()) != null) {
                sb.append(output);
            }
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  null;
	}

		/**
         * 自动组装请求参数
         * */
	private static String getParamsUrl(Map<String, Object> params) {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		try {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(StringUtil.isEmptyToString(entry.getValue()),UTF_8)).append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}



}
