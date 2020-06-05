package com.bairock.iot.hamaCenter.communication;

import com.bairock.iot.hamalib.device.CtrlModel;
import com.bairock.iot.hamalib.device.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bairock.iot.hamaCenter.data.webData.WebDevCtrlModel;
import com.bairock.iot.hamaCenter.service.DeviceBroadcastService;

@Component
public class MyOnCtrlModelChangedListener implements Device.OnCtrlModelChangedListener {

	@Autowired
	private DeviceBroadcastService deviceService;
	
	@Override
	public void onCtrlModelChanged(Device dev, CtrlModel ctrlModel) {
		Device superParent = dev.findSuperParent();
		WebDevCtrlModel webDevCtrlModel = new WebDevCtrlModel(dev.getLongCoding(), dev.getCtrlModel().ordinal());
		deviceService.broadcastCtrlModelChanged(superParent.getUsername(), superParent.getDevGroupName(), webDevCtrlModel);
	}

}
