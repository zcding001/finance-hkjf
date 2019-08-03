package com.hongkun.finance.contract.util;

import com.ancun.aosp.client.AospClient;
import com.ancun.aosp.client.AospClientConfiguration;
import com.hongkun.finance.contract.constants.ContractConstants;
/**
 * 初始化安存客户端
 * @author liangbin
 *
 */
public class AospClientUtil {
	private static AospClient  aospClient;
	
	public static void init() {
		AospClientConfiguration conf = new AospClientConfiguration();
		conf.setConnectionTimeout(15000);
		conf.setMaxErrorRetry(5);
		conf.setMaxConnections(512);
		conf.setSocketTimeout(90000);
		String apiUrl = ContractConstants.API_ADDRESS;
		String partnerKey = ContractConstants.PARTNERKEY;
		String secret = ContractConstants.SECRET;
		aospClient = new AospClient(apiUrl,partnerKey,secret, conf);
	}

	public static void destroy() {
		if (aospClient != null) {
			aospClient.close();
		}
	}

	public static AospClient getAospClient(){
		return aospClient;
	}

	public static void setConn(AospClient aospClient) {

		AospClientUtil.aospClient = aospClient;
	}
}
