package com.bairock.iot.hamaCenter.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bairock.iot.hamaCenter.data.webData.WebDevCtrlModel;
import com.bairock.iot.hamaCenter.service.DeviceBroadcastService;
import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.Device.OnCtrlModelChangedListener;

@Component
public class MyOnCtrlModelChangedListener implements OnCtrlModelChangedListener {

	@Autowired
	private DeviceBroadcastService deviceService;
	
	@Override
	public void onCtrlModelChanged(Device dev, CtrlModel ctrlModel) {
		Device superParent = dev.findSuperParent();
		WebDevCtrlModel webDevCtrlModel = new WebDevCtrlModel(dev.getLongCoding(), dev.getCtrlModel().ordinal());
		deviceService.broadcastCtrlModelChanged(superParent.getUsername(), superParent.getDevGroupName(), webDevCtrlModel);
	}

}
