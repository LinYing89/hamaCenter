package com.bairock.iot.hamaCenter.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bairock.iot.hamaCenter.utils.Result;
import com.bairock.iot.hamaCenter.utils.ResultUtil;
import com.bairock.iot.hamaCenter.vo.DevicesVO;
import com.bairock.iot.hamalib.device.Device;
import com.bairock.iot.hamalib.device.devcollect.DevCollect;
import com.bairock.iot.hamalib.user.DevGroup;
import com.bairock.iot.hamalib.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bairock.iot.hamaCenter.service.DevGroupService;
import com.bairock.iot.hamaCenter.service.DeviceService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceController {

	@Autowired
	private DevGroupService devGroupService;
	@Autowired
	private DeviceService deviceService;
	
	@GetMapping("/deviceGroupId/{devGroupId}")
	public Result<?> findDevices(HttpServletRequest request, @PathVariable String devGroupId, Model model) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		DevGroup group = devGroupService.findById(devGroupId);
		List<Device> listDevState = group.findListIStateDev(true);
		List<DevCollect> listDevValue = group.findListCollectDev(true);
		
		//组昵称不为空显示组昵称, 否则显示组名
		String groupPetName = "";
		if(group.getPetName().isEmpty()) {
			groupPetName = group.getGroupName();
		}else {
			groupPetName = group.getPetName();
		}
//		model.addAttribute("devGroupName", group.getGroupName());
//		model.addAttribute("devGroupPetName", groupPetName);
		List<Device> listDevStateCache = new ArrayList<>();
		List<DevCollect> listDevValueCache = new ArrayList<>();
		for(Device dev : listDevState) {
			Device d = deviceService.findById(dev.getId());
			listDevStateCache.add(d);
		}
		for(Device dev : listDevValue) {
			DevCollect d = (DevCollect) deviceService.findById(dev.getId());
			listDevValueCache.add(d);
		}
		Collections.sort(listDevStateCache);
		Collections.sort(listDevValueCache);
		DevicesVO devicesVO = new DevicesVO();
		devicesVO.setDeviceGroupName(group.getGroupName());
		devicesVO.setDeviceGroupPetName(groupPetName);
		devicesVO.setListDevState(listDevStateCache);
		devicesVO.setListDevValue(listDevValueCache);
//		model.addAttribute("listDevState", listDevStateCache);
//		model.addAttribute("listDevValue", listDevValueCache);
		return ResultUtil.success(devicesVO);
	}
}
