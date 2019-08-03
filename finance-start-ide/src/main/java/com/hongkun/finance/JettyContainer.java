package com.hongkun.finance;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.page.PageServlet;
import com.alibaba.dubbo.container.page.ResourceFilter;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * @Description : TODO wupeng 类描述
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.JettyContainer
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class JettyContainer implements Container {
    public static final String JETTY_PORT = "dubbo.jetty.port";
    public static final String JETTY_DIRECTORY = "dubbo.jetty.directory";
    public static final String JETTY_PAGES = "dubbo.jetty.page";
    public static final int DEFAULT_JETTY_PORT = 9991;
    private static final Logger logger = LoggerFactory.getLogger(com.hongkun.finance.JettyContainer.class);
    SelectChannelConnector connector;

    public JettyContainer() {
    }

    @Override
    public void start() {
        String serverPort = ConfigUtils.getProperty("dubbo.jetty.port");
        int port;
        if (serverPort != null && serverPort.length() != 0) {
            port = Integer.parseInt(serverPort);
        } else {
            port = 9991;
        }

        this.connector = new SelectChannelConnector();
        this.connector.setPort(port);
        ServletHandler handler = new ServletHandler();
        String resources = ConfigUtils.getProperty("dubbo.jetty.directory");
        if (resources != null && resources.length() > 0) {
            FilterHolder resourceHolder = handler.addFilterWithMapping(ResourceFilter.class, "/*", 0);
            resourceHolder.setInitParameter("resources", resources);
        }

        ServletHolder pageHolder = handler.addServletWithMapping(PageServlet.class, "/*");
        pageHolder.setInitParameter("pages", ConfigUtils.getProperty("dubbo.jetty.page"));
        pageHolder.setInitOrder(2);
        //新增的方法
        handler.addServletWithMapping(StatViewServlet.class,"/druid/*");
        Server server = new Server();
        server.addConnector(this.connector);
        server.addHandler(handler);

        try {
            server.start();
        } catch (Exception var8) {
            throw new IllegalStateException("Failed to start jetty server on " + NetUtils.getLocalHost() + ":" + port + ", cause: " + var8.getMessage(), var8);
        }
    }

    @Override
    public void stop() {
        try {
            if (this.connector != null) {
                this.connector.close();
                this.connector = null;
            }
        } catch (Throwable var2) {
            logger.error(var2.getMessage(), var2);
        }

    }

}
