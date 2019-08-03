package com.hongkun.finance.info.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartInfo {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-info.xml");
		context.start();
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait();
		}
		context.close();
	}
}
