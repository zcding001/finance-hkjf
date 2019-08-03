package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.System.out;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-user.xml"})
public class RegUserFriendsServiceImplTest {
	@Reference
	private RegUserFriendsService regUserFriendsService;
	@Reference
	private RegUserService regUserService;
	@Test
	public void initUserFriends(){
		regUserFriendsService.initUserFriends(3);
	}

	@Test
	public void testFindUserSimpleVoByIdList(){
		Set<Integer> list = new HashSet<>();
		list.add(1005);
		list.add(1007);
		Map<Integer, UserSimpleVo> result = regUserService.findUserSimpleVoByIdList(list);
		out.println(JSON.toJSONString(result));
	}
}
