package com.hongkun.finance.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.dao.SmsTelMsgDao;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.service.SmsTelMsgService;
import com.hongkun.finance.sms.utils.SingletonClient;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.SpecialCodeGenerateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hongkun.finance.sms.constants.SmsConstants.*;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.SmsTelMsgServiceImpl.java
 * @Class Name    : SmsTelMsgServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class SmsTelMsgServiceImpl implements SmsTelMsgService {

	private static final Logger logger = LoggerFactory.getLogger(SmsTelMsgServiceImpl.class);
	
	/**
	 * SmsTelMsgDAO
	 */
	@Autowired
	private SmsTelMsgDao smsTelMsgDao;
	
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertSmsTelMsgForSmsCode(String login, String prefix) {
	    prefix = StringUtils.isBlank(prefix) ? "" : prefix;
//		检验已发送的短信次数
		String count = JedisClusterUtils.get(prefix + REDIS_SMS_COUNT + login);
		if(StringUtils.isBlank(count)){
			count = "1";
		}else{
			count = String.valueOf(Integer.parseInt(count) + 1);
		}
		String maxCounts = Optional.ofNullable(PropertiesHolder.getProperty("sms.max.times")).orElse("5");
		logger.info("登录次数: {}, 最大次数: {}", count, maxCounts);
		if(Integer.parseInt(count) > Integer.parseInt(maxCounts)){
			return new ResponseEntity<>(ERROR, "此手机号今日短信获取次数达到上限   ，请明日再试!");
		}
//		判断redis中是否有为失效的短信验证码
		String key = prefix + REDIS_SMS_PRFFIX + login;
		if(JedisClusterUtils.exists(key)){
			return new ResponseEntity<>(ERROR, "短信验证码已发送，注意查收");
		}
		ResponseEntity<?> responeEntity = null;
		//生成6位随机数
		String num = SpecialCodeGenerateUtils.getSpecialNumCode(6);
		SmsTelMsg smsTelMsg = new SmsTelMsg();
		smsTelMsg.setTel(Long.parseLong(login));
		smsTelMsg.setMsg(num);
		smsTelMsg.setType(SmsConstants.SMS_TYPE_VALIDATE_CODE);
		String openMandaoSms = PropertiesHolder.getProperty("open.mandao.sms");
		int result = 1;
		if("1".equals(openMandaoSms)){
			result = SingletonClient.getClient().sendSms(String.valueOf(login), String.format(SmsMsgTemplate.MSG_SMS_CODE.getMsg(), num));
		}
		responeEntity = this.insertSmsTelMsg(smsTelMsg, result);
		//缓存验证码
		if(result > 0){
			JedisClusterUtils.set(key, num, REDIS_SMS_EXPIRES_TIME);
			JedisClusterUtils.set(
                    prefix + REDIS_SMS_COUNT + String.valueOf(login), count, 
					(int)((DateUtils.getLastTimeOfDay().getTime() - System.currentTimeMillis())/1000));
		}
		return responeEntity;
	}
	
	@Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> validateCode(long login, String smsCode) {
        logger.info("need to validate tel {} and smsCode {}", login, smsCode);
        ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS);
        SmsTelMsg smsTelMsg = new SmsTelMsg();
        smsTelMsg.setTel(login);
        smsTelMsg.setMsg(smsCode);
        smsTelMsg.setState(SmsConstants.SMS_TEL_STATE_SUCCESS);
        smsTelMsg.setSortColumns("ID DESC");
        List<SmsTelMsg> list = this.smsTelMsgDao.findByCondition(smsTelMsg);
        if(list == null || list.isEmpty()){
            return new ResponseEntity<>(ERROR, "无效的短信验证码");
        }
        SmsTelMsg result = list.get(0);
		JedisClusterUtils.delete(SmsConstants.REDIS_SMS_PRFFIX + login);
        if(System.currentTimeMillis() - result.getCreateTime().getTime() > SmsConstants.REDIS_SMS_EXPIRES_TIME * 1000){
            SmsTelMsg object = new SmsTelMsg();
            object.setId(result.getId());
            object.setState(SmsConstants.SMS_TEL_STATE_USE);
            this.smsTelMsgDao.update(object);
            return new ResponseEntity<>(ERROR, "短信验证码已失效");
        }
        if(!smsCode.equals(result.getMsg())){
            return new ResponseEntity<>(Constants.ERROR, "错误的短信验证码");
        }
        return responseEntity;
    }

    @Override
    public boolean validateExist(long login, String smsCode) {
        logger.info("need to validate tel {} and smsCode {}", login, smsCode);
        SmsTelMsg smsTelMsg = new SmsTelMsg();
        smsTelMsg.setTel(login);
        smsTelMsg.setMsg(smsCode);
        return this.smsTelMsgDao.getTotalCount(smsTelMsg) > 0;
    }
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertSmsTelMsg(SmsTelMsg smsTelMsg) {
		if(smsTelMsg == null) {
			return new ResponseEntity<>(ERROR, "未找到可发送的短信内容");
		}
		String time = "";
		if(smsTelMsg.getSendTime() != null){
		    time = DateUtils.format(smsTelMsg.getSendTime(), DateUtils.DATE_HH_MM_SS);
        }
		int result = SingletonClient.getClient().sendSms(String.valueOf(smsTelMsg.getTel()), smsTelMsg.getMsg(), time);
		return this.insertSmsTelMsg(smsTelMsg, result);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertSmsTelMsgWithSuffix(SmsTelMsg smsTelMsg, String suffix) {
        String time = "";
        if(smsTelMsg.getSendTime() != null){
            time = DateUtils.format(smsTelMsg.getSendTime(), DateUtils.DATE_HH_MM_SS);
        }
		int result = SingletonClient.getClient().sendSmsWithSuffix(String.valueOf(smsTelMsg.getTel()), smsTelMsg.getMsg(), suffix, time);
		return insertSmsTelMsg(smsTelMsg, result);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertSmsTelMsg(SmsTelMsg smsTelMsg, String suffix, String extCode, String time, String rrid,
			String msgfmt) {
		String result = SingletonClient.getClient().sendSms(String.valueOf(smsTelMsg.getTel()), smsTelMsg.getMsg(), suffix, extCode, time, rrid, msgfmt);
		return this.insertSmsTelMsg(smsTelMsg, (StringUtils.isBlank(result) || result.contains("-")) ? 1 : -1);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertSmsTelMsgBatch(List<SmsTelMsg> list, int count) {
		List<SmsTelMsg> errList = new ArrayList<>();
		if(list != null && !list.isEmpty()){
			for(SmsTelMsg smsTelMsg : list){
				int result = SingletonClient.getClient().sendSms(String.valueOf(smsTelMsg.getTel()), smsTelMsg.getMsg());
				if(result < 0){
					errList.add(smsTelMsg);
				}
				smsTelMsg.setState(1);
				smsTelMsg.setInfo(String.valueOf(result));
				if(result < 0){
					smsTelMsg.setState(0);
				}
			}
		}
		this.smsTelMsgDao.insertBatch(SmsTelMsg.class, list, count);
		return new ResponseEntity<>(SUCCESS, errList);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateSmsTelMsg(SmsTelMsg smsTelMsg) {
		this.smsTelMsgDao.update(smsTelMsg);
	}
	
	@Override
	public SmsTelMsg findSmsTelMsgById(int id) {
		return this.smsTelMsgDao.findByPK(Long.valueOf(id), SmsTelMsg.class);
	}
	
	@Override
	public List<SmsTelMsg> findSmsTelMsgList(SmsTelMsg smsTelMsg) {
		return this.smsTelMsgDao.findByCondition(smsTelMsg);
	}
	
	@Override
	public Pager findSmsTelMsgList(SmsTelMsg smsTelMsg, Pager pager) {
		return this.smsTelMsgDao.findByCondition(smsTelMsg, pager);
	}
	
	@Override
	public int findSmsTelMsgCount(SmsTelMsg smsTelMsg){
		return this.smsTelMsgDao.getTotalCount(smsTelMsg);
	}
	
	/**
	 *  @Description    : 插入短信内容
	 *  @Method_Name    : inserSmsTelMsg
	 *  @param smsTelMsg
	 *  @param result
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月8日 下午7:04:21 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> insertSmsTelMsg(SmsTelMsg smsTelMsg, int result){
		smsTelMsg.setState(1);
		smsTelMsg.setInfo(String.valueOf(result));
		if(result < 0){
			smsTelMsg.setState(0);
		}
		this.smsTelMsgDao.save(smsTelMsg);
		return new ResponseEntity<>(result < 0 ? ERROR : SUCCESS, result < 0 ? "短信验证码发送失败" : "");
	}

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertSmsTelMsgOnly(SmsTelMsg smsTelMsg) {
        if(smsTelMsg != null) {
            this.smsTelMsgDao.save(smsTelMsg);
        }
    }

	@Override
	public ResponseEntity<?> retryTelSms(int id) {
		//添加redis锁防止并发，重复发送短信
		JedisClusterLock lock = new JedisClusterLock();
		String lockKey = LOCK_PREFFIX + SmsTelMsg.class.getSimpleName() + id;
		String successIdsKey = SmsTelMsg.class.getSimpleName() + "_SUCCESSIDS";
		try {
			if (lock.lock(lockKey)){
				//判断该短信记录是否存在，状态是否正确
				SmsTelMsg smsTelMsg = smsTelMsgDao.findByPK(Long.valueOf(id),SmsTelMsg.class);
				if (smsTelMsg == null){
					return new ResponseEntity<>(ERROR,"该短信记录不存在!");
				}
				if (!(smsTelMsg.getState() == SmsConstants.SMS_TEL_STATE_INIT)){
					return new ResponseEntity<>(ERROR,"只有初始化状态的短信才能进行重发!");
				}
				//判断该短信记录类型是否正确
				if (smsTelMsg.getType() == SmsConstants.SMS_TYPE_VALIDATE_CODE){
					return new ResponseEntity<>(ERROR,"不允许重发验证码类型的短信!");
				}
				//获取redis中已成功发送短信记录的id，发送后台重复操作造成重复发送短信
				List<Integer> successIds = JedisClusterUtils.getObjectForJson(successIdsKey, new TypeReference<List<Integer>>() {});
				successIds = successIds == null ? new ArrayList<>() : successIds;
				if (!successIds.contains(id)){
					//调用短信接口重发该条短信
					int result = SingletonClient.getClient().sendSms(String.valueOf(smsTelMsg.getTel()), smsTelMsg.getMsg());
					if (result == -1){
						return new ResponseEntity<>(ERROR,"短信发送失败!");
					}else {
						successIds.add(id);
						JedisClusterUtils.setAsJson(successIdsKey,successIds);
					}
				}
				//发送成功更新该短信记录状态
				SmsTelMsg updateSms = new SmsTelMsg();
				updateSms.setState(SmsConstants.SMS_TEL_STATE_RETRY_SUCCESS);
				updateSms.setId(id);
				int flag = smsTelMsgDao.update(updateSms);
				if (flag !=1){
					return new ResponseEntity<>(ERROR,"短信发送成功，但记录更新失败，请联系技术人员及时处理!");
				}else {
					successIds.remove(Integer.valueOf(id));
					JedisClusterUtils.setAsJson(successIdsKey,successIds);
				}
			}else {
				return new ResponseEntity<>(ERROR,"操作太过频繁，请稍后重试!");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(ERROR,"短信发送异常，请联系技术人员及时处理!");
		} finally {
			lock.freeLock(lockKey);
		}
		return new ResponseEntity<>(SUCCESS);
	}
}
