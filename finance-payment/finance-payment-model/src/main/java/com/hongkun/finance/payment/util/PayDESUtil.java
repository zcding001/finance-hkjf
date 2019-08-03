package com.hongkun.finance.payment.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @Description : DES通用工具类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.utils.PayDESUtils.java
 * @Author : yanbinghuang
 */
public class PayDESUtil {

	private static final String CRYPT_ALGORITHM = "DESede";
	/** 加密Key */
	public static final String PAY_KEY = "C415B35829573470jMN1d97Y";

	/**
	 * @Description : 解密
	 * @Method_Name : decrypt;
	 * @param value
	 *            需要解密的值
	 * @param key
	 *            解密密钥
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年6月15日 上午11:40:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String decrypt(String value, String key) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
			Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);

			byte[] decodedByte = Base64.decodeBase64(value.getBytes("UTF-8"));
			byte[] decryptedByte = cipher.doFinal(decodedByte);
			return new String(decryptedByte, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @Description : 解密
	 * @Method_Name : decrypt;
	 * @param value
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年6月15日 上午11:40:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String decrypt(String value) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(PAY_KEY.getBytes("UTF-8"), CRYPT_ALGORITHM);
			Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);

			byte[] decodedByte = Base64.decodeBase64(value.getBytes("UTF-8"));
			byte[] decryptedByte = cipher.doFinal(decodedByte);
			return new String(decryptedByte, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @Description : 加密
	 * @Method_Name : encrypt;
	 * @param value
	 *            需要加密的值
	 * @param key
	 *            密钥
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年6月15日 上午11:41:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String encrypt(String value, String key) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
			Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);

			byte[] encryptedByte = cipher.doFinal(value.getBytes("UTF-8"));
			byte[] encodedByte = Base64.encodeBase64(encryptedByte);
			return new String(encodedByte, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Description : 加密
	 * @Method_Name : encrypt;
	 * @param value
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年6月15日 上午11:41:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String encrypt(String value) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(PAY_KEY.getBytes("UTF-8"), CRYPT_ALGORITHM);
			Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);

			byte[] encryptedByte = cipher.doFinal(value.getBytes("UTF-8"));
			byte[] encodedByte = Base64.encodeBase64(encryptedByte);
			return new String(encodedByte, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String szSrc = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		System.out.println("加密前的字符串:" + szSrc);
		System.out.println(szSrc.length());
		String encoded = encrypt(szSrc, PAY_KEY);
		System.out.println("加密后的字符串:" + encoded);
		System.out.println(encoded.length());

		String srcBytes = decrypt(encoded, PAY_KEY);
		System.out.println("解密后的字符串:" + srcBytes);

	}
}
