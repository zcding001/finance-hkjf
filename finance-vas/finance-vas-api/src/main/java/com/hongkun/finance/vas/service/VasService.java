package com.hongkun.finance.vas.service;

import java.util.Map;

/**
 * @Description : 增值服务service
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.service.VasService
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public interface VasService {

	/**
	 *  @Description    : 一次加载增值服务数据
	 *  @Method_Name    : getVasInfo
	 *  @param regUserId: 用户id
	 *  @param items	: 长度为5位，索引0~4位表示获取钱袋子规则、用户等级&成长值、用户可领取红包数量、用户体验金金额、投资红包&加息券&好友券数量，1表示查询 ，非0表示不查询
	 *  					eg : 11 表示只查询 钱袋子规则和用户等级，10001表示只查询钱袋子规则和投资红包&加息券&好友券数量
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月13日 下午3:21:42 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
    Map<String,Object> getVasInfo(int regUserId, String items);
}
