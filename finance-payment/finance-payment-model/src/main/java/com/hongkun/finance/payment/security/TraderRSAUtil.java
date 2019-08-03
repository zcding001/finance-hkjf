package com.hongkun.finance.payment.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RSA签名公共类
 * 
 * @author shmily
 */
public class TraderRSAUtil {

	private static TraderRSAUtil instance;

	private TraderRSAUtil() {

	}

	public static TraderRSAUtil getInstance() {
		if (null == instance)
			return new TraderRSAUtil();
		return instance;
	}

	/**
	 * 签名处理
	 * 
	 * @param prikeyvalue：私钥
	 * @param sign_str：签名源内容
	 * @return
	 */
	public static String sign(String prikeyvalue, String sign_str) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.decodes(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(sign_str.getBytes("UTF-8"));
			byte[] signed = signet.sign(); // 对信息的数字签名
			return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 签名验证
	 * 
	 * @param pubkeyvalue：公�?
	 * @param oid_str：源�?
	 * @param signed_str：签名结果串
	 * @return
	 */
	public static boolean checksign(String pubkeyvalue, String oid_str, String signed_str) {
		try {
			if (signed_str == null || "".equals(signed_str)) {
				return false;
			}
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64Util.decodes(pubkeyvalue));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			byte[] signed = Base64Util.decodes(signed_str);// 这是SignatureData输出的数字签�?
			java.security.Signature signetcheck = java.security.Signature.getInstance("MD5withRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(oid_str.getBytes("UTF-8"));
			return signetcheck.verify(signed);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/*-----------------------------------
   笨方法：String s = "你要去除的字符串";
   去除空格：s = s.replace('\\s','');
   去除回车：s = s.replace('\n','');
   这样也可以把空格和回车去掉，其他也可以照这样做。
   注：\n 回车(\u000a)
   \t 水平制表符(\u0009)
   \s 空格(\u0008)
   \r 换行(\u000d)*/
	public static String replaceBlank(String str) {
		String dest = "";
		String newDest = "";
		str = str.trim();
		if (str!=null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
			//一个或多个空格
			Pattern blank = Pattern.compile("[' ']+");
			Matcher bm = blank.matcher(dest);
			newDest = bm.replaceAll("+").trim();
		}
		return newDest;
	}
}
