package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.device.devcollect.CollectProperty;

public interface CollectPropertyMapper {

    int insert(CollectProperty collectProperty);

    CollectProperty findById(String id);

    CollectProperty findByDevCollectId(String devCollectId);

    int update(CollectProperty collectProperty);

    int deleteByDevCollectId(String id);
}
