package com.test.yaoyao;

import java.util.HashMap;
import java.util.Map;

import com.test.util.HttpClientUtils;


public class CommentTest {
	
	public static void main(String[] args) {
		Map<String, String> parmap = new HashMap<String, String>();
		parmap.put("commentId", "2");
		parmap.put("merchantId", "100");
		parmap.put("productId", "20");
		parmap.put("merchantReply", "我是商家");
		String url = "http://127.0.0.1:8090/yaoyao/groupon/merchantReply";
		String res = HttpClientUtils.sendPostRequest(url, parmap,"utf-8");
		System.out.println(res);
	}

}
