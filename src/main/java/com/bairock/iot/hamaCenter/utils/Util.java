package com.bairock.iot.hamaCenter.utils;

import com.bairock.iot.hamaCenter.exception.MyException;
import com.bairock.iot.hamaCenter.jwt.JwtUtil;
import com.bairock.iot.hamalib.order.OrderBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;

public class Util {
	
	public static String orderBaseToString(OrderBase ob) {
		ObjectMapper om = new ObjectMapper();
		String order = "";
		try {
			order = om.writeValueAsString(ob);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return order;
	}

	public static Long getUserId(HttpServletRequest request){
		String token = request.getHeader("token");
		if(null == token){
			throw new MyException(ResultEnum.TOKEN_NULL);
		}
		Long userId = JwtUtil.getUserId(token);
		if(null == userId){
			throw new MyException(ResultEnum.TOKEN_FORMAT_ERR);
		}
		return userId;
	}
	
}
