package com.markerhub.demo;

import com.markerhub.demo.entity.User;
import com.markerhub.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cn.hutool.crypto.SecureUtil.md5;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	void contextLoads() {
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
	}

	@Test
	public void testInsert() {
		User user = new User();
		user.setUsername("张三");
		user.setEmail("150978421@qq.com");
		user.setPassword("111111");
		user.setStatus(0);
		int result = userMapper.insert(user);
		System.out.println(result);
		System.out.println(user);
	}

	@Test
	public void testupdate() {
		User user = new User();
		user.setId(4L);
		user.setPassword(md5("111111"));
		int i = userMapper.updateById(user);
		System.out.println(i);

	}

}
