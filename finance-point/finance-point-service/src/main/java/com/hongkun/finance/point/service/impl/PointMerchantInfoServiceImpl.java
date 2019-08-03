package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.dao.PointMerchantInfoDao;
import com.hongkun.finance.point.model.PointMerchantInfo;
import com.hongkun.finance.point.service.PointMerchantInfoService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.img.QRCodeUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.hongkun.finance.point.constants.PointConstants.POINT_MERCHANT_STATE_CHECK_SUCCESS;
import static com.hongkun.finance.point.constants.PointConstants.POINT_MERCHANT_STATE_WAIT_CHECK;
import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.yirun.framework.core.commons.Constants.ERROR;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointMerchantInfoServiceImpl.java
 * @Class Name    : PointMerchantInfoServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class PointMerchantInfoServiceImpl implements PointMerchantInfoService {

    private static final Logger logger = LoggerFactory.getLogger(PointMerchantInfoServiceImpl.class);

    /**
     * PointMerchantInfoDAO
     */
    @Autowired
    private PointMerchantInfoDao pointMerchantInfoDao;


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updatePointMerchantInfo(PointMerchantInfo pointMerchantInfo) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updatePointMerchantInfo, 更新积分商户信息, 更新为商户信息: {}", pointMerchantInfo.toString());
        }
        try {
            pointMerchantInfoDao.update(pointMerchantInfo);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新积分商户信息异常, 商户信息: {}, 异常信息: {}", pointMerchantInfo.toString(), e);
            }
            throw new GeneralException("更新商户信息出错,请重试");
        }
    }


    @Override
    public PointMerchantInfo findPointMerchantInfoById(int id) {
        return this.pointMerchantInfoDao.findByPK(Long.valueOf(id), PointMerchantInfo.class);
    }

    @Override
    public List<PointMerchantInfo> findPointMerchantInfoList(PointMerchantInfo pointMerchantInfo) {
        return this.pointMerchantInfoDao.findByCondition(pointMerchantInfo);
    }


    @Override
    public Pager findPointMerchantInfoList(PointMerchantInfo pointMerchantInfo, Pager pager) {
        return this.pointMerchantInfoDao.findByCondition(pointMerchantInfo, pager);
    }

    @Override
    public int findPointMerchantInfoCount(PointMerchantInfo pointMerchantInfo) {
        return this.pointMerchantInfoDao.getTotalCount(pointMerchantInfo);
    }

    @Override
    public Pager findPointMerchantInfoList(PointMerchantInfo pointMerchantInfo, Pager pager, String sqlName) {
        return this.pointMerchantInfoDao.findByCondition(pointMerchantInfo, pager, sqlName);
    }

    @Override
    public Integer findPointMerchantInfoCount(PointMerchantInfo pointMerchantInfo, String sqlName) {
        return this.pointMerchantInfoDao.getTotalCount(pointMerchantInfo, sqlName);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity savePointMerchantInfo(PointMerchantInfo pointMerchantInfo) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: savePointMerchantInfo, 保存积分商户信息, 商户信息: {}", pointMerchantInfo);
        }
        try {
           //step 0:检查reguserId是否唯一
            if (getPointMerchantInfoByUserId(pointMerchantInfo.getRegUserId()) != null) {
                return ResponseEntity.error("已申请或已有商户信息");
            }
            /**
             * step 1:生成商户标识码
             */
            String merchantCode;
            while (StringUtils.isEmpty(merchantCode = createMerchantCode())) {
            }
            pointMerchantInfo.setMerchantCode(merchantCode);
            //设置待审核
            pointMerchantInfo.setState(POINT_MERCHANT_STATE_WAIT_CHECK);
           // step 2:保存商户信息
            pointMerchantInfoDao.save(pointMerchantInfo);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("保存商户信息异常, 商户信息: {}\n异常信息: ", pointMerchantInfo, e);
            }
            throw new GeneralException("保存商户信息出错,请重试");
        }
        return ResponseEntity.SUCCESS;
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity checkPointMerchant(PointMerchantInfo pointMerchantInfo) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: checkPointMerchant, 审核积分商户, 商户信息: {}", pointMerchantInfo);
        }
        try {
            PointMerchantInfo orginInfo = findPointMerchantInfoById(pointMerchantInfo.getId());
            //只处理待审核
            if (orginInfo != null && equelsIntWraperPrimit(orginInfo.getState(), PointConstants.POINT_MERCHANT_STATE_WAIT_CHECK)) {
                if (equelsIntWraperPrimit(pointMerchantInfo.getState(), POINT_MERCHANT_STATE_CHECK_SUCCESS)) {
                    //审核成功生成二维码
                    String qrCode = genCollectionCode(orginInfo.getMerchantCode(), orginInfo.getMerchantName());
                    if (qrCode == null) {
                        return ResponseEntity.error("生成收款码失败,请重试");
                    }
                    pointMerchantInfo.setGatheringUrl(qrCode);
                }
                //处理审核
                updatePointMerchantInfo(pointMerchantInfo);
                return ResponseEntity.SUCCESS;
            }
            return new ResponseEntity(ERROR, "所选的商户不存在或者不是待审核状态");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("审核商户信息失败, 商户信息: {}\n异常信息: ", pointMerchantInfo, e);
            }
            throw new GeneralException("审核商户信息出错,请重试");
        }
    }

   /**
   *  @Description    ：生成商户编码
   *  @Method_Name    ：createMerchantCode
   *
   *  @return java.lang.String
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private String createMerchantCode() {
        String merchantCode = null;
        JedisClusterLock lock = new JedisClusterLock();
        String lockStr = "createMerchantCode_lock";
        try {
            int expire = 10;
            if (lock.lock(lockStr, expire, Constants.LOCK_WAITTIME)) {
                while (true) {
                    merchantCode = "SN" + (System.currentTimeMillis() + "").substring(5, 9);
                    Random random = new Random();
                    merchantCode += random.nextInt(9) + "" + random.nextInt(9);
                    PointMerchantInfo countInfo = PointMerchantInfo.instance;
                    countInfo.setMerchantCode(merchantCode);
                    if (pointMerchantInfoDao.getTotalCount(countInfo) == 0) {
                        return merchantCode;
                    }
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("尝试生成商户编码失败, 获取redis锁失败, 异常信息:\n", e);
            }
        } finally {
            lock.freeLock(lockStr);
        }
        //返回空
        return merchantCode;

    }

    /**
    *  @Description    ：生成并上传商户收款二维码
    *  @Method_Name    ：genCollectionCode
    *  @param merchantCode
    *  @param merchantName
    *  @return java.lang.String
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private String genCollectionCode(String merchantCode, String merchantName) {
        // step 1:生成二维码图片
        // 2、根据商户号+商户名称+hongkunjinfu生成二维码对应的图片
        String content = merchantCode + "+" + merchantName + "+" + "hongkunjinfu";
        File orginFile = null;
        try {
            orginFile = QRCodeUtil.getInstance().encodeURL(content, 200, "whatever.png");
        } catch (Exception e) {
            logger.error("商户通过审核操作, 生成图片出错, merchantCode: {}, merchantName: {}\n异常信息: ",
                    merchantCode, merchantName, e);

        }
        FileInfo unUploadFile = new FileInfo();
        unUploadFile.setFileContent(orginFile);
        unUploadFile.setName(orginFile.getName());
        //step 2:上传阿里云
        FileInfo fileInfo = OSSLoader.getInstance()
                                     .bindingUploadFile(unUploadFile)
                                     .setUseRandomName(true)
                                     .setAllowUploadType(FileType.EXT_TYPE_IMAGE)
                                     .setFileState(FileState.UN_UPLOAD)
                                     .setBucketName(OSSBuckets.HKJF)/*存放平台桶*/
                                     .setFilePath(PointConstants.QR_CORD_FILE_PATH)
                                     .doUpload();

       //step 3:校验文件状态,确定是否上传成功
        if (fileInfo.getFileState() == FileState.SAVED) {
            return fileInfo.getSaveKey();
        }
        return null;
    }

    @Override
    public PointMerchantInfo getMerchantInfo(Integer regUserId) {
        PointMerchantInfo condition = new PointMerchantInfo();
        condition.setRegUserId(regUserId);
        condition.setState(POINT_MERCHANT_STATE_CHECK_SUCCESS);
        List<PointMerchantInfo> list = this.pointMerchantInfoDao.findByCondition(condition);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public PointMerchantInfo getMerchantInfoByCode(String merchantCode) {
        PointMerchantInfo condition = new PointMerchantInfo();
        condition.setMerchantCode(merchantCode);
        condition.setState(POINT_MERCHANT_STATE_CHECK_SUCCESS);
        List<PointMerchantInfo> list = this.pointMerchantInfoDao.findByCondition(condition);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public PointMerchantInfo getPointMerchantInfoByUserId(Integer regUserId) {
        List<PointMerchantInfo> list = this.pointMerchantInfoDao.getPointMerchantInfoByUserId(regUserId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public PointMerchantInfo getCheckMerchantInfo(Integer regUserId) {
        PointMerchantInfo condition = new PointMerchantInfo();
        condition.setRegUserId(regUserId);
        condition.setState(PointConstants.POINT_MERCHANT_STATE_WAIT_CHECK);
        List<PointMerchantInfo> list = this.pointMerchantInfoDao.findByCondition(condition);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public PointMerchantInfo getRecentFailMerchantInfo(Integer regUserId) {
        PointMerchantInfo condition = new PointMerchantInfo();
        condition.setRegUserId(regUserId);
        condition.setState(PointConstants.POINT_MERCHANT_STATE_CHECK_FAIL);
        condition.setSortColumns("create_time desc");
        List<PointMerchantInfo> list = this.pointMerchantInfoDao.findByCondition(condition);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Map<String, Object> getMerchantInfoState(Integer regUserId) {
        Map<String, Object> result = new HashMap(16);
        result.put("state", PointConstants.POINT_MERCHANT_STATE_CHECK_NOT_APPLY);//默认用户的商户状态为4-未申请
        //查询用户是否有待审核或审核成功商户信息
        PointMerchantInfo merchantInfo = this.getPointMerchantInfoByUserId(regUserId);
        if (merchantInfo != null) {
            result.put("state", merchantInfo.getState());
        } else if (this.getRecentFailMerchantInfo(regUserId) != null) {
            result.put("state", PointConstants.POINT_MERCHANT_STATE_CHECK_FAIL);//用户没有待审核和审核成功信息,有审核失败信息,则返回2-审核失败状态
        }

        return result;
    }

    @Override
    public List<Integer> findMerchantIdsByInfo(PointMerchantInfo pointMerchantInfo) {
        return this.pointMerchantInfoDao.findMerchantIdsByInfo(pointMerchantInfo);
    }
}
