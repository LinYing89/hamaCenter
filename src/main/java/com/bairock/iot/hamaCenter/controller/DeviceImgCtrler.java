package com.bairock.iot.hamaCenter.controller;

import java.util.List;

import com.bairock.iot.hamaCenter.mapper.DeviceImgMapper;
import com.bairock.iot.hamaCenter.utils.ResultEnum;
import com.bairock.iot.hamalib.data.DeviceImg;
import com.bairock.iot.hamalib.data.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/deviceImg")
public class DeviceImgCtrler {

	@Autowired
	private DeviceImgMapper deviceImgMapper;
	
	@GetMapping("/list")
	private String getAllDeviceImg(Model model) {
		List<DeviceImg> list = deviceImgMapper.findAll();
		model.addAttribute("list", list);
		return "device/deviceImgList";
	}
	
	@ResponseBody
	@GetMapping("/checkVersionCode")
	private Result<List<DeviceImg>> checkVersionCode() {
		Result<List<DeviceImg>> result = new Result<>();
		List<DeviceImg> list = deviceImgMapper.findAll();
		result.setData(list);
		result.setCode(ResultEnum.SUCCESS.getCode());
		return result;
	}
}
