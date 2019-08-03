package com.hongkun.finance.sms.utils;

import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.*;
import com.yirun.framework.core.utils.ApplicationContextUtils;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description   : 异步发送sms消息工具类：支持SmsTelMsg、SmsWebMsg、SmsEmailMsg，其他消息需要额外扩展
 * @Project       : finance-sms-model
 * @Program Name  : com.hongkun.finance.sms.utils.SmsSendUtil.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class SmsSendUtil {

	private static final Logger logger = LoggerFactory.getLogger(SmsSendUtil.class);

	private static final String ERROR_MSG = "SMS-ERROR : can't find send message";

	private static JmsService jmsService;

	static{
		jmsService = ApplicationContextUtils.getBean("jmsService");
	}

	private SmsSendUtil(){/*defalut*/}

	/**
	 *  @Description    : 发送短信：向jms的queue中发送对象类型短信消息
	 *  @Method_Name    : sendTelMsgToQueue
	 *  @param smsMsgInfo 消息内容
	 *  @return         : void
	 *  @Creation Date  : 2017年6月11日 上午10:41:41 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendTelMsgToQueue(SmsMsgInfo smsMsgInfo){
		if(smsMsgInfo == null){
			logger.error(ERROR_MSG);
			return;
		}
		try {
			jmsService.sendMsg(
					SmsConstants.MQ_QUEUE_MSG_TEL, 
					DestinationType.QUEUE, 
					smsMsgInfo, 
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("can't send tel message{}, ERROR: ", smsMsgInfo.toString(), e);
		}
	}
	
	/**
	 *  @Description    : 发送多条短信：向jms的queue中发送集合对象类型短信消息
	 *  @Method_Name    : sendTelMsgToQueue
	 *  @param list
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 上午11:37:57 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendTelMsgToQueue(List<SmsMsgInfo> list){
		if(list == null || list.isEmpty()){
			logger.error(ERROR_MSG);
			return;
		}
		try {
			jmsService.sendMsg(
					SmsConstants.MQ_QUEUE_MSG_TEL, 
					DestinationType.QUEUE, 
					list, 
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("can't send tel message. list is empty or ERROR: ", e);
		}
	}
	
	/**
	 *  @Description    : 发送站内信：向jms的queue中发送对象类站内信消息
	 *  @Method_Name    : sendTelMsgToQueue
	 *  @param smsMsgInfo 消息内容
	 *  @return         : void
	 *  @Creation Date  : 2017年6月11日 上午10:41:41 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendWebMsgToQueue(SmsMsgInfo smsMsgInfo){
		if(smsMsgInfo == null){
			logger.error(ERROR_MSG);
			return;
		}
		try {
			jmsService.sendMsg(
					SmsConstants.MQ_QUEUE_MSG_WEB, 
					DestinationType.QUEUE, 
					smsMsgInfo, 
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("SMS-ERROR : can't send message{}, ERROR: ", smsMsgInfo.toString(), e);
		}
	}

	/**
	 *  @Description    : 发送消息推送：向jms的queue中发送对象类站内信消息
	 *  @Method_Name    : sendAppMsgPushToQueue
	 *  @param smsMsgInfo 消息内容
	 *  @return         : void
	 *  @Creation Date  : 2018年3月19日 下午18:41:41
	 *  @Author         : pengwu@hongkun.com
	 */
	public static void sendAppMsgPushToQueue(SmsMsgInfo smsMsgInfo){
		if(smsMsgInfo == null){
			logger.error(ERROR_MSG);
			return;
		}
		try {
			jmsService.sendMsg(
					SmsConstants.MQ_QUEUE_MSG_PUSH,
					DestinationType.QUEUE,
					smsMsgInfo,
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("SMS-ERROR : can't send message{}, ERROR: ", smsMsgInfo.toString(), e);
		}
	}

	/**
	 *  @Description    : 发送站多条内信：向jms的queue中发送集合对象类站内信消息
	 *  @Method_Name    : sendWebMsgToQueue
	 *  @param list
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 上午11:47:10 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendWebMsgToQueue(List<SmsMsgInfo> list){
		if(list == null || list.isEmpty()){
			logger.error(ERROR_MSG);
			return;
		}
		try {
			jmsService.sendMsg(
					SmsConstants.MQ_QUEUE_MSG_WEB, 
					DestinationType.QUEUE, 
					list, 
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("can't send web message. list is empty or ERROR: ", e);
		}
	}
	
	/**
	 *  @Description    : 发送邮件：向jms的queue中发送对象类站内信消息
	 *  @Method_Name    : sendTelMsgToQueue
	 *  @param smsMsgInfo 消息内容
	 *  @return         : void
	 *  @Creation Date  : 2017年6月11日 上午10:41:41 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendEmailMsgToQueue(SmsMsgInfo smsMsgInfo){
		if(smsMsgInfo == null){
			logger.error(ERROR_MSG);
			return;
		}
		try {
			jmsService.sendMsg(
					SmsConstants.MQ_QUEUE_MSG_EMAIL, 
					DestinationType.QUEUE, 
					smsMsgInfo, 
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("SMS-ERROR : can't send message{}, ERROR: ", smsMsgInfo.toString(), e);
		}
	}
	
	/**
	 *  @Description    : 发送多条邮件：向jms的queue中发送集合对象类站内信消息
	 *  @Method_Name    : sendTelMsgToQueue
	 *  @param list 消息内容
	 *  @return         : void
	 *  @Creation Date  : 2017年6月11日 上午10:41:41 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendEmailMsgToQueue(List<SmsMsgInfo> list){
		if(list == null || list.isEmpty()){
			logger.error(ERROR_MSG);
			return;
		}
		try {
			jmsService.sendMsg(
					SmsConstants.MQ_QUEUE_MSG_EMAIL, 
					DestinationType.QUEUE, 
					list, 
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("can't send email message. list is empty or ERROR: ", e);
		}
	}
	
	/**
	 *  @Description    : 发送消息通知（短信、邮件、站内容）
	 *  @Method_Name    : sendSmsMsgToQueue
	 *  @param arg0		消息内容
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 上午10:47:38 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendSmsMsgToQueue(SmsMsgInfo arg0){
		if(arg0 instanceof SmsTelMsg){
			sendTelMsgToQueue(arg0);
		}
		if(arg0 instanceof SmsEmailMsg){
			sendEmailMsgToQueue(arg0);
		}
		if(arg0 instanceof SmsWebMsg){
			sendWebMsgToQueue(arg0);
		}
		if (arg0 instanceof SmsAppMsgPush){
			sendAppMsgPushToQueue(arg0);
		}
	}
	
	/**
	 *  @Description    : 发送多条消息通知（短信、邮件、站内容）
	 *  @Method_Name    : sendSmsMsgToQueue
	 *  @param list		消息集合(短信|站内信|邮件)
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 上午11:54:26 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendSmsMsgToQueue(List<SmsMsgInfo> list){
		if(list == null || list.isEmpty()){
			logger.error(ERROR_MSG);
			return;
		}
		SmsMsgInfo arg0 = list.get(0);
		if(arg0 instanceof SmsTelMsg){
			sendTelMsgToQueue(list);
		}
		if(arg0 instanceof SmsEmailMsg){
			sendEmailMsgToQueue(list);
		}
		if(arg0 instanceof SmsWebMsg){
			sendWebMsgToQueue(list);
		}
	}

	/**
	 *  @Description    : 发送短信通知（短信、邮件、站内信，3选2，任意组合，参数不分先后顺序）
	 *  @Method_Name    : sendSmsMsgToQueue
	 *  @param arg0 	消息内容
	 *  @param arg1		消息内容
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 上午10:48:51 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendSmsMsgToQueue(SmsMsgInfo arg0, SmsMsgInfo arg1){
		sendSmsMsgToQueue(arg0);
		sendSmsMsgToQueue(arg1);
	}
	
	/**
	 *  @Description    : 发送多条短信通知（短信、邮件、站内信，3选2，任意组合，参数不分先后顺序）
	 *  @Method_Name    : sendSmsMsgToQueue
	 *  @param list0	消息集合(短信|站内信|邮件，3选2)
	 *  @param list1	消息集合(短信|站内信|邮件，3选2)
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 下午12:00:02 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendSmsMsgToQueue(List<SmsMsgInfo> list0, List<SmsMsgInfo> list1){
		sendSmsMsgToQueue(list0);
		sendSmsMsgToQueue(list1);
	}
	
	/**
	 *  @Description    : 发送短信通知（短信、邮件、站内信)
	 *  @Method_Name    : sendSmsMsgToQueue
	 *  @param arg0		消息内容
	 *  @param arg1		消息内容
	 *  @param arg2		消息内容
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 上午10:49:54 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendSmsMsgToQueue(SmsMsgInfo arg0, SmsMsgInfo arg1, SmsMsgInfo arg2){
		sendSmsMsgToQueue(arg0);
		sendSmsMsgToQueue(arg1);
		sendSmsMsgToQueue(arg2);
	}
	
	/**
	 *  @Description    : 发送多条短信通知（短信、邮件、站内信)
	 *  @Method_Name    : sendSmsMsgToQueue
	 *  @param list0	消息集合(短信|站内信|邮件)
	 *  @param list1	消息集合(短信|站内信|邮件)
	 *  @param list2	消息集合(短信|站内信|邮件)
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 下午12:01:00 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void sendSmsMsgToQueue(List<SmsMsgInfo> list0, List<SmsMsgInfo> list1, List<SmsMsgInfo> list2){
		sendSmsMsgToQueue(list0);
		sendSmsMsgToQueue(list1);
		sendSmsMsgToQueue(list2);
	}
}
