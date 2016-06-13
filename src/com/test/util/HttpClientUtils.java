package com.test.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.test.Constant;

public class HttpClientUtils {
	
	protected HttpClientUtils() {

	}
	
	final static int BUFFER_SIZE = 4096;

	/**
	 * Apache Http Post 请求<br>
	 * 
	 * @param urlStr
	 *            -请求路径
	 * @param parmap
	 *            -请求参数
	 * @param charSet
	 *            -编码
	 */
	public static String sendPostRequest(String urlStr,
			Map<String, String> parmap, String charSet) {
		long begainTime = System.currentTimeMillis();
		HttpClient client = new HttpClient();
		// 设置超时时间 假如超时 则返回 ""
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(15 * 1000);
		// 表示用Post方式提交
		PostMethod method = new PostMethod(urlStr);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				charSet);

		// Token
		parmap.put("chshToken", Constant.CHSH_TOKEN);
		// 签名
		parmap.put("chshSign", SignUtils.sign(SignUtils.createLinkStr(parmap)));

		// 设置请求参数
		if (null != parmap && parmap.size() > 0) {
			Iterator it = parmap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> me = (Map.Entry) it.next();
				method.addParameter(me.getKey(), me.getValue() == null ? ""
						: me.getValue());
			}
		}
		try {
			int status = client.executeMethod(method);
			if (status == 200) {
				// String rs = new String(method.getResponseBody(), charSet);
				String rs = null;
				try {
					rs = InputStreamTOString(method.getResponseBodyAsStream(), charSet);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return rs;
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return null;
	}

	/**
	 * Apache get请求
	 * 
	 * @param urlStr
	 *            -请求路径
	 */
	public static String sendApacheGetRequst(String urlStr, String charSet) {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(urlStr);
		// 当请求发生跳转时是否继续请求
		get.setFollowRedirects(true);
		try {
			client.executeMethod(get);
			return new String(get.getResponseBody(), charSet);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return null;
	}
	
	/**  
     * 将InputStream转换成String
     * 
     * @param in InputStream  
     * @return String  
     * @throws Exception  
     *   
     */  
    public static String InputStreamTOString(InputStream in, String charset) throws Exception{  
          
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(), charset);  
    }

}
