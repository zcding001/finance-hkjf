package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasVipGradeStandardDao;
import com.hongkun.finance.vas.dao.VasVipGrowRecordDao;
import com.hongkun.finance.vas.dao.VasVipTreatmentDao;
import com.hongkun.finance.vas.model.VasVipTreatment;
import com.hongkun.finance.vas.service.VasVipTreatmentService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.impl.VasVipTreatmentServiceImpl.java
 * @Class Name    : VasVipTreatmentServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class VasVipTreatmentServiceImpl implements VasVipTreatmentService {

	private static final Logger logger = LoggerFactory.getLogger(VasVipTreatmentServiceImpl.class);
	
	/**
	 * VasVipTreatmentDAO
	 */
	@Autowired
	private VasVipTreatmentDao vasVipTreatmentDao;
	@Autowired
	private VasVipGrowRecordDao vasVipGrowRecordDao;
	@Autowired
	private VasVipGradeStandardDao vasVipGradeStandardDao;

	/**
	 *会员待遇信息中，没有设置任何值则为-1
	 */
	private final String NONE_SETTING = "-1";

	@Override
	public VasVipTreatment findVasVipTreatmentById(int id) {
		return this.vasVipTreatmentDao.findByPK(Long.valueOf(id), VasVipTreatment.class);
	}
	
	@Override
	public List<VasVipTreatment> findVasVipTreatmentList(VasVipTreatment vasVipTreatment) {
		return this.vasVipTreatmentDao.findByCondition(vasVipTreatment);
	}

	@Override
	public ResponseEntity addVipTreatment(VasVipTreatment vasVipTreatment) {
		try{
			//初始状态为不启用
			vasVipTreatment.setState(VasConstants.VAS_STATE_N);
			//判断同类型的会员待遇适用用户注册时间段范围是否冲突
			if (vasVipTreatmentDao.findVasVipTreatmentTimeCount(vasVipTreatment) > 0){
				return new ResponseEntity(ERROR,"适用用户注册时间范围冲突！");
			}
			if (this.vasVipTreatmentDao.save(vasVipTreatment) > 0){
				return new ResponseEntity(SUCCESS,"添加会员待遇记录成功");
			}
		}catch (Exception e){
			logger.error("addVipTreatment, 添加会员待遇记录异常, 会员待遇信息: {}, 异常信息: ",
					vasVipTreatment.toString(), e);
			throw new GeneralException("添加会员待遇记录异常！");
		}
        return new ResponseEntity(ERROR,"添加会员待遇记录失败");
	}

	@Override
	public ResponseEntity updateVipTreatment(VasVipTreatment vasVipTreatment) {
		try{
			VasVipTreatment vipTreatment = this.findVasVipTreatmentById(vasVipTreatment.getId());
			if (vasVipTreatment == null || vasVipTreatment.getId() == null || vipTreatment == null){
				return new ResponseEntity(ERROR,"请选择正确的记录进行修改");
			}
			vasVipTreatment.setType(vipTreatment.getType());
			//判断同类型的会员待遇适用用户注册时间段范围是否冲突
			if (vasVipTreatmentDao.findVasVipTreatmentTimeCount(vasVipTreatment) > 0){
				return new ResponseEntity(ERROR,"适用用户注册时间范围冲突！");
			}
			vasVipTreatment.setModifyTime(new Date());
			if (this.vasVipTreatmentDao.update(vasVipTreatment) > 0){
				return new ResponseEntity(SUCCESS,"修改会员待遇记录成功");
			}
		}catch (Exception e){
			logger.error("updateVipTreatment, 更新会员待遇记录异常, 会员待遇信息: {}, 异常信息: ",
					vasVipTreatment.toString(), e);
			throw new GeneralException("更新会员待遇记录异常！");
		}
        return new ResponseEntity(ERROR,"修改会员待遇记录失败");
	}

	@Override
	public List<VasVipTreatment> getVipTreatMentByRegistTime(Date registTime) {
		VasVipTreatment vipTreatment = new VasVipTreatment();
		vipTreatment.setState(VasConstants.VAS_STATE_Y);
		vipTreatment.setRegistBeginTimeEnd(registTime);
		vipTreatment.setRegistEndTimeBegin(registTime);
		return this.findVasVipTreatmentList(vipTreatment);
	}

	@Override
	public VasVipTreatment getVipTreatMentByTypeAndRegistTime(int type, Date registTime) {
		return this.vasVipTreatmentDao.getVipTreatMentByTypeAndRegistTime(type, registTime);
	}

	@Override
	public List<Map<String,Object>> getVipTreatMentListDescription(int regUserId, Date registTime) {
		//1.获取当前用户等级
		int level = vasVipGradeStandardDao.findLevelByGrowValue(vasVipGrowRecordDao.findUserCurrentGrowValue(regUserId));
		//2.根据用户注册时间获取适用的会员待遇
		List<VasVipTreatment> list = this.getVipTreatMentByRegistTime(registTime);
		//3.根据用户等级过滤出适用于该用户会员等级的待遇描述信息
		return this.getVipTreatMentListDescriptionByLevel(level,list);
	}

	@Override
	public List<Map<String,Object>> getVipTreatMentListDescriptionByLevel(int level,List<VasVipTreatment> list){
		List<Map<String,Object>> descriptionList = new ArrayList<>();
		list.forEach((vipTreatment) -> {
			List<HashMap<String,Object>> contentList = JsonUtils.json2Object(vipTreatment.getRuleContents(),
					List.class,null);
			contentList.forEach((content) -> {
				int grade = Integer.valueOf(content.get("grade").toString());
				String rulem = content.get("rulem").toString();
				if (grade == level && !NONE_SETTING.equals(rulem)){
					Map<String,Object> description = new HashMap<>();
					description.put("type",vipTreatment.getType());
					String introduction = "";
					switch (Integer.valueOf(vipTreatment.getType())){
						case 1:introduction = "用户每月1号可获赠"+content.get("rulen").toString()+"张"+rulem+"%加息优惠券";break;
						case 2:introduction = "每次签到额外获取"+rulem+"积分/次";break;
						case 3:introduction = "用户生日（以身份证为准）当天可收到鸿坤金服送出的生日祝福以及"+content.get("rulen").toString()
								+"张"+rulem+"%加息券";break;
						case 4:introduction = "用户每月1号可获赠提现优惠券"+content.get("rulen").toString()+"张";break;
						case 5:introduction = "用户拨打" + UserConstants.SERVICE_HOTLINE + "客服热线，可享受专属尊贵服务";
							description.put("hotline",UserConstants.SERVICE_HOTLINE);break;
						case 6:introduction = "用户可以享受鸿坤金服不定期举行的活动";break;
						case 7:introduction = "用户每月1号可获赠"+content.get("rulen").toString()+"张"+rulem+"元投资红包";break;
						case 8:introduction = "用户每月1号可获赠"+content.get("rulen").toString()+"张"+rulem+"%好友券";break;
						default:break;
					}
					description.put("num",content.get("rulen"));
					description.put("worth",content.get("rulem"));
					description.put("introduction",introduction);
					descriptionList.add(description);
				}
			});
		});
		return descriptionList;
	}
}
