package com.hongkun.finance.payment.client.yeepay.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*字符串 DESede(3DES) 加密
 * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位
 * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的
 * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加
 * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的
 * 密钥，P代表明文，C代表密表，这样，
 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
 * */
public class ThreeDes {
	/**
	 * @param args在java中调用sun公司提供的3DES加密解密算法时，需要使
	 * 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包：
	 *jce.jar
	 *security/US_export_policy.jar
	 *security/local_policy.jar
	 *ext/sunjce_provider.jar 
	 */
	private static final String Algorithm = "DESede"; // 定义 加密算法,可用

	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
//      添加新安全算法,如果用JCE就要把它添加进去
//    Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		int addProvider = Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// System.out.print(deskey);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// keybyte为加密密钥，长度为24字节
	// src为加密后的缓冲区
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// 转换成十六进制字符串
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	public static String byteArr2HexStr(byte[] arrB) {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		// 最大128位
		String result = sb.toString();
		return result;
	}

	public static void main(String[] args) throws Exception {
//      添加新安全算法,如果用JCE就要把它添加进去
//    Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// 添加新安全算法,如果用JCE就要把它添加进去
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());

		String szSrc = "123456";
		System.out.println("加密前的字符串:" + szSrc);

		byte[] encoded = encryptMode("JUNNET_123456_123456_COM".getBytes(), szSrc.getBytes());
		System.out.println("加密后的字符串:" + ThreeDes.byteArr2HexStr(encoded));

		byte[] srcBytes = decryptMode("JUNNET_123456_123456_COM".getBytes(), encoded);
		System.out.println("解密后的字符串:" + (new String(srcBytes)));
	}
    
}

