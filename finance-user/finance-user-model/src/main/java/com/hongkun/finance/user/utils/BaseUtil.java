package com.hongkun.finance.user.utils;

import com.alibaba.dubbo.rpc.RpcContext;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.*;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.core.utils.response.ResponseUtils;
import com.yirun.framework.oss.FileParser;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import com.yirun.framework.redis.JedisClusterUtils;
import com.yirun.framework.redis.JedisClusterLock;
import com.yirun.framework.redis.RedisClusterPipeline;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Response;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.support.security.SecurityConstants.USER_AUTH_PREFIX;
import static com.hongkun.finance.user.support.security.SecurityConstants.USER_MENU_PREFIX;
import static com.yirun.framework.core.commons.Constants.*;
import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * @Description : Controller数据操作接口
 * @Project : finance-user-model
 * @Program Name : com.hongkun.finance.user.utils.BaseUtil.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
public interface BaseUtil {
	String LOCK_SUFFIX = "LOCK_KEY";
	/**
	 * 普通的异常提示语
	 */
	 String commExceptionMsg = "网络异常";
	/**
	 * 日志类
	 */
	Logger LOG = LoggerFactory.getLogger(BaseUtil.class);

	/**
	 * 缓存操作线程池
	 */
	ExecutorService cacheIfExecutor = Executors.newCachedThreadPool();

	/**
	 * @param result
	 *            响应结果
	 * @return : boolean
	 * @Description : 判断响应结果是否正确, true:响应结果错误
	 * @Method_Name : error
	 * @Creation Date : 2017年6月26日 下午3:42:05
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */

	/**
	 * 获取缓存执行线程池
	 *
	 * @return
	 */
	static ExecutorService getExecutorService() {
		return cacheIfExecutor;
	}

	static boolean error(ResponseEntity<?> result) {
		if (result == null || result.getResStatus() != SUCCESS) {
			return true;
		}
		return false;
	}

