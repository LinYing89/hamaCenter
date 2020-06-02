package com.bairock.iot.hamaCenter.utils;

import com.bairock.iot.intelDev.order.OrderBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
}
