package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.model.vo.RedPacketVO;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.dao.VasRedpacketInfoDao.java
 * @Class Name    : VasRedpacketInfoDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface VasRedpacketInfoDao extends MyBatisBaseDao<VasRedpacketInfo, Long> {

    BigDecimal findVasRedpacketTotalNum(Map map);

    /**
     * 统计红包未审核的状态
     * @param collect
     * @return
     */
    Long findUnCheckRedpackStatesByIds(List<Integer> collect);

    /**
     * 批量修改红包的状态
     *  @param state
     * @param reason
     * @param idsList  @return
     * @param checkUserId
     */
    Integer udpateRedPacketStateByIds(Integer state, String reason, List<Integer> idsList, Integer checkUserId);

    /**
     * 删除红包
     * @param unChangedPacketIds
     * @param reason
     * @param id
     * @return
     */
    Integer deleteRedPacket(List<Integer> unChangedPacketIds, String reason, Integer id);

    /**
     * @param condition     查询条件
     * @param pager          分页参数(必填)
     * @return : ResponseEntity
     * @Description : 获取当前用户红包记录
     * @Method_Name : getRedPacketInfoList
     * @Creation Date  : 2018年03月09日 下午15:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    List<VasRedpacketInfo> getRedPacketInfoList(VasRedpacketInfo condition, Pager pager);

    /**
     * @param condition     查询条件
     * @return : Integer
     * @Description : 获取当前用户红包记录数量
     * @Method_Name : getRedPacketInfoListCount
     * @Creation Date  : 2018年03月09日 下午15:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    Integer getRedPacketInfoListCount(VasRedpacketInfo condition);

    /**
     *  @Description    ：根据红包key获取红包信息
     *  @Method_Name    ：findVasRedpacketInfoByKey
     *  @param key
     *  @return com.hongkun.finance.vas.model.VasRedpacketInfo
     *  @Creation Date  ：2018/4/17
     *  @Author         ：pengwu@hongkun.com.cn
     */
    VasRedpacketInfo findVasRedpacketInfoByKey(String key);

    /**
    *  @Description    ：找截止当前日期失效的红包信息
    *  @Method_Name    ：findInvalidRedPackets
    *  @param currentDate
    *  @return java.util.List<com.hongkun.finance.vas.model.VasRedpacketInfo>
    *  @Creation Date  ：2018/4/27
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<VasRedpacketInfo> findInvalidRedPackets(Date currentDate);

    /**
     *  @Description    ：红包兑换
     *  @Method_Name    ：exchangeRedPacketInfo
     *  @param updateRedPacket 兑换的红包信息
     *  @return int
     *  @Creation Date  ：2018/5/21
     *  @Author         ：pengwu@hongkun.com.cn
     */
    int exchangeRedPacketInfo(VasRedpacketInfo updateRedPacket);

    /**
    *  @Description    ：查找指定id的，状态为待审核的红包
    *  @Method_Name    ：findUnCheckRedpacksByIds
    *  @param ids
    *  @return java.util.List<com.hongkun.finance.vas.model.VasRedpacketInfo>
    *  @Creation Date  ：2018/6/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<VasRedpacketInfo> findUnCheckRedpacksByIds(List<Integer> ids);
    /**
     *  @Description    : 查询领取红包人数
     *  @Method_Name    : findRedPacketUserSum;
     *  @param updateRedPacket
     *  @return
     *  @return         : RedPacketVO;
     *  @Creation Date  : 2018年9月21日 下午1:44:54;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    RedPacketVO findRedPacketUserSum (VasRedpacketInfo updateRedPacket);
}
