package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.AppVersionConstants;
import com.hongkun.finance.vas.dao.SysAppVersionRuleDao;
import com.hongkun.finance.vas.model.SysAppVersionRule;
import com.hongkun.finance.vas.service.SysAppVersionRuleService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.OSSBuckets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.impl.SysAppVersionRuleServiceImpl.java
 * @Class Name    : SysAppVersionRuleServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class SysAppVersionRuleServiceImpl implements SysAppVersionRuleService {

	private static final Logger logger = LoggerFactory.getLogger(SysAppVersionRuleServiceImpl.class);
	
	/**
	 * SysAppVersionRuleDAO
	 */
	@Autowired
	private SysAppVersionRuleDao sysAppVersionRuleDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertSysAppVersionRule(SysAppVersionRule sysAppVersionRule) {
		try{
			return this.sysAppVersionRuleDao.save(sysAppVersionRule);
		}catch (Exception e){
			logger.error("insertSysAppVersionRule, 添加app版本更新规则异常, 规则信息: {}, 异常信息: ", sysAppVersionRule.toString(), e);
			throw new GeneralException("添加app版本更新规则异常！");
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertSysAppVersionRuleBatch(List<SysAppVersionRule> list) {
		return this.sysAppVersionRuleDao.insertBatch(SysAppVersionRule.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertSysAppVersionRuleBatch(List<SysAppVersionRule> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		return this.sysAppVersionRuleDao.insertBatch(SysAppVersionRule.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateSysAppVersionRule(SysAppVersionRule sysAppVersionRule) {
		try{
			return this.sysAppVersionRuleDao.update(sysAppVersionRule);
		}catch (Exception e){
			logger.error("updateSysAppVersionRule, 更新app版本更新规则异常, 规则信息: {}, 异常信息: ",sysAppVersionRule.toString(), e);
			throw new GeneralException("更新app版本更新规则异常！");
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateSysAppVersionRuleBatch(List<SysAppVersionRule> list, int count) {
		return this.sysAppVersionRuleDao.updateBatch(SysAppVersionRule.class, list, count);
	}
	
	@Override
	public SysAppVersionRule findSysAppVersionRuleById(int id) {
		return this.sysAppVersionRuleDao.findByPK(Long.valueOf(id), SysAppVersionRule.class);
	}
	
	@Override
	public List<SysAppVersionRule> findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule) {
		return this.sysAppVersionRuleDao.findByCondition(sysAppVersionRule);
	}
	
	@Override
	public List<SysAppVersionRule> findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule, int start, int limit) {
		return this.sysAppVersionRuleDao.findByCondition(sysAppVersionRule, start, limit);
	}
	
	@Override
	public Pager findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule, Pager pager) {
		return this.sysAppVersionRuleDao.findByCondition(sysAppVersionRule, pager);
	}
	
	@Override
	public int findSysAppVersionRuleCount(SysAppVersionRule sysAppVersionRule){
		return this.sysAppVersionRuleDao.getTotalCount(sysAppVersionRule);
	}
	
	@Override
	public Pager findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule, Pager pager, String sqlName){
		return this.sysAppVersionRuleDao.findByCondition(sysAppVersionRule, pager, sqlName);
	}
	
	@Override
	public Integer findSysAppVersionRuleCount(SysAppVersionRule sysAppVersionRule, String sqlName){
		return this.sysAppVersionRuleDao.getTotalCount(sysAppVersionRule, sqlName);
	}

	@Override
	public ResponseEntity findRuleCountByVersion(String platform, String version) {
		ResponseEntity result = new ResponseEntity(SUCCESS);
		Map<String,Object> params = new HashMap<>(2);
		Map<String,String> versionAndDescribe = getLatestVersionAndDescribe(platform);
		//1.获取最新版本号
		String latestVersion = versionAndDescribe.get("version");
		//如果最新版本号为null，说明配置文件异常
		if(StringUtils.isBlank(latestVersion)){
			return new ResponseEntity(ERROR);
		}
		//如果当前版本号不小于最新版本号则不需要更新
		if (Integer.valueOf(version.replace(".","").trim())
				>= Integer.valueOf(latestVersion.replace(".", "").trim())){
			//无需更新
			params.put("state",3);
		}else {
			//2.在判断是否有强制更新
			SysAppVersionRule condition = new SysAppVersionRule();
			condition.setVersion(version);
			condition.setPlatform(platform);
			condition.setState(AppVersionConstants.APP_VERSION_STATE_UP);
			SysAppVersionRule sysAppVersionRule = this.sysAppVersionRuleDao.findRule(condition);
			//sysAppVersionRule有内容表示该版本的app需要进行强制更新，否则提示普通更新
			params.put("version",latestVersion);
			if (sysAppVersionRule != null){
				params.put("content",sysAppVersionRule.getContent());
				params.put("state",1);
			}else {
				params.put("content",versionAndDescribe.get("describe"));
				params.put("state",2);
			}
			//如果为安卓手机返回app下载地址
			if (platform.equals(AppVersionConstants.APP_VERSION_PLATFORM_ANDROID)){
				//判断apk包是否存在
				String key = "android/apk/HKJF_ANDROID.apk";
				if (OSSLoader.getInstance().doesObjectExist(OSSBuckets.HKJF,key)){
					params.put("androidApkKey",key);
				}
			}
		}
		result.setParams(params);
		return result;
	}

	private Map<String,String> getLatestVersionAndDescribe(String platform){
		Map<String,String> result = new HashMap<>(2);
		if (OSSLoader.getInstance().doesObjectExist(OSSBuckets.HKJF,"android/hkjf_version.properties")){
			InputStreamResource isr = new InputStreamResource(OSSLoader.getInstance().
					getOSSObject(OSSBuckets.HKJF,"android/hkjf_version.properties").getObjectContent());
			EncodedResource encodedResource = new EncodedResource(isr,"UTF-8");
			Properties properties = null;
			try {
				properties = PropertiesLoaderUtils.loadProperties(encodedResource);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (properties != null){
				if (platform.equals(AppVersionConstants.APP_VERSION_PLATFORM_ANDROID)){
					result.put("version",properties.getProperty("android.latest.version"));
					result.put("describe",properties.getProperty("android.latest.version.describe"));
				}
				if (platform.equals(AppVersionConstants.APP_VERSION_PLATFORM_IOS)){
					result.put("version",properties.getProperty("ios.latest.version"));
					result.put("describe",properties.getProperty("ios.latest.version.describe"));
				}
			}
		}else {
			logger.error("oss端app相关配置文件缺失，请及时补充！");
		}
		return result;
	}
	@Override
	public ResponseEntity updateAppVersionRuleState(int id, int state) {
		SysAppVersionRule rule = new SysAppVersionRule();
		rule.setId(id);
		rule.setState(state);
		if (AppVersionConstants.APP_VERSION_STATE_UP == state){
			rule.setUptime(DateUtils.getCurrentDate());
		}else {
			rule.setDowntime(DateUtils.getCurrentDate());
		}
		try {
			if (this.sysAppVersionRuleDao.update(rule) > 0){
				return new ResponseEntity(SUCCESS);
			}
		}catch (Exception e){
			logger.error("updateAppVersionRuleState, 上架或下架app版本规则异常, 规则id: {}, 更新状态: {}", id, state);
			throw new GeneralException("上架或下架app版本规则异常！");
		}
		return new ResponseEntity(ERROR);
	}

	@Override
	public ResponseEntity getIosAuditVersion(String version) {
		ResponseEntity result = new ResponseEntity(Constants.SUCCESS);
		Map<String,Object> params = new HashMap<>(2);
		//默认为审核通过
		params.put("state",1);
		//判断版本是否为审核版本，若为审核版本则返回0-审核中，否则返回1-审核通过
		String iosAuditVersion = PropertiesHolder.getProperty("ios.audit.version");
		if (iosAuditVersion != null && iosAuditVersion.equals(version)){
			params.put("state",0);
		}
		result.setParams(params);
		return result;
	}
}
