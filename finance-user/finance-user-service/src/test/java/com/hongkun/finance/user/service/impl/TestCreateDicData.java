package com.hongkun.finance.user.service.impl;

/**
 * @Description   : 生成字典表的插入sql
 * @Project       : finance-user-service
 * @Program Name  : com.hongkun.finance.user.service.impl.TestCreateDicData.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class TestCreateDicData {

	public static void main(String[] args) {
		/**
		 * 取数据中描述信息
		 */
		String msg = "0-小学,1-初中,2-高中,3-中专,4-大专,5-本科,7-硕士,8-博士,9-博士后";
		/**
		 * 业务类型
		 */
		String busName = "user";
		/**
		 * 实体类常量值
		 */
		String subName = "degree";
		String[] arr = msg.split(",");
		StringBuffer sb = new StringBuffer(
				"insert into \n"
				+ "\tdic_data(business_name, subject_name, value, name) \n"
				+ "values\n");
		for(String tmp : arr){
			String[] a = tmp.split("-");
			sb.append("\t('" + busName + "', '" + subName + "', " + a[0] + ", '" + a[1] + "'),").append("\n");
		}
		System.out.println(sb.toString().substring(0, sb.toString().length() - 2));
	}
}
