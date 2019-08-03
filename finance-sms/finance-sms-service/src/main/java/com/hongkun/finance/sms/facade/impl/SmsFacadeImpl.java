package com.hongkun.finance.sms.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.facade.SmsFacade;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsWebMsgService;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class SmsFacadeImpl implements SmsFacade{

    private final Logger logger = LoggerFactory.getLogger(SmsFacadeImpl.class);

	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Autowired
	private SmsWebMsgService smsWebMsgService;
	
	//创建线程池
	ExecutorService executorService = Executors.newCachedThreadPool();
	
	@Override
	public ResponseEntity<?> sendTelMsg(String ids, String msg) {
		RegUser regUser = new RegUser();
        regUser.setUserIds(Arrays.asList(ids).stream().map(Integer::parseInt).collect(Collectors.toList()));
		List<RegUser> list = this.regUserService.findRegUserList(regUser);
		this.doSendTelMsgBatch(list.stream().map(o -> {
            UserVO userVO = new UserVO();
            userVO.setUserId(o.getId());
            userVO.setLogin(o.getLogin());
            return userVO;
        }).collect(Collectors.toList()), msg);
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	@Override
	public ResponseEntity<?> findWebMsgList(Pager pager, SmsWebMsg smsWebMsg) {
		Pager result = this.smsWebMsgService.findSmsWebMsgList(smsWebMsg, pager);
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
			result.getData().forEach(e -> {
				SmsWebMsg tmp = (SmsWebMsg)e;
				RegUserDetail regUserDetail = this.regUserDetailService.findRegUserDetailByRegUserId(tmp.getRegUserId());
				if(regUserDetail != null){
					tmp.setRealName(regUserDetail.getRealName());
				}
			});
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

    @Override
    public void sendTelMsgBatch(String msg, Integer userGroup) {
        UserVO userVO = new UserVO();
        userVO.setUserGroup(userGroup);
        this.doSendTelMsgBatch(regUserService.findUserWithDetailByInfo(userVO), msg);
    }
    
    /**
    *  执行短信发送操作
    *  @Method_Name             ：doSendTelMsgBatch
    *  @param list              ：用户集合
    *  @param msg               ：短信内容
    *  @return void
    *  @Creation Date           ：2018/4/24
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    private void doSendTelMsgBatch(List<UserVO> list, String msg){
        int len = 0;
        List<SmsMsgInfo> telMsgList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            telMsgList.add(new SmsTelMsg(list.get(i).getUserId(), list.get(i).getLogin(), msg, SmsConstants.SMS_TYPE_SYS_NOTICE));
            len++;
            if(len == 1000 || i == list.size() - 1){
                List<SmsMsgInfo> l = new ArrayList<>(telMsgList);
                telMsgList = new ArrayList<>();
                len = 0;
                executorService.execute(() -> SmsSendUtil.sendTelMsgToQueue(l));
            }
        }
    }
}

