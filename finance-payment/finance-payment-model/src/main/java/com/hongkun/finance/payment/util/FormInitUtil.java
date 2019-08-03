package com.hongkun.finance.payment.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 前端表单初始化工具类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.util.FormInitUtil.java
 * @Author : yanbinghuang@hongkun.com
 */
public class FormInitUtil {
	// 表单提交方式
	private static String POST = "post";
	private static String GET = "get";

	private static String HIDDEN = "hidden";

	/**
	 * @Description : 初始化前台隐藏域表单
	 * @Method_Name : initHideForm;
	 * @param mapData
	 *            初始化对象
	 * @param formAction
	 *            跳转的action
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年12月21日 下午2:28:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String initHideForm(Map<String, Object> mapData, String formAction) {
		return initForm(mapData, formAction, POST, HIDDEN);
	}

	/**
	 * @Description : 初始化前台表单
	 * @Method_Name : initForm;
	 * @param mapData
	 *            初始化对象
	 * @param formAction
	 *            跳转的action
	 * @param formMethod
	 *            提交方式
	 * @param inputType
	 *            input类型
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年12月21日 下午2:30:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String initForm(Map<String, Object> mapData, String formAction, String formMethod, String inputType) {
		// 页面输出内容变量
		StringBuffer sb = new StringBuffer();
		sb.append("<form ");
		sb.append("id=\"" + "myForm" + "\" ");
		sb.append("action=\"" + formAction + "\" ");
		sb.append("method=\"" + formMethod + "\" ");
		// 生存From表单
		mapData.forEach((eleName, eleValue) -> {
			sb.append("<input name=\"" + eleName + "\" type=\"" + inputType + "\" value=\"" + eleValue + "\"/>");
		});
		sb.append("</form>");
		return sb.toString();
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", "lianlian");
		map.put("age", 19);
		map.put("height", 12);
		System.out.println(initForm(map, "http://www.baidu.com", "post", "hidden"));

	}
}
