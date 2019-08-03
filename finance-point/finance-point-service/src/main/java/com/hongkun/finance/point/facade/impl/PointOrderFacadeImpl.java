package com.hongkun.finance.point.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointOrderFacade;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointActivityRecord;
import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.hongkun.finance.point.model.vo.PointOrderVO;
import com.hongkun.finance.point.service.*;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.model.StateList;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.ValidateResponsEntityUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hongkun.finance.point.constants.PointConstants.*;
import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.hongkun.finance.user.utils.BaseUtil.redisLock;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static com.yirun.framework.core.utils.BeanPropertiesUtil.getLimitConditions;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.impl.PointOrderFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class PointOrderFacadeImpl implements PointOrderFacade {

    private static final Logger logger = LoggerFactory.getLogger(PointOrderFacadeImpl.class);

    private static final ValidateResponsEntityUtil validateResponsEntityUtil = new ValidateResponsEntityUtil(logger);

    @Reference
    private RegUserService userService;

    @Autowired
    private PointProductOrderService productOrderService;

    @Autowired
    private PointProductService productService;

    @Autowired
    private PointAccountService pointAccountService;

    @Autowired
    private PointCommonService pointCommonService;

    @Autowired
    private PointRecordService pointRecordService;
    @Autowired
    private PointActivityRecordService  pointActivityRecordService;

    @Override
    public Pager listPointOrder(PointOrderQuery pointProductOrder, Pager pager) {
        StateList stateList = getLimitConditions(pointProductOrder, UserVO.class, userService::findUserIdsByUserVO);
        if (!stateList.isProceed()) {
            return pager;
        }
        pointProductOrder.setSortColumns("modify_time desc");
        pointProductOrder.setLimitIds(stateList);
        Pager result = productOrderService.listPointOrder(pointProductOrder, pager);
        if (!BaseUtil.resultPageHasNoData(result)) {
            List<PointOrderVO> orderVos = result.getData().stream().map(order -> {
                PointProductOrder productOrder = (PointProductOrder) order;
                //赋值订单属性
                PointOrderVO pointOrderVO = BeanPropertiesUtil.mergeAndReturn(new PointOrderVO(), productOrder);
                //处理订单视图属性
                UserVO regUserTelAndRealName = userService.findRegUserTelAndRealNameById(productOrder.getRegUserId());
                pointOrderVO.setRealName(regUserTelAndRealName.getRealName());
                pointOrderVO.setLogin(String.valueOf(regUserTelAndRealName.getLogin()));

                String address = productOrder.getAddress();
                if (StringUtils.isEmpty(address)) {
                    if (BaseUtil.equelsIntWraperPrimit(pointOrderVO.getSendWay(),3)) {
                        pointOrderVO.setConsignee("无");
                    }else{
                        pointOrderVO.setConsignee("地址信息缺失");
                    }

                } else {
                    String[] recevicorInfo = StringUtils.split(address, ",");
                    int sendWayExpressLegth = 4;
                    if (recevicorInfo.length >= sendWayExpressLegth/*选用收货地址的订单必然大于4*/) {
                        List<String> addressList = new ArrayList(Arrays.asList(recevicorInfo));
                        //设置收货人
                        pointOrderVO.setConsignee(addressList.get(2));
                        pointOrderVO.setConsigneeTel(addressList.get(addressList.size() - 1));
                        //设置电话
                        addressList.remove(addressList.size() - 1);
                        addressList.remove(2);
                        //设置收货地址
                        pointOrderVO.setAddress(StringUtils.join(addressList, ","));
                        //设置收货方式
                        reSetSendWayIfNeeded(pointOrderVO,2);
                    } else {
                        //选用门店方式，或者是兑换码商品
                        pointOrderVO.setConsignee(pointOrderVO.getRealName());
                        pointOrderVO.setConsigneeTel(pointOrderVO.getLogin());
                        //设置收货方式
                        reSetSendWayIfNeeded(pointOrderVO,1);
                    }
                }
                return pointOrderVO;
            }).collect(Collectors.toList());
            result.setData(orderVos);

        }
        return result;
    }

    private void reSetSendWayIfNeeded(PointOrderVO pointOrderVO, int i) {
        if (BaseUtil.equelsIntWraperPrimit(pointOrderVO.getSendWay(),0)) {
            pointOrderVO.setSendWay(i);
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity cancelOrder(Integer orderId, Integer adminId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: cancelOrder, 取消积分订单, 订单ID: {}, 操作员ID: {}", orderId, adminId);
        }
        try {
            //锁订单
            ResponseEntity result = (ResponseEntity) redisLock(POINT_CHECK_ORDER_LOCK_KEY + orderId,
                    orderId,
                    "审核订单申请redis锁失败, orderId: " + orderId,
                    (odId) -> {
                        PointProductOrder unCheckOrder = productOrderService.findPointProductOrderById((Integer) odId);
                        unCheckOrder.setModifyTime(null);
                        //前置状态为订单取消状态
                        unCheckOrder.setState(POINT_ORDER_STATE_CANCEL);
                        return rollBackOrder(unCheckOrder, adminId);

                    }
            );
            //rollBackOrder里面涉及到多个更新，验证以回滚
            return (ResponseEntity) validateResponsEntityUtil.validate(result);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("取消积分订单异常, 订单ID: {}, 操作员ID: {}\n异常信息: ", orderId, adminId, e);
            }
            throw new GeneralException("取消积分订单失败,请重试");
        }

    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity updateCourierNo(PointProductOrder productOrder, Integer adminId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updateCourierNo, 修改积分订单快递单号, 订单信息: {}, 操作员ID: {}", productOrder, adminId);
        }
        try {
            return (ResponseEntity) redisLock(POINT_CHECK_ORDER_LOCK_KEY + productOrder.getId(),
                    productOrder.getId(),
                    "审核订单申请redis锁失败, 订单信息: " + productOrder,
                    (orderId) -> {
                        //修改订单号
                        productOrder.setModifyTime(null);
                        productOrder.setState(POINT_ORDER_STATE_MERCHANT_HANDLE_COMPLETE);
                        productOrder.setCourierNo(productOrder.getCourierNo());
                        productOrder.setModifyUserId(adminId);
                        productOrderService.updatePointProductOrder(productOrder);
                        return new ResponseEntity(SUCCESS, "修改快递单号成功");
                    });
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("修改积分订单快递单号失败, 订单信息: {}, 操作员ID: {}\n异常信息: ", productOrder, adminId, e);
            }
            throw new GeneralException("取消积分订单失败,请重试");
        }

    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity createOrderInfo(PointProductOrder pointProductOrder, RegUser loginUser) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: createOrderInfo, 生成积分订单, 订单信息: {}, 当前用户: {}", pointProductOrder, loginUser);
        }
        //设置创建人等信息
        Integer currentUserId = loginUser.getId();
        pointProductOrder.setCreateUserId(currentUserId);
        pointProductOrder.setModifyUserId(currentUserId);
        pointProductOrder.setRegUserId(currentUserId);
        try {
            return (ResponseEntity) Optional.ofNullable(
                    // (锁订单)lockKey：POINT_CREATE_ORDER_LOCK_KEY+当前用户id
                    redisLock(POINT_CREATE_ORDER_LOCK_KEY + currentUserId,
                            pointProductOrder,
                            "用户下单申请用户锁失败, 用户: " + loginUser + "订单信息: " + pointProductOrder,
                            (order) -> {
                                //(锁商品)，同一时间保证库存数量 lockKey：POINT_PRODUCT_LOCK_KEY+商品id
                                return redisLock(POINT_PRODUCT_LOCK_KEY + pointProductOrder.getProductId(),
                                        order,
                                        "锁积分商品申请锁失败, 商品id: " + pointProductOrder.getProductId(),
                                        (finalOrder) -> {
                                            PointProductOrder currentOrder = (PointProductOrder) finalOrder;
                                            //step 1:验证订单前置条件
                                            PointProduct product = productService.findPointProductById(currentOrder.getProductId());
                                            if (PointConstants.ON_SALE != product.getState()) {
                                                return ResponseEntity.error("商品不是待售状态");
                                            }
                                            //验证库存是否充足
                                            if (product.getNumber() <= 0 || (currentOrder.getNumber() > product.getNumber())) {
                                                return ResponseEntity.error("库存数量不足");
                                            }
                                           // step 2:下单，扣除库存，扣除积分
                                            //计算订单总共金额
                                            currentOrder.setPoint(product.getPoint() * currentOrder.getNumber());
                                            //判断是否是特殊商品(目前：限时兑换)，如果是限时兑换，需要做对应的转换
                                            if (INTEGER_ONE.equals(product.getFlashSale())) {
                                                //转换为折扣价格
                                                currentOrder.setPoint(product.getDiscountPoint() * currentOrder.getNumber());
                                            }

                                            //转换积分价值
                                            currentOrder.setWorth(pointCommonService.pointToMoney(currentOrder.getPoint()));
                                            product.setNumber(-currentOrder.getNumber());
                                            product.setSalesCount(currentOrder.getNumber());

                                            //保存订单信息
                                            productOrderService.insertPointProductOrder(currentOrder);

                                            //生成积分记录
                                            PointRecord unSavedRecord = transferOrderSateToRecord(currentOrder);
                                            unSavedRecord.setPoint(-currentOrder.getPoint());
                                            //设置积分已经确认
                                            unSavedRecord.setState(PointConstants.POINT_STATE_CONFIRMED);
                                            unSavedRecord.setCreateUserId(currentUserId);
                                            unSavedRecord.setModifyUserId(currentUserId);
                                            unSavedRecord.setBusinessId(currentOrder.getId());
                                            unSavedRecord.setComments("积分兑换商品");

                                            //记录积分操作
                                            pointRecordService.insertPointRecord(unSavedRecord);

                                            if (logger.isInfoEnabled()) {
                                                logger.info("生成积分订单扣除积分操作, 用户ID: {}, 积分值: {}", currentOrder.getRegUserId(), currentOrder.getPoint());
                                            }
                                            //更新商品的库存信息
                                            productService.updatePointProduct(product);
                                            //如果是活动商品，则更新销量
                                            if(product.getFlashSale() == 1){
                                                PointActivityRecord pointActivityRecord=new PointActivityRecord();
                                                pointActivityRecord.setProductId(product.getId());
                                                pointActivityRecord.setSortColumns("id desc");
                                                List<PointActivityRecord> pointList = pointActivityRecordService.findPointActivityRecordList(pointActivityRecord);
                                                PointActivityRecord newPointActivityRecord=new PointActivityRecord();
                                                newPointActivityRecord.setId(pointList.get(0).getId());
                                                newPointActivityRecord.setSales(1);
                                                pointActivityRecordService.updatePointActivityRecord(newPointActivityRecord);
                                            }

                                            //(锁账户)，此时账户积分收到保护
                                            return redisLock(POINT_ACCOUNT_LOCK_KEY + currentUserId, currentOrder,
                                                    "锁用户账户积分失败, 用户id: " + currentUserId,
                                                    (cd) -> {
                                                        PointAccount userAccount = pointAccountService.findPointAccountByRegUserId(currentUserId);
                                                        //验证用户积分是否充足
                                                        if (userAccount.getPoint() < currentOrder.getPoint()) {
                                                            pointProductOrder.setErrorMsg("积分不足");
                                                            throw new BusinessException("point not enough");
                                                        }
                                                        //已经在sql中处理，只需要减去或者增加对应的金额即可
                                                        userAccount.setPoint(-currentOrder.getPoint());
                                                        //更新用户的积分账户
                                                        pointAccountService.updatePointAccount(userAccount);
                                                        //返回执行成功
                                                        return new ResponseEntity<>(SUCCESS, "订单创建成功");
                                                    });
                                        }
                                );
                            })).orElse(ResponseEntity.error("订单处理异常"));
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("生成积分订单失败, 订单信息: {}, 当前用户: {}\n异常信息: ", pointProductOrder, loginUser, e);
            }
            if(StringUtils.isNotBlank(pointProductOrder.getErrorMsg())){
                throw new GeneralException(pointProductOrder.getErrorMsg());
            }else{
                throw new GeneralException("生成积分订单失败,请重试");
            }
        }
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity checkOrder(List<Integer> orderIds, Integer state, Integer userId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: checkOrder, 审核积分订单, 订单IDS: {}, 审核为状态: {},审核用户ID: {}", orderIds, state, userId);
        }
        final int[] successCount = {0};
        try {
            //锁订单
            orderIds.forEach(orderId -> {
                //(锁订单)
                ResponseEntity result = (ResponseEntity) redisLock(POINT_CHECK_ORDER_LOCK_KEY + orderId,
                        orderId,
                        "审核订单申请锁失败",
                        (odId) -> {
                            PointProductOrder unCheckOrder = productOrderService.findPointProductOrderById((Integer) odId);
                            //前置状态为订单待审核状态
                            if (equelsIntWraperPrimit(unCheckOrder.getState(), POINT_ORDER_STATE_UN_CHECK)) {
                                if (equelsIntWraperPrimit(state, POINT_ORDER_STATE_MERCHANT_HANDING)) {
                                    //商家处理中:代表审核通过订单
                                    unCheckOrder.setModifyTime(null);
                                    unCheckOrder.setState(state);
                                    productOrderService.updatePointProductOrder(unCheckOrder);
                                    return new ResponseEntity(SUCCESS, "审核成功");
                                }
                                if (equelsIntWraperPrimit(state, POINT_ORDER_STATE_CHECK_REJECT)) {
                                    //审核拒绝:审核不通过订单
                                    unCheckOrder.setState(state);
                                    return rollBackOrder(unCheckOrder, userId);
                                }
                            }
                            return ResponseEntity.ERROR;
                        }
                );
                if (BaseUtil.isResponseSuccess(result)) {
                    successCount[0]++;
                }
            });
            return new ResponseEntity(SUCCESS, "审核订单完成，共审核：" + orderIds.size() + "个订单," + "成功：" + successCount[0] + "个");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("审核积分订单失败, 订单IDS: {}, 审核为状态: {},审核用户ID: {}\n异常信息: ", orderIds, state, userId, e);
            }
            throw new GeneralException("审核积分订单失败,请重试");
        }
    }

    /**
     * 回滚订单业务逻辑：
     * 回滚积分,回滚库存,更新订单状态
     *
     * @param unCheckOrder
     * @param adminId
     * @return
     */
    private ResponseEntity rollBackOrder(PointProductOrder unCheckOrder, Integer adminId) {
        //继续更订单的标识
        Boolean continueUpdateOrder;
        //锁账户
        Boolean continueUpdateProduct = (Boolean) redisLock(POINT_ACCOUNT_LOCK_KEY + unCheckOrder.getRegUserId(), null, "审核订单-申请用户锁失败",
                (whatever) -> {
                    PointAccount userAccount = pointAccountService.findPointAccountByRegUserId(unCheckOrder.getRegUserId());
                    userAccount.setPoint(unCheckOrder.getPoint());
                    if (pointAccountService.updatePointAccount(userAccount) > 0) {
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                });
        if (continueUpdateProduct) {
            //锁商品
            continueUpdateOrder = (Boolean) redisLock(POINT_PRODUCT_LOCK_KEY + unCheckOrder.getProductId(), null, "审核订单-申请商品锁失败",
                    (whatever) -> {
                        PointProduct pointProduct = productService.findPointProductById(unCheckOrder.getProductId());
                        pointProduct.setNumber(unCheckOrder.getNumber());
                        pointProduct.setSalesCount(-unCheckOrder.getNumber());
                        unCheckOrder.setName(pointProduct.getName());
                        if (productService.updatePointProduct(pointProduct) > 0) {
                            return Boolean.TRUE;
                        }
                        return Boolean.FALSE;
                    });
            //更新订单状态
            if (continueUpdateOrder) {
                if (productOrderService.updatePointProductOrder(unCheckOrder) > 0) {
                    //生成积分记录
                    PointRecord unSavedRecord = transferOrderSateToRecord(unCheckOrder);
                    unSavedRecord.setPoint(unCheckOrder.getPoint());
                    unSavedRecord.setCreateUserId(adminId);
                    unSavedRecord.setModifyUserId(adminId);
                    //设置积分状态为已经确认
                    unSavedRecord.setState(PointConstants.POINT_STATE_CONFIRMED);
                    unSavedRecord.setBusinessId(unCheckOrder.getId());
                    //记录积分操作
                    unSavedRecord.setComments("积分退还");
                    pointRecordService.insertPointRecord(unSavedRecord);
                    //发送站内信
                    sendMsgToUser(unCheckOrder.getRegUserId(),unCheckOrder.getPoint(),unCheckOrder.getName());
                    if (logger.isInfoEnabled()) {
                        logger.info("回滚积分订单回退积分操作,用户ID:{},积分值:{}", unCheckOrder.getRegUserId(), unCheckOrder.getPoint());
                    }
                    return new ResponseEntity(SUCCESS, "审核成功");
                }
                if (logger.isInfoEnabled()) {
                    logger.error("回滚订单失败：步骤：更新订单信息失败,订单信息{}", unCheckOrder);
                }
            } else {
                if (logger.isInfoEnabled()) {
                    logger.error("回滚订单失败：步骤：更新商品信息失败,订单信息{}", unCheckOrder);
                }
            }

        } else {
            if (logger.isInfoEnabled()) {
                logger.error("回滚订单失败：步骤：更新积分账户失败,订单信息{}", unCheckOrder);
            }
        }
        return ResponseEntity.error("取消订单失败");
    }
    /**
     *  @Description    : 积分兑换，审核不通过，发送站内信
     *  @Method_Name    : sendMsgToUser;
     *  @param userId 用户ID
     *  @param pointValue 积分值
     *  @param name 商品名称
     *  @return         : void;
     *  @Creation Date  : 2018年10月17日 上午10:02:54;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    public void sendMsgToUser(Integer userId,Integer pointValue,String name){
        try{
            String msg = SmsMsgTemplate.MSG_USER_POINT_CHANGE_SUCCESS.getMsg();
            SmsSendUtil.sendSmsMsgToQueue(
                    new SmsWebMsg(userId, SmsMsgTemplate.MSG_USER_POINT_CHANGE_SUCCESS.getTitle(), msg,
                            SmsConstants.SMS_TYPE_NOTICE, new String[] {name, String.valueOf(pointValue) }));
        }catch(Exception e){
            logger.error("积分订单回退积分操作异常, 操作用户ID: {}, 积分值: {}",userId,pointValue,e);
        }
    }

   /***
   *  @Description    ：把积分订单转换为积分记录
   *  @Method_Name    ：transferOrderSateToRecord
   *  @param currentOrder
   *  @return com.hongkun.finance.point.model.PointRecord
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private PointRecord transferOrderSateToRecord(PointProductOrder currentOrder) {
        PointRecord record = new PointRecord();
        record.setRegUserId(currentOrder.getRegUserId());
        record.setBusinessId(currentOrder.getId());
        record.setPlatform(1);
        record.setBusinessId(currentOrder.getId());
        record.setType(POINT_TYPE_CONVERT);
        record.setWorth(currentOrder.getWorth());
        record.setRealWorth(currentOrder.getWorth());
        record.setState(NumberUtils.INTEGER_ZERO);
        return record;
    }

}

