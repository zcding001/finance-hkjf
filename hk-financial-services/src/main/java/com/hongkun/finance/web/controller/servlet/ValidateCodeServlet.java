package com.hongkun.finance.web.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.hongkun.finance.user.utils.ValidateCodeGeneratorUtil;

public class ValidateCodeServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		//生成验证码的类型
		String type = request.getParameter("type");
		try {
			ValidateCodeGeneratorUtil generator = new ValidateCodeGeneratorUtil();
			if(StringUtils.isNotBlank(type) && "2".equals(type)){
				generator.generateCalcCode(request, response);
				return;
			}
			generator.generateRandomcode(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
