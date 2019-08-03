package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.dao.PointAccountDao;
import com.hongkun.finance.point.dao.PointRecordDao;
import com.hongkun.finance.point.dao.PointRuleDao;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.vo.PointVO;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.hongkun.finance.point.constants.PointConstants.POINT_STATE_CONFIRMED;
import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointRecordServiceImpl.java
 * @Class Name    : PointRecordServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class PointRecordServiceImpl implements PointRecordService {

    private static final Logger logger = LoggerFactory.getLogger(PointRecordServiceImpl.class);

	@Autowired
	private PointRecordDao pointRecordDao;
	@Autowired
	private PointAccountDao pointAccountDao;
	@Autowired
	private PointRuleDao pointRuleDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int insertPointRecord(PointRecord pointRecord) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: insertPointRecord, 插入积分记录, 入参: 积分信息: {}", pointRecord);
        }
        try {
			pointRecord.setPlatform(1);
            return this.pointRecordDao.save(pointRecord);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("插入积分记录异常, 积分记录: {}\n异常信息: ", pointRecord, e);
            }
            throw new GeneralException("插入积分记录异常,请重试");
        }

    }


    @Override
    public PointRecord findPointRecordById(int id) {
        return this.pointRecordDao.findByPK(Long.valueOf(id), PointRecord.class);
    }

    @Override
    public List<PointRecord> findPointRecordList(PointRecord pointRecord) {
        return this.pointRecordDao.findByCondition(pointRecord);
    }

    @Override
    public List<PointRecord> findPointRecordList(PointRecord pointRecord, int start, int limit) {
        return this.pointRecordDao.findByCondition(pointRecord, start, limit);
    }

    @Override
    public Pager findPointRecordList(PointRecord pointRecord, Pager pager) {
        return this.pointRecordDao.findByCondition(pointRecord, pager);
    }

    @Override
    public int findPointRecordCount(PointRecord pointRecord) {
        return this.pointRecordDao.getTotalCount(pointRecord);
    }

    @Override
    public Pager listPointRecord(PointVO pointVO, Pager pager) {
        return this.pointRecordDao.listPointRecord(pointVO, pager);
    }

	@Override
	@Compensable(cancelMethod = "transferPointsCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, readOnly = false)
	public ResponseEntity<?> transferPoints(int points, BigDecimal worth, int userId, int type, Integer businessId, String comments) {
		logger.info("tcc transferPoints entrance, reference point#transferPoint. 物业缴费划转积分, 用户标识: {}, 划转积分: {}, 操作类型: {}, 缴费记录id: {}",userId, points, type, businessId);
		try {
			return new ResponseEntity<>(SUCCESS, this.transferPoint(points,worth,userId,type,businessId,comments));
		}catch (Exception e){
			logger.error("tcc error transferPoints, 物业缴费划转积分, 用户标识: {}, 划转积分: {}, 操作类型: {}, 缴费记录id: {}, 异常信息: ",
					userId, points, type, businessId, e);
			throw new GeneralException("积分划转异常！");
		}
	}

	private int transferPoint(int points, BigDecimal worth, int userId, int type, Integer businessId, String comments){
		//1、更新积分账户
		PointAccount pointAccount = new PointAccount();
		pointAccount.setRegUserId(userId);
		pointAccount.setPoint(points);
		
		pointAccountDao.updateByRegUserId(pointAccount);
		//2、添加积分划转记录
		PointRecord pointRecord = new PointRecord();
		pointRecord.setType(type);
		pointRecord.setRegUserId(userId);
		pointRecord.setBusinessId(businessId);
		pointRecord.setWorth(worth);
		pointRecord.setRealWorth(worth);
		pointRecord.setComments(comments);
		pointRecord.setPoint(points);
		pointRecord.setPlatform(1);
		pointRecordDao.save(pointRecord);
		return pointRecord.getId();
	}
	/**
	 * @param points
	 * @param worth
	 * @param userId
	 * @param type
	 * @param businessId
	 * @return : ResponseEntity<?>
	 * @Description : TCC回滚方法  transferPoints
	 * @Method_Name : transferPointsCancel
	 * @Creation Date  : 2017年10月17日 下午4:33:39
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public ResponseEntity<?> transferPointsCancel(int points, BigDecimal worth, int userId, int type, int businessId) {
		logger.info("tcc transferPointsCancel entrance, reference point#transferPoint. 物业缴费划转积分, 用户标识: {}, 划转积分: {}, 操作类型: {}, 缴费记录id: {}",userId, points, type, businessId);
		try {
			//先校验是否已经操作了
			PointRecord pointRecord = new PointRecord();
			pointRecord.setType(type);
			pointRecord.setRegUserId(userId);
			pointRecord.setBusinessId(businessId);
			List<PointRecord> records =  pointRecordDao.findByCondition(pointRecord);
			if(CommonUtils.isNotEmpty(records)){
				//1、更新积分账户
				PointAccount pointAccount = new PointAccount();
				pointAccount.setRegUserId(userId);
				pointAccount.setPoint(points * -1);
				pointAccountDao.updateByRegUserId(pointAccount);
				//2、删除积分划转记录
				pointRecordDao.deleteByContidion(pointRecord);
			}
		} catch (Exception e) {
			logger.error("tcc error transferPointsCancel, 物业缴费划转积分, 用户标识: {}, 划转积分: {}, 操作类型: {}, 缴费记录id: {}, 异常信息: ",
					userId, points, type, businessId, e);
			throw new GeneralException("积分划转异常！");
		}
		return new ResponseEntity<>(SUCCESS);
	}

   /**
   *  @Description    ：审核用户积分
   *  @Method_Name    ：updateRecordState
   *  @param pointVO
   *  @param currentUserId
   *  @return com.yirun.framework.core.model.ResponseEntity
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    @Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity updateRecordState(PointVO pointVO, Integer currentUserId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updateRecordState, 更新积分记录状态, 入参: 积分信息: {}, 操作用户ID: {}", pointVO, currentUserId);
        }
        try {
            List<Integer> recordIds = pointVO.getRecordIds();
            if (recordIds != null) {
                recordIds.stream().forEach((id) -> {
                    PointRecord record = pointRecordDao.findByPK(Long.valueOf(id), PointRecord.class);
                    PointRecord updateRecord = new PointRecord();
                    if (equelsIntWraperPrimit(pointVO.getRecordState(), POINT_STATE_CONFIRMED)) {
                        //审核通过，给用户的积分账上面加入积分
                        //更新用户的积分账户
                        updateRecord.setId(record.getId());
                        updateRecord.setModifyUserId(currentUserId);

                        PointAccount userAccount = new PointAccount();
                        userAccount.setRegUserId(record.getRegUserId());
                        userAccount.setPoint(record.getPoint());
                        //更新用户账户
                        pointAccountDao.updateByRegUserId(userAccount);
                        //发送站内信
                        sendMsgToUser(pointVO.getRegUserId(),pointVO.getPoint());
                        if (logger.isInfoEnabled()) {
                            logger.info("平台执行赠送积分操作, 操作用户ID: {}, 目标用户ID: {}, 积分值: {}", currentUserId, pointVO.getRegUserId(), pointVO.getPoint());
                        }
                    }
                    updateRecord.setState(pointVO.getRecordState());
                    //审核失败设置失败原因
                    if (equelsIntWraperPrimit(pointVO.getRecordState(), PointConstants.POINT_STATE_CHECK_FAIL)) {
                        updateRecord.setRefuseCause(pointVO.getRefuseCause());
                    }
                    pointRecordDao.update(updateRecord);
                });
            }
            if (logger.isInfoEnabled()) {
                logger.info("执行积分赠送审核, 操作用户ID: {},操作积分记录IDS: {},操作状态: {}", currentUserId, pointVO.getRecordIds(), pointVO.getRecordState());
            }
            return ResponseEntity.SUCCESS;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新积分记录状态, 积分信息: {}, 操作用户ID: {}\n异常信息: ", pointVO, currentUserId, e);
            }
            throw new GeneralException("更新积分记录状态异常,请重试");
        }
    }

    /**
     *  @Description    : 平台赠送积分给用户发送站内信
     *  @Method_Name    : sendMsgToUser;
     *  @param userId 用户ID
     *  @param pointValue 积分值
     *  @return         : void;
     *  @Creation Date  : 2018年10月17日 上午9:49:08;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    public void sendMsgToUser(Integer userId,Integer pointValue){
        try{
            String msg = SmsMsgTemplate.MSG_USER_POINT_GIVE_SUCCESS.getMsg();
            SmsSendUtil.sendSmsMsgToQueue(
                    new SmsWebMsg(userId, SmsMsgTemplate.MSG_USER_POINT_GIVE_SUCCESS.getTitle(), msg,
                            SmsConstants.SMS_TYPE_NOTICE, new String[] { String.valueOf(pointValue) }));
        }catch(Exception e){
            logger.error("平台执行赠送积分操作发送站内信异常, 操作用户ID: {}, 积分值: {}",userId,pointValue,e);
        }
    }
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertPointRecordBatch(List<PointRecord> list) {
        this.pointRecordDao.insertBatch(PointRecord.class, list);
    }
}