	/**
	 * @param result
	 * @return : String
	 * @Description : 获取返回错误信息
	 * @Method_Name : errorMsg
	 * @Creation Date : 2017年7月13日 下午2:56:14
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	static String errorMsg(ResponseEntity<?> result) {
		if (result == null) {
			return "unable to obtain information!";
		}
		return result.getResMsg().toString();
	}

	/**
	 * 判断分页结果是否含有数据
	 *
	 * @param pager
	 * @return
	 */
	static boolean resultPageHasNoData(Pager pager) {
		if (pager == null || pager.getData() == null || pager.getData().size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * @param msg
	 *            失败提示信息
	 * @return : ResponseEntity<?>
	 * @Description : 返回失败描述信息
	 * @Method_Name : error
	 * @Creation Date : 2017年6月26日 下午5:53:06
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static ResponseEntity<?> error(Object msg) {
		return new ResponseEntity<>(ERROR, msg);
	}

	/**
	 * 获得session中当前登录用户（从session中）
	 *
	 * @return
	 * @author zc.ding
	 * @since 2017年5月19日
	 */
	static RegUser getLoginUser() {
		RegUser regUser = (RegUser) HttpSessionUtil.getLoginUser();
		if (regUser == null) {// session中没有扣去session中获得
			Cookie cookie = CookieUtil.getCookie(HttpSessionUtil.getRequest(), TICKET);
			if (cookie != null) {
				regUser = JedisClusterUtils.getObjectForJson(cookie.getValue(), RegUser.class);
			} 
		}
		return regUser;
	}

	/**
	 * @Description : 获得登录用户的id
	 * @Method_Name : getLoginUserId
	 * @return : Integer
	 * @Creation Date : 2018年3月9日 上午10:57:41
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static Integer getLoginUserId() {
		return getLoginUser().getId();
	}

	/**
	 * @param supplier
	 *            : 获得用户信息的供应商函数
	 * @return : RegUser
	 * @Description : 获得用户信息（从redis中），如果redis中没有通过supplier从db中加载,
	 *              只用于在Controller层中使用
	 * @Method_Name : getRegUser
	 * @Creation Date : 2018年1月5日 上午11:05:07
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static RegUser getRegUser(Supplier<RegUser> supplier) {
		return (RegUser) tryLoadObjectRedis(RegUser.class.getSimpleName() + BaseUtil.getLoginUser().getId(),
				RegUser.class, supplier);
	}
	   
    /**
    *  从redis获取Object信息，如果没有从Function中获取信息
    *  @param ids key后缀集合
    *  @param clazz 返回值类型
    *  @param fun 查询函数
    *  @return java.util.Map<java.lang.Integer,T>
    *  @date                    ：2019/1/28
    *  @author                  ：zc.ding@foxmail.com
    */
    static <T> Map<Integer, T> getObjects(List<Integer> ids, Class<T> clazz, Function<List<Integer>, Map<Integer, T>> fun){
        Map<Integer, T> result = new HashMap<>();
        try(RedisClusterPipeline pipeline = RedisClusterPipeline.pipeline(JedisClusterUtils.getJedisCluster())){
            Map<Integer, Response<String>> map = new HashMap<>();
            ids.forEach(id -> map.put(id, pipeline.get(clazz.getSimpleName() + id)));
            pipeline.sync();
            List<Integer> reloadList = new ArrayList<>();
            map.forEach((k, v) -> {
                String value = map.get(k).get();
                if(StringUtils.isEmpty(value)){
                    reloadList.add(k);
                }
                result.put(k, JsonUtils.json2Object(value, clazz, null));
            });
            pipeline.close();
            if(CommonUtils.isNotEmpty(reloadList)){
                result.putAll(fun.apply(reloadList));
            } 
        }
	    return result;
    }
    

	/**
	 * @param regUserId
	 * @param supplier
	 * @return : RegUser
	 * @Description : 获得用户信息（从redis中），如果redis中没有通过supplier从db中加载，
	 *              此方法可适用于Controller和Service方法中
	 * @Method_Name : getRegUser
	 * @Creation Date : 2018年1月5日 下午1:40:16
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static RegUser getRegUser(Integer regUserId, Supplier<RegUser> supplier) {
		return (RegUser) tryLoadObjectRedis(RegUser.class.getSimpleName() + regUserId, RegUser.class, supplier);
	}

	/**
	 * @param supplier
	 * @return : RegUserDetail
	 * @Description : 获得当前用户详情（从redis中）,如果redis中没有通过supplier从db中加载，
	 *              只用于在Controller层中使用
	 * @Method_Name : getRegUserDetail
	 * @Creation Date : 2018年1月5日 上午11:36:51
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static RegUserDetail getRegUserDetail(Supplier<RegUserDetail> supplier) {
		return (RegUserDetail) tryLoadObjectRedis(RegUserDetail.class.getSimpleName() + BaseUtil.getLoginUser().getId(),
				RegUserDetail.class, supplier);
	}

	/**
	 * @param regUserId
	 * @param supplier
	 * @return : RegUserDetail
	 * @Description : 获得当前用户详情（从redis中）,
	 *              如果redis中没有通过supplier从db中加载，controller|service都适用
	 * @Method_Name : getRegUserDetail
	 * @Creation Date : 2018年1月5日 上午11:36:51
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static RegUserDetail getRegUserDetail(Integer regUserId, Supplier<RegUserDetail> supplier) {
		return (RegUserDetail) tryLoadObjectRedis(RegUserDetail.class.getSimpleName() + regUserId, RegUserDetail.class,
				supplier);
	}

	/**
	 * @param supplier
	 * @return : RegUserInfo
	 * @Description : 获得用户详情（从redis中） 如果redis中没有通过supplier从db中加载，controller适用
	 * @Method_Name : getRegUserInfo
	 * @Creation Date : 2018年1月5日 上午11:38:26
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static RegUserInfo getRegUserInfo(Supplier<RegUserInfo> supplier) {
		return (RegUserInfo) tryLoadObjectRedis(RegUserInfo.class.getSimpleName() + BaseUtil.getLoginUser().getId(),
				RegUserInfo.class, supplier);
	}

	/**
	 * @param regUserId
	 * @param supplier
	 * @return : RegUserInfo
	 * @Description : 获得用户详情（从redis中） 如果redis中没有通过supplier从db中加载，controller |
	 *              service都适用
	 * @Method_Name : getRegUserInfo
	 * @Creation Date : 2018年1月5日 下午2:19:41
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static RegUserInfo getRegUserInfo(Integer regUserId, Supplier<RegUserInfo> supplier) {
		return (RegUserInfo) tryLoadObjectRedis(RegUserInfo.class.getSimpleName() + regUserId, RegUserInfo.class,
				supplier);
	}

	/**
		 * 存储用户登录状态
		 *
		 * @param response
		 * @param rememberMe
		 * @author zc.ding
		 * @since 2017年5月21日
		 */
	static void storeLoginData(HttpServletResponse response, ResponseEntity<?> result, String rememberMe) {
		RegUser regUser = (RegUser) result.getParams().get("regUser");
		result.addParam("showProducts", 0);
		regUser.setPasswd("******");
		String ticket = SpecialCodeGenerateUtils.getSpecialCode();
		// 保存到session中
		HttpSessionUtil.addLoginUser(regUser);
		// 添加cookie
		Cookie cookie = CookieUtil.createCookie(com.yirun.framework.core.commons.Constants.TICKET + getServerType(),
				ticket);
		// 存储上次登录时间
		HttpSessionUtil.addAttrToSession("lastLoginTime", (Date) result.getParams().get("lastLoginTime"));
		response.addCookie(cookie);
		if ("1".equals(rememberMe)) {
			int time = 604800; // 7天
			Cookie rememberMeCookie = CookieUtil.createCookie("rememberMe", "1", time);
			Cookie loginCookie = CookieUtil.createCookie("login", String.valueOf(regUser.getLogin()), time);
			response.addCookie(rememberMeCookie);
			response.addCookie(loginCookie);
		}
		// 缓存ticket到redis
		JedisClusterUtils.setAsJson(ticket, regUser, TICKET_EXPIRES_TIME);
		// 刷新token
		BaseUtil.refreshSumbToken();
	}

	/**
	 * 清空登录的缓存的数据
	 *
	 * @param request
	 * @param response
	 * @author zc.ding
	 * @since 2017年5月21日
	 */
	static void cleanLoginData(HttpServletRequest request, HttpServletResponse response) {
		// 清除用的菜单信息 >规则，前缀+type+Login ,权限用 “-”分开的字符
		if(getLoginUser() != null){
			JedisClusterUtils.delete(join(asList(USER_MENU_PREFIX, getLoginUser().getType(), getLoginUser().getLogin()), "_"));
			JedisClusterUtils.delete(join(asList(USER_AUTH_PREFIX, getLoginUser().getType(), getLoginUser().getLogin()), "_"));
        }
		// 使cookie失效
		Cookie cookie = CookieUtil.getCookie(request, TICKET + getServerType());
		if (cookie != null) {
			cookie = CookieUtil.createCookie(cookie.getName(), cookie.getValue(), 0);
			// 清除缓存用户登录信息
			JedisClusterUtils.delete(cookie.getValue());
			response.addCookie(cookie);
		}
		// 清楚用户登录状态
		HttpSessionUtil.getSession().removeAttribute(CURRENT_USER);
		HttpSessionUtil.getSession().invalidate();

	}

	/**
	 * 创建临时文件
	 *
	 * @return
	 */
	static File createTempFile(String suffix) {
		File f = null;
		suffix = ".".concat(StringUtils.isEmpty(suffix) ? "temp" : suffix);

		try {
			f = File.createTempFile(FileParser.get13UUID(), suffix);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 把multipartFile转换为File
	 *
	 * @param multipartFile
	 * @return
	 */
	static File transferMultiPartFileToFile(MultipartFile multipartFile) {
		String originalFilename = multipartFile.getOriginalFilename();
		String extension = StringUtils.getFilenameExtension(originalFilename);
		File f = createTempFile(extension);
		try {
			multipartFile.transferTo(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 将multipartFile转换成FileInfo
	 *
	 * @param multipartFile
	 * @return
	 */
	static FileInfo transferMultiPartFileToFileInfo(MultipartFile multipartFile) {

		if (multipartFile == null) {
			return null;
		}
		FileInfo fileInfo = new FileInfo();
		fileInfo.setName(multipartFile.getOriginalFilename());
		fileInfo.setFileContent(transferMultiPartFileToFile(multipartFile));
		return fileInfo;
	}

	/**
	 * 将multipartFile转换成FileInfo
	 *
	 * @param file
	 * @return
	 */
	static FileInfo transferFileToFileInfo(File file) {

		if (file == null) {
			return null;
		}
		FileInfo fileInfo = new FileInfo();
		fileInfo.setName(file.getName());
		fileInfo.setFileContent(file);
		return fileInfo;
	}

	/**
	 * @param result
	 * @param msg
	 * @return : ResponseEntity<?>
	 * @Description : 判断检索结果
	 * @Method_Name : getResult
	 * @Creation Date : 2017年9月6日 下午1:58:58
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static ResponseEntity<?> getResult(Object result, String... msg) {
		if (result == null || ((result instanceof Integer) && (int) (result) == 0)) {
			return new ResponseEntity<>(ERROR, (msg == null || msg.length <= 0) ? "" : msg[0]);
		}
		return new ResponseEntity<>(SUCCESS, result);
	}

	/**
	 * @return : ResponseEntity<?>
	 * @Description : 分页查询返回空集合
	 * @Method_Name : emptyResult
	 * @Creation Date : 2017年10月11日 下午3:55:58
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	static ResponseEntity<?> emptyResult() {
		Pager pager = new Pager();
		pager.setData(Collections.emptyList());
		return new ResponseEntity<>(SUCCESS, pager);
	}

	/**
	 * @param request
	 * @return : ResponseEntity<?>
	 * @Description : 保存客户端上传的文件
	 * @Method_Name : saveFile
	 * @Creation Date : 2017年10月17日 下午6:05:32
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static ResponseEntity<?> saveFile(HttpServletRequest request, MultipartFile multipartFile) {
		String fileName = "";
		try {
			String uploadDir = "WEB-INF/upload";
			String filePathName = request.getServletContext().getRealPath("/") + File.separator + uploadDir;
			File filePath = new File(filePathName);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			fileName = filePathName + File.separator + DateUtils.format(new Date(), DateUtils.DATE_HHMMSS);
			multipartFile.transferTo(new File(fileName));// 将本地文件上传到服务器
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Constants.ERROR, "文件上传失败!");
		}
		return new ResponseEntity<>(Constants.SUCCESS, fileName);
	}

	/**
	 * 判断集合是否为空
	 *
	 * @param collection
	 * @return
	 */
	static boolean collectionIsEmpty(Collection<?> collection) {
		return (collection == null || collection.size() == 0);
	}

	/**
	 * 转换文件类型
	 *
	 * @param platform
	 * @return
	 */
	static FileType transformFileType(String platform) {
		List<FileType> collect = Arrays.stream(FileType.values()).filter((e) -> e.getFileTypeName().equals(platform))
				.collect(Collectors.toList());
		return (collect == null || collect.size() == 0) ? null : collect.get(0);
	}

	/**
	 * 转换桶名称
	 *
	 * @param platform
	 * @return
	 */
	static String transformPlatform(String platform) {
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

	/**
	 * 抽取一般性的上传方法
	 *
	 * @param platform
	 * @param fileType
	 * @param filePath
	 * @param multipartFile
	 *            可以是MultipartFile类型或者File类型
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static Object uploadFile(String platform, String fileType, String filePath, Object multipartFile) {
		/**
		 * step 1:确定平台类型
		 */
		if ((platform = transformPlatform(platform)) == null) {
			return new ResponseEntity(ERROR, "请正确指定上传文件存放桶名称");
		}

		/**
		 * step 2:确定文件类型
		 */
		FileType resolvedType = null;
		if ((resolvedType = transformFileType(fileType)) == null) {
			return new ResponseEntity(ERROR, "请正确指定上传文件类型");
		}

		/**
		 * step 3:执行上传操作
		 */
		FileInfo fileInfo = null;
		if (multipartFile instanceof MultipartFile) {
			fileInfo = transferMultiPartFileToFileInfo(MultipartFile.class.cast(multipartFile));
		}
		if (multipartFile instanceof File) {
			fileInfo = transferFileToFileInfo(File.class.cast(multipartFile));
		}
		return OSSLoader.getInstance().bindingUploadFile(fileInfo).setAllowUploadType(resolvedType)
				.setFileState(FileState.UN_UPLOAD).setBucketName(platform)/* 存放平台桶 */
				.setFilePath(
						StringUtils.cleanPath(StringUtils.replace(StringUtils.replace(filePath, "%2F", "/"), "%5C", "/")
						/* 剔除转义字符 */)).doUpload();
	}

	/**
	 * 检查Response是否为成功
	 *
	 * @param responseEntity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	static boolean isResponseSuccess(ResponseEntity responseEntity) {
		return responseEntity == null ? false : (responseEntity.getResStatus() == Constants.SUCCESS);
	}

	/**
	 * @return : String
	 * @Description : 获得服务类型是前端项目还是后台项目
	 * @Method_Name : getServerType
	 * @Creation Date : 2017年12月6日 下午4:40:09
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static String getServerType() {
		String serverType = PropertiesHolder.getProperty(Constants.SERVER_TYPE);
		if (org.apache.commons.lang.StringUtils.isNotBlank(serverType) && !"null".equals(serverType)) {
			return "_" + serverType;
		}
		return "";
	}

	/**
	 * @return : String
	 * @Description : 创建新的submit token
	 * @Method_Name : getNewSubmitToken
	 * @Creation Date : 2017年12月7日 上午10:49:25
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static String getNewSubmitToken() {
		return Constants.WEB_SUBMIT_TOKEN_PREFIX + UUID.randomUUID().toString();
	}

	/**
	 * @return : String
	 * @Description : 组装用于获得submitToken的key
	 * @Method_Name : getSubmitTokenKey
	 * @Creation Date : 2017年12月7日 上午10:51:54
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static String getSubmitTokenKey() {
		return Constants.WEB_SUBMIT_TOKEN + BaseUtil.getServerType();
	}

	/**
	 * @return : String
	 * @Description : 刷新submit token
	 * @Method_Name : refreshSumbToken
	 * @Creation Date : 2017年12月7日 上午10:58:28
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static String refreshSumbToken() {
		String newTokenValue = BaseUtil.getNewSubmitToken();
		HttpSessionUtil.addAttrToSession(getSubmitTokenKey(), newTokenValue);
		HttpSessionUtil.addAttrToRequest(getSubmitTokenKey(), newTokenValue);
		Cookie cookie = CookieUtil.createCookie(getSubmitTokenKey(), newTokenValue);
		HttpSessionUtil.getResponse().addCookie(cookie);
		JedisClusterUtils.set(newTokenValue, newTokenValue, Constants.TICKET_EXPIRES_TIME);
		return newTokenValue;
	}

	/**
	 * 申请分布式锁
	 *
	 * @param lockKey
	 * @param paramter
	 * @param logWhenFail
	 * @param logicFunc
	 * @return
	 */
	static Object redisLock(String lockKey, Object paramter,String logWhenFail,Function logicFunc) {
		// 申请分布式锁，防止多个缓存请求并发操作
		final JedisClusterLock jedisLock = new JedisClusterLock();
		try {
			if (!jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME)) {
				if (LOG.isInfoEnabled()) {
					LOG.error("申请分布式锁异常, 将返回null, LOCK_KEY={} ", lockKey);
				}
				return null;
			}
			//已经获取锁，执行获取锁的逻辑
			return logicFunc.apply(paramter);
		} catch (Exception e) {
			if (LOG.isInfoEnabled()) {
				LOG.error(logWhenFail+"\n异常信息为: {}", e);
			}
			throw new GeneralException(logWhenFail);
		} finally {
			// 释放redis锁
			jedisLock.freeLock(lockKey);
		}
	}

	/**
	 * 尝试从reids中获取，如果获取不到再实现其他的逻辑 带缓存实现
	 *
	 * @param redisKey
	 * @param isList
	 * @param ifNotBackup
	 * @return
	 */
	static Object tryLoadDataFromRedis(String redisKey, boolean isList, Supplier ifNotBackup, Consumer cacheIf) {
		Assert.notNull(redisKey, "键值不能为null");
		// 从reids中获取结果
		Object result = null;
		boolean errorFlag = false;
		try {
			result = isList ? JedisClusterUtils.getList(redisKey, 0, -1) : JedisClusterUtils.get(redisKey);
		} catch (Exception e) {
			errorFlag = true;
			if (LOG.isInfoEnabled()) {
				LOG.info("-->从redis中获取 Key 失败，key为：{}", redisKey);
			}
		}
		// 取到结果直接返回
		if (isList ? !BaseUtil.collectionIsEmpty((Collection) result) : (result != null)) {
			return result;
		}
		// 尝试通过其他途径获取
		if (ifNotBackup != null) {
			result = ifNotBackup.get();
		}
		if (cacheIf != null
				&& !errorFlag/* 如果有errorFlag，多半是redis无法正常工作，可以考虑此时不进行缓存操作 */) {
			/**
			 * 开启线程执行,因为正常情况下，不会从redis中读取不到数据，如果
			 * 到了这一步，那么多半是redis已经down了，开启异步执行，提高效率。
			 */
			Object finalResult = result;
			// 使用多线程在实践中出现了“读脏"现象(读了setList设置未完全的值)
			// getExecutorService().execute(() -> {
			// //申请分布式锁，防止多个缓存请求并发操作
			// redisLock(join(Arrays.asList(redisKey, LOCK_SUFFIX), "_"),
			// finalResult, "执行缓存出错：{}", (paramter) -> {
			// //执行缓存
			// cacheIf.accept(paramter);
			// return null;
			// });
			// });
			// 申请分布式锁，防止多个缓存请求并发操作
			redisLock(join(asList(redisKey, LOCK_SUFFIX), "_"), finalResult, "执行缓存出错：{}", (paramter) -> {
				// 执行缓存
				cacheIf.accept(paramter);
				return null;
			});
		}
		return result;
	}

	/**
	 * @param key
	 * @param clazz
	 * @param ifNotBackup
	 * @return : Object
	 * @Description : 尝试从reids中获取，如果获取不到再实现其他的逻辑
	 * @Method_Name : tryLoadObjectRedis
	 * @Creation Date : 2018年1月5日 下午2:41:57
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	static Object tryLoadObjectRedis(String key, Class<?> clazz, Supplier<?> ifNotBackup) {
		Assert.notNull(key, "键值不能为null");
		// 从reids中获取结果
		Object result = null;
		try {
			result = clazz == null ? JedisClusterUtils.get(key) : JedisClusterUtils.getObjectForJson(key, clazz);
		} catch (Exception e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("-->从redis中获取 Key 失败，key为：{}", key);
			}
		}
		if (result != null) {
			return result;
		}
		// 尝试通过其他途径获取
		if (ifNotBackup != null) {
			result = ifNotBackup.get();
		}
		return result;
	}

	/**
	 * 尝试从reids中获取，如果获取不到再实现其他的逻辑 不带缓存实现
	 *
	 * @param redisKey
	 * @param isList
	 * @param ifNotBackup
	 * @return
	 */
	static Object tryLoadDataFromRedis(String redisKey, boolean isList, Supplier ifNotBackup) {
		Assert.notNull(redisKey, "键值不能为null");
		// 从reids中获取结果
		Object result = null;
		try {
			result = isList ? JedisClusterUtils.getList(redisKey, 0, -1) : JedisClusterUtils.get(redisKey);
		} catch (Exception e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("-->从redis中获取 Key 失败，key为：{}", redisKey);
			}
		}
		// 取到结果直接返回
		if (isList ? !BaseUtil.collectionIsEmpty((Collection) result) : (result != null)) {
			return result;
		}
		// 尝试通过其他途径获取
		if (ifNotBackup != null) {
			result = ifNotBackup.get();
		}
		return result;
	}

	/**
	 * 尝试从reids中获取，如果获取不到再实现其他的逻辑 使用默认缓存实现
	 *
	 * @param redisKey
	 * @param isList
	 * @param ifNotBackup
	 * @return
	 */
	static Object tryLoadDataFromRedisWithDefaultCache(String redisKey, boolean isList, Supplier ifNotBackup) {
		return tryLoadDataFromRedis(redisKey, isList, ifNotBackup, (result) -> {
			JedisClusterUtils.delete(redisKey);
			if (isList) {
				if (!collectionIsEmpty((Collection<?>) result)) {
					JedisClusterUtils.setList(redisKey,
							((List<?>) result).stream().map((e) -> JsonUtils.toJson(e)).collect(Collectors.toList()));
				}
				return;
			}
			JedisClusterUtils.set(redisKey, (String) result);
		});
	}

	/**
	 * Integer的包装类和原生类型比较(防空判断)
	 *
	 * @param wraperInt
	 * @param primitiveInt
	 * @return
	 */
	static boolean equelsIntWraperPrimit(Integer wraperInt, int primitiveInt) {
		if (wraperInt == null) {
			return false;
		}
		return wraperInt == primitiveInt;
	}

	/**
	*  处理tcc的try操作的日志前缀
	*  @Method_Name    ：getTccTryLogPrefix
	*  @return java.lang.String
	*  @Creation Date  ：2018/4/17
	*  @Author         ：zhichaoding@hongkun.com.cn
	*/
	static String getTccTryLogPrefix() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String currMethod = stackTraceElements[2].getClassName() + "#" + stackTraceElements[2].getMethodName();
		if(RpcContext.getContext() != null){
            String caller = RpcContext.getContext().getAttachment("caller");
            if(org.apache.commons.lang.StringUtils.isNotBlank(caller)){
                return "tcc try " + currMethod + ", reference " + caller;
            }
        }
		return currMethod;
	}
	
	static <T> T getTccProxyBean(Class<T> clazz, Class<?> callerClass, String callerMethod) {
		RpcContext.getContext().setAttachment("caller", callerClass.getName() + "#" + callerMethod);
		return ApplicationContextUtils.getBean(clazz);
	}

	/**
	 *  @Description    ：中断请求方法，当用户没有权限访问某个url时候，应该中断其请求,返回前端提示信息
	 *  @Method_Name    ：responseRequestBreak
	 *  @param response
	 *  @param responseEntity
	 *  @return java.lang.Boolean
	 *  @Creation Date  ：2018/4/13
	 *  @Author         ：zhongpingtang@hongkun.com.cn
	 */
	 static Boolean responseRequestBreak(HttpServletResponse response, ResponseEntity responseEntity) {
		String responseStr = JsonUtils.toJson(responseEntity);
		ResponseUtils.responseJson(response, responseStr);
		return FALSE;
	}

	/**
	 * 验证是否是ajax请求
	 * @param webRequest
	 * @return
	 */
	static boolean isAjaxRequest(WebRequest webRequest) {
		String requestedWith = webRequest.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	/**
	 *  @Description    ：从dubbo封装的Runtime中解析出GeneralExeption
	 *  @Method_Name    ：resolveGeneralExceptionFromRemoteRuntimeExcetion
	 *  @param message
	 *  @return java.lang.String
	 *  @Creation Date  ：2018/5/29
	 *  @Author         ：zhongpingtang@hongkun.com.cn
	 */
	 static String  resolveGeneralExceptionFromRemoteRuntimeExcetion(String message){
		String[] msges = StringUtils.split(message, "\r\n");
		if (ArrayUtils.isNotEmpty(msges)) {
			String topMsg = msges[0];
			String spilt=":";
			if (topMsg.indexOf(spilt)>0) {
				String[] exceptionNameAndMsgs = topMsg.split(spilt);
				if (GeneralException.class.getName().equals(exceptionNameAndMsgs[0])) {
					//返回信息
					return exceptionNameAndMsgs[1];
				}
			}
		}
		return null;
	}

    /**
    *  添加sessionId到客户端的cookie中
    *  @Method_Name             ：addTicket
    *  @param sessionId
    *  @return void
    *  @Creation Date           ：2018/6/29
    *  @Author                  ：zc.ding@foxmail.com
    */
    static void addTicket(String sessionId) {
        Cookie cookie = CookieUtil.createCookie(Constants.TICKET, sessionId);
        HttpSessionUtil.getResponse().addCookie(cookie);
    }

}
