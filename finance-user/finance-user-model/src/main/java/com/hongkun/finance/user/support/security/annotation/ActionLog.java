package com.hongkun.finance.user.support.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description   : 操作日志注解
 * 					用法：
 * 						1、@ActionLog应用在Controller的方法上
 * 						2、支持从方法中获得对象中的参数，目前支持3目运算
 * 					实例：
 *						@ActionLog(msg = "管理员更新用户:{args[0]}的状态为{args[1] == 1 ? '启用' : '禁用'}")
 *						public ResponseEntity<?> chgRegUserState(Integer id, Integer state){...}
 *						其中args[0]对应id，args[1]对应state
 *
 *						@ActionLog(msg = "检索用户，检索条件手机号：{args[1].login}， 姓名：{args[1].realName}")
 *						public ResponseEntity<?> userList(Pager pager, UserVO userWithDetailVO){...}
 *						args[1].login 对应 userWithDetailVO中login属性 args[1].realName对应userWithDetailVO中的realName属性
 * @Project       : finance-user-model
 * @Program Name  : com.hongkun.finance.user.support.security.annotation.ActionLog.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionLog {

	/**
	 *  @Description    : 操作的描述信息
	 *  @Method_Name    : msg 支持3目运算
	 *  @return         : String
	 *  @Creation Date  : 2017年12月5日 上午11:45:23 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	String msg();
	
}
