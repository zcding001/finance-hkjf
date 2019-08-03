package com.hongkun.finance.sms.utils;

import com.yirun.framework.core.utils.PropertiesHolder;

/**
 * @Described	: 短信第三方接口(单例)
 * @project		: com.yirun.framework.sms.utils.SingletonClient
 * @author 		: zc.ding
 * @date 		: 2017年3月14日
 */
public class SingletonClient {
//	漫道接口
	private volatile static ClientOfManDao clientOfManDao;
//	同步锁
	private static Object LOCK = new Object();
	
	private SingletonClient(){}
	
	/**
	 * @Described			: 初始化漫道短信接口客户端
	 * @author				: zc.ding
	 * @project				: framework-sms
	 * @package				: com.yirun.framework.sms.utils.SingletonClient.java
	 * @return				: ClientOfManDao
	 * @date 				: 2017年3月14日
	 * @param softwareSerialNo
	 * @param key
	 * @return
	 */
	public static ClientOfManDao getClient(String softwareSerialNo, String key){
		if(clientOfManDao == null){
			synchronized (LOCK) {
				if(clientOfManDao == null){
					try {
						clientOfManDao = new ClientOfManDao(softwareSerialNo, key);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return clientOfManDao;
	}
	
	/**
	 * @Described			: 初始化漫道短信接口客户端
	 * @author				: zc.ding
	 * @project				: framework-sms
	 * @package				: com.yirun.framework.sms.utils.SingletonClient.java
	 * @return				: ClientOfManDao
	 * @date 				: 2017年3月14日
	 * @return
	 */
	public static ClientOfManDao getClient(){
		if(clientOfManDao == null){
			synchronized (LOCK) {
				if(clientOfManDao == null){
					try {
						clientOfManDao=new ClientOfManDao(
								PropertiesHolder.getProperty("sms.mandao.softwareSerialNo"),
								PropertiesHolder.getProperty("sms.mandao.key"), 
								PropertiesHolder.getProperty("sms.mandao.uri")
								);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return clientOfManDao;
	}
}
