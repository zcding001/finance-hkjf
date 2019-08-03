package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.dao.VasRedpacketInfoDao;
import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.model.vo.RedPacketVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.dao.impl.VasRedpacketInfoDaoImpl.java
 * @Class Name    : VasRedpacketInfoDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasRedpacketInfoDaoImpl extends MyBatisBaseDaoImpl<VasRedpacketInfo, Long> implements VasRedpacketInfoDao {

    private static final String FIND_VAS_REDPACKET_TOTAL_NUM=".findVasRedpacketTotalNum";

    private static final String FIND_REDPACKET_STATES_BY_IDS=".findUnCheckRedpackStatesByIds";

    private static final String FIND_REDPACKET_BY_IDS=".findUnCheckRedpacketsByIds";

    private static final String UPDATE_REDPACKET_STATE_BY_IDS=".udpateRedPacketStateByIds";

    private static final String DELETE_REDPACKET=".deleteRedPacket";

    private static final String GET_RED_PACKET_INFO_LIST=".getRedPacketInfoList";

    private static final String GET_RED_PACKET_INFO_LIST_COUNT=".getRedPacketInfoListCount";


    @Override
    public BigDecimal findVasRedpacketTotalNum(Map map) {
        return  getCurSqlSessionTemplate().selectOne(VasRedpacketInfo.class.getName() + FIND_VAS_REDPACKET_TOTAL_NUM, map);
    }

    @Override
    public Long findUnCheckRedpackStatesByIds(List<Integer> list) {
        return  getCurSqlSessionTemplate().selectOne(VasRedpacketInfo.class.getName() + FIND_REDPACKET_STATES_BY_IDS,list);
    }

    @Override
    public Integer udpateRedPacketStateByIds(Integer state, String reason, List<Integer> list, Integer checkUserId) {
        return  getCurSqlSessionTemplate().update(VasRedpacketInfo.class.getName() + UPDATE_REDPACKET_STATE_BY_IDS,new HashMap(){
            {
                put("state", state);
                put("reason", reason);
                put("list", list);
                put("checkUserId", checkUserId);
            }
        });
    }

    @Override
    public Integer deleteRedPacket(List<Integer> unChangedPacketIds, String reason, Integer checkUserId) {
        return  getCurSqlSessionTemplate().update(VasRedpacketInfo.class.getName() + DELETE_REDPACKET,new HashMap(){
            {
                put("reason", reason);
                put("list", unChangedPacketIds);
                put("checkUserId", checkUserId);
            }
        });
    }

    @Override
    public List<VasRedpacketInfo> getRedPacketInfoList(VasRedpacketInfo condition, Pager pager) {
        return (List<VasRedpacketInfo>) this.findByCondition(condition,pager,VasRedpacketInfo.class,GET_RED_PACKET_INFO_LIST).getData();
    }

    @Override
    public Integer getRedPacketInfoListCount(VasRedpacketInfo condition) {
        return Integer.valueOf(this.getCurSqlSessionTemplate().selectOne(VasRedpacketInfo.class.getName() +
                GET_RED_PACKET_INFO_LIST_COUNT,condition).toString());
    }

    @Override
    public VasRedpacketInfo findVasRedpacketInfoByKey(String key) {
        return this.getCurSqlSessionTemplate().selectOne(VasRedpacketInfo.class.getName() +
                ".findVasRedpacketInfoByKey", key);
    }

    @Override
    public List<VasRedpacketInfo> findInvalidRedPackets(Date currentDate) {
        return this.getCurSqlSessionTemplate().selectList(VasRedpacketInfo.class.getName() +
                ".findInvalidRedPackets", currentDate);
    }

    @Override
    public int exchangeRedPacketInfo(VasRedpacketInfo updateRedPacket) {
        return this.getCurSqlSessionTemplate().update(VasRedpacketInfo.class.getName() +
                ".exchangeRedPacketInfo",updateRedPacket);
    }

    @Override
    public List<VasRedpacketInfo> findUnCheckRedpacksByIds(List<Integer> list) {
        return  getCurSqlSessionTemplate().selectList(VasRedpacketInfo.class.getName() + FIND_REDPACKET_BY_IDS,list);
    }

    @Override
    public RedPacketVO findRedPacketUserSum(VasRedpacketInfo updateRedPacket) {
        return this.getCurSqlSessionTemplate().selectOne(VasRedpacketInfo.class.getName() +
                ".findRedPacketUserSum", updateRedPacket);
    }
}
