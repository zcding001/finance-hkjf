package com.hongkun.finance.invest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartInvest {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-invest.xml");
		context.start();
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait();
		}
		context.close();
	}
}
