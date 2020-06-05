package com.bairock.iot.hamaCenter.controller;

import com.bairock.iot.hamalib.communication.DevChannelBridge;
import com.bairock.iot.hamalib.device.CtrlModel;
import com.bairock.iot.hamalib.device.DevStateHelper;
import com.bairock.iot.hamalib.device.Device;
import com.bairock.iot.hamalib.device.IStateDev;
import com.bairock.iot.hamalib.order.DeviceOrder;
import com.bairock.iot.hamalib.order.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.bairock.iot.hamaCenter.utils.Util;
import com.bairock.iot.hamaCenter.communication.MyDevChannelBridge;
import com.bairock.iot.hamaCenter.communication.PadChannelBridgeHelper;
import com.bairock.iot.hamaCenter.data.webData.WebUserInfo;
import com.bairock.iot.hamaCenter.service.DeviceService;

/**
 * 组的设备页面websocket controller
 * 
 * @author 44489
 *
 */
@Controller
public class DevWebSocketController {

	@Autowired
	private DeviceService deviceService;
	private Logger logger = LoggerFactory.getLogger(DevWebSocketController.class);

	@MessageMapping("/userInfo")
	public void userInfo(WebUserInfo userInfo) {
		logger.info(userInfo.getUserName() + ":" + userInfo.getDevGroupName());
	}

	/**
	 * 向本地客户端发送刷新命令
	 * @param order
	 */
	@MessageMapping("/refresh")
    public void refresh(DeviceOrder order) {
	    sendCtrlOrderToPad(order);
    }
	
	/**
	 * 网页发出的控制命令 控制命令相当于档位切换命令, 不需要单独发送档位命令
	 *
	 */
	@MessageMapping("/ctrlDev")
	public void ctrlDev(DeviceOrder order) {
		String devId = order.getDevId();
		Device dev = deviceService.findById(devId);
		if (dev == null) {
			return;
		}
		if(order.getOrderType() == OrderType.GEAR) {
		    sendCtrlOrderToPad(order);
			return;
		}
		
		IStateDev subDev = (IStateDev)dev;
        String strOrder;
        if(order.getData().equals(DevStateHelper.DS_KAI)) {
            strOrder = subDev.getTurnOnOrder();
        }else {
            strOrder = subDev.getTurnOffOrder();
        }
        order.setData(strOrder);
		if (dev.getCtrlModel() == CtrlModel.LOCAL) {
			sendLocalOrder(order);
		} else if (dev.getCtrlModel() == CtrlModel.REMOTE) {
			sendRemoteOrder(dev, order);
		}else {
			sendLocalOrder(order);
			sendRemoteOrder(dev, order);
		}
	}
	
	private void sendLocalOrder(DeviceOrder order) {
		sendCtrlOrderToPad(order);
	}
	
	private void sendRemoteOrder(Device dev, DeviceOrder order) {
		if(order.getOrderType() != OrderType.CTRL_DEV) {
			return;
		}
		DevChannelBridge db = MyDevChannelBridge.findDevChannelBridge(dev.getLongCoding(), order.getUsername(), order.getDevGroupName());
		if(null == db) {
			return;
		}
		db.sendOrder(order.getData());
	}

	private void sendCtrlOrderToPad(DeviceOrder order) {
		String strOrder = Util.orderBaseToString(order);
		PadChannelBridgeHelper.getIns().sendOrderToLocal(order.getUsername(), order.getDevGroupName(), strOrder);
	}

}
