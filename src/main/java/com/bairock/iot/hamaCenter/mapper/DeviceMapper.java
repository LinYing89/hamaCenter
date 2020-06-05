package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.device.Device;

import java.util.List;

public interface DeviceMapper {

    int insert(Device device);

    Device findById(String id);

    List<Device> findByGroupId(Long devGroupId);

    List<Device> findByPid(String pid);

    int deleteById(String id);

    int deleteByDevGroupId(String devGroupId);
}
