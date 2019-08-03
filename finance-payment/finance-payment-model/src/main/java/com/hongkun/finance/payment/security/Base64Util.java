package com.hongkun.finance.payment.security;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;

public class Base64Util {
	/**
	 * @Description : 对字符串进行BASE64加密，返回加密字符串
	 * @Method_Name : encode;
	 * @param str
	 *            字符串
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月18日 上午10:42:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String encode(String str) {
		if (str == null)
			return null;
		try {
			return (new sun.misc.BASE64Encoder()).encode(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Description : 对字节进行BASE64加密，返回加密字符串
	 * @Method_Name : encode;
	 * @param str
	 *            字符串
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月18日 上午10:42:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String encode(byte[] b) {
		return (new sun.misc.BASE64Encoder()).encode(b);
	}

	/**
	 * @Description : 对BASE64 编码的字符串 str 进行解码,返回字符串
	 * @Method_Name : decodes;
	 * @param str
	 *            编码的字符串
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月18日 上午10:43:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String decode(String str) {
		if (str == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(str);
			return new String(b, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Description : 对BASE64 编码的字符串 str 进行解码,返回字节
	 * @Method_Name : decodes;
	 * @param str
	 *            编码的字符串
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月18日 上午10:43:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static byte[] decodes(String str) {
		if (str == null) {
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(str);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
