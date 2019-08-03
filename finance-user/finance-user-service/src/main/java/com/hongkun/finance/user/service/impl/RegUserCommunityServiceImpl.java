package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.dao.RegUserCommunityDao;
import com.hongkun.finance.user.dao.RegUserDao;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserCommunity;
import com.hongkun.finance.user.model.vo.OfflineStoreVO;
import com.hongkun.finance.user.model.vo.RegUserCommunityVO;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserCommunityService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.annotation.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.constants.UserConstants.COMMUNITY_TYPE_NOT_SELF_PICK_UP;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.RegUserCommunityServiceImpl.java
 * @Class Name    : RegUserCommunityServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class RegUserCommunityServiceImpl implements RegUserCommunityService {

    private static final Logger logger = LoggerFactory.getLogger(RegUserCommunityServiceImpl.class);

    /**
     * RegUserCommunityDAO
     */
    @Autowired
    private RegUserCommunityDao regUserCommunityDao;

    @Autowired
    private RegUserDao regUserDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public Integer insertRegUserCommunity(RegUserCommunity regUserCommunity) {
        return this.regUserCommunityDao.save(regUserCommunity);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertRegUserCommunityBatch(List<RegUserCommunity> list) {
        this.regUserCommunityDao.insertBatch(RegUserCommunity.class, list);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertRegUserCommunityBatch(List<RegUserCommunity> list, int count) {
        if (logger.isDebugEnabled()) {
            logger.debug("default batch insert size is " + count);
        }
        this.regUserCommunityDao.insertBatch(RegUserCommunity.class, list, count);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateRegUserCommunity(RegUserCommunity regUserCommunity) {
        this.regUserCommunityDao.update(regUserCommunity);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateRegUserCommunityBatch(List<RegUserCommunity> list, int count) {
        this.regUserCommunityDao.updateBatch(RegUserCommunity.class, list, count);
    }

    @Override
    public RegUserCommunity findRegUserCommunityById(int id) {
        return this.regUserCommunityDao.findByPK(Long.valueOf(id), RegUserCommunity.class);
    }

    @Override
    public List<RegUserCommunity> findRegUserCommunityList(RegUserCommunity regUserCommunity) {
        return this.regUserCommunityDao.findByCondition(regUserCommunity);
    }


    @Override
    public Pager findRegUserCommunityList(RegUserCommunity regUserCommunity, Pager pager) {
        return this.regUserCommunityDao.findByCondition(regUserCommunity, pager);
    }

    @Override
    public int findRegUserCommunityCount(RegUserCommunity regUserCommunity) {
        return this.regUserCommunityDao.getTotalCount(regUserCommunity);
    }

    @Cache(prefix = "community_list", key = "#0")
    @Override
    public List<Map<String, Object>> findCommunityDicDataList(Integer regUserId) {
        return this.regUserCommunityDao.findCommunityDicDataList(regUserId);
    }

    @Override
    public List<OfflineStoreVO> loadOfflineStoreAddress() {
        List<RegUserCommunityVO> unGroupedCommunities = regUserCommunityDao.loadOfflineStoreAddress();
        if (!BaseUtil.collectionIsEmpty(unGroupedCommunities)) {
            Map<String, List<RegUserCommunityVO>> groupedCommunities = unGroupedCommunities.stream().collect(Collectors.groupingBy(e -> e.getNickName()));
            List<OfflineStoreVO> returnVO = new ArrayList<>();
            groupedCommunities.forEach((key,value)->{
                OfflineStoreVO vo=new OfflineStoreVO();
                vo.setTenementName(key);
                //设置线下门店的list
                vo.setOfflineStoreName(value.stream().map(RegUserCommunityVO::getCommunityName).collect(Collectors.toList()));
                returnVO.add(vo);
            });
            return returnVO;
        }

        return Collections.emptyList();
    }

    @Override
    public Pager findCommunityList(RegUserCommunityVO communityVO, Pager pager) {
        communityVO.setQueryColumnId("queryCommunityVO");
        //查询限制id
        communityVO.setLimitIds(BeanPropertiesUtil.getLimitConditions(communityVO, UserVO.class,regUserDao::findUserIdsByUserVO));
        //执行查询
        return regUserCommunityDao.findCommunityList(communityVO, pager);
    }

    @Override
    public ResponseEntity deleteCommunityOnCascade(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: deleteCommunityOnCascade, 级联删除小区信息, 小区ID: {}", id);
        }
        try {
            regUserCommunityDao.delectCommunityOnCascade(id);
            return ResponseEntity.SUCCESS;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("级联删除小区信息, 小区ID: {}\n异常信息: ", id, e);
            }
            throw new GeneralException("级联删除小区信息失败,请重试");
        }
    }

    @Override
    public List<RegUser> findUserTypeTenementNoPage() {
        RegUser queryUser = new RegUser();
        queryUser.setType(UserConstants.USER_TYPE_TENEMENT);
        queryUser.setQueryColumnId("idAndName");
        return regUserDao.findByCondition(queryUser);
    }

    @Override
    public List<RegUserCommunity> findTenementsCommunity(Integer id) {
        RegUserCommunity queryCommunity = new RegUserCommunity();
        queryCommunity.setRegUserId(id);
        queryCommunity.setCommunityType(COMMUNITY_TYPE_NOT_SELF_PICK_UP)/*只查询非自提点*/;
        queryCommunity.setQueryColumnId("idAndName");
        return regUserCommunityDao.findByCondition(queryCommunity);
    }

    @Override
    public List<RegUserCommunity> findCommunityAvailable() {
        return regUserCommunityDao.findCommunityAvailable();
    }

    @Override
    public ResponseEntity bindCommunityToTenement(RegUserCommunityVO communityVO) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: bindCommunityToTenement, 绑定小区到物业, 小区信息: {}", communityVO);
        }
        try {
            Set<Integer> communitiesIdsNew = communityVO.getCommunitiesIdsNew();
            //查询已经绑定的小区关系
            RegUserCommunity queryCoummunity = new RegUserCommunity();
            queryCoummunity.setRegUserId(communityVO.getRegUserId());
            queryCoummunity.setCommunityType(COMMUNITY_TYPE_NOT_SELF_PICK_UP);/*只处理小区*/
            queryCoummunity.setQueryColumnId("id");
            List<Integer> orginRels = regUserCommunityDao.findByCondition(queryCoummunity).stream().map(RegUserCommunity::getId).collect(Collectors.toList());

            //校验是否做了新的绑定操作
            Set orginRelsSet = new HashSet(orginRels);
            if(orginRelsSet.containsAll(communitiesIdsNew) && communitiesIdsNew.containsAll(orginRelsSet)){
                return new ResponseEntity(ERROR, "WARN");
            }

            List<Integer> communityRelShouldDelete/*需要被解绑的小区关系id*/ = new ArrayList<>();
            if (!BaseUtil.collectionIsEmpty(orginRels)) {
                orginRels.stream().forEach((oid)->{boolean ignoreState=communitiesIdsNew.contains(oid) ? communitiesIdsNew.remove(oid) : communityRelShouldDelete.add(oid);});
            }

            //删除不在此次分析的关系
            if (!BaseUtil.collectionIsEmpty(communityRelShouldDelete)) {
                regUserCommunityDao.unBindCommunity(communityRelShouldDelete);
            }
            //插入新的关系
            if (!BaseUtil.collectionIsEmpty(communitiesIdsNew)) {
                communityVO.setCommunitiesIdsNew(communitiesIdsNew);
                regUserCommunityDao.bindCommunityToTenument(communityVO);
            }
            return new ResponseEntity(SUCCESS, "小区物业重新绑定成功");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("绑定小区到物业, 小区信息: {}\n异常信息: ", communityVO, e);
            }
            throw new GeneralException("绑定小区到物业失败,请重试");
        }
    }

    @Override
    public List<Map<String, Object>> loadProperties() {
        return  regUserDao.findPropertyDicDataList();
    }

    @Override
    public List<Map<String, Object>> loadOfflineStoreAddressByPropertyId(Integer propertyId) {
        return regUserCommunityDao.findCommunityDicDataList(propertyId);
    }

	@Override
	public List<RegUserCommunity> findAllCommunityList() {
		return regUserCommunityDao.findAllCommunityList();
	}
}
