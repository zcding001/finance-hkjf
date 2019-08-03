package com.hongkun.finance.payment.service;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartPayment {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-dubbo-provider.xml",
				"classpath:spring/applicationContext-payment.xml"
				);
		context.start();
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait();
		}
		context.close();
	}
}
