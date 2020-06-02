package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.data.DragConfig;

public interface DragConfigMapper {

    int insert(DragConfig dragConfig);

    DragConfig findByDevGroupId(String devGroupId);
}
