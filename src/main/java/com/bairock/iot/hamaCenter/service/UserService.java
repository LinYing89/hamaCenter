package com.bairock.iot.hamaCenter.service;

import java.io.IOException;

import com.bairock.iot.hamalib.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bairock.iot.hamaCenter.exception.UserException;
import com.bairock.iot.hamaCenter.repository.UserRepository;
import com.bairock.iot.hamaCenter.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

	@Autowired
	private UserService userService;
	
	public User findById(long userId) {
		return userService.findById(userId);
	}

	public User findByUsername(String username){
		return userService.findByUsername(username);
	}
}
