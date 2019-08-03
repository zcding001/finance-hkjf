package com.hongkun.finance.web.controller.ros;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.roster.model.RosNotice;
import com.hongkun.finance.roster.service.RosNoticeService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 功能消息通知（短信、邮件、站内信管理）
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.ros
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("rosNoticeController/")
public class RosNoticeController {
    @Reference
    private RosNoticeService rosNoticeService;

    /**
    *  @Description    ：查询通知功能列表
    *  @Method_Name    ：rosNoticeList
    *  @param pager
    *  @param rosNotice
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/5/30
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("rosNoticeList")
    @ResponseBody
    public ResponseEntity<?> rosNoticeList(Pager pager, RosNotice rosNotice){
        return new ResponseEntity<>(Constants.SUCCESS,rosNoticeService.findRosNoticeList(rosNotice,pager));
    }

    /**
    *  @Description    ：为某个功能添加通知邮件/手机号
    *  @Method_Name    ：addRosNotice
    *  @param rosNotice
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/5/30
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("addRosNotice")
    @ResponseBody
    @ActionLog(msg = "添加功能通知, 功能类型type: {args[0].type}, 通知类型: {args[0].noticeWay}, 加入邮件: {args[0].receiveEmail},加入手机号: {args[0].receiveTel}")
    public ResponseEntity<?> addRosNotice(RosNotice rosNotice){
       return  rosNoticeService.addRosNotice(rosNotice);
    }

    /**
    *  @Description    ：修改某个功能通知信息
    *  @Method_Name    ：editRosNotice
    *  @param rosNotice
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/5/30
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("editRosNotice")
    @ResponseBody
    @ActionLog(msg = "添加功能通知, 功能id:{args[0].id} ,功能类型type: {args[0].type}, 通知类型: {args[0].noticeWay}, 加入邮件: {args[0].receiveEmail},加入手机号: {args[0].receiveTel}")
    public ResponseEntity<?> editRosNotice(RosNotice rosNotice){
        return  rosNoticeService.editRosNotice(rosNotice);
    }
    /**
    *  @Description    ：删除某个功能通知
    *  @Method_Name    ：delRosNotice
    *  @param id
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/5/30
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("delRosNotice")
    @ResponseBody
    @ActionLog(msg = "删除功能通知, 功能id: {args[0]}")
    public ResponseEntity<?> delRosNotice(Integer id){
        return  rosNoticeService.delRosNotice(id);
    }
}
