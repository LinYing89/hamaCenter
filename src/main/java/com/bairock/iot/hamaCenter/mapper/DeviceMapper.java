package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.device.Device;

import java.util.List;

public interface DeviceMapper {

    Device findById(String id);

    List<Device> findByGroupId(String devGroupId);

    List<Device> findByPid(String pid);

    int deleteById(String id);

    int deleteByDevGroupId(String devGroupId);
}
