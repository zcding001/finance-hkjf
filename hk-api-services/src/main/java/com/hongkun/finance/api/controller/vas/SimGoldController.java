package com.hongkun.finance.api.controller.vas;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;

@Controller
@RequestMapping("/simGoldController")
public class SimGoldController {
    @Reference
    private VasSimRecordService vasSimRecordService;
    @Reference
    private DicDataService dicDataService;
    /**
     *  @Description    : 查询体验金记录
     *  @Method_Name    : getSimRecord;
     *  @param pager
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年11月1日 下午3:37:31;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/getSimRecord")
    @ResponseBody
    public Map<String, Object> getSimRecord(Pager pager) {
        RegUser regUser = BaseUtil.getLoginUser();
        pager.setInfiniteMode(true);
        VasSimRecord vasSimRecord = new VasSimRecord();
        vasSimRecord.setRegUserId(regUser.getId());
        Pager pagerInfo = vasSimRecordService.findVasSimRecordList(vasSimRecord, pager);
        List<VasSimRecord> list=(List<VasSimRecord>)pagerInfo.getData();
        String[] includeProperteis={"money","expireTime","state"};
       Function<Object, Object> getKeyDesc = (key) -> dicDataService.findNameByValue("vas",
                "sim_state", (Integer) key);
        
        return AppResultUtil.successOfListInProperties(list, "查询成功", includeProperteis).addParameterDescInList("state", getKeyDesc);
    }
}
