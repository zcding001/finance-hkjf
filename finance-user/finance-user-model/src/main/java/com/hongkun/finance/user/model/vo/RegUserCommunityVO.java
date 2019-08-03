package com.hongkun.finance.user.model.vo;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Set;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUserCommunity.java
 * @Class Name    : RegUserCommunity.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserCommunityVO extends BaseModel {

	private static final long serialVersionUID = 1L;

	/*************来源于RegUserCommunity字段***********************/
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;

	/**
	 * 描述: 物业用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private Integer regUserId;

	/**
	 * 描述: communityName
	 * 字段: community_name  VARCHAR(50)
	 */
	private String communityName;

	/**
	 * 描述: '是否自取 0 非自取 1代表 为自取地址',
	 * 字段: community_type  TINYINT(3)
	 */
	private Integer communityType;

	/**
	 * 描述: 小区父id
	 * 字段: p_id  INT(10)
	 * 默认值: 0
	 */
	private Integer pid;

	/**
	 * 描述: 小区状态
	 * 字段: state  INT(2)
	 * 默认值: 1
	 */
	private java.lang.Integer state;

	/*************其他补充字段***********************/
	/**
	 * 物业对应的小区名
	 */
	@Union(forLimitQuery = true)
	private String nickName;

	/**
	 * 父小区名称
	 */
	private String parentCommunityName;

	/**
	 * 限制id
	 */
	private List<Integer> limitIds;

	/**
	 * 重新分配的小区id
	 */

	private Set<Integer>  communitiesIdsNew;

	public RegUserCommunityVO(){
	}

	public RegUserCommunityVO(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public Integer getRegUserId() {
		return this.regUserId;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getCommunityName() {
		return this.communityName;
	}

	public void setCommunityType(Integer communityType) {
		this.communityType = communityType;
	}

	public Integer getCommunityType() {
		return this.communityType;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getPid() {
		return this.pid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getParentCommunityName() {
		return parentCommunityName;
	}

	public void setParentCommunityName(String parentCommunityName) {
		this.parentCommunityName = parentCommunityName;
	}

	public List<Integer> getLimitIds() {
		return limitIds;
	}

	public void setLimitIds(List<Integer> limitIds) {
		this.limitIds = limitIds;
	}

	public Set<Integer> getCommunitiesIdsNew() {
		return communitiesIdsNew;
	}

	public void setCommunitiesIdsNew(Set<Integer> communitiesIdsNew) {
		this.communitiesIdsNew = communitiesIdsNew;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

