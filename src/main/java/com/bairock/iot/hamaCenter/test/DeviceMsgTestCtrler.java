package com.bairock.iot.hamaCenter.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deviceMsg")
public class DeviceMsgTestCtrler {

	@GetMapping("/page")
	private String getDeviceMsgPage() {
		return "test/deviceMsg";
	}
	
	@GetMapping("/padMsg")
	private String getDPadMsgPage() {
		return "test/padMsg";
	}
}
