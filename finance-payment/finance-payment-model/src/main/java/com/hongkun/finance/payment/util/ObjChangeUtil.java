package com.hongkun.finance.payment.util;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description : 对象转换 通用工具类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.util.ObjChangeUtils.java
 * @Author : yanbinghuang
 */
public class ObjChangeUtil {
	/***
	 * 
	 * @Description : 将对象转换为Map<String, Object>
	 * @Method_Name : objToMap;
	 * @param obj
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年6月7日 上午9:33:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, Object> objToMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field fields[] = obj.getClass().getDeclaredFields();
		String[] name = new String[fields.length];
		Object[] value = new Object[fields.length];
		try {
			Field.setAccessible(fields, true);
			for (int i = 0; i < name.length; i++) {
				name[i] = fields[i].getName();
				value[i] = fields[i].get(obj);
				if (!"serialVersionUID".equals(name[i]) && value[i] != null) {
					map.put(name[i], value[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @Description : 转换为merId=9996&amtType=1&retCode=1001&retMsg=
	 *              zOG9u8r9vt1bP13R6dakyqew3A格式
	 * @Method_Name : objToRequestStr;
	 * @param obj
	 * @param signFlag
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年6月7日 上午11:22:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String objToRequestStr(Object obj, String signFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field fields[] = obj.getClass().getDeclaredFields();
		String[] name = new String[fields.length];
		Object[] value = new Object[fields.length];
		String res = "";
		try {
			Field.setAccessible(fields, true);
			for (int i = 0; i < name.length; i++) {
				name[i] = fields[i].getName();
				value[i] = fields[i].get(obj);
				if (!"serialVersionUID".equals(name[i]) && value[i] != null) {
					map.put(name[i], value[i]);
					res += name[i] + "=" + URLEncoder.encode((String) value[i], "UTF-8") + signFlag;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "".equals(res) ? "" : StringUtils.chop(res);
	}

}
