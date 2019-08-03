package com.hongkun.finance.vas;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartVas {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-jms.xml", "classpath:spring/applicationContext-vas.xml");
		context.start();
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait();
		}
		context.close();
	}
}
