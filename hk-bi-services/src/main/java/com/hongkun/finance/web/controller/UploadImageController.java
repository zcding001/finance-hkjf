package com.hongkun.finance.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;

/**
 * @Description : 富文本图片上传
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.UploadImageController.java
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/uploadImageController.do")
public class UploadImageController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(UploadImageController.class);

	/**
	 * @Description : 图片上传
	 * @Method_Name : upload;
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws FileUploadException
	 * @return : void;
	 * @Creation Date : 2017年11月9日 下午5:25:59;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping(params = "method=upload")
	public void upload(HttpServletRequest request, HttpServletResponse response)
			throws IOException, FileUploadException {
		// 文件保存路径
		String savePath = request.getParameter("filePath");
		// 平台来源
		String platform = request.getParameter("platform");
		// 文件保存目录URL
		String saveUrl = PropertiesHolder.getProperty("oss.url");
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		// 最大文件大小
		long maxSize = 1000000;
		// 判断是否选择了文件
		if (!ServletFileUpload.isMultipartContent(request)) {
			out.println(getError("请选择文件。"));
			return;
		}
		if ((platform = transformPlatform(platform)) == null) {
			out.println(getError("请正确指定上传文件存放桶名称!"));
			return;
		}
		if (StringUtils.isBlank(savePath)) {
			out.println(getError("请正确指定上传文件路径!"));
			return;
		}
		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		// 这里需要后台springmvc-servlet.xml配置multipartResolver
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		multipartRequest.setCharacterEncoding("UTF-8");
		MultipartFile multipartfile = multipartRequest.getFiles("imgFile").get(0);
		String oldName = multipartfile.getOriginalFilename();
		String ext = multipartfile.getOriginalFilename().substring(oldName.lastIndexOf(".") + 1);
		long fileSize = multipartfile.getSize();// 文件大小
		if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(ext)) {
			out.println(getError("<font size='3'>非常抱歉，目前上传附件格式类型只允许为：<br>" + extMap.get(dirName) + ",你选择的文件【" + oldName
					+ "】不符合要求，无法上传！</font>"));
			return;
		}
		if (fileSize > maxSize) {
			out.println(getError("上传文件大小超过限制。"));
			return;
		}
		try {
			// 文件上传到阿里云服务器
			FileInfo fileInfo = OSSLoader.getInstance().setUseRandomName(true)
					.bindingUploadFile(BaseUtil.transferMultiPartFileToFileInfo(multipartfile))
					.setAllowUploadType(FileType.EXT_TYPE_IMAGE).setFileState(FileState.UN_UPLOAD)
					.setBucketName(platform).setFilePath(savePath).doUpload();
			if (fileInfo.getFileState() == FileState.SAVED) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("error", 0);
				map.put("url", saveUrl + fileInfo.getSaveKey());
				out.println(JsonUtils.toJson(map));
				out.flush();
				out.close();
			} else {
				throw new Exception("上传失败，请重试");
			}
		} catch (Exception e) {
			out.println(getError("上传文件失败。"));
			out.flush();
			out.close();
			return;
		}
	}

	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toString();
	}

	/**
	 * 转换桶名称
	 * 
	 * @param platform
	 * @return
	 */
	private String transformPlatform(String platform) {
		switch (platform) {
		case "qsh":
			platform = OSSBuckets.QSH;
			break;
		case "hkjf":
			platform = OSSBuckets.HKJF;
			break;
		case "universal":
			platform = OSSBuckets.UNIVERSAL;
			break;
		default:
			platform = null;
			break;
		}
		return platform;
	}
}
