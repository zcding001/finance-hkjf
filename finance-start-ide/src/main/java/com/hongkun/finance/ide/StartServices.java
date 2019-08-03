package com.hongkun.finance.ide;

import java.util.EventListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.alibaba.druid.support.http.StatViewServlet;

public class StartServices {
	 public static void main(String[] args) throws Exception {
	    long time = System.currentTimeMillis();
        start();
//        startByJetty();
	    System.out.println("start ok. expenses " + (System.currentTimeMillis() - time));
	 }

	 /**
	 *  @Description    : 发布服务，不启动durid监控                     
	 *  @Method_Name    : start
	 *  @throws Exception
	 *  @return         : void
	 *  @Creation Date  : 2018年1月18日 上午10:48:56 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void start() throws Exception{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-ide.xml");
		context.start();
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait();
		}
		context.close();
	}

	/**
	 *  @Description    : 以jetty容器方式发布服务并启动监控
	 *  @Method_Name    : startByJetty
	 *  @throws Exception
	 *  @return         : void
	 *  @Creation Date  : 2018年1月18日 上午10:38:39 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static void startByJetty() throws Exception{
		// 服务器的监听端口
		Server server = new Server(7001);
		// 关联一个已经存在的上下文
		WebAppContext context = new WebAppContext();
		String basePath = StartServices.class.getResource("/").getPath();
		context.setResourceBase(basePath);
		// 设置访问根路径
		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		//配置初始化参数
		context.setInitParameter("contextConfigLocation", "classpath:spring/applicationContext-ide.xml");
//		context.setInitParameter("contextConfigLocation", "classpath:spring/*.xml");
		context.setInitParameter("webAppRootKey", "durid-monitor");
		//设置启动监听器
		EventListener contextLoaderListener = new ContextLoaderListener();
		context.addEventListener(contextLoaderListener);
		//添加druid监控的servlet
		context.addServlet(StatViewServlet.class.getName(), "/druid/*");
		server.setHandler(context);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}