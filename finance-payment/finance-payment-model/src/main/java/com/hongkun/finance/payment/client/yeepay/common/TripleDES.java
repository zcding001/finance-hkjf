package com.hongkun.finance.payment.client.yeepay.common;

import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 * 3DES算法
 *
 */
public class TripleDES {
    public static void main(String[] args) throws Exception {  
        byte[] key = "JUNNET_123456_123456_COM".getBytes();  
        byte[] iv = "JUNNET_1".getBytes();  //密钥前八位
        byte[] data="123456".getBytes("GB2312");  
          
        System.out.println("3DES加密:");  
        String hexString = encryptToHex(data,key,iv);
//        String hexString2 = new String(decrypt(hexStringToBytes("55cb3e6c7b07182c"), key, iv),"GB2312");
        System.out.println(hexString);
//        System.out.println(hexString2);
    }
	
    static {  
        Security.addProvider(new com.sun.crypto.provider.SunJCE());  
    }  
  
    private static final String MCRYPT_TRIPLEDES = "DESede";  
    private static final String TRANSFORMATION = "DESede/ECB/PKCS7Padding";  
  
    /**
     * 加密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
      public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {  
    	  Security.addProvider(new BouncyCastleProvider());  
          DESedeKeySpec spec = new DESedeKeySpec(key);  
          SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");  
          SecretKey sec = keyFactory.generateSecret(spec);  
          Cipher cipher = Cipher.getInstance(TRANSFORMATION);  
          //IvParameterSpec IvParameters = new IvParameterSpec(iv);  
          cipher.init(Cipher.ENCRYPT_MODE, sec);  
          return cipher.doFinal(data);  
      }  
    /**
     * 解密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {  
        DESedeKeySpec spec = new DESedeKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);  
        SecretKey sec = keyFactory.generateSecret(spec);  
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);  
        IvParameterSpec IvParameters = new IvParameterSpec(iv);  
        cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);  
        return cipher.doFinal(data);  
    }  
  
  
    public static byte[] generateSecretKey() throws NoSuchAlgorithmException {  
        KeyGenerator keygen = KeyGenerator.getInstance(MCRYPT_TRIPLEDES);  
        return keygen.generateKey().getEncoded();  
    }  
  
    public static byte[] randomIVBytes() {  
        Random ran = new Random();  
        byte[] bytes = new byte[8];  
        for (int i = 0; i < bytes.length; ++i) {  
            bytes[i] = (byte) ran.nextInt(Byte.MAX_VALUE + 1);  
        }  
        return bytes;  
    }
    /**
     * Convert byte[] string to hex
     * @param arrB
     * @return
     */
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
    /** 
     * Convert hex string to byte[] 
     * @param hexString the hex string 
     * @return byte[] 
     */  
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
        }  
        return d;  
    }
    /** 
     * Convert char to byte 
     * @param c char 
     * @return byte 
     */  
     private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    } 
    /**
     * 加密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encryptToHex(byte[] data, byte[] key,byte[] iv) throws Exception {
    	return byteArr2HexStr(encrypt(data, key, iv));
    }
    /**
     * 解密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String decryptToString(byte[] data, byte[] key,byte[] iv) throws Exception {
    	return new String(decrypt(data, key, iv));
    }
  
  
}  
