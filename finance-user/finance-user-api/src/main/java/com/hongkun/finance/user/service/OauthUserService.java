package com.hongkun.finance.user.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.user.model.OauthUser;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.OauthUserService.java
 * @Class Name    : OauthUserService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface OauthUserService {
	
	/**
	 * @Described			: 单条插入
	 * @param oauthUser 持久化的数据对象
	 * @return				: void
	 */
	void insertOauthUser(OauthUser oauthUser);
	
	/**
	 * @Described			: 批量插入
	 * @param List<OauthUser> 批量插入的数据
	 * @return				: void
	 */
	void insertOauthUserBatch(List<OauthUser> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<OauthUser> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertOauthUserBatch(List<OauthUser> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param oauthUser 要更新的数据
	 * @return				: void
	 */
	void updateOauthUser(OauthUser oauthUser);
	
	/**
	 * @Described			: 批量更新数据
	 * @param oauthUser 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateOauthUserBatch(List<OauthUser> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	OauthUser
	 */
	OauthUser findOauthUserById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param oauthUser 检索条件
	 * @return	List<OauthUser>
	 */
	List<OauthUser> findOauthUserList(OauthUser oauthUser);
	
	/**
	 * @Described			: 条件检索数据
	 * @param oauthUser 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<OauthUser>
	 */
	List<OauthUser> findOauthUserList(OauthUser oauthUser, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param oauthUser 检索条件
	 * @param pager	分页数据
	 * @return	List<OauthUser>
	 */
	Pager findOauthUserList(OauthUser oauthUser, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param oauthUser 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findOauthUserCount(OauthUser oauthUser);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findOauthUserList(OauthUser oauthUser, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findOauthUserCount(OauthUser oauthUser, String sqlName);

	/**
	*  @Description    ：通过用户名检索oauthUser
	*  @Method_Name    ：findOauthUserByUserName
	*  @param userName
	*  @return com.hongkun.finance.user.model.OauthUser
	*  @Creation Date  ：2018/4/17
	*  @Author         ：zhichaoding@hongkun.com.cn
	*/
	OauthUser findOauthUserByUserName(String userName);
}
