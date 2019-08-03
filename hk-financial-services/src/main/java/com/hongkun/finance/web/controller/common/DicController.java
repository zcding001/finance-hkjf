package com.hongkun.finance.web.controller.common;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.DicArea;
import com.hongkun.finance.user.model.DicData;
import com.hongkun.finance.user.service.DicAreaService;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.redis.JedisClusterUtils;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("dicController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class DicController {
	@Reference
	private DicDataService dicDataService;
	@Reference
    private DicAreaService dicAreaService;
	
	/**
	 * 
	 *  @Description    : 检索所有字典数据
	 *  @Method_Name    : dicList
	 *  @return
	 *  @return         : List<DicData>
	 *  @Creation Date  : 2017年12月4日 上午9:02:59 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/dicList")
    @ResponseBody
    public List<DicData> dicList(){
    	return this.dicDataService.findDicDataList();
    }
	
	/**
     *  @Description    : 检索区域字典表
     *  @Method_Name    : dicAreaList
     *  @return         : List<DicArea>
     *  @Creation Date  : 2017年9月26日 下午4:56:04 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
	@RequestMapping("/dicAreaList")
    @ResponseBody
    public List<DicArea> dicAreaList(){
    	List<DicArea> list = JedisClusterUtils.getObjectForJson(DicArea.class.getSimpleName(), new TypeReference< List<DicArea>>(){});
    	if(CommonUtils.isEmpty(list)){
    		list = this.dicAreaService.findDicAreaList(new DicArea());
			JedisClusterUtils.setAsJson(DicArea.class.getSimpleName(), list);
    	}
    	return list;
    }
}
