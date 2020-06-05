package com.bairock.iot.hamaCenter.vo;

import com.bairock.iot.hamalib.device.Device;
import com.bairock.iot.hamalib.device.devcollect.DevCollect;

import java.util.ArrayList;
import java.util.List;

public class DevicesVO {
    private String deviceGroupName;
    private String deviceGroupPetName;
    List<Device> listDevState = new ArrayList<>();
    List<DevCollect> listDevValue = new ArrayList<>();

    public String getDeviceGroupName() {
        return deviceGroupName;
    }

    public void setDeviceGroupName(String deviceGroupName) {
        this.deviceGroupName = deviceGroupName;
    }

    public String getDeviceGroupPetName() {
        return deviceGroupPetName;
    }

    public void setDeviceGroupPetName(String deviceGroupPetName) {
        this.deviceGroupPetName = deviceGroupPetName;
    }

    public List<Device> getListDevState() {
        return listDevState;
    }

    public void setListDevState(List<Device> listDevState) {
        this.listDevState = listDevState;
    }

    public List<DevCollect> getListDevValue() {
        return listDevValue;
    }

    public void setListDevValue(List<DevCollect> listDevValue) {
        this.listDevValue = listDevValue;
    }
}
