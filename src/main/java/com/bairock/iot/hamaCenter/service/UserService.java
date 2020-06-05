package com.bairock.iot.hamaCenter.service;

import com.bairock.iot.hamaCenter.mapper.UserMapper;
import com.bairock.iot.hamalib.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public User findById(long userId) {
		return userMapper.findById(userId);
	}

	public Long findIdByUsername(String username){
		return userMapper.findIdByUsername(username);
	}

	public User findByUsername(String username){
		return userMapper.findByUsername(username);
	}
}
