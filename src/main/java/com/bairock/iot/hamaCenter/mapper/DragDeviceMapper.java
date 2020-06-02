package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.data.DragDevice;

public interface DragDeviceMapper {

    int insert(DragDevice dragDevice);

    DragDevice findByDeviceId(String deviceId);

    int update(DragDevice dragDevice);

    int deleteByDeviceId(String deviceId);
}
