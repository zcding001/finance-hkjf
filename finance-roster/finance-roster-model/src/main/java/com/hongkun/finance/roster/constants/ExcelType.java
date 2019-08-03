package com.hongkun.finance.roster.constants;

public enum ExcelType {
	/**
	 * 黑白名单模板
	 */
	ROSTER(1, "黑白名单模板"),
	/**
	 * 意向金名单模板
	 */
	ROSTER_DEPOSIT(2, "意向金模板"),
	/**
	 * 企业员工关系模板
	 */
	ROSTER_STAFF(3, "企业员工关系模板");
	
	private int value;
	private String name;
	
	private ExcelType(int value, String name){
		this.value = value;
		this.name = name;
	}
	public int getValue(){
		return this.value;
	}
	
	public String getName(){
		return this.name;
	}
}
