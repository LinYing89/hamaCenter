package com.bairock.iot.hamaCenter.service;

import com.bairock.iot.hamaCenter.mapper.DragConfigMapper;
import com.bairock.iot.hamalib.data.DragConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DragConfigService {

    @Autowired
    private DragConfigMapper dragConfigMapper;
    
    public DragConfig findByDevGroupId(String devGroupId) {
        return dragConfigMapper.findByDevGroupId(devGroupId);
    }

    public void insert(DragConfig dragConfig) {
        dragConfigMapper.insert(dragConfig);
    }
}
