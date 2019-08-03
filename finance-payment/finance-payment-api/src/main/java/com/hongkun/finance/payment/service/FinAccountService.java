package com.hongkun.finance.payment.service;

import com.hongkun.finance.payment.model.FinAccount;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinAccountService.java
 * @Class Name : FinAccountService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinAccountService {

    /**
     * @Described : 单条插入
     * @param finAccount 持久化的数据对象
     * @return : void
     */
    int insert(FinAccount finAccount);

    /**
     * @Described : 批量插入
     * @param List<FinAccount> 批量插入的数据
     * @return : void
     */
    void insertBatch(List<FinAccount> list);

    /**
     * @Described : 更新数据
     * @param finAccount 要更新的数据
     * @return : void
     */
    int update(FinAccount finAccount);

    /**
     * @Described : 通过id查询数据
     * @param id id值
     * @return FinAccount
     */
    FinAccount findById(int id);

    /**
     * @Described : 条件检索数据
     * @param finAccount 检索条件
     * @return List<FinAccount>
     */
    List<FinAccount> findByCondition(FinAccount finAccount);

    /**
     * @Described : 条件检索数据
     * @param finAccount 检索条件
     * @param start 起始页
     * @param limit 检索条数
     * @return List<FinAccount>
     */
    List<FinAccount> findByCondition(FinAccount finAccount, int start, int limit);

    /**
     * @Described : 条件检索数据
     * @param finAccount 检索条件
     * @param pager 分页数据
     * @return List<FinAccount>
     */
    Pager findByCondition(FinAccount finAccount, Pager pager);

    /**
     * @Description : caoxb 方法描述:通過用戶ID查詢賬戶信息
     * @Method_Name : findByRegUserId
     * @return : void
     * @Creation Date : 2017年6月14日 下午6:46:22
     * @Author : caoxinbang@hongkun.com.cn 曹新帮
     */

    FinAccount findByRegUserId(Integer regUserId);

    /**
     * @Description :账户的批量更新
     * @Method_Name : updateFinAccountBatch;
     * @param list 要更新数据,根据ID更新
     * @param count多少条数提交一次
     * @return : void;
     * @Creation Date : 2017年6月26日 上午10:39:14;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    void updateFinAccountBatch(List<FinAccount> list, int count);

    /**
     * @Description :根据用户ID，批量更新账户信息
     * @Method_Name : updateFinAccountBatchByUserId;
     * @param list
     * @param count
     * @return : void;
     * @Creation Date : 2017年7月4日 下午2:55:20;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    int updateFinAccountBatchByUserId(List<FinAccount> list, int count);
    /**
     * 
     *  @Description    : 查询借款人和本金接收人账户
     *  @Method_Name    : findBorroweAndReceiveAccount
     *  @param borrowerId
     *  @param receiptUserId
     *  @return
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2018年2月2日 上午10:23:06 
     *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
     */
	ResponseEntity<?> findBorroweAndReceiveAccount(Integer borrowerId, Integer receiptUserId);
	
	/**
	 *  @Description    : 查询账户资产信息
	 *  @Method_Name    : findFinAccontInfo
	 *  @param regUserId
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月12日 下午4:34:56 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Map<String, Object> findFinAccontInfo(Integer regUserId);

    /**
     * @Description : 设置支付密码
     * @Method_Name : setPayPassword
     * @Date : 2018/3/15 10:55
     * @Author : ruilima@hongkun.com.cn 马瑞丽
     * @param regUserId
     * @param payPassword
     * @return
     */
    ResponseEntity<?> setPayPassword(Integer regUserId,String payPassword);

    /**
     * @Description : 判断支付密码是否正确
     * @Method_Name : judgePayPassword
     * @Date : 2018/03/22 15:55
     * @Author : pengwu@hongkun.com.cn
     * @param regUserId
     * @param payPassword
     * @return
     */
    ResponseEntity<?> judgePayPassword(Integer regUserId,String payPassword);
    /**
     * @Description : 根据用户id集合查询用户账户集合
     * @Method_Name : judgePayPassword
     * @Date : 2018/03/22 15:55
     * @Author : pengwu@hongkun.com.cn
     * @param regUserId
     * @param payPassword
     * @return
     */
	List<FinAccount> findFinAccountByRegUserIds(List<Integer> regUserIds);
}
