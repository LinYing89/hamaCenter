package com.bairock.iot.hamaCenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bairock.iot.hamaCenter.exception.MyException;
import com.bairock.iot.hamaCenter.mapper.DeviceGroupMapper;
import com.bairock.iot.hamaCenter.utils.Result;
import com.bairock.iot.hamaCenter.utils.ResultUtil;
import com.bairock.iot.hamalib.communication.DevChannelBridge;
import com.bairock.iot.hamalib.communication.DevChannelBridgeHelper;
import com.bairock.iot.hamalib.data.DragConfig;
import com.bairock.iot.hamalib.data.DragDevice;
import com.bairock.iot.hamalib.device.DevHaveChild;
import com.bairock.iot.hamalib.device.Device;
import com.bairock.iot.hamalib.device.devcollect.DevCollect;
import com.bairock.iot.hamalib.linkage.LinkageHolder;
import com.bairock.iot.hamalib.user.DevGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.iot.hamaCenter.communication.MyDevChannelBridge;
import com.bairock.iot.hamaCenter.communication.PadChannelBridge;
import com.bairock.iot.hamaCenter.communication.PadChannelBridgeHelper;
import com.bairock.iot.hamaCenter.Config;

@Service
public class DevGroupService {

	@Autowired
	private DeviceGroupMapper deviceGroupMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private DragDeviceService dragDeviceService;
	@Autowired
	private DragConfigService dragConfigService;
	
	@Autowired
	private Config config;

	public int insert(DevGroup group) {
		return deviceGroupMapper.insert(group);
	}

	public DevGroup findById(String id) {
		return deviceGroupMapper.findById(id);
	}
	
	public List<DevGroup> findByUserId(Long userId) {
		return deviceGroupMapper.findByUserId(userId);
	}
	
	public DevGroup findByNameAndUserId(String name, Long userId) {
		return deviceGroupMapper.findByNameAndUserId(name, userId);
	}

	public DevGroup findByUsernameAndDevGroupNameAndDevGroupPassword(String username, String deviceGroupName, String deviceGroupPassword){
		Long userId = userService.findIdByUsername(username);
		return deviceGroupMapper.findByUserIdAndDevGroupNameAndDevGroupPassword(userId, deviceGroupName, deviceGroupPassword);
	}



	public int update(DevGroup group) {
		return deviceGroupMapper.update(group);
	}

	public int deleteGroup(String id) {
		return deviceGroupMapper.deleteById(id);
	}
	
	/**
	 * 客户端组登录
	 * @param username 账号
	 * @param devGroupName 组名
	 * @param devGroupPsg 组密码
	 */
	public DevGroup devGroupLogin(Long username, String devGroupName, String devGroupPsg) {
		DevGroup group = deviceGroupMapper.findByNameAndPsdAndUserId(devGroupName, devGroupPsg, username);
		if(null == group) {
			throw new MyException("用户名或密码错误!");
		}
		return group;
	}
	
	/**
	 * 根据用户名和组名获取组
	 * @param userId 账号
	 * @param devGroupName 组名
	 */
	public DevGroup groupDownload(Long userId, String devGroupName) {
		return deviceGroupMapper.findByNameAndUserId(devGroupName, userId);
	}
	
	public List<DragDevice> dragDeviceDownload(Long userId, String devGroupName) {
        DevGroup group = deviceGroupMapper.findByNameAndUserId(devGroupName, userId);
        if(null == group) {
			throw new MyException("用户名或组错误!");
        }
        
        List<DragDevice> dragDevices = new ArrayList<>();
        
        List<Device> listDev = deviceService.findDeviceByDevGroupId(group.getId());
        for (Device dev : listDev) {
            DragDevice dragDevice = dragDeviceService.findByDeviceId(dev.getId());
            if(null != dragDevice) {
                dragDevices.add(dragDevice);
            }
        }
        List<DevCollect> listDevCollector = group.findListCollectDev(true);
        for (Device dev : listDevCollector) {
            DragDevice dragDevice = dragDeviceService.findByDeviceId(dev.getId());
            if(null != dragDevice) {
                dragDevices.add(dragDevice);
            }
        }

        return dragDevices;
    }
	
	public DragConfig dragConfigDownload(String devGroupId){
        return dragConfigService.findByDevGroupId(devGroupId);
	}
	
	public Result<?> groupUpload(DevGroup groupUpload){
		Result<Object> result = new Result<>();
		DevGroup groupDb = deviceGroupMapper.findByNameAndUserId(groupUpload.getGroupName(), groupUpload.getUserId());
		if(null == groupDb) {
			return ResultUtil.error("用户名或组名错误");
		}
		
		List<Device> listOldDevice = new ArrayList<>(groupDb.getListDevice());
		
//		groupDb.getListDevice().clear();
		for(Device dev : listOldDevice) {
			groupDb.removeDevice(dev);
		}
		for(Device dev : groupUpload.getListDevice()) {
			groupDb.addDevice(dev);
		}
//		groupDb.getListDevice().addAll(groupUpload.getListDevice());
//		groupDb.getListLinkageHolder().clear();
		List<LinkageHolder> listOldLinkageHolder = new ArrayList<>(groupDb.getListLinkageHolder());
		for(LinkageHolder h : listOldLinkageHolder) {
			h.setDevGroup(null);
			groupDb.getListLinkageHolder().remove(h);
		}
		for(LinkageHolder h : groupUpload.getListLinkageHolder()) {
			h.setDevGroup(groupDb);
			groupDb.getListLinkageHolder().add(h);
		}
		groupDb.getListLinkageHolder().addAll(groupUpload.getListLinkageHolder());
		deviceGroupMapper.update(groupDb);
		
		//移除被删除设备的缓存
		for(Device dev : listOldDevice) {
			removeCacheDevice(dev);
		}
		
		for(DevChannelBridge bridge : DevChannelBridgeHelper.getIns().getListDevChannelBridge()) {
			MyDevChannelBridge myBridge = (MyDevChannelBridge)bridge;
			//找到已连接的设备, 并且用户信息一致的设备链接, 重新从缓存中获取设备
			if(null != myBridge.getUserName() && null != myBridge.getGroupName()) {
				if(myBridge.getUserName().equals(groupUpload.getUser().getUsername()) && myBridge.getGroupName().equals(groupUpload.getGroupName())){
					Device oldDev = bridge.getDevice();
					if(null != oldDev) {
						Device dev = deviceService.findById(oldDev.getId());
						bridge.setDevice(dev);
					}
				}
			}
		}
		
		//找到所有已在pad链接中保存的设备对象, 重新从缓存中获取
		for(PadChannelBridge bridge : PadChannelBridgeHelper.getIns().getListPadChannelBridge(groupUpload.getUser().getUsername(), groupUpload.getGroupName())) {
			List<Device> listDevice = new ArrayList<>();
			for(Device oldDev : bridge.getListDevice()) {
				Device dev = deviceService.findById(oldDev.getId());
				if(null != dev) {
					listDevice.add(dev);
				}
			}
			bridge.setListDevice(listDevice);
		}
		
		return result;
	}
	
	private void removeCacheDevice(Device device) {
//		cacheManager.getCache("device").evict(device.getId());
		if(device instanceof DevHaveChild) {
			for(Device d : ((DevHaveChild) device).getListDev()) {
				removeCacheDevice(d);
			}
		}
	}
}
