package com.bairock.iot.hamaCenter.service;

import java.util.List;

import com.bairock.iot.hamaCenter.mapper.CollectPropertyMapper;
import com.bairock.iot.hamaCenter.mapper.DeviceMapper;
import com.bairock.iot.hamalib.device.DevHaveChild;
import com.bairock.iot.hamalib.device.Device;
import com.bairock.iot.hamalib.device.devcollect.CollectProperty;
import com.bairock.iot.hamalib.device.devcollect.DevCollect;
import com.bairock.iot.hamalib.device.devcollect.DevCollectClimateContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.bairock.iot.hamaCenter.communication.PadChannelBridgeHelper;
import com.bairock.iot.hamaCenter.data.webData.WebDevGear;
import com.bairock.iot.hamaCenter.data.webData.WebDevState;
import com.bairock.iot.hamaCenter.data.webData.WebDevValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeviceService {

	private SimpMessageSendingOperations messaging;
	
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private CollectPropertyMapper collectPropertyMapper;
	
	public Device findById(String id) {
		return deviceMapper.findById(id);
	}

	public List<Device> findDeviceByDevGroupId(Long devGroupId){
		List<Device> devices = deviceMapper.findByGroupId(devGroupId);
		devices.forEach(device -> {
			if(device instanceof DevHaveChild){
				findChildDevice(device);
			}else if(device instanceof DevCollect){
				CollectProperty collectProperty = collectPropertyMapper.findByDevCollectId(device.getId());
				((DevCollect) device).setCollectProperty(collectProperty);
			}
		});
		return devices;
	}

	public void findChildDevice(Device device){
		if (device instanceof DevHaveChild) {
			DevHaveChild parent = (DevHaveChild) device;

			List<Device> childDevices = findByPid(device.getId());
			childDevices.forEach(dev -> {
				parent.addChildDev(dev);
				findChildDevice(dev);
			});
		}
	}

	public List<Device> findByPid(String pid){
		List<Device> devices = deviceMapper.findByPid(pid);
		devices.forEach(device -> {
			if(device instanceof DevHaveChild){
				DevHaveChild parent = (DevHaveChild) device;

				List<Device> childDevices = findByPid(device.getId());
				childDevices.forEach(parent::addChildDev);

				((DevHaveChild) device).setListDev(findByPid(device.getId()));
			}
		});
		return devices;
	}
	
    @Autowired
    public DeviceService(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }
    
    /**
     * 向网页发送设备的状态bean
     * @param userName
     * @param devGroupName
     * @param webDevState
     */
    //topic/userName-devGroupName/devState
    public void broadcastStateChanged(String userName, String devGroupName, WebDevState webDevState){
    	String topic = String.format("/topic/%s-%s/devState", userName, devGroupName);
        messaging.convertAndSend(topic, webDevState);
        
        //发往pad
        ObjectMapper mapper = new ObjectMapper();
		try {
			String order;
			order = mapper.writeValueAsString(webDevState);
			PadChannelBridgeHelper.getIns().sendOrderSynable(userName, devGroupName, order);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 向采集设备发送设备的档位bean
     * @param userName
     * @param devGroupName
     * @param webDevGear
     */
    public void broadcastGearChanged(String userName, String devGroupName, WebDevGear webDevGear){
    	String topic = String.format("/topic/%s-%s/devGear", userName, devGroupName);
        messaging.convertAndSend(topic, webDevGear);
    }
    
    /**
     * 向网页发送采集设备的value bean
     * @param userName
     * @param devGroupName
     * @param webDevValue
     */
    public void broadcastValueChanged(String userName, String devGroupName, WebDevValue webDevValue){
    	String topic = String.format("/topic/%s-%s/devValue", userName, devGroupName);
        messaging.convertAndSend(topic, webDevValue);
    }
    
}
