package com.hongkun.finance.vas.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.hongkun.finance.vas.model.RecommendEarnVo;
import com.hongkun.finance.vas.model.VasBidRecommendEarn;

public class ClassReflection {
	/***
	 * @Description :反射实体类赋值
	 * @Method_Name : reflectionAttr;
	 * @param class1
	 *            用于赋值的实体类
	 * @param class2
	 *            需要待赋值的实体类
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2017年6月27日 下午1:29:15;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static void reflectionAttr(Object class1, Object class2) throws Exception {
		Class clazz1 = Class.forName(class1.getClass().getName());
		Class clazz2 = Class.forName(class2.getClass().getName());
		// 获取两个实体类的所有属性
		Field[] fields1 = clazz1.getDeclaredFields();
		Field[] fields2 = clazz2.getDeclaredFields();
		ClassReflection cr = new ClassReflection();
		// 遍历class1Bean，获取逐个属性值，然后遍历class2Bean查找是否有相同的属性，如有相同则赋值
		for (Field f1 : fields1) {
			Object value = cr.invokeGetMethod(class1, f1.getName(), null);
			for (Field f2 : fields2) {
				if (f1.getName().equals(f2.getName())) {
					Object[] obj = new Object[1];
					obj[0] = value;
					cr.invokeSetMethod(class2, f2.getName(), obj);
				}
			}
		}
	}

	/**
	 * @Description : 执行某个Field的getField方法
	 * @Method_Name : invokeGetMethod;
	 * @param clazz
	 *            类
	 * @param fieldName
	 *            类的属性名称
	 * @param args
	 *            参数，默认为null
	 * @return
	 * @return : Object;
	 * @Creation Date : 2017年6月29日 下午2:34:57;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private Object invokeGetMethod(Object clazz, String fieldName, Object[] args) {
		String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method method = null;
		try {
			method = Class.forName(clazz.getClass().getName()).getDeclaredMethod("get" + methodName);
			return method.invoke(clazz);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @Description 执行某个Field的setField方法
	 * @param clazz
	 *            类
	 * @param fieldName
	 *            类的属性名称
	 * @param args
	 *            参数，默认为null
	 * @return
	 * @return : Object;
	 * @Creation Date : 2017年6月29日 下午2:33:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private Object invokeSetMethod(Object clazz, String fieldName, Object[] args) {
		String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method method = null;
		try {
			Class[] parameterTypes = new Class[1];
			Class c = Class.forName(clazz.getClass().getName());
			Field field = c.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			method = c.getDeclaredMethod("set" + methodName, parameterTypes);
			return method.invoke(clazz, args);
		} catch (Exception e) {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {

		VasBidRecommendEarn vasBiddRecommendEarn = new VasBidRecommendEarn();

		vasBiddRecommendEarn.setState(1);
		vasBiddRecommendEarn.setId(123456789);
		vasBiddRecommendEarn.setFriendLevel(2);
		RecommendEarnVo biddRecommendEarnVo = new RecommendEarnVo();
		reflectionAttr(vasBiddRecommendEarn, biddRecommendEarnVo);
		System.out.println(biddRecommendEarnVo.getId());
		System.out.println(biddRecommendEarnVo.getState());
		System.out.println(biddRecommendEarnVo.getFriendLevel());
	}

}
