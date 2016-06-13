package com.test.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.test.Constant;


public class SignUtils {
	
	 /**
     * requestParams请求参数的转换
     * 
     * @param params
     *            请求的参数
     * @return 验证结果
     */
    public static Map<String, String> conversion(Map map) {
        if (null == map || map.size() <= 0) {
            return null;
        }
        Map<String, String> params = new HashMap<String, String>(map.size());
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) map.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 签名字符串(接口系统提供给外的签名)
     * 
     * @param text
     *            需要签名的字符串
     * @return 签名结果
     */
    public static String sign(String text) {
        return signParam(text, Constant.CHSH_SIGN);
    }

    /**
     * 签名字符串
     * 
     * @param text
     *            需要签名的字符串
     * @return 签名结果
     */
    public static String signParam(String text, String signKey) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        text = text + signKey;
        try {
            return DigestUtils.md5Hex(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 参数组装成一个字符串
     * 
     * @param params
     *            需要签名的字符串
     * @return 签名结果
     */
    public static String createLinkStr(Map<String, String> params) {
        // 没有包含签名;或者签名值为空
        if (null == params || params.size() <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        List<String> kl = new ArrayList<String>(params.keySet());
        // 排序
        Collections.sort(kl);
        for (String key : kl) {
            String value = params.get(key);
            if (!StringUtils.equals(key, "chshSign")
                    && StringUtils.isNotBlank(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 验证签名
     * 
     * @param params
     *            请求的参数
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params) {
        String linkStr = createLinkStr(params);
        if (StringUtils.isBlank(linkStr)) {
            return false;
        }
        String sign = params.get("chshSign");
        return StringUtils.equals(sign(linkStr), sign);
    }

    /**
     * 验证签名(万通公司专业)
     * 
     * @param params
     *            请求的参数
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params, String wtKey) {
        String linkStr = createLinkStr(params);
        if (StringUtils.isBlank(linkStr)) {
            return false;
        }
        String sign = params.get("chshSign");
        return StringUtils.equals(signParam(linkStr, wtKey), sign);
    }

}
