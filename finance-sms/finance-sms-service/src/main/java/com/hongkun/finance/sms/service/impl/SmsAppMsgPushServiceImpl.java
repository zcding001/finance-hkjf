package com.hongkun.finance.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.hongkun.finance.sms.constants.AppMsgPushConfig;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.dao.SmsAppMsgPushDao;
import com.hongkun.finance.sms.model.SmsAppMsgPush;
import com.hongkun.finance.sms.service.SmsAppMsgPushService;
import com.hongkun.finance.sms.utils.AppMsgPushUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hongkun.finance.sms.constants.SmsConstants.SMS_APP_MSG_TYPE_LINK;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.sms.service.impl.SmsAppMsgPushServiceImpl.java
 * @Class Name    : SmsAppMsgPushServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class SmsAppMsgPushServiceImpl implements SmsAppMsgPushService {

	private static final Logger logger = LoggerFactory.getLogger(SmsAppMsgPushServiceImpl.class);
	
	/**
	 * SmsAppMsgPushDAO
	 */
	@Autowired
	private SmsAppMsgPushDao smsAppMsgPushDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertSmsAppMsgPush(SmsAppMsgPush smsAppMsgPush) {
		return this.smsAppMsgPushDao.save(smsAppMsgPush);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsAppMsgPushBatch(List<SmsAppMsgPush> list) {
		this.smsAppMsgPushDao.insertBatch(SmsAppMsgPush.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsAppMsgPushBatch(List<SmsAppMsgPush> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.smsAppMsgPushDao.insertBatch(SmsAppMsgPush.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateSmsAppMsgPush(SmsAppMsgPush smsAppMsgPush) {
		return this.smsAppMsgPushDao.update(smsAppMsgPush);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateSmsAppMsgPushBatch(List<SmsAppMsgPush> list, int count) {
		this.smsAppMsgPushDao.updateBatch(SmsAppMsgPush.class, list, count);
	}
	
	@Override
	public SmsAppMsgPush findSmsAppMsgPushById(int id) {
		return this.smsAppMsgPushDao.findByPK(Long.valueOf(id), SmsAppMsgPush.class);
	}
	
	@Override
	public List<SmsAppMsgPush> findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush) {
		return this.smsAppMsgPushDao.findByCondition(smsAppMsgPush);
	}
	
	@Override
	public List<SmsAppMsgPush> findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush, int start, int limit) {
		return this.smsAppMsgPushDao.findByCondition(smsAppMsgPush, start, limit);
	}
	
	@Override
	public Pager findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush, Pager pager) {
		return this.smsAppMsgPushDao.findByCondition(smsAppMsgPush, pager);
	}
	
	@Override
	public int findSmsAppMsgPushCount(SmsAppMsgPush smsAppMsgPush){
		return this.smsAppMsgPushDao.getTotalCount(smsAppMsgPush);
	}
	
	@Override
	public Pager findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush, Pager pager, String sqlName){
		return this.smsAppMsgPushDao.findByCondition(smsAppMsgPush, pager, sqlName);
	}
	
	@Override
	public Integer findSmsAppMsgPushCount(SmsAppMsgPush smsAppMsgPush, String sqlName){
		return this.smsAppMsgPushDao.getTotalCount(smsAppMsgPush, sqlName);
	}

	@Override
	public ResponseEntity addAppMsgPush(SmsAppMsgPush smsAppMsgPush) {
		//如果为即时发送
		if (smsAppMsgPush.getPushMode() == 0){
			String result = "";
			//若为给个人用户发送推送
			if (smsAppMsgPush.getRegUserId() != null && smsAppMsgPush.getRegUserId() != 0){
				result = this.pushToSingle(smsAppMsgPush);
			}else {
				result = this.push(smsAppMsgPush);
			}
			if (StringUtils.isNotBlank(result) && "ok".equals(result)){
				smsAppMsgPush.setState(SmsConstants.SMS_APP_MSG_STATE_SUCCESS);
			}else {
				smsAppMsgPush.setState(SmsConstants.SMS_APP_MSG_STATE_FAIL);
			}
		}else {
			smsAppMsgPush.setState(SmsConstants.SMS_APP_MSG_STATE_SEND);
		}
		if (this.insertSmsAppMsgPush(smsAppMsgPush) > 0){
			return new ResponseEntity(SUCCESS,"添加app消息推送成功");
		}
		return new ResponseEntity(ERROR,"添加app消息推送失败！");
	}

	/**
	 *  @Description    : 进行app端推送消息-推送消息给个人
	 *  @Method_Name    : pushToSingle
	 *  @param smsAppMsgPush
	 *  @return         : String
	 *  @Creation Date  : 2018年03月20日 上午09:36:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	private String pushToSingle(SmsAppMsgPush smsAppMsgPush) {
		logger.info("pushToSingle,用户标识：{},推送消息",smsAppMsgPush.getRegUserId());
		TransmissionTemplate template = getTemplate(smsAppMsgPush);
		SingleMessage singleMessage = new SingleMessage();
		singleMessage.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		singleMessage.setOfflineExpireTime(24 * 3600 * 1000);
		singleMessage.setData(template);
		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		singleMessage.setPushNetWorkType(0);
		Target target = new Target();
		target.setAppId(AppMsgPushConfig.getPushAppid());
		target.setAlias(smsAppMsgPush.getRegUserId().toString());

		IPushResult ret = null;
		String result;
		try {
			ret = AppMsgPushUtil.pushMessageToSingle(singleMessage, target);
		} catch (RequestException e) {
			logger.error("pushToSingle, 推送消息异常, 用户标识：{}, 推送消息: {}, 结果为：{}, 异常信息: ",
					smsAppMsgPush.getRegUserId(), smsAppMsgPush.toString(), JSON.toJSONString(ret) ,e);
			ret = AppMsgPushUtil.pushMessageToSingle(singleMessage, target, e.getRequestId());
		}
		logger.info("pushToSingle: {}, 推送消息至个人, 用户标识: {}, 推送消息: {}, 结果为：{}",
				smsAppMsgPush.getRegUserId(), smsAppMsgPush.toString(), JSON.toJSONString(ret));
		if (ret != null) {
			result = ret.getResponse().get("result").toString();
		} else {
			result = "error";
			logger.error("pushToSingle, 消息推送服务器响应异常, 用户标识: {}，",smsAppMsgPush.getRegUserId());
		}
		return result;
	}

	@Override
	public ResponseEntity deleteAppMsgPush(int id) {
		SmsAppMsgPush appMsgPush =  this.findSmsAppMsgPushById(id);
		if (appMsgPush == null){
			return new ResponseEntity(ERROR,"无效的app消息推送记录！");
		}
		appMsgPush.setState(SmsConstants.SMS_APP_MSG_STATE_DELETE);
		appMsgPush.setModifyTime(new Date());
		if (this.updateSmsAppMsgPush(appMsgPush) > 0){
			return new ResponseEntity(SUCCESS,"app消息推送删除成功！");
		}
		return new ResponseEntity(ERROR,"app消息推送删除失败！");
	}

	@Override
	public ResponseEntity stopAppMsgPush(int id) {
		SmsAppMsgPush appMsgPush =  this.findSmsAppMsgPushById(id);
		if (appMsgPush == null){
			return new ResponseEntity(ERROR,"无效的app消息推送记录！");
		}
		if (this.stop(appMsgPush)){
			appMsgPush.setState(SmsConstants.SMS_APP_MSG_STATE_STOP);
			appMsgPush.setModifyTime(new Date());
			if (this.updateSmsAppMsgPush(appMsgPush) > 0){
				return new ResponseEntity(SUCCESS,"app消息推送停止成功！");
			}
		}
		return new ResponseEntity(ERROR,"app消息推送停止失败！");
	}

	/**
	 *  @Description    : 进行app端推送消息-后台群推消息
	 *  @Method_Name    : push
	 *  @param smsAppMsgPush
	 *  @return         : String
	 *  @Creation Date  : 2018年02月05日 下午16:55:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	private String push(SmsAppMsgPush smsAppMsgPush) {
		TransmissionTemplate template = getTemplate(smsAppMsgPush);
		AppMessage message = new AppMessage();
		message.setOffline(true);
		if (smsAppMsgPush.getMsgExpireSet() == 1){
			// 离线有效时间，单位为毫秒，可选
			message.setOfflineExpireTime(smsAppMsgPush.getMsgExpireTime() * 3600 * 1000);
		}else{
			//未设置有效期，默认24小时
			message.setOfflineExpireTime(24 * 3600 * 1000);
		}
		message.setData(template);
		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		message.setPushNetWorkType(0);
		List<String> appIdList = new ArrayList<>();
		appIdList.add(AppMsgPushConfig.getPushAppid());
		//设置推送的应用
		message.setAppIdList(appIdList);
		//推送给App的目标用户需要满足的条件
		AppConditions cdt = new AppConditions();
		//手机类型
		List<String> phoneTypeList = new ArrayList<>();
		if (smsAppMsgPush.getTargetPlatform().contains("1")){
			phoneTypeList.add("ANDROID");
		}
		if (smsAppMsgPush.getTargetPlatform().contains("2")){
			phoneTypeList.add("IOS");
		}
		cdt.addCondition(AppConditions.PHONE_TYPE,phoneTypeList, AppConditions.OptType.or);

		//自定义用户标签
		List<String> tagList = new ArrayList<>();
		if (smsAppMsgPush.getTargetUser() == 1){
			if (smsAppMsgPush.getTargetUserTag().contains("1")){
				tagList.add("男");
			}
			if (smsAppMsgPush.getTargetUserTag().contains("2")){
				tagList.add("女");
			}
			if (smsAppMsgPush.getTargetUserTag().contains("3")){
				tagList.add("未投资");
			}
			if (smsAppMsgPush.getTargetUserTag().contains("4")){
				tagList.add("已投资");
			}
			if (smsAppMsgPush.getTargetUserTag().contains("5")){
				tagList.add("有投资资质");
			}
			if (smsAppMsgPush.getTargetUserTag().contains("6")){
				tagList.add("无投资资质");
			}
			if (tagList.size() > 0){
				//判断是交集还是并集
				if (smsAppMsgPush.getTargetUserTagSet() == 1){
					cdt.addCondition(AppConditions.TAG,tagList, AppConditions.OptType.or);
				}else {
					cdt.addCondition(AppConditions.TAG,tagList,AppConditions.OptType.and);
				}
			}
		}

		//为消息设置条件
		message.setConditions(cdt);
		IPushResult ret = AppMsgPushUtil.pushMessageToApp(message,"运营后台群推");
		String result = "";
		if (ret != null){
			result = ret.getResponse().get("result").toString();
			if("ok".equals(result)){
				//获取推送任务id
				String contentId = ret.getResponse().get("contentId").toString();
				smsAppMsgPush.setTaskId(contentId);
			}
		}
		return result;
	}

	private TransmissionTemplate getTemplate(SmsAppMsgPush smsAppMsgPush){
		TransmissionTemplate template = new TransmissionTemplate();
		APNPayload payload = new APNPayload();
		template.setAppId(PropertiesHolder.getProperty("push.appid"));
		template.setAppkey(PropertiesHolder.getProperty("push.appkey"));

		StringBuffer tc = new StringBuffer();
		//查看推送消息时的动作为打开链接时
//		if (smsAppMsgPush.getMsgAction() == 2){
//			tc.append(SMS_APP_MSG_TYPE_LINK + "^" + smsAppMsgPush.getTitle() + "^" + smsAppMsgPush.getContent() + "^" + smsAppMsgPush.getMsgActionLink());
//			payload.addCustomMsg("linkUrl",smsAppMsgPush.getMsgActionLink());
//		}else if(smsAppMsgPush.getType() != null ){
//			//比如type=4时好友投资跳转到站内信详情 后面拼上站内信code
//			//CustomParams用于安卓获取
//			tc.append(smsAppMsgPush.getType() +"^" + smsAppMsgPush.getTitle() + "^" + smsAppMsgPush.getContent() + "^" + smsAppMsgPush.getCustomParams());
//		}else {
//			tc.append(smsAppMsgPush.getContent());
//		}
		Map pashData = new HashMap();
		pashData.put("title" , smsAppMsgPush.getTitle());
		pashData.put("content" , smsAppMsgPush.getContent());
		pashData.put("time" , System.currentTimeMillis());
		if(smsAppMsgPush.getMsgAction() != null && smsAppMsgPush.getMsgAction() == 2){
			smsAppMsgPush.setType(SmsConstants.SMS_APP_MSG_TYPE_LINK);
			//后台运营推送带链接消息
			pashData.put("type" , SmsConstants.SMS_APP_MSG_TYPE_LINK);
			//设置自定义参数为链接url 安卓需要 ios也需要这个存链接
			pashData.put("customParams" , smsAppMsgPush.getMsgActionLink());
			//ios可用自定义参数
			payload.addCustomMsg("linkUrl", smsAppMsgPush.getMsgActionLink());
		}else{
			pashData.put("customParams" , smsAppMsgPush.getCustomParams() != null ? smsAppMsgPush.getCustomParams() : "");
		}
		if(smsAppMsgPush.getType() != null){
			pashData.put("type" , smsAppMsgPush.getType());
			//设置ios自定义参数
			if(SmsConstants.SMS_APP_MSG_TYPE_NOTICE == smsAppMsgPush.getType()){
				//好友投资提示，站内信id和时间(毫秒值)
				//^特殊字符需要转义，满标审核发送时将站内信信息拼起来
				if(smsAppMsgPush.getCustomParams() != null){
					String[] params = smsAppMsgPush.getCustomParams().split("\\^");
					//ios apn使用专属自定义参数
					payload.addCustomMsg("msgId", params[0]);
					payload.addCustomMsg("msgTime", params[1]);
					//安卓可以使用
					pashData.put("customParams" , params[0]);
					pashData.put("time" , params[1] != null ? params[1] : System.currentTimeMillis());
				}
			}
		}else{
			pashData.put("type" , SmsConstants.SMS_APP_MSG_TYPE_DEFAULT);
		}
		tc.append(JSON.toJSONString(pashData));
		template.setTransmissionContent(tc.toString());
		template.setTransmissionType(2);
		//payload.setBadge(1);
		try {
			if(smsAppMsgPush.getTargetPlatform() != null && smsAppMsgPush.getTargetPlatform().contains("2")) {
				//ios角标不支持自增，设置固定值
				//payload.setBadge(Integer.parseInt(smsAppMsgPush.get));
				payload.setBadge(1);
			}
			//payload.setAutoBadge("+1");//角标数量自增长 群发不支持此方法
			if(smsAppMsgPush.getRegUserId() != null && smsAppMsgPush.getRegUserId() != 0){
				//单发用户角标自增  jdk6及以下不支持此参数
				payload.setAutoBadge("+1");
			}
		} catch (Exception e) {
			logger.error("消息推送设置角标异常");
		}
		payload.setContentAvailable(1);
		payload.setSound("default");
		payload.setCategory("$由客户端定义");
		payload.setAlertMsg(getDictionaryAlertMsg(smsAppMsgPush.getContent(),smsAppMsgPush.getTitle()));

		template.setAPNInfo(payload);
		return template;
	}

	private APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String message , String title){
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody(message);
		alertMsg.setActionLocKey("ActionLockey");
		alertMsg.setLocKey(message);
		alertMsg.addLocArg("loc-args");
		alertMsg.setLaunchImage("launch-image");
		// IOS8.2以上版本支持
		alertMsg.setTitle(title);
		alertMsg.setTitleLocKey(title);
		alertMsg.addTitleLocArg(title);
		return alertMsg;
	}

	@Override
	public boolean stop(SmsAppMsgPush smsAppMsgPush) {
		IGtPush push = new IGtPush(PropertiesHolder.getProperty("push.host"),PropertiesHolder.getProperty("push.appkey"),
				PropertiesHolder.getProperty("push.mastersecret"));
		try {
			return push.stop(smsAppMsgPush.getTaskId());
		} catch (Exception e) {
			logger.error("stop, 停止推送异常, 推送信息: {}, 异常信息: ",smsAppMsgPush.toString(), e);
		}
		return false;
	}

	@Override
	public void appMsgPush(Date currentDate, int shardingItem){
		//获取当前时间需要推送的消息
		SmsAppMsgPush condition = new SmsAppMsgPush();
		condition.setState(SmsConstants.SMS_APP_MSG_STATE_SEND);
		condition.setPushMode(1);
		condition.setPushTime(DateUtils.parse(DateUtils.format(currentDate,"yyyy-MM-dd HH:00:00")));
		List<SmsAppMsgPush> list = smsAppMsgPushDao.findByCondition(condition,0,100);
		logger.info("appMsgPush, 定时执行app消息推送, 执行时间: {}, 处理分片项: {}, 推送消息: {}",
				currentDate, shardingItem, JSON.toJSON(list));
		//对查询出的消息进行推送
		int totalNum = 0;
		int sucNum = 0;
		List<SmsAppMsgPush> updateList = new ArrayList<>();
		try{
			if (list.size() > 0){
				for (SmsAppMsgPush msg : list){
					if (sucNum >0 && (sucNum%5 == 0)){
						//推送数量过多，每五条等待一分钟(个推一分钟限制6条)
						Thread.sleep(50 * 1000);
					}
					SmsAppMsgPush smsAppMsgPush = new SmsAppMsgPush();
					smsAppMsgPush.setId(msg.getId());
					smsAppMsgPush.setModifyTime(currentDate);
					//发起推送
					String result = this.push(msg);
					if (StringUtils.isNotBlank(result) && "ok".equals(result)){
						smsAppMsgPush.setState(SmsConstants.SMS_APP_MSG_STATE_SUCCESS);
						smsAppMsgPush.setTaskId(msg.getTaskId());
						sucNum++;
						Thread.sleep(10*1000);
					}else {
						smsAppMsgPush.setState(SmsConstants.SMS_APP_MSG_STATE_FAIL);
						Thread.sleep(30*1000);
					}
					updateList.add(smsAppMsgPush);
				}
				this.smsAppMsgPushDao.updateBatch(SmsAppMsgPush.class,updateList,updateList.size());
				logger.info("appMsgPush, 定时消息推送完成, 执行时间: {}, 处理分片项: {}, 共有{}条消息, 其中成功推送{}条, 消息集合: {}",
						currentDate, shardingItem, totalNum, sucNum, JSON.toJSON(updateList));
			}
		}catch (Exception e){
			logger.error("appMsgPush, 定时消息推送异常, 执行时间: {}, 处理分片项: {}, 推送信息: {}, 异常信息: ",
					currentDate, shardingItem, JSON.toJSON(list), e);
			throw new GeneralException("定时消息推送异常！");
		}
	}
}
