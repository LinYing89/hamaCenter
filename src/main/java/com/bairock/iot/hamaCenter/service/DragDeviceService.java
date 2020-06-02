package com.bairock.iot.hamaCenter.service;

import java.util.List;

import com.bairock.iot.hamaCenter.mapper.DragDeviceMapper;
import com.bairock.iot.hamalib.data.DragDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.iot.hamaCenter.repository.DragDeviceRepository;

@Service
public class DragDeviceService {

    @Autowired
    private DragDeviceMapper dragDeviceMapper;

    public DragDevice findByDeviceId(String deviceId) {
        return dragDeviceMapper.findByDeviceId(deviceId);
    }

    public void insert(List<DragDevice> dragDevices) {
        for (DragDevice dd : dragDevices) {
            DragDevice dragDevice = dragDeviceMapper.findByDeviceId(dd.getDeviceId());
            if (null == dragDevice) {
                dragDeviceMapper.insert(dd);
            } else {
                dragDevice.setImageHeight(dd.getImageHeight());
                dragDevice.setImageWidth(dd.getImageWidth());
                dragDevice.setRotate(dd.getRotate());
                dragDevice.setImageName(dd.getImageName());
                dragDevice.setImageType(dd.getImageType());
                dragDevice.setLayoutx(dd.getLayoutx());
                dragDevice.setLayouty(dd.getLayouty());
                dragDeviceMapper.update(dragDevice);
            }
        }
    }
}
