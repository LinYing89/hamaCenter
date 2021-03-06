package com.bairock.iot.hamaCenter.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class DeviceMsgTestService {

	private SimpMessageSendingOperations messaging;

	@Autowired
	public DeviceMsgTestService(SimpMessageSendingOperations messaging) {
		this.messaging = messaging;
	}

	public void broadcastDeviceMsg(DeviceMsg deviceMsg) {
		// 查看过滤条件
		if (DeviceMsgTestWSCtrler.deviceMsgFilter != null && !DeviceMsgTestWSCtrler.deviceMsgFilter.isEmpty()) {
			if (!deviceMsg.getMsg().contains(DeviceMsgTestWSCtrler.deviceMsgFilter)) {
				return;
			}
		}
		String topic = String.format("/topic/deviceMsg");
		messaging.convertAndSend(topic, deviceMsg);
	}
	
	public void broadcastPadMsg(DeviceMsg deviceMsg) {
		// 查看过滤条件
		if (DeviceMsgTestWSCtrler.padMsgFilter != null && !DeviceMsgTestWSCtrler.padMsgFilter.isEmpty()) {
			if (!deviceMsg.getMsg().contains(DeviceMsgTestWSCtrler.padMsgFilter)) {
				return;
			}
		}
		String topic = String.format("/topic/padMsg");
		messaging.convertAndSend(topic, deviceMsg);
	}
}
