package com.hongkun.finance.qdz.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartQdz {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-dubbo-provider.xml",
				"classpath:spring/applicationContext-qdz.xml");
		context.start();
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait();
		}
		context.close();
	}
}
