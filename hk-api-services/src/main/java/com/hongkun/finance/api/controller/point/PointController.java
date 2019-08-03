package com.hongkun.finance.api.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointMerchantFacade;
import com.hongkun.finance.point.facade.PointRecordFacade;
import com.hongkun.finance.point.model.PointMerchantInfo;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.vo.PointVO;
import com.hongkun.finance.point.service.*;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import static com.yirun.framework.core.commons.Constants.ERROR;

/**
 * @Description : 积分处理controller层
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.vip.VipController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/pointController")
public class PointController {

    @Reference
    private PointRecordService pointRecordService;
    @Reference
    private PointCommonService pointCommonService;
    @Reference
    private PointMerchantInfoService pointMerchantInfoService;
    @Reference
    private PointRuleService pointRuleService;
    @Reference
    private PointRecordFacade pointRecordFacade;
    @Reference
    private RegUserService regUserService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private PointMerchantFacade pointMerchantFacade;
    @Reference
    private FinFundtransferService finFundtransferService;
    @Reference
    private PointAccountService pointAccountService;
    @Value(value = "${oss.url.hkjf}")
    private String ossUrl;
    /**判断商户信息是否为图片url地址*/
    private final String URL = "Url";


    /**
     *  @Description    ：获取用户可用积分、可转赠积分、转赠利率
     *  @Method_Name    ：getUserUsablePoint
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/10/17
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserUsablePoint")
    @ResponseBody
    public Map<String,Object> getUserUsablePoint(){
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity<?> result = pointAccountService.getUserPointAndRate(regUser.getId());
        if (result.getResStatus() == ERROR){
            return AppResultUtil.mapOfResponseEntity(result);
        }
        Map<String,Object> pointData = (Map<String, Object>) result.getResMsg();
        Integer point = (Integer) pointData.get("point");
        BigDecimal rate = (BigDecimal) pointData.get("rate");
        //可转赠积分=可用积分-转赠积分手续费,手续费不满1积分按1积分计算
        BigDecimal cost = rate.multiply(BigDecimal.valueOf(point)).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_HALF_UP);
        Integer feePoint = (int)Math.ceil(cost.doubleValue());
        Integer transferablePoint = point - feePoint;
        pointData.put("transferablePoint",transferablePoint);
        return AppResultUtil.successOf(pointData,"preMoneyToPoint");
    }
    /**
     *  @Description    : 获取用户积分记录信息
     *  @Method_Name    : getUserPointRecord
     *  @return
     *  @Creation Date  : 2018年01月02日 下午16:57:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserPointRecord")
    @ResponseBody
    public Map<String,Object> getUserPointRecord(Pager pager){
        RegUser regUser = BaseUtil.getLoginUser();
        PointRecord pointRecord = new PointRecord();
        pointRecord.setRegUserId(regUser.getId());
        pointRecord.setSortColumns("create_time desc");
        pager.setInfiniteMode(true);
        pointRecord.setStateList(Arrays.asList(PointConstants.POINT_STATE_CONFIRMED));
        Pager resultPager = pointRecordService.findPointRecordList(pointRecord, pager);
        return AppResultUtil.successOfListInProperties(resultPager.getData(),
                null,"point","comments","createTime");
    }

    /**
     *  @Description    : 签到送积分
     */
    @RequestMapping("/signInPoint")
    @ResponseBody
    public Map<String,Object> signInPoint(){
        RegUser regUser = BaseUtil.getLoginUser();
        PointVO unSavedPointVO = new PointVO();
        unSavedPointVO.setPoint(1);
        unSavedPointVO.setComments("签到送积分");

        unSavedPointVO.setUserIds(Collections.singletonList(BaseUtil.getLoginUserId()));
        return AppResultUtil.mapOfResponseEntity(pointCommonService.signInPoint(unSavedPointVO, regUser.getId()))
                .addResParameter("point",1);
    }

    /**
     *  @Description    ：判断用户是否已经签到
     *  @Method_Name    ：hasSign
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/10/12
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/hasSign")
    @ResponseBody
    public Map<String,Object> hasSign(){
        RegUser regUser = BaseUtil.getLoginUser();
        return AppResultUtil.mapOfResponseEntity(pointCommonService.hasSign(regUser.getId()));
    }

    /**
     * @return : Map<String,Object>
     * @Description : 获取用户审核成功的商户信息
     * @Method_Name : getMerchantNameByCode
     * @Creation Date  : 2018年03月15日 下午17:11:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getMerchantInfo")
    @ResponseBody
    public Map<String,Object> getMerchantInfo(){
        RegUser regUser = BaseUtil.getLoginUser();
        PointMerchantInfo merchantInfo = pointMerchantInfoService.getMerchantInfo(regUser.getId());
        if (merchantInfo == null){
            return AppResultUtil.errorOfMsg("没有审核成功的商户信息");
        }
        return AppResultUtil.successOfInProperties(merchantInfo,"merchantCode","merchantName");
    }

    /**
     * @param merchantCode 商户编号
     * @return : Map<String,Object>
     * @Description : 根据商户编号获取商户名称
     * @Method_Name : getMerchantNameByCode
     * @Creation Date  : 2018年03月15日 下午17:11:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getMerchantNameByCode")
    @ResponseBody
    public Map<String,Object> getMerchantNameByCode(String merchantCode){
        if(StringUtils.isBlank(merchantCode)){
            AppResultUtil.errorOfMsg("商户编号不能为空！");
        }
        PointMerchantInfo merchantInfo = pointMerchantInfoService.getMerchantInfoByCode(merchantCode);
        if (merchantInfo == null){
            AppResultUtil.errorOfMsg("商户信息不存在！");
        }
        return AppResultUtil.successOfInProperties(merchantInfo,"merchantName");
    }

    /**
     * @param merchantCode 商户编号
     * @param money         支付金额
     * @param payPass       支付密码
     * @param sourceType    平台来源：10-PC,11-IOS,12-ANDROID,13-WAP,14-WX
     * @return : Map<String,Object>
     * @Description : 根据商户编号获取商户名称
     * @Method_Name : pointPayment
     * @Creation Date  : 2018年03月15日 下午17:11:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/pointPayment")
    @ResponseBody
    public Map<String,Object> pointPayment(String merchantCode, BigDecimal money, String payPass, int sourceType){
        if (StringUtils.isBlank(merchantCode)){
            AppResultUtil.errorOfMsg("商户编号不能为空！");
        }
        if (money == null || money.compareTo(BigDecimal.ZERO) < 1){
            return AppResultUtil.errorOfMsg("请输入正确的支付金额！");
        }
        if (StringUtils.isBlank(payPass)){
            return AppResultUtil.errorOfMsg("支付密码不能为空！");
        }
        RegUser regUser = BaseUtil.getLoginUser();
        //判断积分支付用户是否实名
        if (regUser.getIdentify() == 0){
            return AppResultUtil.errorOfMsg(UserConstants.NO_IDENTIFY,"为了保障您的财产安全，请您先实名后再进行积分支付！");
        }
        ResponseEntity result = pointMerchantFacade.pointPayment(regUser.getId(),payPass,merchantCode,money,
                PlatformSourceEnums.typeByValue(sourceType));
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     * @param money         支付金额
     * @return : Map<String,Object>
     * @Description : 金额转换为积分
     * @Method_Name : moneyTransferPoint
     * @Creation Date  : 2018年03月15日 下午17:11:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/moneyTransferPoint")
    @ResponseBody
    public Map<String,Object> moneyTransferPoint(BigDecimal money){
        if (money == null || money.compareTo(BigDecimal.ZERO) < 1){
            return AppResultUtil.errorOfMsg("请输入正确的支付金额！");
        }
        RegUser regUser = BaseUtil.getLoginUser();
        Map<String,Object> result = new HashMap<>(1);
        int point = pointRuleService.moneyToPoint(money);
        //若返回的积分为0
        if (point <= 0){
            return AppResultUtil.errorOfMsg("积分值应大于0！");
        }
        result.put("point",point);
        return AppResultUtil.successOf(result);
    }

    /**
     * @param point              转赠积分数量
     * @param receiveTel         接收人手机号
     * @return : Map<String,Object>
     * @Description : 积分转赠
     * @Method_Name : moneyTransferPoint
     * @Creation Date  : 2018年03月16日 上午09:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/pointTransfer")
    @ResponseBody
    public Map<String,Object> pointTransfer(int point, long receiveTel){
        RegUser regUser = BaseUtil.getLoginUser();
        //判断积分转赠用户是否实名
        if (regUser.getIdentify() == 0){
            return AppResultUtil.errorOfMsg(UserConstants.NO_IDENTIFY,"为了保障您的财产安全，请您先实名后再进行积分转赠！");
        }
        //判断手机号格式是否正确
        ResponseEntity validateTel = ValidateUtil.validateLogin(String.valueOf(receiveTel));
        if (validateTel.getResStatus() == ERROR){
            return AppResultUtil.errorOfMsg(validateTel.getResMsg());
        }
        //判断手机号对应的用户信息是否存在
        RegUser receiveUser = regUserService.findRegUserByLogin(receiveTel);
        if (receiveUser == null){
            return AppResultUtil.errorOfMsg("没有查询到该积分接收人信息！");
        }
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        List<Integer> list = new ArrayList<>();
        list.add(receiveUser.getId());
        ResponseEntity result =  pointRecordFacade.userPointTransfer(regUser.getId(),point,list,regUserDetail.getRealName());
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     * @return : Map<String,Object> {
     *     state:状态：1待审核，3审核成功，4未申请
     * }
     * @Description : 获取用户商户状态
     * @Method_Name : getMerchantInfoState
     * @Creation Date  : 2018年03月16日 上午09:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getMerchantInfoState")
    @ResponseBody
    public Map<String,Object> getMerchantInfoState(){
        RegUser regUser = BaseUtil.getLoginUser();
        //判断用户是否实名
        if (regUser.getIdentify() == 0){
            return AppResultUtil.errorOfMsg(UserConstants.NO_IDENTIFY,"为了保障您的财产安全，请您先实名后再进行商户申请！");
        }
        Map<String,Object> result = pointMerchantInfoService.getMerchantInfoState(regUser.getId());
        return AppResultUtil.successOf(result);
    }


    /**
     * @return : Map<String,Object>
     * @Description : 获取用户待审核商户信息
     * @Method_Name : getCheckMerchantInfo
     * @Creation Date  : 2018年03月16日 上午09:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getCheckMerchantInfo")
    @ResponseBody
    public Map<String,Object> getCheckMerchantInfo(){
        RegUser regUser = BaseUtil.getLoginUser();
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        PointMerchantInfo merchantInfo = pointMerchantInfoService.getCheckMerchantInfo(regUser.getId());
        if (merchantInfo == null){
            return AppResultUtil.errorOfMsg("用户没有待审核的商户信息！");
        }
        Map<String,Object> result = AppResultUtil.successOfInProperties(merchantInfo,"createTime","state",
                "merchantCode","merchantName","businessLicenseUrl","permitUrl","idcardFrontUrl","idcardBackUrl",
                "hygieneLicenseUrl").addResParameter("tel",regUser.getLogin().toString().substring(0,3) + "****" + regUser.getLogin().toString().substring(7)).
                addResParameter("userName","*" + regUserDetail.getRealName().substring(1));
        //给url相关参数地址组装完全
        result.forEach((k,v)->{
            if (k.contains(URL)){
                result.put(k,ossUrl + v);
            }
        });

        return result;
    }

    /**
     * @return : Map<String,Object>
     * @Description : 获取用户最近审核失败商户信息
     * @Method_Name : getRecentFailMerchantInfo
     * @Creation Date  : 2018年03月16日 上午09:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getRecentFailMerchantInfo")
    @ResponseBody
    public Map<String,Object> getRecentFailMerchantInfo(){
        RegUser regUser = BaseUtil.getLoginUser();
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        PointMerchantInfo merchantInfo = pointMerchantInfoService.getRecentFailMerchantInfo(regUser.getId());
        if (merchantInfo == null){
            return AppResultUtil.errorOfMsg("用户没有审核失败的商户信息！");
        }
        Map<String,Object> result = AppResultUtil.successOfInProperties(merchantInfo,"createTime","state","merchantCode"
                ,"userName","tel","merchantName","businessLicenseUrl","permitUrl","idcardFrontUrl","idcardBackUrl",
                "hygieneLicenseUrl","reason").addResParameter("tel",regUser.getLogin().toString().substring(0,3) + "****" + regUser.getLogin().toString().substring(7)).
                addResParameter("userName", "*" + regUserDetail.getRealName().substring(1));
        //给url相关参数地址组装完全
        result.forEach((k,v)->{
            if (k.contains(URL)){
                result.put(k,ossUrl + v);
            }
        });
        return result;
    }

    /**
     * @param isToday            0-查询所有，1-查询今天
     * @param pager               分页参数
     * @return : Map<String,Object>
     * @Description : 获取用户收款信息
     * @Method_Name : getMerchantFundtransferList
     * @Creation Date  : 2018年03月16日 上午09:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getMerchantFundtransferList")
    @ResponseBody
    public Map<String,Object> getMerchantFundtransferList(int isToday,Pager pager){
        RegUser regUser = BaseUtil.getLoginUser();
        FinFundtransfer condition = new FinFundtransfer();
        condition.setRegUserId(regUser.getId());
        condition.setSubCode(TradeTransferConstants.getFundTransferSubCodeByType
                (FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.POINT_MONEY));
        if (isToday == 1){
            condition.setCreateTimeBegin(DateUtils.getFirstTimeOfDay());
            condition.setCreateTimeEnd(DateUtils.getLastTimeOfDay());
        }
        condition.setSortColumns("create_time desc");
        Map<String,Object> result = finFundtransferService.findPageAndIncomeTotalMoney(condition,pager,"111");
        List<FinFundtransfer> list = (List<FinFundtransfer>) ((Pager)result.get("pager")).getData();
        result.put("list",AppResultUtil.successOfListInProperties(list,"过滤出相应的字段","transMoney","createTime")
                .get("dataList"));
        result.remove("pager");
        return AppResultUtil.successOf(result);
    }

    /**
     * @param merchantInfo            商户信息
     * @return : Map<String,Object>
     * @Description : 获取用户收款信息
     * @Method_Name : saveMerchantInfo
     * @Creation Date  : 2018年03月16日 上午09:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/saveMerchantInfo")
    @ResponseBody
    public Map<String,Object> saveMerchantInfo(PointMerchantInfo merchantInfo){
        RegUser regUser = BaseUtil.getLoginUser();
        merchantInfo.setRegUserId(regUser.getId());
        return AppResultUtil.mapOfResponseEntity(pointMerchantInfoService.savePointMerchantInfo(merchantInfo));
    }

    /**
     * @param picFlow            图片流
     * @param picType            图片类型：1-营业执照，2-开户许可证，3-法人身份证正面，4-法人身份证反面，5-卫生许可证
     * @return : Map<String,Object>
     * @Description : 商户信息图片上传
     * @Method_Name : uploadMerchantImage
     * @Creation Date  : 2018年03月16日 下午15:25:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/uploadMerchantImage")
    @ResponseBody
    public Map<String,Object> uploadMerchantImage(String picFlow,int picType){
        if (StringUtils.isBlank(picFlow)){
            return AppResultUtil.errorOfMsg("图片流不能为空！");
        }
        RegUser regUser = BaseUtil.getLoginUser();
        //处理图片流
        //解码
        byte[] decode = Base64.decode(picFlow.getBytes());
        InputStream input = new ByteArrayInputStream(decode);
        //图片名称
        String fileName = System.currentTimeMillis() + "-" + regUser.getId() + "-" +picType+".jpg";
        FileInfo fileInfo = OSSLoader.getInstance()
                .setUseRandomName(false)
                .setAllowUploadType(FileType.EXT_TYPE_IMAGE)
                .bindingUploadFile(new FileInfo(input))
                .setFileState(FileState.UN_UPLOAD)
                .setBucketName(OSSBuckets.HKJF)
                .setFilePath("/point/merchant/")
                .setFileName(fileName)
                .doUpload();
        if (!fileInfo.getFileState().equals(FileState.SAVED)){
            return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
        }
        Map<String,Object> result = new HashMap<>(1);
        result.put("picUrl",fileInfo.getSaveKey());
        return AppResultUtil.successOf(result);
    }

    /**
     * @param picUrl            图片路径
     * @return : Map<String,Object>
     * @Description : 删除用户已上传的商户信息图片
     * @Method_Name : delMerchantImage
     * @Creation Date  : 2018年03月16日 下午15:25:55
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/delMerchantImage")
    @ResponseBody
    public Map<String,Object> delMerchantImage(String picUrl){
        if (StringUtils.isBlank(picUrl)){
            return AppResultUtil.errorOfMsg("图片地址不能为空！");
        }
        int index = picUrl.indexOf("point/");
        String fileKey = picUrl.substring(index,picUrl.length());
        FileInfo delFile = new FileInfo();
        delFile.setSaveKey(fileKey);
        FileInfo fileInfo = OSSLoader.getInstance()
                .bindingUploadFile(delFile)
                .setFileState(FileState.SAVED)
                .setBucketName(OSSBuckets.HKJF)
                .doDelete();
        if (!fileInfo.getFileState().equals(FileState.DELETE)){
            return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
        }
        return AppResultUtil.successOfMsg("删除成功");
    }

}
