package com.hongkun.finance.user.utils;
import com.yirun.framework.core.utils.crypto.MD5;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;  
  
/**
 *  
 * @Description   : RSA非对称加密解密工具类, 支持JS、IOS、android、php(待测)加密，服务端解密，由于ios和android中已存在公钥，所以!!!!非特殊情况下不得更新私钥!!!
 * @Project       : finance-user-model
 * @Program Name  : com.hongkun.finance.user.utils.RsaEncryptUtil.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class RsaEncryptUtil {  
	/**
	 * 默认公钥
	 */
	public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChL6GJmmrKhiYuJTwW8fVX+75GIGaGA2EosBdlQJhrW4T9xTVNi4x/rwg+JMnnXOEX3k/qhBoAsgWxWJTPtcdptJNLOebGYG79mUfgziB+MouIYR+BF4h8XpMvfosKgvADkRjh2I9yg5HhYG6rT9B4LwXvfm/YwLnJvJCqQxXaRQIDAQAB";
	/**
	 * 默认私钥
	 */
	public static final String DEFAULT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKEvoYmaasqGJi4lPBbx9Vf7vkYgZoYDYSiwF2VAmGtbhP3FNU2LjH+vCD4kyedc4RfeT+qEGgCyBbFYlM+1x2m0k0s55sZgbv2ZR+DOIH4yi4hhH4EXiHxeky9+iwqC8AORGOHYj3KDkeFgbqtP0HgvBe9+b9jAucm8kKpDFdpFAgMBAAECgYAUbab5e5qhRFM+cfWlqtC/b+ZzmNOllHQR5g9xA7jNknHOf9n7k/4giJxw09TK/9h/X+uc+1UYVXenPGTP1mgJaBn/9V1IqSwI1pGEWE5iofW05+FAqjI1LQhl2YehaHsKJnxft58QMoDrDMwfqOXYladyr+Wd0beGxu6YHdHogQJBANAhmJD+i6CAH+IzKrsBLCRpP+MiHBDZWEx17kEQFHRYOicbNzM5dXJ2fIuUea3BNzKDiKDZCKVxGU77MgtrLDUCQQDGQfwoKPP4cAGShCHUqAOS9l4EnIm4XyYRFFnmNgQrbXsU2MxdQIrrOW1oko0zrHUW5CfjMG8aMc4Ajuc45hfRAkA/zSyxtfYje2NKuhitPszDVKhK/lfQKnBQ8A3bUAyVJb6d4k/nmgOQ1RWUzC0IwKQlDgFCHYXzAVqaxZIvwejJAkEAmzXPXfVpwqaVZIoTHjQG0EgWJNAUNYYv/BENrF+/dew4/oIXxC2iVDLaR5Lr/ndWF3y5CXDCmiRBnUhDFtVz8QJAQLLJFWnDD5HlXGKpMieHg1leuEIn2dMp0edgNNwsy0BTSpk60ByxbnIMv2L6HU4PcoP3IMFbi7+0/2SlD5L3gw==";
  
    /** 
     * 加密算法RSA 
     */  
    public static final String KEY_ALGORITHM = "RSA";// RSA/ECB/PKCS1Padding  
  
    /** 
     * String to hold name of the encryption padding. 
     */  
    public static final String PADDING = "RSA/NONE/PKCS1Padding";// RSA/NONE/NoPadding  
  
    /** 
     * String to hold name of the security provider. 
     */  
    public static final String PROVIDER = "BC";
    
    /** 
     * 签名算法 
     */  
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";  
  
    /** 
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
  
    /** 
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128;
    
    /**
	 * RSA密钥长度 默认1024位，密钥长度必须是64的倍数， 范围在512至65536位之间。
	 */
	private static final int KEY_SIZE = 1024;
  
    /* 
     * 公钥加密 
     */  
    public static String encryptByPublicKey(String str) throws Exception {  
        Security.addProvider(new BouncyCastleProvider());  
        Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);  
        // 获得公钥  
        Key publicKey = getPublicKey();  
        // 用公钥加密  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        // 读数据源  
        byte[] data = str.getBytes("UTF-8");  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
  
        return Base64Utils.encodeToString(encryptedData);  
    }  
  
    /* 
     * 私钥解密 
     */  
	public static String decryptByPrivateKey(String str) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
		// 得到Key
		Key privateKey = getPrivateKey();
		// 用私钥去解密
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 读数据源
		byte[] encryptedData = Base64Utils.decodeFromString(str);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		// 二进制数据要变成字符串需解码
		return new String(decryptedData, "UTF-8");
	}  
  
    /** 
     * 从文件中读取公钥 
     * @return 
     * @throws Exception 
     * @author kokJuis 
     * @date 2016-4-6 下午4:38:22 
     * @comment 
     */  
    private static Key getPublicKey() throws Exception {  
        byte[] keyBytes;  
        keyBytes = Base64Utils.decodeFromString(DEFAULT_PUBLIC_KEY);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey publicKey = keyFactory.generatePublic(keySpec);  
        return publicKey;  
    }  
  
    /** 
     * 从文件中读取公钥String 
     * @return 
     * @throws Exception 
     * @author kokJuis 
     * @date 2016-4-6 下午4:38:22 
     * @comment 
     */  
    public static String getStringPublicKey() throws Exception {  
        return DEFAULT_PUBLIC_KEY;  
    }  
  
    /** 
     * 获取私钥 
     *  
     * @return 
     * @throws Exception 
     * @author kokJuis 
     * @date 2016-4-7 上午11:46:12 
     * @comment 
     */  
    private static Key getPrivateKey() throws Exception {  
        byte[] keyBytes;  
        keyBytes = Base64Utils.decodeFromString(DEFAULT_PRIVATE_KEY);  
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
        return privateKey;  
    }  
    
    
    /**
	 *  @Description    : 生成秘钥对
	 *  @Method_Name    : initKeyPair
	 *  @throws Exception
	 *  @return         : String[] 里面存放的数据， 0：publicKey， 1：privateKey，2：publicExponent 3：modulus
	 *  @Creation Date  : 2017年5月26日 下午3:30:08 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static String [] initKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new String[]{Base64.encodeBase64String(publicKey.getEncoded()), 
				Base64.encodeBase64String(privateKey.getEncoded()),
				String.valueOf(Hex.encodeHex(publicKey.getPublicExponent().toByteArray())),
				String.valueOf(Hex.encodeHex(publicKey.getModulus().toByteArray()))};
	}
	
	/**
	 *  @Description    : 生成公私钥
	 *  @Method_Name    : testInit
	 *  @throws Exception
	 *  @return         : void
	 *  @Creation Date  : 2018年3月23日 下午5:37:26 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void testInit() throws Exception {
		String[] arr = initKeyPair();
		System.out.println(arr[0]);//公钥
		System.out.println(arr[1]);//私钥
		System.out.println(arr[2]);
		System.out.println(arr[3]);
	}
	
	/**
	 *  @Description    : 测试IOS的RSA解密
	 *  @Method_Name    : testIosRSA
	 *  @throws Exception
	 *  @return         : void
	 *  @Creation Date  : 2018年3月23日 下午5:23:33 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void testIosRsa() throws Exception{
		System.out.println("测试IOS解密");
		//IOS第一次加密
		String encPwd = "M05YrfoNKnlbGKaojzInq53l9tRdP4aow/AijMYUGO7PJNZ3omYXSHJPeQCUI97bKN/7W4lYzmDntWVszn/GZxK0XCLR0APuUPY65T3AuDqagMTc5YfeRs6ixY9yA8lXD0VyMLVJyNONmafWmjM1pR8d2dqPlM/Pbo3zeYFpCiE=";
		String decPwd = decryptByPrivateKey(encPwd);
		System.out.println(decPwd);
	}
	
	/**
	 *  @Description    : 测试android的RSA解密
	 *  @Method_Name    : testAndroidRSA
	 *  @throws Exception
	 *  @return         : void
	 *  @Creation Date  : 2018年3月23日 下午5:38:28 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void testAndroidRsa() throws Exception{
		System.out.println("测试android解密");
		//android第一次加密
		String encPwd = "gVt3918MB+Yo8G9mdXY6HLSxzynd9L7rQy5jUpswbi0tHKACtM8KsHsmPTB3Sgq/WOfX6oTWFq6u+TaS1ThEV3wTrMi/3i0n3GBJFG7a2lqeLBQfm5xw2c1oId5Gab017QdXzDXzvhXO4JHrxdjR3HejlyC/E8XWSaeKvVUMFuA=";
		String decPwd = decryptByPrivateKey(encPwd);
		System.out.println(decPwd);
	}
	
	/**
	 *  @Description    : 测试JS的RSA解密
	 *  @Method_Name    : testJsRsa
	 *  @throws Exception
	 *  @return         : void
	 *  @Creation Date  : 2018年3月26日 上午10:05:38 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void testJsRsa() throws Exception{
		System.out.println("测试JS解密");
		String encPwd = "QsZHLQibNaOen/MobWtQdzNCJar+xrraZDxssy38em7nuYy+ZeCMZZUpS1fjlmDxWyMTp6klu/lq/eHxtqz9UD2I2C3UjK4avIC+ceH60TX/kwbFe10E6SoRtiZrjqQsvHovy0fDZe3U3VWyEVYTGO0/AIs1LThtLFGrubOWNJI=";
		String decPwd = decryptByPrivateKey(encPwd);
		System.out.println(decPwd);
	}
	
	
	public static void main(String[] args) throws Exception{
//		String pwd = "412DD75BF975FB95";
		String pwd = "123456";
		System.out.println("数据库存储密码：" + MD5.encrypt(pwd));
		String encPwd = encryptByPublicKey(pwd);
		System.out.println("加密后数据");
		System.out.println(encPwd);
		String decPwd = decryptByPrivateKey(encPwd);
		System.out.println("解密后数据");
		System.out.println(decPwd);
		System.out.println("测试客户端解密");
		testInit();
		testJsRsa();
		testIosRsa();
		testAndroidRsa();
	}
	
  
}  