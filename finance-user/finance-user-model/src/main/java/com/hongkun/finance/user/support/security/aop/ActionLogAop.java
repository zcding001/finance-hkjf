package com.hongkun.finance.user.support.security.aop;

import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.apache.commons.collections4.map.HashedMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description   : 管理员行为日志
 * @Project       : finance-user-model
 * @Program Name  : com.hongkun.finance.user.support.security.aop.ActionLogAop.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Component
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
public class ActionLogAop implements ProxyTargetI{

	private static final Logger logger = LoggerFactory.getLogger(ActionLogAop.class);
	
	@Autowired
	private JmsService jmsService;
	
	private ExecutorService executorService = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
			ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

	@Around("@annotation(com.hongkun.finance.user.support.security.annotation.ActionLog)")
	public Object around(ProceedingJoinPoint point) throws Throwable {
	    logger.info("切入对象{}, 切入方法", point.getTarget().getClass().getName(), point.getSignature().getName());
		ActionLog actionLog = getAnnotation(point, ActionLog.class);
		if(actionLog != null) {
			final Object[] args = point.getArgs();
			//此处一定要单独去regUser，jdk8中在单独的线程中无法使用BaseUtil.getLoginUser()中的((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            final RegUser regUser = BaseUtil.getLoginUser();
            executorService.execute(() ->{
			    try{
                    String msg = actionLog.msg();
//				    用于匹配msg中{args[0].id} 或 {args[1].state == 1 ? '禁用' : '解禁'}
                    Pattern p = Pattern.compile("([\\{ *args\\[\\d]+\\][^\\}]* *\\})");
                    Matcher m = p.matcher(msg);
                    while(m.find()) {
                        String eval = m.group();
                        if(eval.contains("?") && eval.contains(":")) {
                            String[] arr = eval.split("[=<>]++");
                            String tmp = getParamValue(arr[0].replace("{", "").replace("}", "").trim(), args);
                            msg = msg.replace(eval, getParamValue(eval.replace(arr[0], tmp).replace("}", "").trim()));
                        }else {
                            msg = msg.replace(eval, getParamValue(eval.replace("{", "").replace("}", "").trim(), args));
                        }
                    }
                    this.jmsService.sendMsg(UserConstants.MQ_QUEUE_ACTION_LOG, DestinationType.QUEUE, fitActionLog(regUser, msg, point), JmsMessageType.MAP);
			    }catch(Exception e){
			        logger.error("用户操作日志解析失败: 模板:{}", actionLog.msg(), e);
			    }
			});
		}
		return point.proceed();
	}
	
	/**
	 * 
	 *  @Description    : 组装管理员操作日志数据
	 *  @Method_Name    : fitActionLog
	 *  @param regUser	: 当前管理员
	 *  @param msg		: 自定义管理员操作日志描述信息
	 *  @return         : void
	 *  @Creation Date  : 2017年12月5日 下午9:14:05 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static Map<String, Object> fitActionLog(RegUser regUser, String msg, ProceedingJoinPoint point) {
		Map<String, Object> map = new HashedMap<>();
        map.put("login", regUser.getLogin());
        map.put("action", msg);
        map.put("type", regUser.getType());
        map.put("className", point.getTarget().getClass().getName());
        map.put("methodName", point.getSignature().getName());
        logger.info("管理员：{}，操作：{}", regUser.getId(), msg);
        return map;
	}
}
