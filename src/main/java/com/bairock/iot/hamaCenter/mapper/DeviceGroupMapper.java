package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.user.DevGroup;

import java.util.List;

public interface DeviceGroupMapper {

    int insert(DevGroup devGroup);

    DevGroup findById(String id);

    List<DevGroup> findByUserId(Long userId);

    DevGroup findByUserIdAndDevGroupNameAndDevGroupPassword(Long userId, String deviceGroupName, String deviceGroupPassword);

    DevGroup findByNameAndUserId(String groupName, Long userId);

    DevGroup findByNameAndPsdAndUserId(String groupName, String password, Long userId);

    int update(DevGroup devGroup);

    int deleteById(String id);
}
