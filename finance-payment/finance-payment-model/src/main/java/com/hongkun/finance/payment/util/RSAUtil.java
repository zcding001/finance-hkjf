package com.hongkun.finance.payment.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hongkun.finance.payment.security.Base64Util;
import com.yirun.framework.core.utils.ReadCipherFileUtil;

/**
 * @Description : RSA签名公共类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.utils.RSAUtil.java
 * @Author : yanbinghuang
 */
public class RSAUtil {
	private static final Logger log = LoggerFactory.getLogger(RSAUtil.class);
	/** 编码 */
	public final static String ENCODE = "UTF-8";
	public final static String RSA_CHIPER = "RSA/ECB/PKCS1Padding";
	/** 1024bit 加密块 大小 */
	public final static int ENCRYPT_KEYSIZE = 117;
	/** 1024bit 解密块 大小 */
	public final static int DECRYPT_KEYSIZE = 128;
	/**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

	private static RSAUtil instance;

	private RSAUtil() {

	}

	public static RSAUtil getInstance() {
		if (null == instance)
			return new RSAUtil();
		return instance;
	}

	/**
	 * @Description 签名处理
	 * @param prikeyvalue：私钥文件
	 * @param sign_str：签名源内容
	 * @Creation Date : 2018年1月12日 下午4:42:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
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
	 * @Description 签名验证
	 * @param pubkeyvalue：公钥
	 * @param oid_str：源串
	 * @param signed_str：签名结果串
	 * @Creation Date : 2018年1月12日 下午4:42:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 * @return boolean
	 */
	public static boolean checksign(String pubkeyvalue, String oid_str, String signed_str) {
		try {
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64Util.decodes(pubkeyvalue));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			byte[] signed = Base64Util.decodes(signed_str);// 这是SignatureData输出的数字签名
			java.security.Signature signetcheck = java.security.Signature.getInstance("MD5withRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(oid_str.getBytes("UTF-8"));
			return signetcheck.verify(signed);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
    *
    * 用私钥对信息生成数字签名
    * @param data 已加密数据
    * @return
    * @throws Exception
    */
   public static String sign(byte[] data, byte[] keyBytes) throws Exception {
       PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
       KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
       PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
       Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
       signature.initSign(privateK);
       signature.update(data);
       return FormatUtil.byte2Hex(signature.sign());
   }
	/**
	 * @Description : 根据私钥文件加密
	 * @Method_Name : encryptByPriPfxFile;
	 * @param content
	 *            内容
	 * @param pfxPath
	 *            路径
	 * @param priKeyPass
	 *            私钥密码
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月12日 下午4:42:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String encryptByPriPfxFile(String content, String pfxPath, String priKeyPass) {
		PrivateKey privateKey = ReadCipherFileUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);
		if (privateKey == null) {
			return null;
		}
		return encryptByPrivateKey(content, privateKey);
	}
	/**
     * @encryptStr 摘要
     * @pfxPath pfx证书路径
     * @priKeyPass 私钥
     * @charset 编码方式
     * 签名
     */
    public static String encryptByRSA(String encryptStr, String pfxPath, String priKeyPass)throws Exception {
        PrivateKey privateKey = ReadCipherFileUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);
        return  sign(encryptStr.getBytes("UTF-8") ,privateKey.getEncoded());
    }
	/**
	 * @Description : 指定Cer公钥路径解密
	 * @Method_Name : decryptByPubCerFile;
	 * @param content
	 * @param pubCerPath
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月15日 上午9:54:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String decryptByPubCerFile(String content, String pubCerPath) {
		PublicKey publicKey = ReadCipherFileUtil.getPublicKeyFromFile(pubCerPath);
		if (publicKey == null) {
			return null;
		}
		return decryptByPublicKey(content, publicKey);
	}
	/**
	 * @Description : 指定Cer公钥路径加密
	 * @Method_Name : decryptByPubCerFile;
	 * @param content
	 * @param pubCerPath
	 * @return
	 * @return : String;
	 * @Creation Date : 2018-05-10 16:13:21;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static String encryptByPubCerFile(String content, String pubCerPath) {
		PublicKey publicKey = ReadCipherFileUtil.getPublicKeyFromFile(pubCerPath);
		if (publicKey == null) {
			return null;
		}
		return encryptByPublicKey(content, publicKey);
	}
	/**
	 * 公钥加密返回
	 * 
	 * @param content
	 * @param publicKey
	 * @param encryptMode
	 * @return hex串
	 */
	public static String encryptByPublicKey(String content, PublicKey publicKey) {
		byte[] destBytes = rsaByPublicKey(content.getBytes(), publicKey, Cipher.ENCRYPT_MODE);

		if (destBytes == null) {
			return null;
		}

		return FormatUtil.byte2Hex(destBytes);

	}

	/**
	 * @Description : 根据公钥解密
	 * @Method_Name : decryptByPublicKey;
	 * @param content
	 * @param publicKey
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月15日 上午10:02:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String decryptByPublicKey(String content, PublicKey publicKey) {

		try {
			byte[] destBytes = rsaByPublicKey(hex2Bytes(content), publicKey, Cipher.DECRYPT_MODE);
			if (destBytes == null) {
				return null;
			}
			return new String(destBytes, ENCODE);
		} catch (UnsupportedEncodingException e) {
			log.error("解密内容不是正确的UTF8格式:", e);
		}
		return null;
	}
	/**
	 * 根据私钥文件解密
	 * 
	 * @param src
	 * @param pfxPath
	 * @param priKeyPass
	 * @return
	 */
	public static String decryptByPriPfxFile(String src, String pfxPath, String priKeyPass) {
		if (FormatUtil.isEmpty(src) || FormatUtil.isEmpty(pfxPath)) {
			return null;
		}
		PrivateKey privateKey = ReadCipherFileUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);
		if (privateKey == null) {
			return null;
		}
		return decryptByPrivateKey(src, privateKey);
	}
	/**
	 * 私钥解密
	 * 
	 * @param src
	 * @param privateKey
	 * @return
	 */
	public static String decryptByPrivateKey(String src, PrivateKey privateKey) {
		if (FormatUtil.isEmpty(src)) {
			return null;
		}
		try {
			byte[] destBytes = rsaByPrivateKey(FormatUtil.hex2Bytes(src), privateKey, Cipher.DECRYPT_MODE);
			if (destBytes == null) {
				return null;
			}
			return new String(destBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
//			//log.error("解密内容不是正确的UTF8格式:", e);
		} catch (Exception e) {
//			//log.error("解密内容异常", e);
		}

		return null;
	}
	/**
	 * @Description : 根据私钥加密
	 * @Method_Name : encryptByPrivateKey;
	 * @param content
	 * @param privateKey
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月12日 下午4:57:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String encryptByPrivateKey(String content, PrivateKey privateKey) {
		byte[] destBytes = rsaByPrivateKey(content.getBytes(), privateKey, Cipher.ENCRYPT_MODE);
		if (destBytes == null) {
			return null;
		}
		return byte2Hex(destBytes);
	}

	/**
	 * @Description : 将byte[] 转换成字符串
	 * @Method_Name : byte2Hex;
	 * @param srcBytes
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月12日 下午4:49:26;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String byte2Hex(byte[] srcBytes) {
		StringBuilder hexRetSB = new StringBuilder();
		for (byte b : srcBytes) {
			String hexString = Integer.toHexString(0x00ff & b);
			hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
		}
		return hexRetSB.toString();
	}

	/**
	 * @Description : 将字符串转换成16进制字节
	 * @Method_Name : hex2Bytes;
	 * @param source
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月15日 上午10:00:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static byte[] hex2Bytes(String source) {
		byte[] sourceBytes = new byte[source.length() / 2];
		for (int i = 0; i < sourceBytes.length; i++) {
			sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
		}
		return sourceBytes;
	}

	/**
	 * @Description : 私钥算法
	 * @Method_Name : rsaByPrivateKey;
	 * @param srcData
	 *            源字节
	 * @param privateKey
	 *            私钥
	 * @param mode
	 *            加密 OR 解密
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月12日 下午4:49:43;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static byte[] rsaByPrivateKey(byte[] srcData, PrivateKey privateKey, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_CHIPER);
			cipher.init(mode, privateKey);
			// 分段加密
			int blockSize = (mode == Cipher.ENCRYPT_MODE) ? ENCRYPT_KEYSIZE : DECRYPT_KEYSIZE;
			byte[] decryptData = null;

			for (int i = 0; i < srcData.length; i += blockSize) {
				byte[] doFinal = cipher.doFinal(subArray(srcData, i, i + blockSize));
				decryptData = addAll(decryptData, doFinal);
			}
			return decryptData;
		} catch (NoSuchAlgorithmException e) {
			log.error("私钥算法-不存在的解密算法:", e);
		} catch (NoSuchPaddingException e) {
			log.error("私钥算法-无效的补位算法:", e);
		} catch (IllegalBlockSizeException e) {
			log.error("私钥算法-无效的块大小:", e);
		} catch (BadPaddingException e) {
			log.error("私钥算法-补位算法异常:", e);
		} catch (InvalidKeyException e) {
			log.error("私钥算法-无效的私钥:", e);
		}
		return null;
	}

	/**
	 * @Description : 公钥算法
	 * @Method_Name : rsaByPublicKey;
	 * @param srcData
	 *            源字节
	 * @param publicKey
	 *            公钥
	 * @param mode
	 *            加密 OR 解密
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月15日 上午9:59:56;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static byte[] rsaByPublicKey(byte[] srcData, PublicKey publicKey, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_CHIPER);
			cipher.init(mode, publicKey);
			// 分段加密
			int blockSize = (mode == Cipher.ENCRYPT_MODE) ? ENCRYPT_KEYSIZE : DECRYPT_KEYSIZE;
			byte[] encryptedData = null;
			for (int i = 0; i < srcData.length; i += blockSize) {
				// 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
				byte[] doFinal = cipher.doFinal(subArray(srcData, i, i + blockSize));
				encryptedData = addAll(encryptedData, doFinal);
			}
			return encryptedData;
		} catch (NoSuchAlgorithmException e) {
			log.error("公钥算法-不存在的解密算法:", e);
		} catch (NoSuchPaddingException e) {
			log.error("公钥算法-无效的补位算法:", e);
		} catch (IllegalBlockSizeException e) {
			log.error("公钥算法-无效的块大小:", e);
		} catch (BadPaddingException e) {
			log.error("公钥算法-补位算法异常:", e);
		} catch (InvalidKeyException e) {
			log.error("公钥算法-无效的私钥:", e);
		}
		return null;
	}

	/**
	 * @Description : 将数组分隔成指定大小的数组
	 * @Method_Name : subArray;
	 * @param array
	 * @param startIndexInclusive
	 * @param endIndexExclusive
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月12日 下午5:08:06;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static byte[] subArray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return new byte[0];
		}
		byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * @Description : 将两个字节数组合并
	 * @Method_Name : addAll;
	 * @param array1
	 *            字节数组1
	 * @param array2
	 *            字节数组2
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月12日 下午4:51:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static byte[] addAll(byte[] array1, byte[] array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * @Description : 克隆数组
	 * @Method_Name : clone;
	 * @param array
	 * @return
	 * @return : byte[];
	 * @Creation Date : 2018年1月12日 下午5:05:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return (byte[]) array.clone();
	}

	/**
	 * @Description : MD5加密
	 * @Method_Name : MD5;
	 * @param str
	 *            加密参数
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年1月15日 下午3:50:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String MD5(String str) {
		if (str == null)
			return null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte[] digest = md5.digest();
			StringBuffer hexString = new StringBuffer();
			String strTemp;
			for (int i = 0; i < digest.length; i++) {
				strTemp = Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
				hexString.append(strTemp);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * @Description : 根据公钥验证数字签名
     * @encryptStr 摘要
     * @signature  签名
     * @pubCerPath 公钥路径
     * 验签
     * @Creation Date : 2018-05-11 9:23:18;
	 * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static boolean verifySignature(String pubCerPath, String encryptStr, String signature) throws Exception {
        PublicKey publicKey = ReadCipherFileUtil.getPublicKeyFromFile(pubCerPath);
        return  verify(encryptStr.getBytes(ENCODE),publicKey.getEncoded(), signature);
    }
    /**
     * @Description : 校验数字签名
     * @param data 已加密数据
     * @param keyBytes 公钥
     * @param sign 数字签名
     * @throws Exception
     * @Creation Date : 2018-05-11 9:25:15;
	 * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static boolean verify(byte[] data, byte[] keyBytes, String sign) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(FormatUtil.hex2Bytes(sign));
    }
    
    /**
     * @Description 根据map生成RSA签名
     * @param map
     * @param privateKey
     * @Author : binliang@hongkun.com.cn 梁彬;
     * @Creation Date :2018-09-13 16:47:21
     * @return
     */
	public static String handleRSA(TreeMap<String, Object> map,
			String privateKey) {
		StringBuffer sbuffer = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				sbuffer.append(entry.getValue());
			}
		}
		String signTemp = sbuffer.toString();
		String sign = "";
		if (privateKey != null) {
			sign = sign(signTemp, privateKey);
		}
		return sign;
	}
	
	
    
}
